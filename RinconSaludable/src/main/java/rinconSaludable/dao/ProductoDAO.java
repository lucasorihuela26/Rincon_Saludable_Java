package rinconSaludable.dao;

import java.util.List;
import rinconSaludable.BaseDatosException;
import rinconSaludable.Producto;
import rinconSaludable.ProductoNoEncontradoException;

public interface ProductoDAO {
    void insertar(Producto producto) throws BaseDatosException;
    Producto buscarPorId(String id) throws BaseDatosException, ProductoNoEncontradoException;
    List<Producto> listarTodos() throws BaseDatosException;
    void actualizarStock(String id, int nuevoStock)
            throws BaseDatosException, ProductoNoEncontradoException;
    void eliminar(String id) throws BaseDatosException, ProductoNoEncontradoException;
}
