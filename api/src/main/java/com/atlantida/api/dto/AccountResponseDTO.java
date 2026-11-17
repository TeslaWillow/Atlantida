// ./src/main/java/com/atlantida/api/dto/AccountResponseDTO.java
package com.atlantida.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private Long id;
    private String holderName;
    private BigDecimal balance;
}