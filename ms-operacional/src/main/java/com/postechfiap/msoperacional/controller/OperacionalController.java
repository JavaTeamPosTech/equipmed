package com.postechfiap.msoperacional.controller;

import com.postechfiap.msoperacional.dto.*;
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

    // --- ENDPOINTS DE REGISTRO ---

    @PostMapping("/usos")
    public ResponseEntity<UsoResponseDTO> registrarUso(@RequestBody @Valid UsoRequestDTO dto) {
        log.info("REST: Solicitação de registro de uso - Equipamento: {}", dto.equipamentoId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarUso(dto));
    }

    @PostMapping("/manutencoes")
    public ResponseEntity<ManutencaoResponseDTO> registrarManutencao(@RequestBody @Valid ManutencaoRequestDTO dto) {
        log.info("REST: Solicitação de registro de manutenção - Equipamento: {}", dto.equipamentoId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarManutencao(dto));
    }

    // --- ENDPOINTS DE CONSULTA DE HISTÓRICO ---

    @GetMapping("/usos/{equipamentoId}")
    public ResponseEntity<List<UsoResponseDTO>> listarUsosPorEquipamento(@PathVariable UUID equipamentoId) {
        log.info("REST: Consultando histórico de usos para equipamento: {}", equipamentoId);
        return ResponseEntity.ok(service.listarUsosPorEquipamento(equipamentoId));
    }

    @GetMapping("/manutencoes/{equipamentoId}")
    public ResponseEntity<List<ManutencaoResponseDTO>> listarManutencoesPorEquipamento(@PathVariable UUID equipamentoId) {
        log.info("REST: Consultando histórico de manutenções para equipamento: {}", equipamentoId);
        return ResponseEntity.ok(service.listarManutencoesPorEquipamento(equipamentoId));
    }

    // --- ENDPOINT DE INTEGRAÇÃO (MS-TRANSPARÊNCIA) ---

    @PostMapping("/batch-summary")
    public ResponseEntity<Map<UUID, EquipamentoResumoDTO>> buscarResumoEmLote(@RequestBody List<UUID> ids) {
        log.info("REST: Gerando resumo operacional em lote para {} equipamentos", ids.size());
        return ResponseEntity.ok(service.buscarResumoParaTransparencia(ids));
    }
}