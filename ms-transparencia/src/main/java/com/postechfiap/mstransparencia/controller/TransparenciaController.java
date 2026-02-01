package com.postechfiap.mstransparencia.controller;

import com.postechfiap.mstransparencia.dto.EquipamentoCompletoDTO;
import com.postechfiap.mstransparencia.dto.PainelResumoDTO;
import com.postechfiap.mstransparencia.dto.UnidadeResumoDTO;
import com.postechfiap.mstransparencia.service.TransparenciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/transparencia")
@RequiredArgsConstructor
public class TransparenciaController {

    private final TransparenciaService transparenciaService;

    @GetMapping("/painel-geral")
    public ResponseEntity<List<EquipamentoCompletoDTO>> getPainelGeral() {
        log.info("Solicitação recebida: Painel Geral de Transparência");
        long startTime = System.currentTimeMillis();

        List<EquipamentoCompletoDTO> response = transparenciaService.listarPainelGeral();

        long duration = System.currentTimeMillis() - startTime;
        log.info("Painel Geral processado com sucesso em {}ms. Total de registros: {}", duration, response.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sumario")
    public ResponseEntity<PainelResumoDTO> getSumario() {
        return ResponseEntity.ok(transparenciaService.obterSumarioExecutivo());
    }

    @GetMapping("/alertas")
    public ResponseEntity<List<EquipamentoCompletoDTO>> getAlertas() {
        return ResponseEntity.ok(transparenciaService.listarAlertasCriticos());
    }

    @GetMapping("/unidades")
    public ResponseEntity<List<UnidadeResumoDTO>> getPorUnidade() {
        return ResponseEntity.ok(transparenciaService.listarResumoPorUnidade());
    }
}