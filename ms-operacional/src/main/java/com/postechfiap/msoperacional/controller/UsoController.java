package com.postechfiap.msoperacional.controller;

import com.postechfiap.msoperacional.dto.UsoRequestDTO;
import com.postechfiap.msoperacional.dto.UsoResponseDTO;
import com.postechfiap.msoperacional.service.OperacionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/usos")
@RequiredArgsConstructor
public class UsoController {

    private final OperacionalService service;

    @PostMapping
    public ResponseEntity<UsoResponseDTO> registrarUso(@RequestBody @Valid UsoRequestDTO dto) {
        log.info("Requisição para registrar uso do equipamento: {}", dto.equipamentoId());
        UsoResponseDTO response = service.registrarUso(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}