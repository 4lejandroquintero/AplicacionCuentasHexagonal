# Aplicacion Cuentas Hexagonal: guia para sustentar el proyecto

Ubicacion del codigo en tu disco: `C:\AplicacionCuentasHexagonal` (raiz del disco `C:`).

Este documento resume la idea del sistema, la arquitectura hexagonal y como estan repartidas las piezas en el backend (Spring Boot) y el frontend (Angular). Sirve para explicarlo en una entrevista tecnica con lenguaje claro y ordenado.

## Que problema resuelve

Es un nucleo de cuentas simplificado: crear cuentas, listarlas, consultar una por identificador, consignar y retirar dinero respetando reglas de negocio (por ejemplo, no retirar mas del saldo disponible). El dominio esta pensado como un bounded context pequeno pero serio: suficiente para mostrar modelo rico, puertos y pruebas de arranque.

## Arquitectura hexagonal (puertos y adaptadores)

La arquitectura hexagonal separa el nucleo de la aplicacion de los detalles externos. El nucleo no conoce HTTP, JPA ni bases de datos; solo define contratos (puertos) y reglas (dominio). Los adaptadores conectan ese nucleo con el mundo real.

### Dominio (reglas puras)

Ubicacion en Java: `com.aplicacion.cuentas.cuenta.domain`.

- `Account` es el agregado principal. Encapsula saldo, titular, estado y numero de cuenta. Las operaciones `consignar` y `retirar` aplican invariantes: una cuenta cerrada no opera; un retiro que supera el saldo lanza una excepcion de dominio.
- `Money` modela montos con escala monetaria fija y valida que no se mezclen monedas en una misma operacion.
- `AccountId` identifica la cuenta de forma estable (UUID).
- Excepciones como `InsufficientFundsException` expresan violaciones de reglas de negocio sin depender de infraestructura.

Punto clave para la entrevista: el dominio no importa frameworks. Si manana cambias JPA por JDBC o el API por gRPC, estas clases siguen igual.

### Puertos de entrada (casos de uso)

Ubicacion: `com.aplicacion.cuentas.cuenta.domain.port.in`.

Son interfaces que describen lo que la aplicacion permite hacer desde fuera: crear cuenta, consultar, listar, consignar, retirar. La capa de aplicacion implementa estos contratos.

### Puertos de salida (lo que el dominio necesita del exterior)

Ubicacion: `com.aplicacion.cuentas.cuenta.domain.port.out`.

- `AccountRepositoryPort` abstrae la persistencia. El dominio y la aplicacion dependen de esta interfaz, no de Spring Data.
- `AccountNumberGeneratorPort` abstrae la generacion del numero de cuenta. Asi puedes cambiar la estrategia (secuencial, aleatoria, integracion con otro sistema) sin tocar reglas de negocio.

### Capa de aplicacion (orquestacion)

Ubicacion: `com.aplicacion.cuentas.cuenta.application`.

`CuentaApplicationService` implementa los puertos de entrada y coordina: obtiene entidades del repositorio, invoca metodos del dominio y persiste el resultado. Los comandos (`CrearCuentaCommand`, `MovimientoCommand`) agrupan datos de entrada sin mezclarlos con detalles HTTP.

Clase de arranque Spring Boot: `com.aplicacion.cuentas.AplicacionCuentasApplication`.

### Adaptadores de salida (infraestructura)

- Persistencia JPA: `adapter.out.persistence` contiene entidades JPA, repositorio Spring Data y `CuentaPersistenceAdapter`, que implementa `AccountRepositoryPort` y traduce entre modelo de dominio y filas de base de datos.
- Numeracion: `NumeroCuentaGeneradorAdapter` implementa `AccountNumberGeneratorPort`.

### Adaptadores de entrada (API REST)

- `CuentaRestController` recibe peticiones HTTP, valida DTOs y delega en los puertos de entrada. No contiene reglas de negocio; solo traduce HTTP a comandos y respuestas a JSON.
- `ApiExceptionHandler` traduce excepciones de dominio y de aplicacion a respuestas HTTP coherentes (por ejemplo, 404 para cuenta inexistente, 422 para fondos insuficientes).

### Configuracion y datos

- `application.yml` define conexion a PostgreSQL y comportamiento de JPA para desarrollo (usuario, base y nombre de aplicacion alineados con este proyecto).
- `docker-compose.yml` levanta PostgreSQL con usuario, clave y base alineados con esa configuracion.
- Perfil `test` con H2 en memoria permite ejecutar el contexto de Spring en pruebas sin depender de Docker.

## API REST (contrato estable)

Base: `http://localhost:8080`

- `POST /api/v1/cuentas` crea una cuenta (cuerpo JSON con `nombreTitular` y `codigoMoneda`).
- `GET /api/v1/cuentas` lista cuentas.
- `GET /api/v1/cuentas/{id}` obtiene una cuenta.
- `POST /api/v1/cuentas/{id}/consignaciones` acredita saldo (`monto` positivo).
- `POST /api/v1/cuentas/{id}/retiros` debita saldo respetando disponibilidad.

CORS permite el origen del frontend de desarrollo en `http://localhost:4200`.

## Frontend Angular (hexagonal ligero)

Version: Angular 21.x.

Estructura:

- `cuenta/domain`: tipos de vista (`Cuenta`, payloads) sin dependencias de framework.
- `cuenta/ports`: `CuentaRepositoryPort` define el contrato de acceso a datos (listar, obtener, crear, movimientos).
- `cuenta/adapters`: `CuentaHttpRepository` implementa el puerto usando `HttpClient` contra el API REST. La URL base esta en `api-base-url.ts`.
- `app.config.ts` registra el token de inyeccion `CUENTA_REPOSITORY` apuntando a la implementacion HTTP. Las pantallas solo conocen el puerto, no la clase concreta.

Las paginas bajo `cuenta/pages` consumen el puerto inyectado y mantienen la UI simple: listado, alta y detalle con formularios para movimientos.

## Como ejecutarlo en local

1. Abrir una terminal en `C:\AplicacionCuentasHexagonal`.
2. Base de datos: `docker compose up -d`.
3. Backend: carpeta `backend`, ejecutar `mvnw.cmd spring-boot:run` (Windows).
4. Frontend: carpeta `frontend`, ejecutar `npm start` y abrir `http://localhost:4200`.

## Ideas para conversar en entrevista

- Por que hexagonal: testear dominio sin base de datos, cambiar adaptadores sin reescribir reglas, alinear equipos con contratos claros (puertos).
- Diferencia entre validacion de entrada (DTOs y `@Valid`) y reglas de dominio (saldo, estado de cuenta).
- Como evolucionaria el modelo: movimientos como entidad separada, auditoria, idempotencia en pagos, segregacion de lectura/escritura.

Este proyecto esta pensado como referencia tecnica: codigo legible, capas reconocibles y un dominio de cuentas con reglas claras, sin pretender ser un sistema productivo completo.
