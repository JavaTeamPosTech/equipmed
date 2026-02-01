package com.postechfiap.mstransparencia.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record EquipamentoCompletoDTO(
        // Dados Cadastrais (Origem: ms-equipamentos)
        UUID id,
        String tagPatrimonio,
        String tipo,
        String modelo,
        String unidadeDeSaude,
        String localizacaoCompleta,
        String status,
        BigDecimal valorAquisicao,
        LocalDate dataAquisicao,

        // Dados Operacionais (Origem: ms-operacional)
        Long totalUsos,
        LocalDateTime dataUltimoUso,
        BigDecimal custoTotalManutencao,
        Integer quantidadeManutencoes,

        // Dados Inteligentes (Calculados pelo ms-transparencia)
        boolean isOciosa,
        boolean isCustoElevado,
        Double percentualCustoSobreAquisicao
) {
    /**
     * Método auxiliar para calcular se o custo de manutenção ultrapassou
     * o limite de 50% do valor de compra.
     */
    public static Double calcularPercentualCusto(BigDecimal aquisicao, BigDecimal manutencao) {
        if (aquisicao == null || aquisicao.compareTo(BigDecimal.ZERO) == 0) return 0.0;
        if (manutencao == null) return 0.0;
        return (manutencao.doubleValue() / aquisicao.doubleValue()) * 100;
    }
}