package com.postechfiap.msoperacional.model;

import com.postechfiap.msoperacional.enums.TipoManutencaoEnum;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_manutencoes", indexes = {
        @Index(name = "idx_manutencao_equipamento", columnList = "equipamentoId")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID equipamentoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoManutencaoEnum tipo;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate dataInicio;

    private LocalDate dataFim;

    private String cnpjEmpresa;
    private String nomeEmpresa;

    @Column(columnDefinition = "TEXT")
    private String descricao;
}