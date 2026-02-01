package com.postechfiap.mstransparencia.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EquipamentoExternalDTO(
        UUID id,
        String tagPatrimonio,
        String tipo,
        String modelo,
        String unidadeDeSaude,
        String localizacaoCompleta,
        String status,
        BigDecimal valorAquisicao,
        LocalDate dataAquisicao
) {}