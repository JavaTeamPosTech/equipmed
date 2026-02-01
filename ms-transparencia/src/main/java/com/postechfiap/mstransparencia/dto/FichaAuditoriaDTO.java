package com.postechfiap.mstransparencia.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FichaAuditoriaDTO(
        String tagPatrimonio,
        String modelo,
        String unidadeDeSaude,
        String localizacao,
        LocalDate dataAquisicao,
        long idadeEquipamentoEmMeses,
        long totalUsos,
        LocalDateTime dataUltimoUso,
        BigDecimal custoTotalManutencao,
        double percentualConsumidoDoValorOriginal,
        String statusSaude, // "EXCELENTE", "ALERTA", "CRÍTICO"
        String recomendacaoAuditoria // "MANTER", "REVISAR CONTRATO", "AVALIAR SUBSTITUIÇÃO"
) {}