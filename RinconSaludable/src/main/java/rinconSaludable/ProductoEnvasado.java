package rinconSaludable;

public class ProductoEnvasado extends Producto {
    private double impuestoIVA = 0.21;

    public ProductoEnvasado(String id, String nombre, double precioBase, int stock) {
        super(id, nombre, precioBase, stock);
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecioBase() * (1 + impuestoIVA);
    }
}