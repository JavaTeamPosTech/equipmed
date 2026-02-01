package com.postechfiap.mstransparencia.service;

import com.postechfiap.mstransparencia.dto.EquipamentoCompletoDTO;
import java.util.List;

/**
 * Contrato para agregação e inteligência de dados de transparência pública.
 */
public interface TransparenciaService {

    /**
     * Consolida dados cadastrais e operacionais de todos os equipamentos
     * para exibição no painel público.
     * * @return Lista de equipamentos com KPIs de uso e custo integrados.
     */
    List<EquipamentoCompletoDTO> listarPainelGeral();

}