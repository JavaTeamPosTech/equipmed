package com.postechfiap.msoperacional.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postechfiap.msoperacional.client.EquipamentoClient;
import com.postechfiap.msoperacional.dto.EquipamentoExternalDTO;
import com.postechfiap.msoperacional.enums.SigtapEnum;
import com.postechfiap.msoperacional.enums.TipoManutencaoEnum;
import com.postechfiap.msoperacional.model.Manutencao;
import com.postechfiap.msoperacional.model.Uso;
import com.postechfiap.msoperacional.repository.ManutencaoRepository;
import com.postechfiap.msoperacional.repository.UsoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EquipamentoClient equipamentoClient;
    private final UsoRepository usoRepository;
    private final ManutencaoRepository manutencaoRepository;
    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        if (usoRepository.count() > 0) return;

        log.info("Iniciando Super Carga de Daos Operacionais...");
        InputStream is = new ClassPathResource("cenario-operacional.json").getInputStream();
        JsonNode cenario = objectMapper.readTree(is);
        List<EquipamentoExternalDTO> equipamentos = equipamentoClient.listarTodos(100).content();

        List<Uso> listaUsos = new ArrayList<>();
        List<Manutencao> listaManut = new ArrayList<>();

        JsonNode operadores = cenario.get("operadores");
        JsonNode empresas = cenario.get("empresasManutencao");
        JsonNode motivos = cenario.get("motivosManutencao");

        for (EquipamentoExternalDTO equip : equipamentos) {
            double perfil = determinarPerfil(equip.status());

            // Ponderação: 5000 usos / 50 equips = média de 100 por equip
            int qtdUsos = (int) (perfil * (random.nextInt(50) + 150));
            listaUsos.addAll(gerarUsos(equip, qtdUsos, operadores));

            // Ponderação: 500 manut / 50 equips = média de 10 por equip
            int qtdManut = (int) (perfil * (random.nextInt(10) + 10));
            listaManut.addAll(gerarManutencao(equip, qtdManut, empresas, motivos));
        }

        usoRepository.saveAll(listaUsos);
        manutencaoRepository.saveAll(listaManut);

        log.info("SUPER CARGA CONCLUÍDA: {} Usos e {} Manutenções.",
                usoRepository.count(), manutencaoRepository.count());
    }

    private List<Uso> gerarUsos(EquipamentoExternalDTO equip, int qtd, JsonNode operadores) {
        List<Uso> list = new ArrayList<>();
        List<SigtapEnum> compativeis = List.of(SigtapEnum.values()).stream()
                .filter(s -> s.getTipoEquipamentoPermitido().equals(equip.tipo()))
                .toList();

        for (int i = 0; i < qtd; i++) {
            if (compativeis.isEmpty()) break;
            SigtapEnum s = compativeis.get(random.nextInt(compativeis.size()));
            list.add(Uso.builder()
                    .equipamentoId(equip.id())
                    .codigoSigtap(s.getCodigo())
                    .procedimentoNome(s.getDescricao())
                    .dataHora(LocalDateTime.now().minusDays(random.nextInt(365)).minusMinutes(random.nextInt(1440)))
                    .operadorId(operadores.get(random.nextInt(operadores.size())).asText())
                    .build());
        }
        return list;
    }

    private List<Manutencao> gerarManutencao(EquipamentoExternalDTO equip, int qtd, JsonNode empresas, JsonNode motivos) {
        List<Manutencao> list = new ArrayList<>();
        for (int i = 0; i < qtd; i++) {
            JsonNode emp = empresas.get(random.nextInt(empresas.size()));
            TipoManutencaoEnum tipo = TipoManutencaoEnum.values()[random.nextInt(TipoManutencaoEnum.values().length)];
            list.add(Manutencao.builder()
                    .equipamentoId(equip.id())
                    .tipo(tipo)
                    .valor(BigDecimal.valueOf(tipo == TipoManutencaoEnum.CORRETIVA ? 20000 + random.nextInt(80000) : 1500 + random.nextInt(5000)))
                    .dataInicio(LocalDate.now().minusDays(random.nextInt(365)))
                    .cnpjEmpresa(emp.get("cnpj").asText())
                    .nomeEmpresa(emp.get("nome").asText())
                    .descricao(motivos.get(random.nextInt(motivos.size())).asText())
                    .build());
        }
        return list;
    }

    private double determinarPerfil(String status) {
        return switch (status) {
            case "ATIVO" -> 0.8 + (random.nextDouble() * 0.2); // 80% a 100% de atividade
            case "MANUTENCAO_PREVENTIVA" -> 0.4 + (random.nextDouble() * 0.3); // 40% a 70%
            case "MANUTENCAO_CORRETIVA" -> 0.1 + (random.nextDouble() * 0.2); // 10% a 30%
            default -> random.nextDouble() * 0.1; // Ociosas: 0% a 10%
        };
    }
}