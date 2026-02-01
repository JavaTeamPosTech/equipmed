package com.postechfiap.mstransparencia.service.impl;

import com.postechfiap.mstransparencia.client.EquipamentoClient;
import com.postechfiap.mstransparencia.client.OperacionalClient;
import com.postechfiap.mstransparencia.dto.*;
import com.postechfiap.mstransparencia.service.TransparenciaService;
import com.postechfiap.mstransparencia.util.LocalizacaoUtil;
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
        log.info("Iniciando agregação para o Painel Geral");
        var response = equipamentoClient.listarTodos(1000);
        List<EquipamentoExternalDTO> cadastros = response.content();

        List<UUID> ids = cadastros.stream().map(EquipamentoExternalDTO::id).toList();
        Map<UUID, EquipamentoResumoDTO> operacionais = operacionalClient.buscarResumoEmLote(ids);

        return cadastros.stream()
                .map(cad -> montarEquipamentoCompleto(cad, operacionais.get(cad.id())))
                .collect(Collectors.toList());
    }

    @Override
    public PainelResumoDTO obterSumarioExecutivo() {
        List<EquipamentoCompletoDTO> todos = this.listarPainelGeral();
        long ociosos = todos.stream().filter(EquipamentoCompletoDTO::isOciosa).count();
        long custoElevado = todos.stream().filter(EquipamentoCompletoDTO::isCustoElevado).count();
        BigDecimal investimentoTotal = todos.stream()
                .map(EquipamentoCompletoDTO::custoTotalManutencao)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long totalExames = todos.stream().mapToLong(EquipamentoCompletoDTO::totalUsos).sum();
        double disponibilidade = todos.isEmpty() ? 0.0 : ((double) (todos.size() - ociosos) / todos.size()) * 100;

        return new PainelResumoDTO(todos.size(), totalExames, investimentoTotal, ociosos, custoElevado, disponibilidade);
    }

    @Override
    public List<EquipamentoCompletoDTO> listarAlertasCriticos() {
        return this.listarPainelGeral().stream()
                .filter(e -> e.isOciosa() || e.isCustoElevado())
                .sorted((e1, e2) -> e2.percentualCustoSobreAquisicao().compareTo(e1.percentualCustoSobreAquisicao()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UnidadeResumoDTO> listarResumoPorUnidade() {
        return this.listarPainelGeral().stream()
                .collect(Collectors.groupingBy(EquipamentoCompletoDTO::unidadeDeSaude))
                .entrySet().stream()
                .map(entry -> {
                    List<EquipamentoCompletoDTO> equips = entry.getValue();
                    BigDecimal custo = equips.stream().map(EquipamentoCompletoDTO::custoTotalManutencao).reduce(BigDecimal.ZERO, BigDecimal::add);
                    long exames = equips.stream().mapToLong(EquipamentoCompletoDTO::totalUsos).sum();
                    long ociosos = equips.stream().filter(EquipamentoCompletoDTO::isOciosa).count();
                    long alertas = equips.stream().filter(e -> e.isOciosa() || e.isCustoElevado()).count();
                    double disp = ((double) (equips.size() - ociosos) / equips.size()) * 100;

                    return new UnidadeResumoDTO(entry.getKey(), equips.size(), exames, custo, disp, alertas);
                })
                .sorted(Comparator.comparing(UnidadeResumoDTO::investimentoManutencao).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<CidadeResumoDTO> listarResumoPorCidade() {
        return this.listarPainelGeral().stream()
                // AGORA USANDO O CAMPO CORRETO: localizacaoCompleta
                .collect(Collectors.groupingBy(e -> LocalizacaoUtil.extrairCidade(e.localizacaoCompleta())))
                .entrySet().stream()
                .map(entry -> {
                    List<EquipamentoCompletoDTO> equips = entry.getValue();
                    BigDecimal custo = equips.stream()
                            .map(EquipamentoCompletoDTO::custoTotalManutencao)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    long exames = equips.stream().mapToLong(EquipamentoCompletoDTO::totalUsos).sum();
                    long ociosos = equips.stream().filter(EquipamentoCompletoDTO::isOciosa).count();
                    long alertas = equips.stream().filter(EquipamentoCompletoDTO::isCustoElevado).count();
                    double disp = equips.isEmpty() ? 0 : ((double) (equips.size() - ociosos) / equips.size()) * 100;

                    return new CidadeResumoDTO(entry.getKey(), equips.size(), exames, custo, disp, alertas);
                })
                .sorted(Comparator.comparing(CidadeResumoDTO::totalEquipamentos).reversed())
                .collect(Collectors.toList());
    }

    private EquipamentoCompletoDTO montarEquipamentoCompleto(EquipamentoExternalDTO cad, EquipamentoResumoDTO oper) {
        if (oper == null) oper = new EquipamentoResumoDTO(0L, null, BigDecimal.ZERO, 0, true, false);

        double percentual = EquipamentoCompletoDTO.calcularPercentualCusto(cad.valorAquisicao(), oper.custoTotalManutencao());
        boolean ociosaReal = oper.dataUltimoUso() == null || oper.dataUltimoUso().isBefore(LocalDateTime.now().minusDays(5));

        return new EquipamentoCompletoDTO(
                cad.id(),
                cad.tagPatrimonio(),
                cad.tipo(),
                cad.modelo(),
                cad.unidadeDeSaude(),
                cad.localizacaoCompleta(),
                cad.status(),
                cad.valorAquisicao(),
                cad.dataAquisicao(),
                oper.totalUsos(),
                oper.dataUltimoUso(),
                oper.custoTotalManutencao(),
                oper.quantidadeManutencoes(),
                ociosaReal,
                percentual > 50.0,
                percentual
        );
    }
}