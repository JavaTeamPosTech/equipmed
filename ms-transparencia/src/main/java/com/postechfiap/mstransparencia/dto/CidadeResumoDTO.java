package com.postechfiap.mstransparencia.dto;

import java.math.BigDecimal;

public record CidadeResumoDTO(
        String cidade,
        long totalEquipamentos,
        long examesRealizados,
        BigDecimal investimentoManutencao,
        double indiceDisponibilidade,
        long alertasCustoElevado
) {}