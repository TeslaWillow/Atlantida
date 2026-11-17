// ./src/main/java/com/atlantida/api/repository/AccountRepository.java
package com.atlantida.api.repository;

import com.atlantida.api.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Inherance the CRUD Methods (findAll, findById, save, etc.)
}