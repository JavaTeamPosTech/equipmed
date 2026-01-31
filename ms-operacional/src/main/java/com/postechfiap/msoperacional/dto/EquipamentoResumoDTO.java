package com.postechfiap.msoperacional.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EquipamentoResumoDTO(
        Long totalUsos,
        LocalDateTime dataUltimoUso,
        BigDecimal custoTotalManutencao,
        Integer quantidadeManutencoes,
        boolean isOciosa,         // Calculado: sem uso há > 5 dias
        boolean isCustoElevado    // O MS-Transparência decidirá com base no valor de aquisição
) {}