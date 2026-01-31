package com.postechfiap.msoperacional.dto;

import com.postechfiap.msoperacional.enums.TipoManutencaoEnum;
import com.postechfiap.msoperacional.model.Manutencao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ManutencaoResponseDTO(
        UUID id,
        UUID equipamentoId,
        TipoManutencaoEnum tipo,
        BigDecimal valor,
        LocalDate dataInicio,
        LocalDate dataFim,
        String cnpjEmpresa,
        String nomeEmpresa,
        String descricao
) {
    public static ManutencaoResponseDTO fromEntity(Manutencao manutencao) {
        return new ManutencaoResponseDTO(
                manutencao.getId(),
                manutencao.getEquipamentoId(),
                manutencao.getTipo(),
                manutencao.getValor(),
                manutencao.getDataInicio(),
                manutencao.getDataFim(),
                manutencao.getCnpjEmpresa(),
                manutencao.getNomeEmpresa(),
                manutencao.getDescricao()
        );
    }
}