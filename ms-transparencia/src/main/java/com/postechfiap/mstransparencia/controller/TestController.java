package com.postechfiap.mstransparencia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transparencia")
public class TestController {
    @GetMapping("/public-check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("MS-TRANSPARENCIA (Público) ativo. Dados prontos para dashboard.");
    }
}