package com.postechfiap.msequipamentos.model;

import com.postechfiap.msequipamentos.enums.StatusEquipamentoEnum;
import com.postechfiap.msequipamentos.enums.TipoEquipamentoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "equipamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String tagPatrimonio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEquipamentoEnum tipo;

    private String marca;
    private String modelo;
    private LocalDate dataAquisicao;
    private BigDecimal valorAquisicao;

    private String unidadeDeSaude; // Representando o local/CNES
    private String cidade;
    private String estado;
    private String fornecedor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEquipamentoEnum status;
}