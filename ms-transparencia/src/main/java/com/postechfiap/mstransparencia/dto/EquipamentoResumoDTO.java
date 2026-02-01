package com.postechfiap.mstransparencia.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EquipamentoResumoDTO(
        Long totalUsos,
        LocalDateTime dataUltimoUso,
        BigDecimal custoTotalManutencao,
        Integer quantidadeManutencoes,
        boolean isOciosa,
        boolean isCustoElevado // Note: O ms-operacional envia false, nós calculamos aqui
) {}