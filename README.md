# FitLab Backend

API de la tienda FitLab hecha con Spring Boot y MongoDB Atlas para la Evaluación Parcial 3 de Desarrollo Fullstack II. Maneja productos, categorías, usuarios con roles y boletas de compra. El login funciona con tokens JWT.

El frontend que usa esta API está en el repositorio nota2, rama `ep3-integracion`.

API publicada en Railway: https://fitlab-backend-production.up.railway.app

## Requisitos

- JDK 17 o superior
- Maven Wrapper (viene en el proyecto)
- Base de datos en MongoDB Atlas

## Clonar y ejecutar

```powershell
git clone https://github.com/pancho5698/backend.git
cd backend
$env:MONGODB_URI="mongodb+srv://<usuario>:<clave>@<cluster>.mongodb.net/fitlab"
.\mvnw.cmd spring-boot:run
```

Si arranca bien aparece el mensaje `Started FitlabApplication` en la consola.

## Configuración

La configuración está en `src/main/resources/application.properties`. Los datos sensibles se leen desde variables de entorno para no subir claves a GitHub:

- `MONGODB_URI`: conexión a Atlas (obligatoria)
- `JWT_SECRET`: clave para firmar los tokens
- `ADMIN_CLAVE`: clave de los usuarios iniciales (en local es `admin123`)

Al arrancar se crean los usuarios `admin@fitlab.cl` (ADMIN) y `vendedor@fitlab.cl` (VENDEDOR) si no existen.

Para cargar los productos se puede importar `src/main/resources/data/productos.json` en la colección `productos` de la base `fitlab`.

## Explorar la API con Swagger

1. Inicia el backend.
2. Abre http://localhost:8080/swagger-ui.html
3. En Autenticación ejecuta `POST /api/v1/auth/login` y copia el token.
4. Pulsa Authorize, pega el token y prueba las rutas protegidas.

## Roles

- ADMIN: puede todo (productos, categorías, usuarios y boletas).
- VENDEDOR: ve los productos y las boletas.
- CLIENTE: ve los productos y puede comprar.

Sin token las rutas protegidas responden 401 y si el rol no tiene permiso responden 403.

## Endpoints

Todas las rutas parten con `/api/v1`.

| Método | Ruta | Acceso |
| --- | --- | --- |
| POST | /auth/login | Público |
| POST | /auth/registro | Público |
| POST | /auth/refresh | Con token |
| GET | /productos y /productos/{id} | Público |
| POST, PUT, DELETE | /productos | ADMIN |
| GET | /categorias | Público |
| POST | /categorias | ADMIN |
| GET, POST, PUT, DELETE | /usuarios | ADMIN |
| POST | /boletas | Con token |
| GET | /boletas y /boletas/{id} | ADMIN y VENDEDOR |

## Organización

- `controller`: rutas de la API y documentación para Swagger.
- `service`: lógica de negocio y validaciones.
- `repository`: acceso a MongoDB con Spring Data.
- `model`: clases que se guardan en las colecciones.
- `dto`: datos que llegan en el login y en la compra.
- `security`: configuración de Spring Security y del JWT.
- `config`: usuarios iniciales.

## Pruebas

```powershell
.\mvnw.cmd test
```

Las pruebas usan JUnit y Mockito y no necesitan la base de datos.

## Generar el JAR

```powershell
.\mvnw.cmd clean package
```

El JAR queda en `target/`.
