// ./src/main/java/com/atlantida/api/dto/TransferResponseDTO.java
package com.atlantida.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponseDTO {
    private Long transferId;
    private Long sourceAccountId;
    private String sourceHolderName;
    private BigDecimal sourcePreviousBalance;
    private BigDecimal sourceNewBalance;
    
    private Long destinationAccountId;
    private String destinationHolderName;
    private BigDecimal destinationPreviousBalance;
    private BigDecimal destinationNewBalance;
    
    private BigDecimal amount;
    private LocalDateTime createdAt;
}