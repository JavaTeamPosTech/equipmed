package com.postechfiap.msoperacional.client;

import com.postechfiap.msoperacional.dto.EquipamentoExternalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ms-equipamentos", url = "${ms.equipamentos.url}")
public interface EquipamentoClient {

    /**
     * Busca os dados básicos do equipamento para validação de tipo e existência.
     */
    @GetMapping("/{id}")
    EquipamentoExternalDTO buscarPorId(@PathVariable("id") UUID id);
}