package com.postechfiap.msoperacional.client;

import com.postechfiap.msoperacional.dto.EquipamentoExternalDTO;
import com.postechfiap.msoperacional.dto.EquipamentoPageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "ms-equipamentos", url = "${ms.equipamentos.url}")
public interface EquipamentoClient {

    @GetMapping("/{id}")
    EquipamentoExternalDTO buscarPorId(@PathVariable("id") UUID id);

    @GetMapping
    EquipamentoPageDTO listarTodos(@RequestParam(value = "size", defaultValue = "100") int size);
}
