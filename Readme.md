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