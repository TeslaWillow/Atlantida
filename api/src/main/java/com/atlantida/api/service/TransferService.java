// ./src/main/java/com/atlantida/api/service/TransferService.java
package com.atlantida.api.service;

import com.atlantida.api.dto.TransferRequestDTO;
import com.atlantida.api.dto.TransferResponseDTO;
import com.atlantida.api.exception.InsufficientFundsException;
import com.atlantida.api.exception.InvalidTransferException;
import com.atlantida.api.exception.ResourceNotFoundException;
import com.atlantida.api.model.entity.Account;
import com.atlantida.api.model.entity.Transfer;
import com.atlantida.api.repository.AccountRepository;
import com.atlantida.api.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    @Transactional
    public TransferResponseDTO executeTransfer(TransferRequestDTO request) {
        // 1. Validate that source and destination accounts are not the same
        if (request.getSourceAccountId().equals(request.getDestinationAccountId())) {
            throw new InvalidTransferException("La cuenta de origen y destino no pueden ser la misma.");
        }

        // 2. Search for both accounts in the database (with locking to prevent concurrent modifications)
        Account sourceAccount = accountRepository.findByIdWithLock(request.getSourceAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("La cuenta de origen con ID " + request.getSourceAccountId() + " no existe."));

        Account destinationAccount = accountRepository.findByIdWithLock(request.getDestinationAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("La cuenta de destino con ID " + request.getDestinationAccountId() + " no existe."));

        // Save previous balances for response
        BigDecimal sourcePrevBalance = sourceAccount.getBalance();
        BigDecimal destPrevBalance = destinationAccount.getBalance();

        // 3. Validar fondos suficientes
        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Fondos insuficientes en la cuenta " + sourceAccount.getId() + ". Los saldos no cambiaron.");
        }

        // 4. Apply business logic: Deduct from source and add to destination
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // 5. Register the transfer in the Transfer table
        Transfer transfer = Transfer.builder()
                .sourceAccountId(sourceAccount.getId())
                .destinationAccountId(destinationAccount.getId())
                .amount(request.getAmount())
                .build();

        Transfer savedTransfer = transferRepository.save(transfer);

        // 6. Construct the response DTO with all required details
        return TransferResponseDTO.builder()
                .transferId(savedTransfer.getId())
                .sourceAccountId(sourceAccount.getId())
                .sourceHolderName(sourceAccount.getHolderName())
                .sourcePreviousBalance(sourcePrevBalance)
                .sourceNewBalance(sourceAccount.getBalance())
                .destinationAccountId(destinationAccount.getId())
                .destinationHolderName(destinationAccount.getHolderName())
                .destinationPreviousBalance(destPrevBalance)
                .destinationNewBalance(destinationAccount.getBalance())
                .amount(request.getAmount())
                .createdAt(savedTransfer.getCreatedAt())
                .build();
    }
}