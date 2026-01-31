package com.postechfiap.msequipamentos.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postechfiap.msequipamentos.dto.EquipamentoRequestDTO;
import com.postechfiap.msequipamentos.repository.EquipamentoRepository;
import com.postechfiap.msequipamentos.service.EquipamentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final EquipamentoService service;
    private final EquipamentoRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            log.info("Iniciando Seed de dados: Banco vazio detectado.");

            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/equipamentos-seed.json")) {
                List<EquipamentoRequestDTO> seedData = objectMapper.readValue(inputStream, new TypeReference<List<EquipamentoRequestDTO>>(){});

                seedData.forEach(service::criar);

                log.info("Seed finalizado com sucesso. {} registros inseridos.", seedData.size());
            } catch (Exception e) {
                log.error("Falha ao carregar seed de dados: {}", e.getMessage());
            }
        } else {
            log.info("Seed ignorado: Banco ja contem registros.");
        }
    }
}