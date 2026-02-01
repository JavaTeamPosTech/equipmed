package com.postechfiap.mstransparencia.service.impl;

import com.postechfiap.mstransparencia.client.EquipamentoClient;
import com.postechfiap.mstransparencia.client.OperacionalClient;
import com.postechfiap.mstransparencia.dto.*;
import com.postechfiap.mstransparencia.service.TransparenciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransparenciaServiceImpl implements TransparenciaService {

    private final EquipamentoClient equipamentoClient;
    private final OperacionalClient operacionalClient;

    @Override
    @Cacheable(value = "painelGeralCache")
    public List<EquipamentoCompletoDTO> listarPainelGeral() {
        log.info("Iniciando agregação de dados para o Painel Geral de Transparência");

        // 1. Busca todos os dados cadastrais (ms-equipamentos)
        var response = equipamentoClient.listarTodos(1000);
        List<EquipamentoExternalDTO> cadastros = response.content();

        // 2. Extrai os IDs para buscar o operacional em lote (Batch Pattern)
        List<UUID> ids = cadastros.stream()
                .map(EquipamentoExternalDTO::id)
                .toList();

        // 3. Busca o resumo de usos e manutenções (ms-operacional)
        Map<UUID, EquipamentoResumoDTO> operacionais = operacionalClient.buscarResumoEmLote(ids);

        // 4. Faz o "Join" e aplica as regras de negócio
        return cadastros.stream()
                .map(cad -> montarEquipamentoCompleto(cad, operacionais.get(cad.id())))
                .collect(Collectors.toList());
    }

    @Override
    public PainelResumoDTO obterSumarioExecutivo() {
        // Busca a lista completa (aproveitando o cache do Redis se existir)
        List<EquipamentoCompletoDTO> todos = this.listarPainelGeral();

        long ociosos = todos.stream().filter(EquipamentoCompletoDTO::isOciosa).count();
        long custoElevado = todos.stream().filter(EquipamentoCompletoDTO::isCustoElevado).count();

        BigDecimal investimentoTotal = todos.stream()
                .map(EquipamentoCompletoDTO::custoTotalManutencao)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalExames = todos.stream()
                .mapToLong(EquipamentoCompletoDTO::totalUsos)
                .sum();

        double disponibilidade = todos.isEmpty() ? 0.0 :
                ((double) (todos.size() - ociosos) / todos.size()) * 100;

        return new PainelResumoDTO(
                todos.size(),
                totalExames,
                investimentoTotal,
                ociosos,
                custoElevado,
                disponibilidade
        );
    }

    @Override
    public List<EquipamentoCompletoDTO> listarAlertasCriticos() {
        // Retorna apenas equipamentos que são ociosos OU têm custo elevado
        return this.listarPainelGeral().stream()
                .filter(e -> e.isOciosa() || e.isCustoElevado())
                .sorted((e1, e2) -> e2.percentualCustoSobreAquisicao().compareTo(e1.percentualCustoSobreAquisicao()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UnidadeResumoDTO> listarResumoPorUnidade() {
        List<EquipamentoCompletoDTO> todos = this.listarPainelGeral();

        return todos.stream()
                .collect(Collectors.groupingBy(EquipamentoCompletoDTO::unidadeDeSaude))
                .entrySet().stream()
                .map(entry -> {
                    String unidade = entry.getKey();
                    List<EquipamentoCompletoDTO> equipsDaUnidade = entry.getValue();

                    long totalExames = equipsDaUnidade.stream()
                            .mapToLong(EquipamentoCompletoDTO::totalUsos).sum();

                    BigDecimal custoTotal = equipsDaUnidade.stream()
                            .map(EquipamentoCompletoDTO::custoTotalManutencao)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    long ociosos = equipsDaUnidade.stream()
                            .filter(EquipamentoCompletoDTO::isOciosa).count();

                    double ociosidade = (double) ociosos / equipsDaUnidade.size() * 100;

                    return new UnidadeResumoDTO(
                            unidade,
                            (long) equipsDaUnidade.size(),
                            totalExames,
                            custoTotal,
                            ociosidade
                    );
                })
                .sorted(Comparator.comparing(UnidadeResumoDTO::custoTotalManutencao).reversed())
                .collect(Collectors.toList());
    }

    private EquipamentoCompletoDTO montarEquipamentoCompleto(EquipamentoExternalDTO cad, EquipamentoResumoDTO oper) {
        // Fallback para caso o equipamento não tenha dados operacionais ainda
        if (oper == null) {
            oper = new EquipamentoResumoDTO(0L, null, BigDecimal.ZERO, 0, true, false);
        }

        // Cálculo da Regra de Negócio: Custo Elevado (> 50% do valor de aquisição)
        boolean ociosaReal = oper.dataUltimoUso() == null ||
                oper.dataUltimoUso().isBefore(LocalDateTime.now().minusDays(5));
        double percentual = EquipamentoCompletoDTO.calcularPercentualCusto(cad.valorAquisicao(), oper.custoTotalManutencao());
        boolean isCustoElevado = percentual > 50.0;

        return new EquipamentoCompletoDTO(
                cad.id(),
                cad.tagPatrimonio(),
                cad.tipo(),
                cad.modelo(),
                cad.unidadeDeSaude(),
                cad.status(),
                cad.valorAquisicao(),
                cad.dataAquisicao(),
                oper.totalUsos(),
                oper.dataUltimoUso(),
                oper.custoTotalManutencao(),
                oper.quantidadeManutencoes(),
                ociosaReal,
                isCustoElevado,
                percentual
        );
    }
}