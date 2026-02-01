package com.postechfiap.mstransparencia.client;

import com.postechfiap.mstransparencia.dto.EquipamentoResumoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Cliente para buscar dados de performance e custos no ms-operacional.
 */
@FeignClient(name = "ms-operacional", url = "${app.services.ms-operacional.url}")
public interface OperacionalClient {

    /**
     * Busca o resumo consolidado de uma lista de equipamentos.
     * @param ids Lista de UUIDs dos equipamentos desejados
     * @return Um Map onde a chave é o ID do equipamento e o valor é o resumo operacional.
     */
    @PostMapping("/batch-summary")
    Map<UUID, EquipamentoResumoDTO> buscarResumoEmLote(@RequestBody List<UUID> ids);
}