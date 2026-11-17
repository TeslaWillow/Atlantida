// ./src/main/java/com/atlantida/api/model/entity/Account.java
package com.atlantida.api.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id; // No autoincremental según el Anexo A

    @Column(name = "holder_name", nullable = false, length = 100)
    private String holderName;

    @Column(name = "balance", nullable = false, precision = 18, scale = 2)
    private BigDecimal balance;
}