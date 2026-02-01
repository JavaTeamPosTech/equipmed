package com.postechfiap.msequipamentos.controller;

import com.postechfiap.msequipamentos.dto.EquipamentoRequestDTO;
import com.postechfiap.msequipamentos.dto.EquipamentoResponseDTO;
import com.postechfiap.msequipamentos.enums.StatusEquipamentoEnum;
import com.postechfiap.msequipamentos.enums.TipoEquipamentoEnum;
import com.postechfiap.msequipamentos.service.EquipamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/equipamentos")
@RequiredArgsConstructor
public class EquipamentoController {

    private final EquipamentoService service;

    @PostMapping
    public ResponseEntity<EquipamentoResponseDTO> criar(
            @RequestBody EquipamentoRequestDTO dto,
            @RequestHeader("X-Unidade-Nome") String unidadeHeader,
            @RequestHeader("X-Unidade-Cidade") String cidadeHeader
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto, unidadeHeader, cidadeHeader));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<EquipamentoResponseDTO>> listar(
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) TipoEquipamentoEnum tipo,
            @RequestParam(required = false) String unidadeDeSaude,
            @RequestParam(required = false) StatusEquipamentoEnum status,
            Pageable pageable) {
        return ResponseEntity.ok(service.listarComFiltros(cidade, estado, tipo, unidadeDeSaude, status, pageable));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EquipamentoResponseDTO> atualizarStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body) {

        StatusEquipamentoEnum novoStatus = StatusEquipamentoEnum.valueOf(body.get("status"));
        return ResponseEntity.ok(service.atualizarStatus(id, novoStatus));
    }

    @PatchMapping("/{id}/localizacao")
    public ResponseEntity<EquipamentoResponseDTO> atualizarLocalizacao(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body) {

        return ResponseEntity.ok(service.atualizarLocalizacao(id, body.get("cidade"), body.get("estado")));
    }
}