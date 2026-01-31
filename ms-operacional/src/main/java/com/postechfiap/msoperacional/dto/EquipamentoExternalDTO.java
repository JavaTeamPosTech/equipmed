package com.postechfiap.msoperacional.dto;

import java.util.UUID;

public record EquipamentoExternalDTO(
        UUID id,
        String tipo, // Ex: "RESSONANCIA_MAGNETICA"
        String status
) {}