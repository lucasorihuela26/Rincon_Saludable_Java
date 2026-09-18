package rinconSaludable;

import java.util.ArrayList; 
import java.util.List; 
import java.util.Scanner;

//============================================================================ 
// 1\. CLASE ABSTRACTA (ABSTRACCIÓN Y SUPERCLASE) 
// ===========================================================================

abstract class Producto { 
	private String id; 
	private String nombre; 
	private double precioBase; 
	private int stock;
	
	public Producto(String id, String nombre, double precioBase, int stock) { 
		this.id = id; 
		this.nombre = nombre; 
		this.precioBase = precioBase; 
		this.stock = stock; }


//Método polimórfico abstracto (Cada subclase lo redefinirá con su regla) 
	public abstract double calcularPrecioFinal();

	// Getters y Setters Encapsulados 
		public String getId() { return id; } 
		public String getNombre() { return nombre; } 
		public double getPrecioBase() { return precioBase; } 
		public int getStock() { return stock; } 
		public void setStock(int stock) { this.stock = stock; } 
		}

// ============================================================================ 
// 2\. SUBCLASE 1 (HERENCIA Y POLIMORFISMO: Descuento a Granel) 
// ============================================================================
		
	class ProductoGranel extends Producto {
		private double gramos;		
		
		public ProductoGranel(String id, String nombre, double precioPorKilo, int stock, double gramos) { 
			super(id, nombre, precioPorKilo, stock); this.gramos = gramos; }	
		
		
	@Override
	public double calcularPrecioFinal() { 
		double subtotal = (getPrecioBase() / 1000.0) * gramos;
		// Regla de negocio: 10% de descuento si lleva 1000g (1kg) o más
		if (gramos >= 1000) { 
			subtotal *= 0.90; } 
		return subtotal; 
		}	
		
	public double getGramos() { return gramos; } 
	}		

// ============================================================================ 
// 3\. SUBCLASE 2 (HERENCIA Y POLIMORFISMO: Recargo de IVA Envasado) 
// ============================================================================

	class ProductoEnvasado extends Producto { 
		private double impuestoIVA = 0.21;
	
		public ProductoEnvasado(String id, String nombre, double precioBase, int stock) { 
			super(id, nombre, precioBase, stock); }
		
	@Override public double calcularPrecioFinal() { 
		// Calcula el precio base sumándole el 21% de IVA 
		return getPrecioBase() * (1 + impuestoIVA); } 
		}
	
// ============================================================================
// 4\. CLASE DETALLE DE PEDIDO (AGREGACIÓN con Producto) 
// ============================================================================	
	
	class DetallePedido { 
		private Producto producto; 
		// Agregación: El producto existe independientemente del pedido 
		private int cantidad;	
	
		public DetallePedido(Producto producto, int cantidad) { 
		this.producto = producto; 
		this.cantidad = cantidad; 
		}
	
		public double calcularSubtotal() { 
			return producto.calcularPrecioFinal() * cantidad; 
			}
	
		public Producto getProducto() { return producto; } 
		public int getCantidad() { return cantidad; } 
		}
	
// ============================================================================ 
// 5\. CLASE PEDIDO (COMPOSICIÓN con DetallePedido) 
// ============================================================================	
	
	class Pedido { private int idPedido; 
	private List<DetallePedido> detalles; // Composición: El detalle no existe sin el pedido
	
	public Pedido(int idPedido) { 
	this.idPedido = idPedido; 
	this.detalles = new ArrayList<>(); 
	}
	
	public void agregarDetalle(Producto producto, int cantidad) { 
		this.detalles.add(new DetallePedido(producto, cantidad)); 
		}
	
	public double calcularTotal() { 
		double total = 0; 
		for (DetallePedido detalle : detalles) { 
			total += detalle.calcularSubtotal(); // Ejecuta el cálculo polimórfico 
			} 
			return total; 
			}
	
		public List<DetallePedido> getDetalles() {return detalles; }
		public int getIdPedido() { return idPedido; } 
		}
	
// ============================================================================ 
// 6\. CLASE PRINCIPAL CON SCANNER Y MENÚ INTERACTIVO 
// ============================================================================	
	
	public class Main { 
		public static void main(String[] args) { 
			Scanner scanner = new Scanner(System.in); 
			Pedido miPedido = new Pedido(1001);	
	
	System.out.println("=================================================="); 
	System.out.println(" ¡BIENVENIDO A RINCÓN SALUDABLE (SISTEMA POO)! "); 
	System.out.println("==================================================");
	
	int opcion = 0; 
	do {
	
		System.out.println("\\n----------------- MENÚ PRINCIPAL -----------------"); 
		System.out.println("1. Agregar Frutos Secos (A Granel - 10% desc. &gt;= 1000g)"); 
		System.out.println("2. Agregar Frasco Envasado (Aceite/Miel - Con IVA 21%)"); 
		System.out.println("3. Ver Carrito / Factura Detallada (Polimorfismo)"); 
		System.out.println("4. Salir"); 
		System.out.print("Seleccioná una opción (1-4): ");

	if (scanner.hasNextInt()) { 
		opcion = scanner.nextInt(); 
		} else { scanner.next(); // Limpia la entrada si no es un número 
		opcion = 0; 
		}	
	
	switch (opcion) { 
		case 1: 
			System.out.print("\\nIngrese los gramos de Almendras Nativas ($8000/kg) a comprar (ej: 500 o 1200): "); 
			double gramos = scanner.nextDouble(); 
			// Instanciamos ProductoGranel (Subclase) 
			Producto almendras = new ProductoGranel("P01", "Almendras Nativas", 8000.0, 50, gramos); 
			miPedido.agregarDetalle(almendras, 1); 
			System.out.println("-&gt; [ÉXITO] Almendras a granel agregadas al pedido."); 
			break;	
	
		case 2:
			System.out.print("\\nIngrese la cantidad de frascos de Aceite de Coco ($4500 c/u + IVA): "); 
			int cantidad = scanner.nextInt(); // Instanciamos ProductoEnvasado (Subclase) 
			Producto aceite = new ProductoEnvasado("P02", "Aceite de Coco Orgánico", 4500.0, 20); 
			miPedido.agregarDetalle(aceite, cantidad); 
			System.out.println("-&gt; [ÉXITO] Aceite de Coco agregado al pedido."); 
			break;
			
		case 3: 
			System.out.println("\\n================ FACTURA DEL PEDIDO #" + miPedido.getIdPedido() + " ================"); 
			if (miPedido.getDetalles().isEmpty()) { 
				System.out.println("El carrito está vacío. Agregá algún producto primero."); 
				} else { 
					for (DetallePedido d : miPedido.getDetalles()) { 
						System.out.printf("- %-25s | Cant: %d | Subtotal: $%.2f\\n", 
								d.getProducto().getNombre(), 
								d.getCantidad(), 
								d.calcularSubtotal()); } 
					System.out.println("--------------------------------------------------"); 
					System.out.printf("TOTAL FINAL CALCULADO: $%.2f\\n", miPedido.calcularTotal()); } 
			System.out.println("=================================================="); 
			break;
		
		case 4: 
			System.out.println("\\n¡Gracias por usar el sistema de Rincón Saludable! Cerrando..."); 
				break; 
			
		default: 
			System.out.println("\\n[!] Opcion no valida. Ingrese un numero entre 1 y 4."); 
			break; 
		} 
	
	} while (opcion != 4); // &lt;--- Asegúrate de tener esta línea completa con el ; al final 
	
		scanner.close(); 
		} // &lt;--- Cierra el método main 
} // &lt;--- Cierra la clase Main