# 📂 Guía de Convenciones y Reglas de Flyway

Este directorio gestiona el versionado estricto del esquema de la base de datos de forma automatizada al arrancar Spring Boot.

---

## 📌 Reglas de Nomenclatura de Archivos
Los scripts deben residir en `db/migration/` y seguir estrictamente este patrón:

```text
V<VERSION>__<DESCRIPCION_BREVE>.sql
```