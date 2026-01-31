package com.postechfiap.msoperacional.service.impl;

import com.postechfiap.msoperacional.client.EquipamentoClient;
import com.postechfiap.msoperacional.dto.*;
import com.postechfiap.msoperacional.enums.SigtapEnum;
import com.postechfiap.msoperacional.model.Uso;
import com.postechfiap.msoperacional.repository.ManutencaoRepository;
import com.postechfiap.msoperacional.repository.UsoRepository;
import com.postechfiap.msoperacional.service.OperacionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        log.info("Operacao=RegistroUso EquipamentoId={} Sigtap={}", dto.equipamentoId(), dto.codigoSigtap());

        // 1. Busca info do equipamento (via Feign com Cache)
        EquipamentoExternalDTO equipamento = buscarEquipamentoComCache(dto.equipamentoId());

        // 2. Valida SIGTAP vs Tipo Equipamento
        SigtapEnum sigtap = SigtapEnum.deCodigo(dto.codigoSigtap());
        if (!sigtap.getTipoEquipamentoPermitido().equals(equipamento.tipo())) {
            log.error("Status=Erro Mensagem='Incompatibilidade SIGTAP' SigtapTipo={} EquipamentoTipo={}",
                    sigtap.getTipoEquipamentoPermitido(), equipamento.tipo());
            throw new IllegalArgumentException("Este procedimento não pode ser realizado neste tipo de equipamento.");
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

    @Cacheable(value = "equipamentos", key = "#id")
    public EquipamentoExternalDTO buscarEquipamentoComCache(UUID id) {
        log.debug("CacheMiss=BuscaFeign EquipamentoId={}", id);
        return equipamentoClient.buscarPorId(id);
    }

    @Override
    public Map<UUID, EquipamentoResumoDTO> buscarResumoParaTransparencia(List<UUID> equipamentoIds) {
        log.info("Operacao=BuscaResumoTransparencia TotalIds={}", equipamentoIds.size());

        // Aqui implementaremos a lógica de agregação para o MS-Transparência no próximo passo
        return new HashMap<>();
    }

    // Método de manutenção será implementado na sequência...
}