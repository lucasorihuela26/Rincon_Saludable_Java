# Rincon_Saludable_Java
💚 Tu bienestar comienza con pequeñas elecciones saludables 💚

# 🌿 Rincón Saludable - Sistema de Gestión E-commerce (Java POO)
> **Materia: Programación Orientada a Objetos ( POO)
> **Entrega: Parcial 1
> **Entorno de Desarrollo: Java 17+ | Eclipse IDE

---
## 📌 Descripción del Proyecto
**Rincón Saludable** es un sistema de consola en Java diseñado para la gestión de ventas
y presupuestos de una tienda de productos naturales, frutos secos, mieles e infusiones.
El objetivo principal de esta aplicación es modelar la lógica de negocio de un e-commerce
aplicando de forma rigurosa los principios y pilares de la **Programación Orientada a
Objetos (POO)**. El sistema permite registrar productos, parametrizar precios según reglas
específicas (descuentos por gramaje a granel o recargos por impuestos en productos
envasados), gestionar el carrito de compras y calcular el total facturado mediante
comportamiento polimórfico.

---
## 🏗️ Arquitectura y Aplicación de Pilares POO El modelo de dominio del sistema está
diseñado bajo los cuatro pilares fundamentales de la POO:

1. **Abstracción:** Se define la superclase abstracta `Producto`, que modela los atributos
esenciales de cualquier artículo de la tienda (`id`, `nombre`, `precioBase`, `stock`) e
independiza el comportamiento general del cálculo específico.

2. **Encapsulamiento:** Todos los atributos de las clases son privados (`private`),
garantizando la integridad de los datos. La lectura y modificación se realiza exclusivamente
a través de métodos públicos *getters* y *setters*.

3. **Herencia:** - `ProductoGranel` extiende de `Producto` (representa la venta por peso, ej.
frutos secos). - `ProductoEnvasado` extiende de `Producto` (representa frascos o
empaques cerrados, ej. aceites y mieles).

4. **Polimorfismo:** La superclase `Producto` declara el método abstracto
`calcularPrecioFinal()`, el cual es redefinido (`@Override`) por cada subclase según su regla
de negocio: - `ProductoGranel`: Otorga un **10% de descuento** si la compra es igual o
superior a 1000 gramos (1 kg). - `ProductoEnvasado`: Suma un **21% de IVA** sobre el
precio base del producto.

--- ## 📐 Diagrama de Clases UML y Relaciones El diseño estático del sistema aplica las
siguientes relaciones de la notación UML:

- **Herencia (Generalización):** `ProductoGranel` y `ProductoEnvasado` heredan de la
clase abstracta `Producto`.

- **Composición (◆):** Entre `Pedido` y `DetallePedido`. Las líneas de detalle existen
únicamente dentro de un pedido. Si el pedido se elimina, sus detalles se destruyen.

- **Agregación (◇):** Entre `DetallePedido` y `Producto`. El producto existe de forma
independiente en el catálogo sin depender de un pedido en particular.

- **Asociación / Control:** La clase principal `Main` gestiona la interacción por consola con
el usuario a través de la clase `Scanner` e interactúa con el objeto `Pedido`.

--- ## 📁 Estructura del Repositorio

```text RinconSaludable/
├── src/
│ └── rinconSaludable/
│ └── Main.java # Código ejecutable con clases POO y menú interactivo
├── docs/
│ └── Diagrama_Clases_UML.png # Diagrama de clases estático en formato UML
└── README.md # Documentación del proyecto
