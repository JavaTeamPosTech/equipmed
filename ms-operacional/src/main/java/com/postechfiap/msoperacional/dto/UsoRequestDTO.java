package com.postechfiap.msoperacional.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UsoRequestDTO(
        @NotNull(message = "ID do equipamento é obrigatório")
        UUID equipamentoId,

        @NotBlank(message = "Código SIGTAP é obrigatório")
        String codigoSigtap,

        @NotBlank(message = "ID do operador é obrigatório")
        String operadorId
) {}