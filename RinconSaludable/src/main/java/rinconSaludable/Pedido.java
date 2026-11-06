package rinconSaludable;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int idPedido;
    private List<DetallePedido> detalles;

    public Pedido(int idPedido) {
        this.idPedido = idPedido;
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
    }

    public double calcularTotal() {
        double total = 0;
        for (DetallePedido detalle : detalles) {
            total += detalle.calcularSubtotal();
        }
        return total;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public int getIdPedido() {
        return idPedido;
    }
}