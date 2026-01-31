package com.postechfiap.msequipamentos.dto;

import com.postechfiap.msequipamentos.enums.StatusEquipamentoEnum;
import com.postechfiap.msequipamentos.enums.TipoEquipamentoEnum;
import com.postechfiap.msequipamentos.model.Equipamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EquipamentoResponseDTO(
        UUID id,
        String tagPatrimonio,
        TipoEquipamentoEnum tipo,
        String modelo,
        String unidadeDeSaude,
        String localizacaoCompleta,
        StatusEquipamentoEnum status,
        BigDecimal valorAquisicao,
        LocalDate dataAquisicao
) {
    public static EquipamentoResponseDTO fromEntity(Equipamento e) {
        return new EquipamentoResponseDTO(
                e.getId(),
                e.getTagPatrimonio(),
                e.getTipo(),
                e.getModelo(),
                e.getUnidadeDeSaude(),
                String.format("%s - %s", e.getCidade(), e.getEstado()),
                e.getStatus(),
                e.getValorAquisicao(),
                e.getDataAquisicao()
        );
    }
}