# POS Abarrotes

MVP funcional de un sistema de Punto de Venta para una tienda de abarrotes. El sistema usa backend Spring Boot con MySQL y frontend HTML/CSS/JavaScript modular con Bootstrap 5. La experiencia principal ahora está centrada en la pantalla de venta de escritorio: después del login el usuario entra directamente a `venta.html` para buscar productos, agregar al carrito, cobrar y registrar ventas.

## Tecnologías usadas

- Java 17+
- Spring Boot 3.x
- Maven
- Spring Web
- Spring Data JPA
- Bean Validation
- MySQL Driver
- MySQL
- HTML, CSS y JavaScript modular con estructura MVC
- Bootstrap 5 desde CDN
- Escáner con `BarcodeDetector` API y fallback con ZXing Browser

## Estructura del proyecto

```text
pos-abarrotes/
├── backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/abarrotes/pos/
│       │   ├── config/
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
5. Cámara disponible si se quiere probar el escáner.

## Crear la base de datos

En MySQL, crear la base de datos antes de iniciar el backend:

```sql
CREATE DATABASE punto_venta_abarrotes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Las tablas se generan por JPA con `spring.jpa.hibernate.ddl-auto=update`. Los datos iniciales se insertan desde `pos-abarrotes/backend/src/main/resources/data.sql` con `INSERT IGNORE` para facilitar reinicios.

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

El backend escucha en:

```text
http://localhost:8080
```

## Ejecutar frontend

Desde la carpeta del frontend:

```bash
cd pos-abarrotes/frontend
python3 -m http.server 5500 --bind 0.0.0.0
```

Abrir en computadora:

```text
http://localhost:5500/login.html
```

Abrir desde celular en la misma red, reemplazando la IP por la de tu computadora:

```text
http://192.168.0.8:5500/login.html
```

## Configurar `API_BASE_URL` para celular

La URL base está centralizada en `pos-abarrotes/frontend/js/services/api.js`:

```js
export const API_BASE_URL = "http://localhost:8080/api";
```

Para probar desde un celular en la misma red, cambia temporalmente a la IP del equipo que ejecuta Spring Boot:

```js
export const API_BASE_URL = "http://192.168.0.8:8080/api";
```

## Usuario de prueba

- Usuario: `admin`
- Contraseña: `admin123`
- Rol: `Administrador`

## Flujo recomendado de venta

1. Iniciar MySQL y el backend.
2. Servir el frontend con Python.
3. Abrir `http://localhost:5500/login.html`.
4. Iniciar sesión con `admin / admin123`.
5. El sistema redirige automáticamente a `venta.html`.
6. Si no hay caja abierta, entrar a `Caja` y abrir la Caja 1.
7. Volver a `Venta`.
8. Buscar producto por código de barras o por nombre.
9. Opcional: usar `Escanear código` para leer el código con la cámara.
10. Agregar productos al carrito.
11. Cambiar cantidades o eliminar productos si es necesario.
12. Elegir método de pago, capturar monto recibido y revisar el cambio.
13. Presionar `Cobrar y finalizar venta`.
14. Confirmar que aparece el folio de venta y que el carrito queda listo para otra venta.
15. Validar en Inventario que la existencia disminuyó.

## Escáner de código de barras

La pantalla `venta.html` incluye un botón `Escanear código` que abre un modal con la cámara.

El flujo del escáner es:

1. Solicita permiso de cámara.
2. Intenta usar `BarcodeDetector` si el navegador lo soporta.
3. Si no está disponible, usa ZXing Browser desde CDN.
4. Detecta formatos comunes como EAN-13, EAN-8, UPC-A, UPC-E y CODE-128.
5. Coloca el código detectado en el input de código.
6. Busca el producto automáticamente y lo agrega al carrito.
7. Detiene la cámara al cerrar el modal.

### Limitaciones conocidas del escáner

- La cámara requiere `localhost` o HTTPS en muchos navegadores.
- En celular puede requerir servir el frontend por IP local y configurar `API_BASE_URL` con la IP del backend.
- Algunos navegadores no soportan `BarcodeDetector`; en ese caso se usa ZXing.
- Si no hay permiso de cámara, no hay cámara física o el CDN de ZXing no carga, se debe capturar el código manualmente.

## Endpoints principales

### Auth

- `POST /api/auth/login`

### Productos

- `GET /api/productos`
- `GET /api/productos?buscar=texto`
- `GET /api/productos/buscar?texto=texto`
- `GET /api/productos/{id}`
- `GET /api/productos/codigo/{codigoBarras}`
- `POST /api/productos`
- `PUT /api/productos/{id}`
- `DELETE /api/productos/{id}`: baja lógica con `activo = false`.

### Métodos de pago

- `GET /api/metodos-pago`

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

## Reglas de negocio implementadas

- Login básico sin JWT para el MVP.
- Después del login se redirige a `venta.html`.
- No se borra producto físicamente; se desactiva.
- No se venden productos inactivos.
- No se vende si no hay turno de caja abierto.
- No se abre una caja si ya tiene turno abierto.
- La venta usa transacción con `@Transactional`.
- El backend calcula subtotal, impuestos y total.
- Se valida stock suficiente y el inventario no queda negativo.
- Cada venta genera folio `V-000001` incremental.
- Cada venta descuenta inventario y registra movimiento `VENTA`.
- Los ajustes manuales registran movimiento de inventario.
- CORS está habilitado para `/api/**` durante el MVP.

## Pendiente fuera del MVP

- JWT y seguridad avanzada.
- Compras a proveedores.
- Proveedores.
- Devoluciones.
- Caducidades y lotes.
- Auditoría avanzada.
- Reportes gráficos.
- Facturación.
- Modo offline/PWA completa.
