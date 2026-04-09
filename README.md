# Tienda de Libros Online 

Proyecto Spring Boot  del sistema, incluyendo:

- Autenticación básica con sesión (`/login`, `/registro`, `/logout`)
- Catálogo de libros (`/libros`)
- Carrito de compras (`/carrito`)
- Gestión de pedidos (`/pedidos`)
- Panel de administración (`/admin`)
- CRUD de libros para administrador
- MariaDB con Docker Compose
- Datos iniciales automáticos (usuario admin y libros de ejemplo)

## Credenciales iniciales

- Usuario administrador: `admin`
- Clave: `admin123`

También puedes registrar un usuario cliente desde `/registro`.

## Rutas principales

- `/` Inicio
- `/login` Inicio de sesión
- `/registro` Registro
- `/libros` Catálogo
- `/carrito` Carrito de compras
- `/pedidos` Mis pedidos
- `/admin` Panel de administración

## Cómo ejecutar

```bash
docker compose up -d
./mvnw spring-boot:run
```

Luego abre `http://localhost:8080`


