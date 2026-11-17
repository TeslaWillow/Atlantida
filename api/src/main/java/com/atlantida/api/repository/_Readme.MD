# 📚 Guía de Convenciones de Spring Data JPA (`repositories`)

Este directorio contiene las interfaces que extienden de `JpaRepository` para interactuar con la base de datos. Spring Data JPA traduce el nombre de los métodos directamente a consultas SQL.

---

## 1. Estructura Base de un Repositorio

```java
package com.atlantida.api.repositories;

import com.atlantida.api.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Los métodos CRUD básicos (save, findById, findAll, delete) ya vienen incluidos.
}
```

---

## 2. Prefijos de Consultas (Query Methods)
El nombre del método **debe** comenzar con uno de estos prefijos seguido de `By` y el nombre exacto del atributo en la entidad Java (en **camelCase**):

* **`findBy...` / `getBy...` / `readBy...`**: Retorna entidades (`Optional<T>`, `List<T>`, o `T`).
* **`existsBy...`**: Retorna un `boolean` (ideal para validar duplicados antes de guardar).
* **`countBy...`**: Retorna un `long` con el total de registros.
* **`deleteBy...`**: Elimina registros según el criterio.

---

## 3. Operadores y Palabras Clave Comunes

| Palabra Clave | Equivalente SQL | Ejemplo en Java |
| :--- | :--- | :--- |
| `And` | `AND` | `findByEmailAndActiveTrue(String email)` |
| `Or` | `OR` | `findByAccountNumberOrId(String accNum, Long id)` |
| `GreaterThan` | `>` | `findByBalanceGreaterThan(BigDecimal amount)` |
| `LessThanEqual` | `<=` | `findByBalanceLessThanEqual(BigDecimal amount)` |
| `Between` | `BETWEEN` | `findByCreatedAtBetween(LocalDateTime start, LocalDateTime end)` |
| `Containing` | `LIKE %val%` | `findByFirstNameContainingIgnoreCase(String name)` |
| `OrderBy...Desc` | `ORDER BY ... DESC` | `findByClientIdOrderByCreatedAtDesc(Long clientId)` |

---

## 4. Navegación por Relaciones (Llaves Foráneas)
Si buscas por un campo de una entidad relacionada (ej. `Account` tiene un atributo `private Client client;`), concatena el nombre de la relación y su atributo:

```java
// Busca cuentas donde client.id sea igual al parámetro
List<Account> findByClientId(Long clientId);

// Busca cuentas donde client.email sea igual al parámetro
List<Account> findByClientEmail(String email);
```

---

## ⚠️ Reglas Críticas para Evitar Errores al Arrancar

1. **Nombres exactos:** El nombre después de `By` debe coincidir **letra por letra** con el atributo de la clase Java (`Entity`), **no** con el nombre de la columna en PostgreSQL (`accountNumber` en Java vs `account_number` en SQL).

2. **Retornos seguros:** Usa `Optional<Entidad>` cuando esperes un único resultado (como buscar por número de cuenta o correo) para evitar `NullPointerException`.

3. **Consultas complejas:** Si un método requiere más de 3 condiciones o un `JOIN` muy complejo, usa `@Query("SELECT ...")` en lugar de crear un nombre de método ilegible.