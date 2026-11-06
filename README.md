# 🌿 Rincón Saludable

## Descripción

Rincón Saludable es un proyecto desarrollado en **Java** que simula un sistema de gestión de productos y pedidos para una tienda de alimentos saludables.

El programa funciona desde la consola y permite administrar productos envasados y a granel, realizar compras y controlar el stock.

## Funcionalidades

- Consultar el catálogo de productos.
- Agregar nuevos productos.
- Modificar el stock disponible.
- Eliminar productos.
- Realizar compras y descontar el stock.
- Generar un ticket con el total del pedido.
- Guardar la información de los productos en una base de datos SQLite.

## Tecnologías utilizadas

- **Java 17:** lenguaje de programación.
- **Eclipse:** entorno de desarrollo.
- **SQLite:** base de datos.
- **Letos:** aplicación utilizada para administrar la base de datos.
- **JDBC:** conexión entre Java y SQLite.
- **Maven:** gestión de dependencias.

## Estructura del proyecto

El sistema utiliza programación orientada a objetos, incluyendo herencia, encapsulamiento, polimorfismo y manejo de excepciones.

Las clases principales son:

- `Main`: menú principal del programa.
- `Producto`: clase base de los productos.
- `ProductoEnvasado` y `ProductoGranel`: tipos de productos.
- `Pedido` y `DetallePedido`: gestión de compras.
- `ConexionBD`: conexión con SQLite.
- `ProductoDAO` y `ProductoDAOImpl`: operaciones sobre la base de datos.

## Ejecución

1. Abrir el proyecto en Eclipse.
2. Actualizar las dependencias de Maven.
3. Verificar la conexión con la base de datos SQLite.
4. Ejecutar `Main.java`.
5. Utilizar el menú de la consola para administrar los productos.

## Objetivo

Aplicar los conocimientos de programación orientada a objetos en Java e integrar una base de datos para almacenar y gestionar productos mediante operaciones CRUD.
