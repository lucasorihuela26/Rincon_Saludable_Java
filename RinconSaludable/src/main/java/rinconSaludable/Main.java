package rinconSaludable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import rinconSaludable.conexion.ConexionBD;
import rinconSaludable.dao.ProductoDAO;
import rinconSaludable.dao.ProductoDAOImpl;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            ConexionBD.inicializarBaseDatos();
            probarConexion();
            ProductoDAO dao = new ProductoDAOImpl();
            Pedido pedido = new Pedido(1);
            int opcion;
            do {
                mostrarMenu();
                opcion = leerEntero(sc);
                try {
                    switch (opcion) {
                        case 1 -> mostrarCatalogo(dao);
                        case 2 -> comprar(sc, dao, pedido);
                        case 3 -> agregarProducto(sc, dao);
                        case 4 -> actualizarStock(sc, dao);
                        case 5 -> eliminarProducto(sc, dao);
                        case 0 -> System.out.println("Finalizando pedido...");
                        default -> System.out.println("Opción inválida.");
                    }
                } catch (CantidadInvalidaException | StockInsuficienteException
                        | ProductoNoEncontradoException | BaseDatosException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } while (opcion != 0);
            mostrarTicket(pedido);
        } catch (BaseDatosException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private static void probarConexion() throws BaseDatosException {
        try (Connection con = ConexionBD.obtenerConexion()) {
            System.out.println("Conexión OK a SQLite: " + con.getMetaData().getURL());
        } catch (SQLException e) {
            throw new BaseDatosException("Error al comprobar la conexión: " + e.getMessage());
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n=== RINCÓN SALUDABLE ===");
        System.out.println("1. Ver catálogo");
        System.out.println("2. Comprar producto");
        System.out.println("3. Agregar producto");
        System.out.println("4. Actualizar stock");
        System.out.println("5. Eliminar producto");
        System.out.println("0. Finalizar pedido");
        System.out.print("Elija una opción: ");
    }

    private static void mostrarCatalogo(ProductoDAO dao) throws BaseDatosException {
        List<Producto> productos = dao.listarTodos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos en la base de datos.");
            return;
        }
        for (Producto p : productos) {
            System.out.printf("%s | %s | Stock: %d | Precio: $%.2f%n",
                    p.getId(), p.getNombre(), p.getStock(), p.calcularPrecioFinal());
        }
    }

    private static void comprar(Scanner sc, ProductoDAO dao, Pedido pedido)
            throws BaseDatosException, ProductoNoEncontradoException,
            CantidadInvalidaException, StockInsuficienteException {
        mostrarCatalogo(dao);
        System.out.print("ID del producto: ");
        Producto producto = dao.buscarPorId(sc.nextLine().trim());
        System.out.print("Cantidad: ");
        int cantidad = leerEntero(sc);
        DetallePedido detalle = new DetallePedido(producto, cantidad);
        producto.reducirStock(cantidad);
        dao.actualizarStock(producto.getId(), producto.getStock());
        pedido.agregarDetalle(detalle);
        System.out.println("Agregado al pedido.");
    }

    private static void agregarProducto(Scanner sc, ProductoDAO dao)
            throws BaseDatosException, CantidadInvalidaException {
        System.out.print("Tipo (1 = Envasado, 2 = Granel): ");
        int tipo = leerEntero(sc);
        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo inválido.");
            return;
        }
        System.out.print("ID: ");
        String id = sc.nextLine().trim();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Precio base: ");
        double precio = leerDecimal(sc);
        System.out.print("Stock: ");
        int stock = leerEntero(sc);
        if (id.isBlank() || nombre.isBlank() || !Double.isFinite(precio)
                || precio < 0 || stock < 0) {
            System.out.println("Datos inválidos: ID y nombre obligatorios; precio y stock no negativos.");
            return;
        }
        Producto producto;
        if (tipo == 1) {
            producto = new ProductoEnvasado(id, nombre, precio, stock);
        } else {
            System.out.print("Gramos: ");
            double gramos = leerDecimal(sc);
            if (!Double.isFinite(gramos) || gramos <= 0) {
                System.out.println("Los gramos deben ser mayores que cero.");
                return;
            }
            producto = new ProductoGranel(id, nombre, precio, stock, gramos);
        }
        dao.insertar(producto);
        System.out.println("Producto agregado a la base de datos.");
    }

    private static void actualizarStock(Scanner sc, ProductoDAO dao)
            throws BaseDatosException, ProductoNoEncontradoException {
        System.out.print("ID del producto: ");
        String id = sc.nextLine().trim();
        Producto producto = dao.buscarPorId(id);
        System.out.print("Nuevo stock para " + producto.getNombre() + ": ");
        int nuevoStock = leerEntero(sc);
        if (nuevoStock < 0) {
            System.out.println("El stock debe ser un entero no negativo.");
            return;
        }
        dao.actualizarStock(id, nuevoStock);
        System.out.println("Stock actualizado.");
    }

    private static void eliminarProducto(Scanner sc, ProductoDAO dao)
            throws BaseDatosException, ProductoNoEncontradoException {
        System.out.print("ID del producto a eliminar: ");
        String id = sc.nextLine().trim();
        dao.eliminar(id);
        System.out.println("Producto eliminado.");
    }

    private static void mostrarTicket(Pedido pedido) {
        System.out.println("\n=== TICKET PEDIDO #" + pedido.getIdPedido() + " ===");
        for (DetallePedido d : pedido.getDetalles()) {
            System.out.printf("%s x%d = $%.2f%n",
                    d.getProducto().getNombre(), d.getCantidad(), d.calcularSubtotal());
        }
        System.out.printf("TOTAL: $%.2f%n", pedido.calcularTotal());
    }

    private static int leerEntero(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double leerDecimal(Scanner sc) {
        try {
            return Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
