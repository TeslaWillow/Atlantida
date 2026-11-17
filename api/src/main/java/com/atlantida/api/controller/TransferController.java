// ./src/main/java/com/atlantida/api/controller/TransferController.java
package com.atlantida.api.controller;

import com.atlantida.api.dto.TransferRequestDTO;
import com.atlantida.api.dto.TransferResponseDTO;
import com.atlantida.api.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Allow only from the frontend's origin
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponseDTO> executeTransfer(@RequestBody @Valid TransferRequestDTO requestDTO) {
        TransferResponseDTO response = transferService.executeTransfer(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}