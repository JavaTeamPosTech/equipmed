package com.postechfiap.mstransparencia.dto;

import java.math.BigDecimal;

public record PainelResumoDTO(
        long totalEquipamentos,
        long totalExamesRealizados,
        BigDecimal investimentoTotalManutencao,
        long equipamentosOciosos,
        long equipamentosComCustoElevado,
        double indiceDisponibilidadeGeral // % de máquinas não ociosas
) {}