package com.postechfiap.msoperacional.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operacional")
public class TestController {
    @GetMapping("/check")
    public ResponseEntity<String> check(@RequestHeader(value = "X-Unidade-Saude", defaultValue = "Desconhecido") String unidade) {
        return ResponseEntity.ok("MS-OPERACIONAL ativo. Unidade solicitante: " + unidade);
    }
}