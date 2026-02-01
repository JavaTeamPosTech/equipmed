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

        // Mantemos os 12 meses para custo, mas a ociosidade é calculada sobre a data global
        LocalDate periodoBusca = LocalDate.now().minusMonths(12);
        LocalDateTime limiteOciosidade = LocalDateTime.now().minusDays(5);

        for (UUID id : equipamentoIds) {
            // 1. Total de usos (Long)
            long totalUsos = usoRepository.countByEquipamentoId(id);

            // 2. Data do último uso (Busca no histórico total via MAX do SQL)
            LocalDateTime dataUltimoUso = usoRepository.findDataUltimoUso(id);

            // 3. Custos de manutenção
            BigDecimal custoTotal = manutencaoRepository.somarGastosNoPeriodo(id, periodoBusca);
            long qtdManutencoes = manutencaoRepository.contarManutencoesNoPeriodo(id, periodoBusca);

            // 4. Lógica de Ociosidade:
            // Se a data do último uso for nula ou anterior a 5 dias atrás, está ociosa.
            boolean isOciosa = (dataUltimoUso == null || dataUltimoUso.isBefore(limiteOciosidade));

            resultado.put(id, new EquipamentoResumoDTO(
                    totalUsos,
                    dataUltimoUso,
                    custoTotal,
                    (int) qtdManutencoes,
                    isOciosa,
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