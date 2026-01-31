package com.postechfiap.msoperacional.service;

import com.postechfiap.msoperacional.dto.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OperacionalService {
    UsoResponseDTO registrarUso(UsoRequestDTO dto);

    ManutencaoResponseDTO registrarManutencao(ManutencaoRequestDTO dto);

    // Método chave para o MS-Transparência
    Map<UUID, EquipamentoResumoDTO> buscarResumoParaTransparencia(List<UUID> equipamentoIds);
}