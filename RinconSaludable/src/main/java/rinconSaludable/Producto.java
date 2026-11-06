package rinconSaludable;

public abstract class Producto {
    private String id;
    private String nombre;
    private double precioBase;
    private int stock;

    public Producto(String id, String nombre, double precioBase, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.stock = stock;
    }

    public abstract double calcularPrecioFinal();

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public int getStock() {
        return stock;
    }

    // Lanza una excepción si no hay suficiente stock
    public void reducirStock(int cantidad) throws StockInsuficienteException {
        if (cantidad > this.stock) {
            throw new StockInsuficienteException("Error de Stock: Solo quedan " + stock + " unidades de " + nombre + ".");
        }
        this.stock -= cantidad;
    }
}