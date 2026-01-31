package com.postechfiap.msoperacional.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_usos", indexes = {
        @Index(name = "idx_uso_equipamento", columnList = "equipamentoId"),
        @Index(name = "idx_uso_data", columnList = "dataHora")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Uso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID equipamentoId;

    @Column(nullable = false)
    private String codigoSigtap;

    @Column(nullable = false)
    private String procedimentoNome;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private String operadorId;
}