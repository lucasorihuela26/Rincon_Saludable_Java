package rinconSaludable;

public class ProductoGranel extends Producto {
    private double gramos;

    public ProductoGranel(String id, String nombre, double precioPorKilo, int stock, double gramos) throws CantidadInvalidaException {
        super(id, nombre, precioPorKilo, stock);
        if (gramos <= 0) {
            throw new CantidadInvalidaException("Error: Los gramos ingresados deben ser mayores a cero.");
        }
        this.gramos = gramos;
    }

    @Override
    public double calcularPrecioFinal() {
        double subtotal = (getPrecioBase() / 1000.0) * gramos;
        if (gramos >= 1000) {
            subtotal *= 0.90; // 10% de descuento
        }
        return subtotal;
    }

    public double getGramos() {
        return gramos;
    }
}