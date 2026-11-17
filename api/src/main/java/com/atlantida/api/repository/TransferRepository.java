// ./src/main/java/com/atlantida/api/repository/TransferRepository.java
package com.atlantida.api.repository;

import com.atlantida.api.model.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    // Opcional para historial de transferencias por cuenta (valor agregado)
    List<Transfer> findBySourceAccountIdOrDestinationAccountId(Long sourceAccountId, Long destinationAccountId);
}