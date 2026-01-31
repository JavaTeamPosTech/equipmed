package com.postechfiap.msoperacional.service;

import com.postechfiap.msoperacional.dto.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OperacionalService {

    /**
     * Registra um novo uso de equipamento com validação rigorosa de SIGTAP.
     */
    UsoResponseDTO registrarUso(UsoRequestDTO dto);

    /**
     * Registra uma intervenção técnica/manutenção.
     */
    ManutencaoResponseDTO registrarManutencao(ManutencaoRequestDTO dto);

    /**
     * Retorna o histórico completo de usos de um equipamento específico (ordenado pelo mais recente).
     */
    List<UsoResponseDTO> listarUsosPorEquipamento(UUID equipamentoId);

    /**
     * Retorna o histórico completo de manutenções de um equipamento específico.
     */
    List<ManutencaoResponseDTO> listarManutencoesPorEquipamento(UUID equipamentoId);

    /**
     * Agrega dados operacionais de múltiplos equipamentos para o MS-Transparência.
     * @param equipamentoIds Lista de UUIDs dos equipamentos
     * @return Mapa onde a chave é o ID do equipamento e o valor são seus indicadores
     */
    Map<UUID, EquipamentoResumoDTO> buscarResumoParaTransparencia(List<UUID> equipamentoIds);
}