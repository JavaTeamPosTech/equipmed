package com.postechfiap.msoperacional.service.impl;

import com.postechfiap.msoperacional.client.EquipamentoClient;
import com.postechfiap.msoperacional.dto.*;
import com.postechfiap.msoperacional.enums.SigtapEnum;
import com.postechfiap.msoperacional.model.Manutencao;
import com.postechfiap.msoperacional.model.Uso;
import com.postechfiap.msoperacional.repository.ManutencaoRepository;
import com.postechfiap.msoperacional.repository.UsoRepository;
import com.postechfiap.msoperacional.service.OperacionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperacionalServiceImpl implements OperacionalService {

    private final UsoRepository usoRepository;
    private final ManutencaoRepository manutencaoRepository;
    private final EquipamentoClient equipamentoClient;

    @Override
    @Transactional
    public UsoResponseDTO registrarUso(UsoRequestDTO dto) {
        log.info("Operacao=RegistroUso EquipamentoId={} CodigoSigtap={}", dto.equipamentoId(), dto.codigoSigtap());

        // Validação Rigorosa: Busca info (Feign + Cache)
        EquipamentoExternalDTO equipamento = buscarEquipamentoComCache(dto.equipamentoId());

        // Validação SIGTAP vs Tipo Equipamento
        SigtapEnum sigtap = SigtapEnum.deCodigo(dto.codigoSigtap());
        if (!sigtap.getTipoEquipamentoPermitido().equals(equipamento.tipo())) {
            log.error("Erro=IncompatibilidadeTecnica SigtapRequer={} EquipamentoEh={}",
                    sigtap.getTipoEquipamentoPermitido(), equipamento.tipo());
            throw new IllegalArgumentException("Procedimento SIGTAP incompatível com este tipo de equipamento.");
        }

        Uso uso = Uso.builder()
                .equipamentoId(dto.equipamentoId())
                .codigoSigtap(dto.codigoSigtap())
                .procedimentoNome(sigtap.getDescricao())
                .dataHora(LocalDateTime.now())
                .operadorId(dto.operadorId())
                .build();

        return UsoResponseDTO.fromEntity(usoRepository.save(uso));
    }

    @Override
    @Transactional
    public ManutencaoResponseDTO registrarManutencao(ManutencaoRequestDTO dto) {
        log.info("Operacao=RegistroManutencao EquipamentoId={} Valor={}", dto.equipamentoId(), dto.valor());

        // Garante que o equipamento existe antes de permitir o lançamento financeiro
        buscarEquipamentoComCache(dto.equipamentoId());

        Manutencao manutencao = Manutencao.builder()
                .equipamentoId(dto.equipamentoId())
                .tipo(dto.tipo())
                .valor(dto.valor())
                .dataInicio(dto.dataInicio())
                .cnpjEmpresa(dto.cnpjEmpresa())
                .nomeEmpresa(dto.nomeEmpresa())
                .descricao(dto.descricao())
                .build();

        return ManutencaoResponseDTO.fromEntity(manutencaoRepository.save(manutencao));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, EquipamentoResumoDTO> buscarResumoParaTransparencia(List<UUID> equipamentoIds) {
        Map<UUID, EquipamentoResumoDTO> resultado = new HashMap<>();

        // Para teste, vamos pegar os últimos 10 anos para garantir que a massa de dados apareça
        LocalDate periodoBusca = LocalDate.now().minusMonths(12);
        LocalDateTime cincoDiasAtras = LocalDateTime.now().minusDays(5);

        for (UUID id : equipamentoIds) {
            // Usos
            long totalUsos = usoRepository.countByEquipamentoId(id);
            List<Uso> usosRecentes = usoRepository.findByEquipamentoIdAndDataHoraAfter(id, cincoDiasAtras);

            LocalDateTime dataUltimoUso = usosRecentes.stream()
                    .map(Uso::getDataHora)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            // Manutenções
            BigDecimal custoTotal = manutencaoRepository.somarGastosNoPeriodo(id, periodoBusca);
            long qtdManutencoes = manutencaoRepository.contarManutencoesNoPeriodo(id, periodoBusca);

            log.debug("ID={} | Custo={} | Qtd={}", id, custoTotal, qtdManutencoes);

            resultado.put(id, new EquipamentoResumoDTO(
                    totalUsos,
                    dataUltimoUso,
                    custoTotal,
                    (int) qtdManutencoes,
                    usosRecentes.isEmpty(),
                    false
            ));
        }
        return resultado;
    }

    @Cacheable(value = "equipamentos", key = "#id", unless = "#result == null")
    public EquipamentoExternalDTO buscarEquipamentoComCache(UUID id) {
        log.debug("CacheMiss=BuscaExterna EquipamentoId={}", id);
        return equipamentoClient.buscarPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsoResponseDTO> listarUsosPorEquipamento(UUID equipamentoId) {
        log.info("Operacao=ListarHistoricoUsos EquipamentoId={}", equipamentoId);
        return usoRepository.findByEquipamentoIdOrderByDataHoraDesc(equipamentoId)
                .stream()
                .map(UsoResponseDTO::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoResponseDTO> listarManutencoesPorEquipamento(UUID equipamentoId) {
        log.info("Operacao=ListarHistoricoManutencoes EquipamentoId={}", equipamentoId);
        return manutencaoRepository.findByEquipamentoIdOrderByDataInicioDesc(equipamentoId)
                .stream()
                .map(ManutencaoResponseDTO::fromEntity)
                .toList();
    }
}