package com.postechfiap.msequipamentos.dto;

import com.postechfiap.msequipamentos.enums.StatusEquipamentoEnum;
import com.postechfiap.msequipamentos.enums.TipoEquipamentoEnum;
import com.postechfiap.msequipamentos.model.Equipamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EquipamentoRequestDTO(
        String tagPatrimonio,
        TipoEquipamentoEnum tipo,
        String marca,
        String modelo,
        LocalDate dataAquisicao,
        BigDecimal valorAquisicao,
        String unidadeDeSaude,
        String cidade,
        String estado,
        String fornecedor,
        StatusEquipamentoEnum status
) {
    public Equipamento toEntity() {
        return Equipamento.builder()
                .tagPatrimonio(this.tagPatrimonio)
                .tipo(this.tipo)
                .marca(this.marca)
                .modelo(this.modelo)
                .dataAquisicao(this.dataAquisicao)
                .valorAquisicao(this.valorAquisicao)
                .unidadeDeSaude(this.unidadeDeSaude)
                .cidade(this.cidade)
                .estado(this.estado)
                .fornecedor(this.fornecedor)
                .status(this.status)
                .build();
    }
}