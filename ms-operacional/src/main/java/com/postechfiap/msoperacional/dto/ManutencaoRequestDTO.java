package com.postechfiap.msoperacional.dto;

import com.postechfiap.msoperacional.enums.TipoManutencaoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ManutencaoRequestDTO(
        @NotNull(message = "ID do equipamento é obrigatório")
        UUID equipamentoId,

        @NotNull(message = "Tipo de manutenção é obrigatório (PREVENTIVA, CORRETIVA, CALIBRACAO)")
        TipoManutencaoEnum tipo,

        @NotNull(message = "O valor da manutenção é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "A data de início da manutenção é obrigatória")
        LocalDate dataInicio,

        @NotBlank(message = "O CNPJ da empresa prestadora é obrigatório")
        String cnpjEmpresa,

        @NotBlank(message = "O nome da empresa prestadora é obrigatório")
        String nomeEmpresa,

        String descricao
) {}