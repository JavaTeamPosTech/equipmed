package com.postechfiap.mstransparencia.dto;

import java.util.List;

public record EquipamentoPageResponseDTO(
        List<EquipamentoExternalDTO> content
) {}