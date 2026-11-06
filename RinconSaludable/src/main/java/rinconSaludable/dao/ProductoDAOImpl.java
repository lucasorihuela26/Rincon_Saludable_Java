package rinconSaludable.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import rinconSaludable.BaseDatosException;
import rinconSaludable.CantidadInvalidaException;
import rinconSaludable.Producto;
import rinconSaludable.ProductoEnvasado;
import rinconSaludable.ProductoGranel;
import rinconSaludable.ProductoNoEncontradoException;
import rinconSaludable.conexion.ConexionBD;

public class ProductoDAOImpl implements ProductoDAO {
    private static final String INSERT =
            "INSERT INTO productos (id, nombre, tipo, precio_base, stock, gramos) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL =
            "SELECT id, nombre, tipo, precio_base, stock, gramos FROM productos ORDER BY id";
    private static final String SELECT_BY_ID =
            "SELECT id, nombre, tipo, precio_base, stock, gramos FROM productos WHERE id = ?";
    private static final String UPDATE_STOCK = "UPDATE productos SET stock = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM productos WHERE id = ?";

    @Override
    public void insertar(Producto p) throws BaseDatosException {
        if (p == null || p.getId() == null || p.getId().isBlank()
                || p.getNombre() == null || p.getNombre().isBlank()
                || !Double.isFinite(p.getPrecioBase()) || p.getPrecioBase() < 0
                || p.getStock() < 0) {
            throw new BaseDatosException("Los datos del producto son inválidos.");
        }
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getNombre());
            ps.setString(3, p instanceof ProductoGranel ? "GRANEL" : "ENVASADO");
            ps.setDouble(4, p.getPrecioBase());
            ps.setInt(5, p.getStock());
            if (p instanceof ProductoGranel granel) {
                ps.setDouble(6, granel.getGramos());
            } else {
                ps.setNull(6, Types.REAL);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 19 && e.getMessage() != null
                    && (e.getMessage().contains("UNIQUE") || e.getMessage().contains("PRIMARY KEY"))) {
                throw new BaseDatosException("Ya existe un producto con el ID " + p.getId() + ".");
            }
            throw new BaseDatosException("Error al insertar producto: " + e.getMessage());
        }
    }

    @Override
    public List<Producto> listarTodos() throws BaseDatosException {
        List<Producto> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Producto buscarPorId(String id)
            throws BaseDatosException, ProductoNoEncontradoException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al buscar producto: " + e.getMessage());
        }
        throw new ProductoNoEncontradoException("No existe un producto con el ID " + id + ".");
    }

    @Override
    public void actualizarStock(String id, int nuevoStock)
            throws BaseDatosException, ProductoNoEncontradoException {
        if (nuevoStock < 0) {
            throw new BaseDatosException("El stock no puede ser negativo.");
        }
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(UPDATE_STOCK)) {
            ps.setInt(1, nuevoStock);
            ps.setString(2, id);
            if (ps.executeUpdate() == 0) {
                throw new ProductoNoEncontradoException("No existe un producto con el ID " + id + ".");
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al actualizar stock: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(String id)
            throws BaseDatosException, ProductoNoEncontradoException {
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            ps.setString(1, id);
            if (ps.executeUpdate() == 0) {
                throw new ProductoNoEncontradoException("No existe un producto con el ID " + id + ".");
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al eliminar producto: " + e.getMessage());
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException, BaseDatosException {
        String id = rs.getString("id");
        String nombre = rs.getString("nombre");
        double precio = rs.getDouble("precio_base");
        int stock = rs.getInt("stock");
        String tipo = rs.getString("tipo");
        try {
            if ("GRANEL".equals(tipo)) {
                return new ProductoGranel(id, nombre, precio, stock, rs.getDouble("gramos"));
            }
            if ("ENVASADO".equals(tipo)) {
                return new ProductoEnvasado(id, nombre, precio, stock);
            }
            throw new BaseDatosException("Tipo de producto desconocido: " + tipo);
        } catch (CantidadInvalidaException e) {
            throw new BaseDatosException("Dato inválido en la base de datos para " + id + ": " + e.getMessage());
        }
    }
}
