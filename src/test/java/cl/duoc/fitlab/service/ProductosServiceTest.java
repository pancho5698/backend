package cl.duoc.fitlab.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.fitlab.model.Producto;
import cl.duoc.fitlab.repository.RepositorioProductos;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Pruebas de las reglas de negocio de productos.
// El repositorio es un "mock": un repositorio falso, así las pruebas no necesitan MongoDB.
@ExtendWith(MockitoExtension.class)
class ProductosServiceTest {

    @Mock
    private RepositorioProductos repositorio;

    @InjectMocks
    private ProductosService servicio;

    private Producto nuevoProducto(int precio, Integer precioOferta, int stock) {
        Producto producto = new Producto();
        producto.setCodigo("TEST-001");
        producto.setNombre("Producto de prueba");
        producto.setCategoria("Equipamiento");
        producto.setPrecio(precio);
        producto.setPrecioOferta(precioOferta);
        producto.setStock(stock);
        return producto;
    }

    @Test
    void crearConPrecioCeroRespondeError400() {
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> servicio.crear(nuevoProducto(0, null, 5)));

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        // Como los datos son inválidos, nunca se guarda nada.
        verify(repositorio, never()).save(any());
    }

    @Test
    void crearConOfertaMayorAlPrecioRespondeError400() {
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> servicio.crear(nuevoProducto(10000, 15000, 5)));

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verify(repositorio, never()).save(any());
    }

    @Test
    void buscarProductoInexistenteRespondeError404() {
        when(repositorio.buscarPorId(99)).thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(ResponseStatusException.class, () -> servicio.buscar(99));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    void crearProductoValidoAsignaElSiguienteId() {
        Producto ultimo = nuevoProducto(5000, null, 1);
        ultimo.setId(6);
        when(repositorio.findByCodigo("TEST-001")).thenReturn(Optional.empty());
        when(repositorio.findAll(any(Sort.class))).thenReturn(List.of(ultimo));
        when(repositorio.save(any(Producto.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Producto creado = servicio.crear(nuevoProducto(10000, 8000, 5));

        // Si el último producto tiene id 6, el nuevo debe quedar con id 7.
        assertEquals(7, creado.getId());
    }
}
