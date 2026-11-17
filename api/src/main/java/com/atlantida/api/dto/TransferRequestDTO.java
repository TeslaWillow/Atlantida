// ./src/main/java/com/atlantida/api/dto/TransferRequestDTO.java
package com.atlantida.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDTO {

    @NotNull(message = "La cuenta de origen es obligatoria")
    private Long sourceAccountId;

    @NotNull(message = "La cuenta de destino es obligatoria")
    private Long destinationAccountId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    private BigDecimal amount;
}