package com.postechfiap.msoperacional.controller;

import com.postechfiap.msoperacional.dto.EquipamentoResumoDTO;
import com.postechfiap.msoperacional.dto.ManutencaoRequestDTO;
import com.postechfiap.msoperacional.dto.ManutencaoResponseDTO;
import com.postechfiap.msoperacional.service.OperacionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/operacional")
@RequiredArgsConstructor
public class OperacionalController {

    private final OperacionalService service;

    @PostMapping("/manutencoes")
    public ResponseEntity<ManutencaoResponseDTO> registrarManutencao(@RequestBody @Valid ManutencaoRequestDTO dto) {
        log.info("Registrando manutenção para o equipamento: {}", dto.equipamentoId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarManutencao(dto));
    }

    /**
     * Endpoint especializado para o MS-Transparência.
     * Recebe uma lista de IDs e retorna o status operacional de cada um.
     */
    @PostMapping("/batch-summary")
    public ResponseEntity<Map<UUID, EquipamentoResumoDTO>> buscarResumoEmLote(@RequestBody List<UUID> ids) {
        log.info("Solicitado resumo operacional para {} equipamentos", ids.size());
        return ResponseEntity.ok(service.buscarResumoParaTransparencia(ids));
    }
}