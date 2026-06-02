# POS Abarrotes

MVP funcional de un sistema de Punto de Venta Web Móvil para una tienda de abarrotes. El proyecto separa backend y frontend con una arquitectura MVC sencilla: vistas HTML/CSS, controladores JavaScript, servicios `fetch`, controladores REST Spring Boot, servicios de negocio, repositorios JPA, entidades y MySQL.

## Tecnologías usadas

- Java 17+
- Spring Boot 3.x
- Maven
- Spring Web
- Spring Data JPA
- Bean Validation
- MySQL Driver
- MySQL
- HTML, CSS y JavaScript modular
- Bootstrap 5 desde CDN

## Estructura del proyecto

```text
pos-abarrotes/
├── backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/abarrotes/pos/
│       │   ├── controller/
│       │   ├── service/
│       │   ├── model/entity/
│       │   ├── model/repository/
│       │   ├── model/dto/
│       │   ├── exception/
│       │   └── PosApplication.java
│       └── resources/
│           ├── application.properties
│           └── data.sql
└── frontend/
    ├── index.html
    ├── login.html
    ├── menu.html
    ├── productos.html
    ├── inventario.html
    ├── caja.html
    ├── venta.html
    ├── css/styles.css
    └── js/
        ├── services/
        ├── models/
        ├── views/
        └── controllers/
```

## Requisitos

1. Java 17 o superior.
2. Maven.
3. MySQL Server.
4. Navegador web moderno.
5. Visual Studio Code opcional para editar y servir archivos estáticos.

## Crear la base de datos

En MySQL, crear la base de datos antes de iniciar el backend:

```sql
CREATE DATABASE punto_venta_abarrotes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Las tablas se generan por JPA con `spring.jpa.hibernate.ddl-auto=update`. Los datos iniciales se insertan desde `backend/src/main/resources/data.sql` con `INSERT IGNORE` para facilitar reinicios.

## Configurar `application.properties`

Editar `pos-abarrotes/backend/src/main/resources/application.properties` y reemplazar la contraseña:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/punto_venta_abarrotes?useSSL=false&serverTimezone=America/Mexico_City&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

## Ejecutar backend

```bash
cd pos-abarrotes/backend
mvn spring-boot:run
```

Para compilar:

```bash
cd pos-abarrotes/backend
mvn clean package
```

El backend escucha en `http://localhost:8080`.

## Abrir frontend

Opción rápida desde la carpeta del frontend:

```bash
cd pos-abarrotes/frontend
python3 -m http.server 5500
```

Luego abrir:

```text
http://localhost:5500/login.html
```

También puedes abrir `login.html` desde VS Code con una extensión tipo Live Server.

## Usuario inicial

- Usuario: `admin`
- Contraseña: `admin123`
- Rol: `Administrador`

## Datos iniciales

El sistema incluye roles, usuario administrador, sucursal principal, Caja 1, categorías, unidades, impuestos, métodos de pago y productos de prueba con inventario inicial:

- Coca Cola 600ml
- Sabritas Original
- Leche Lala 1L
- Galletas Marías
- Jabón Zote

## Endpoints principales

### Auth

- `POST /api/auth/login`

### Productos

- `GET /api/productos`
- `GET /api/productos/{id}`
- `GET /api/productos/codigo/{codigoBarras}`
- `POST /api/productos`
- `PUT /api/productos/{id}`
- `DELETE /api/productos/{id}`: baja lógica con `activo = false`.

### Inventario

- `GET /api/inventario`
- `GET /api/inventario/producto/{idProducto}`
- `GET /api/inventario/bajo-stock`
- `POST /api/inventario/ajuste`

Ejemplo de ajuste:

```json
{
  "idSucursal": 1,
  "idProducto": 1,
  "idUsuario": 1,
  "tipoMovimiento": "ENTRADA",
  "cantidad": 5,
  "motivo": "Carga inicial extra"
}
```

### Caja

- `POST /api/caja/abrir`
- `POST /api/caja/cerrar`
- `GET /api/caja/turno-activo/{idCaja}`

### Ventas

- `POST /api/ventas`
- `GET /api/ventas`
- `GET /api/ventas/{id}`
- `GET /api/ventas/turno/{idTurno}`

Ejemplo de venta:

```json
{
  "idSucursal": 1,
  "idCaja": 1,
  "idTurno": 1,
  "idUsuario": 1,
  "detalles": [
    { "idProducto": 1, "cantidad": 2 },
    { "idProducto": 2, "cantidad": 1 }
  ],
  "pagos": [
    { "idMetodoPago": 1, "monto": 100.00, "referencia": null, "cambio": 20.00 }
  ]
}
```

## Flujo de prueba manual

1. Iniciar MySQL y crear la base de datos.
2. Configurar contraseña de MySQL en `application.properties`.
3. Ejecutar backend con `mvn spring-boot:run`.
4. Abrir `frontend/login.html` mediante un servidor estático.
5. Iniciar sesión con `admin / admin123`.
6. Ir a Caja y abrir Caja 1 con monto inicial.
7. Ir a Productos y registrar o consultar productos.
8. Ir a Inventario y validar existencias o hacer ajustes de entrada/salida.
9. Ir a Vender, buscar productos, agregar al carrito, capturar pago y finalizar venta.
10. Confirmar que el inventario baja en la pantalla de Inventario.

## Reglas de negocio implementadas

- Login básico sin JWT para el MVP.
- No se borra producto físicamente; se desactiva.
- No se venden productos inactivos.
- No se vende si no hay turno de caja abierto.
- No se abre una caja si ya tiene turno abierto.
- La venta usa transacción con `@Transactional`.
- El backend calcula subtotal, impuestos y total.
- Se valida stock suficiente y el inventario no queda negativo.
- Cada venta genera folio `V-000001`.
- Cada venta descuenta inventario y registra movimiento `VENTA`.
- Los ajustes manuales registran movimiento de inventario.

## Pendiente fuera del MVP

- JWT y seguridad avanzada.
- Compras a proveedores.
- Proveedores.
- Devoluciones.
- Caducidades y lotes.
- Auditoría avanzada.
- Reportes gráficos.
- Facturación.
- PWA offline completa.
