# 🚀 Guía de Arranque Rápido - Prueba Técnica INFATLAN

Estructura centralizada para levantar los servicios de base de datos, backend (Spring Boot) y frontend (React).

---

## 1. Base de Datos (Docker)

Levantar PostgreSQL 16 y pgAdmin desde la raíz donde se encuentra el `docker-compose.yml`:

```bash
# Iniciar contenedores en segundo plano
docker compose up -d

# Detener contenedores
docker compose down
```

Entrar a travez de:

- pgAdmin: http://localhost:5050
- PostgreSQL: http://localhost:5432

---

## 2. Backend (Spring Boot / Java 21)

```bash
cd backend

# Compilar y arrancar el servidor
mvn spring-boot:run
```

- URL Base: http://localhost:8080

---

## 3. Frontend (React / NPM 22 / Vite)

```bash
cd frontend

# Instalar dependencias (solo la primera vez)
npm install

# Iniciar entorno de desarrollo
npm run dev
```

## Documentación de la API (Endpoints)

Base URL: `http://localhost:8080/api`

### 1. Listar todas las cuentas
Obtiene el catálogo completo de cuentas bancarias registradas junto con su titular y saldo actual.

* **Endpoint:** `GET /accounts`
* **Respuesta Exitosa (`200 OK`):**

```json
[
  {
    "id": 1001,
    "holderName": "Juan Pérez",
    "balance": 5000.00
  },
  {
    "id": 1002,
    "holderName": "María López",
    "balance": 2000.00
  },
  {
    "id": 1003,
    "holderName": "Carlos Gómez",
    "balance": 10000.00
  }
]
```

---

### 2. Consultar detalle de una cuenta
Obtiene la información de una cuenta específica mediante su identificador único.

* **Endpoint:** `GET /accounts/{id}`
* **Ejemplo de Petición:** `GET /api/accounts/1001`
* **Respuesta Exitosa (`200 OK`):**

```json
{
  "id": 1001,
  "holderName": "Juan Pérez",
  "balance": 5000.00
}
```

* **Respuesta de Error (`404 Not Found`):**

```json
{
  "timestamp": "2026-11-17T11:15:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cuenta con ID 9999 no encontrada."
}
```

---

### 3. Registrar una transferencia

Debita el monto de la cuenta origen, lo acredita en la cuenta destino y guarda el registro de la transacción de forma atómica.

* **Endpoint:** `POST /transfers`
* **Headers:** `Content-Type: application/json`
* **Body (Request):**

```json
{
  "sourceAccountId": 1001,
  "destinationAccountId": 1002,
  "amount": 500.00
}
```

* **Respuesta Exitosa (`201 Created`):**

```json
{
  "transferId": 1,
  "sourceAccountId": 1001,
  "sourceHolderName": "Juan Pérez",
  "sourcePreviousBalance": 5000.00,
  "sourceNewBalance": 4500.00,
  "destinationAccountId": 1002,
  "destinationHolderName": "María López",
  "destinationPreviousBalance": 2000.00,
  "destinationNewBalance": 2500.00,
  "amount": 500.00,
  "createdAt": "2026-11-17T11:16:30"
}
```

* **Respuesta de Error - Fondos Insuficientes (`400 Bad Request`):**

```json
{
  "timestamp": "2026-11-17T11:17:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Fondos insuficientes en la cuenta 1001. Los saldos no cambiaron."
}
```

---

## Decisiones Técnicas

### Arquitectura y Diseño

* **Arquitectura en Capas (Controller → Service → Repository):** Separación estricta de responsabilidades. Los controladores gestionan únicamente el protocolo HTTP y validación de entrada (`@Valid`), los servicios encapsulan las reglas de negocio y transacciones, y los repositorios abstraen el acceso a datos mediante Spring Data JPA.

* **Patrón DTO (Data Transfer Objects):** Se desacoplaron las entidades JPA (`Account`, `Transfer`) de los contratos de la API mediante DTOs específicos (`AccountResponseDTO`, `TransferRequestDTO`, `TransferResponseDTO`), evitando exponer el modelo interno y entregando exactamente los datos requeridos por la interfaz (como saldos anteriores y nuevos).

* **Manejo Global de Excepciones (`@RestControllerAdvice`):** Centralización del manejo de errores mediante excepciones de dominio personalizadas (`ResourceNotFoundException`, `InsufficientFundsException`, `InvalidTransferException`) que garantizan respuestas JSON estructuradas y códigos HTTP semánticos (`400`, `404`, `500`).

### Integridad de Datos y Concurrencia

* **Precisión Monetaria (`BigDecimal` / `NUMERIC(18,2)`):** Se utilizó `BigDecimal` en Java mapeado a `NUMERIC(18,2)` en PostgreSQL para evitar errores de redondeo de punto flotante (`double`/`float`).

* **Atomicidad Transaccional (`@Transactional`):** Toda operación de transferencia se ejecuta dentro de una única transacción de base de datos. Ante cualquier validación fallida o excepción en tiempo de ejecución, se ejecuta un *rollback* automático que garantiza que los saldos nunca queden inconsistentes.

* **Control de Concurrencia (Bloqueo Pesimista):** Se implementó `LockModeType.PESSIMISTIC_WRITE` (`SELECT ... FOR UPDATE`) al consultar las cuentas involucradas en una transferencia. Esto bloquea las filas a nivel de motor de base de datos hasta que finaliza la transacción, previniendo condiciones de carrera (*race conditions*) y sobregiros ante peticiones simultáneas sobre una misma cuenta.

* **Doble Capa de Validación:** Las reglas críticas (saldos no negativos, montos mayores a cero, cuentas origen y destino distintas) se validan tanto en la capa de servicio/DTOs para ofrecer mensajes claros al usuario, como mediante restricciones `CHECK` en PostgreSQL como última línea de defensa.

### Base de Datos y Versionado

* **Migraciones con Flyway:** El esquema DDL, los datos semilla iniciales y los índices están versionados de forma inmutable (`V1`, `V2`, `V3`), asegurando reproducibilidad exacta en cualquier entorno.

* **Optimización de Consultas:** Se crearon índices explícitos sobre las llaves foráneas (`source_account_id`, `destination_account_id`) y sobre `created_at DESC` en la tabla `transfers` (ya que PostgreSQL no indexa automáticamente las *Foreign Keys*), optimizando futuras consultas de historial de movimientos.

### Frontend (React + TypeScript)

* **Capa de Servicios Independiente:** Las llamadas HTTP mediante Axios están aisladas en `src/services/` (`accountService`, `transferService`), manteniendo los componentes de UI limpios y enfocados exclusivamente en el estado y renderizado.

* **Tipado Estricto:** Uso de interfaces TypeScript alineadas con los contratos DTO del backend para prevenir errores en tiempo de compilación.