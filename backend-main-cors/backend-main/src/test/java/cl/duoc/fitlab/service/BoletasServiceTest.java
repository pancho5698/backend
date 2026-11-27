package cl.duoc.fitlab.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.fitlab.dto.ItemCompra;
import cl.duoc.fitlab.dto.NuevaBoleta;
import cl.duoc.fitlab.model.Boleta;
import cl.duoc.fitlab.model.Producto;
import cl.duoc.fitlab.model.Usuario;
import cl.duoc.fitlab.repository.RepositorioBoletas;
import cl.duoc.fitlab.repository.RepositorioProductos;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Pruebas de la lógica de compra: total, precio de oferta y descuento de stock.
@ExtendWith(MockitoExtension.class)
class BoletasServiceTest {

    @Mock
    private RepositorioBoletas repositorio;
    @Mock
    private RepositorioProductos repositorioProductos;
    @Mock
    private ProductosService productosService;
    @Mock
    private UsuariosService usuariosService;

    @InjectMocks
    private BoletasService servicio;

    private Producto producto(int id, String nombre, int precio, Integer oferta, int stock) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setPrecioOferta(oferta);
        producto.setStock(stock);
        return producto;
    }

    private ItemCompra item(int productoId, int cantidad) {
        ItemCompra item = new ItemCompra();
        item.setProductoId(productoId);
        item.setCantidad(cantidad);
        return item;
    }

    private NuevaBoleta pedido(ItemCompra... items) {
        NuevaBoleta pedido = new NuevaBoleta();
        pedido.setItems(List.of(items));
        return pedido;
    }

    @Test
    void comprarCalculaElTotalConPrecioDeOfertaYDescuentaStock() {
        Producto prostar = producto(1, "Prostar 5 lb", 90000, 79990, 12);
        Producto creatina = producto(2, "Creatina 300 g", 50000, null, 4);
        when(usuariosService.buscarPorCorreo("ana@gmail.com")).thenReturn(new Usuario());
        when(productosService.buscar(1)).thenReturn(prostar);
        when(productosService.buscar(2)).thenReturn(creatina);
        when(repositorio.findAll(any(Sort.class))).thenReturn(List.of());
        when(repositorio.save(any(Boleta.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Boleta boleta = servicio.crear("ana@gmail.com", pedido(item(1, 2), item(2, 1)));

        // 2 x 79.990 (oferta) + 1 x 50.000 = 209.980
        assertEquals(209980, boleta.getTotal());
        assertEquals(2, boleta.getDetalles().size());
        assertEquals(10, prostar.getStock());
        assertEquals(3, creatina.getStock());
        assertEquals("ana@gmail.com", boleta.getCorreoCliente());
    }

    @Test
    void comprarSinStockSuficienteRespondeError400YNoDescuentaNada() {
        Producto creatina = producto(2, "Creatina 300 g", 50000, null, 4);
        when(usuariosService.buscarPorCorreo("ana@gmail.com")).thenReturn(new Usuario());
        when(productosService.buscar(2)).thenReturn(creatina);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> servicio.crear("ana@gmail.com", pedido(item(2, 50))));

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        assertEquals(4, creatina.getStock());
        // Ni el stock ni la boleta se guardan si la compra falla.
        verify(repositorioProductos, never()).saveAll(any());
        verify(repositorio, never()).save(any());
    }

    @Test
    void comprarConCarritoVacioRespondeError400() {
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> servicio.crear("ana@gmail.com", pedido()));

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
    }
}
