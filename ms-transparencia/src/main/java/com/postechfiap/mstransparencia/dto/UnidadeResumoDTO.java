package com.postechfiap.mstransparencia.dto;

import java.math.BigDecimal;

public record UnidadeResumoDTO(
        String nomeUnidade,
        long quantidadeEquipamentos,
        long totalExames,
        BigDecimal custoTotalManutencao,
        double percentualOciosidade
) {}