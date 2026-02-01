package com.postechfiap.mstransparencia.client;

import com.postechfiap.mstransparencia.dto.EquipamentoExternalDTO;
import com.postechfiap.mstransparencia.dto.EquipamentoPageResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * Cliente para integração com o microserviço de Cadastro de Equipamentos.
 * O nome 'ms-equipamentos' deve ser o mesmo registrado no Service Discovery (Eureka/Consul).
 */
@FeignClient(name = "ms-equipamentos", url = "${app.services.ms-equipamentos.url}")
public interface EquipamentoClient {

    @GetMapping("/{id}")
    EquipamentoExternalDTO buscarPorId(@PathVariable("id") UUID id);

    @GetMapping()
    EquipamentoPageResponseDTO listarTodos(@RequestParam("size") int size);

    @GetMapping("/patrimonio/{tag}")
    EquipamentoExternalDTO buscarPorPatrimonio(@PathVariable("tag") String tag);
}