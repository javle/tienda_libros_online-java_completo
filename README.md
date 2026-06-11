# Libros Online - Plataforma E-Commerce Literaria 📚🚀

Bienvenido al repositorio oficial de **Libros Online**, una plataforma de comercio electrónico de alto nivel diseñada para ofrecer una experiencia premium en la compra de libros, combinando una interfaz de usuario interactiva y moderna con un backend robusto y seguro a nivel empresarial.

## ✨ Características Principales

*   **Catálogo Interactivo y Landing Page**: Una experiencia visual inmersiva con *Dark Mode* integrado, componentes *Glassmorphism*, diseño responsivo, filtrado dinámico mediante etiquetas (chips) y un motor de recomendaciones (Quiz Interactivo vía Fetch API).
*   **Carrito de Compras y Pedidos**: Flujo completo de compra (Checkout) con cálculo de totales exactos utilizando `BigDecimal`, almacenamiento seguro en sesión, persistencia de compras y deducción/restauración inteligente de stock.
*   **Panel Admin Premium**: Dashboard administrativo protegido con Spring Security, que incluye listado de inventario con mini-portadas (thumbnails), alertas visuales para stock bajo (<5) y un **Feed de Actividad** interactivo para gestionar los últimos pedidos de los clientes.
*   **Sistema de Reseñas de la Comunidad**: Los usuarios registrados pueden calificar los libros con estrellas (1-5) y dejar opiniones visibles en una página de Detalles del Producto sumamente detallada.

## 🛠️ Stack Tecnológico

El proyecto está construido utilizando las tecnologías más sólidas y modernas de la industria Java:

*   **Lenguaje**: Java 17
*   **Framework**: Spring Boot 3.5
*   **Seguridad**: Spring Security 6.x (con Bcrypt y control de accesos por roles `ADMIN`/`CLIENTE`)
*   **Motor de Plantillas**: Thymeleaf (con integración directa de seguridad)
*   **Persistencia**: Spring Data JPA / Hibernate
*   **Base de Datos**: MySQL 8.0 (y H2 en memoria para pruebas)
*   **Contenerización**: Docker & Docker Compose
*   **Diseño Web**: HTML5, CSS3 Vainilla, JS, animaciones mediante `IntersectionObserver`.

## 🛡️ Suite de Pruebas Robusta

Se implementó una fase de testing profunda y estructurada garantizando un 100% de éxito en todos los escenarios probados, utilizando:

*   **JUnit 5** y **Mockito**: Para aislar la capa de servicios y verificar exhaustivamente la lógica del `CarritoService` (duplicados, totales exactos) y el `PedidoService` (descuento de stock en checkout, excepciones de carrito vacío, lógica de restock en cancelación).
*   **MockMvc / Spring Security Test**: Para blindar la capa de controladores, inyectando `CustomUserDetails` y certificando que los usuarios anónimos sean redirigidos, los usuarios estándar (`CLIENTE`) reciban un contundente `403 Forbidden` al intentar invadir rutas del `/admin`, y solo el Administrador pueda acceder a su Dashboard.

## 🚀 Cómo Ejecutar el Proyecto (Vía Docker)

El proyecto está completamente dockerizado utilizando un *Multi-Stage Build*, lo que significa que no necesitas tener Maven ni Java instalados en tu máquina local. Docker se encarga de empaquetar y levantar todo el ecosistema.

1. **Clonar el repositorio y ubicarse en la raíz**:
   ```bash
   git clone <url-del-repositorio>
   cd tienda_libros_online-java_completo
   ```

2. **Levantar la Infraestructura**:
   Asegúrate de que Docker Desktop o tu motor de Docker esté en ejecución y corre el siguiente comando:
   ```bash
   docker-compose up --build
   ```
   *Nota: El archivo `docker-compose.yml` cuenta con un `healthcheck` que garantiza que la base de datos MySQL 8.0 se inicialice completamente antes de arrancar la aplicación de Spring Boot.*

3. **Acceder a la Tienda**:
   Una vez que los contenedores estén corriendo (`librosonline-app` y `librosonline-db`), abre tu navegador y visita:
   [http://localhost:8080](http://localhost:8080)

¡Disfruta navegando, comprando y gestionando esta plataforma premium!
