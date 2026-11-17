// ./src/main/java/com/atlantida/api/service/AccountService.java
package com.atlantida.api.service;

import com.atlantida.api.dto.AccountResponseDTO;
import com.atlantida.api.exception.ResourceNotFoundException;
import com.atlantida.api.model.entity.Account;
import com.atlantida.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    public List<AccountResponseDTO> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AccountResponseDTO findById(Long id) {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta con ID " + id + " no encontrada."));
        return mapToDTO(account);
    }

    private AccountResponseDTO mapToDTO(Account account) {
        return AccountResponseDTO.builder()
                .id(account.getId())
                .holderName(account.getHolderName())
                .balance(account.getBalance())
                .build();
    }
}