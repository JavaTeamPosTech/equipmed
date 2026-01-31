package com.postechfiap.msoperacional.dto;

import com.postechfiap.msoperacional.model.Uso;
import java.time.LocalDateTime;
import java.util.UUID;

public record UsoResponseDTO(
        UUID id,
        UUID equipamentoId,
        String codigoSigtap,
        String procedimentoNome,
        LocalDateTime dataHora,
        String operadorId
) {
    public static UsoResponseDTO fromEntity(Uso uso) {
        return new UsoResponseDTO(
                uso.getId(),
                uso.getEquipamentoId(),
                uso.getCodigoSigtap(),
                uso.getProcedimentoNome(),
                uso.getDataHora(),
                uso.getOperadorId()
        );
    }
}