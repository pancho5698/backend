package cl.duoc.fitlab.service;

import cl.duoc.fitlab.dto.ItemCompra;
import cl.duoc.fitlab.dto.NuevaBoleta;
import cl.duoc.fitlab.model.Boleta;
import cl.duoc.fitlab.model.DetalleBoleta;
import cl.duoc.fitlab.model.Producto;
import cl.duoc.fitlab.repository.RepositorioBoletas;
import cl.duoc.fitlab.repository.RepositorioProductos;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BoletasService {
    private final RepositorioBoletas repositorio;
    private final RepositorioProductos repositorioProductos;
    private final ProductosService productosService;
    private final UsuariosService usuariosService;

    public BoletasService(RepositorioBoletas repositorio, RepositorioProductos repositorioProductos,
            ProductosService productosService, UsuariosService usuariosService) {
        this.repositorio = repositorio;
        this.repositorioProductos = repositorioProductos;
        this.productosService = productosService;
        this.usuariosService = usuariosService;
    }

    public List<Boleta> listar() {
        return repositorio.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Boleta buscar(int id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Boleta no encontrada"));
    }

    // Lógica de la compra: valida, calcula el total y descuenta el stock.
    public Boleta crear(NuevaBoleta pedido) {
        if (pedido.getItems() == null || pedido.getItems().isEmpty()) {
            throw error("La boleta debe tener al menos un producto.");
        }
        // El cliente debe existir (404 si no).
        usuariosService.buscarPorCorreo(pedido.getCorreoCliente());

        Boleta boleta = new Boleta();
        // Guardamos cada producto una sola vez, por si el carrito lo trae repetido.
        Map<Integer, Producto> productos = new HashMap<>();
        int total = 0;

        for (ItemCompra item : pedido.getItems()) {
            if (item.getCantidad() <= 0) {
                throw error("La cantidad debe ser mayor a 0.");
            }
            Producto producto = productos.computeIfAbsent(item.getProductoId(), productosService::buscar);
            if (producto.getStock() < item.getCantidad()) {
                throw error("Stock insuficiente para " + producto.getNombre() + " (quedan " + producto.getStock() + ").");
            }
            // Si el producto está en oferta, se cobra el precio de oferta.
            int precio = producto.getPrecioOferta() != null ? producto.getPrecioOferta() : producto.getPrecio();

            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setProductoId(producto.getId());
            detalle.setNombre(producto.getNombre());
            detalle.setPrecioUnitario(precio);
            detalle.setCantidad(item.getCantidad());
            detalle.setSubtotal(precio * item.getCantidad());
            boleta.getDetalles().add(detalle);

            total += detalle.getSubtotal();
            producto.setStock(producto.getStock() - item.getCantidad());
        }

        // Recién cuando todo es válido se descuenta el stock en la base.
        repositorioProductos.saveAll(productos.values());

        boleta.setId(siguienteId());
        boleta.setFecha(LocalDateTime.now());
        boleta.setCorreoCliente(pedido.getCorreoCliente());
        boleta.setTotal(total);
        return repositorio.save(boleta);
    }

    private int siguienteId() {
        return repositorio.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .findFirst().map(b -> b.getId() + 1).orElse(1);
    }

    private ResponseStatusException error(String mensaje) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensaje);
    }
}
