package com.postechfiap.msoperacional.dto;

import java.util.List;

public record EquipamentoPageDTO(
        List<EquipamentoExternalDTO> content
) {}