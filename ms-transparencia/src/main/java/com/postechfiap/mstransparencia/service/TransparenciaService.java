package com.postechfiap.mstransparencia.service;

import com.postechfiap.mstransparencia.dto.CidadeResumoDTO;
import com.postechfiap.mstransparencia.dto.EquipamentoCompletoDTO;
import com.postechfiap.mstransparencia.dto.PainelResumoDTO;
import com.postechfiap.mstransparencia.dto.UnidadeResumoDTO;

import java.util.List;

/**
 * Contrato para agregação e inteligência de dados de transparência pública.
 */
public interface TransparenciaService {

    List<EquipamentoCompletoDTO> listarPainelGeral();
    PainelResumoDTO obterSumarioExecutivo();
    List<EquipamentoCompletoDTO> listarAlertasCriticos();
    List<UnidadeResumoDTO> listarResumoPorUnidade();
    List<CidadeResumoDTO> listarResumoPorCidade();
}