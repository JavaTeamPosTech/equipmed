package com.postechfiap.msoperacional.service.impl;

import com.postechfiap.msoperacional.client.EquipamentoClient;
import com.postechfiap.msoperacional.dto.EquipamentoExternalDTO;
import com.postechfiap.msoperacional.dto.ManutencaoRequestDTO;
import com.postechfiap.msoperacional.dto.UsoRequestDTO;
import com.postechfiap.msoperacional.enums.SigtapEnum;
import com.postechfiap.msoperacional.model.Manutencao;
import com.postechfiap.msoperacional.model.Uso;
import com.postechfiap.msoperacional.repository.ManutencaoRepository;
import com.postechfiap.msoperacional.repository.UsoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class OperacionalServiceImplTest {

    @Mock
    UsoRepository usoRepository;

    @Mock
    ManutencaoRepository manutencaoRepository;

    @Mock
    EquipamentoClient equipamentoClient;

    @InjectMocks
    OperacionalServiceImpl service;

    @Test
    void registrarUso_success_whenSigtapCompatible() {
        UUID equipamentoId = UUID.randomUUID();
        String codigoSigtap = "03.04.01.026-6"; // exemplo usado no projeto
        UsoRequestDTO dto = mock(UsoRequestDTO.class);
        when(dto.equipamentoId()).thenReturn(equipamentoId);
        when(dto.codigoSigtap()).thenReturn(codigoSigtap);
        when(dto.operadorId()).thenReturn("OP-1");

        // garantir tipo compatível com o Sigtap retornado
        SigtapEnum sigtap = SigtapEnum.deCodigo(codigoSigtap);
        EquipamentoExternalDTO equipamento = mock(EquipamentoExternalDTO.class);
        when(equipamento.tipo()).thenReturn(sigtap.getTipoEquipamentoPermitido());
        when(equipamentoClient.buscarPorId(equipamentoId)).thenReturn(equipamento);

        // salvar deve retornar a própria entidade passada
        when(usoRepository.save(any(Uso.class))).thenAnswer(inv -> inv.getArgument(0));

        var resp = service.registrarUso(dto);

        verify(equipamentoClient, times(1)).buscarPorId(equipamentoId);
        verify(usoRepository, times(1)).save(any(Uso.class));
        assertNotNull(resp);
    }

    @Test
    void registrarUso_throwsWhenSigtapIncompatible() {
        UUID equipamentoId = UUID.randomUUID();
        String codigoSigtap = "03.04.01.026-6";
        UsoRequestDTO dto = mock(UsoRequestDTO.class);
        when(dto.equipamentoId()).thenReturn(equipamentoId);
        when(dto.codigoSigtap()).thenReturn(codigoSigtap);

        EquipamentoExternalDTO equipamento = mock(EquipamentoExternalDTO.class);
        when(equipamento.tipo()).thenReturn("TIPO-DIFERENTE");
        when(equipamentoClient.buscarPorId(equipamentoId)).thenReturn(equipamento);

        assertThrows(IllegalArgumentException.class, () -> service.registrarUso(dto));

        verify(equipamentoClient, times(1)).buscarPorId(equipamentoId);
        verify(usoRepository, never()).save(any());
    }

    @Test
    void registrarManutencao_success_whenEquipamentoExists() {
        UUID equipamentoId = UUID.randomUUID();
        ManutencaoRequestDTO dto = mock(ManutencaoRequestDTO.class);
        when(dto.equipamentoId()).thenReturn(equipamentoId);
        when(dto.tipo()).thenReturn(null);
        when(dto.valor()).thenReturn(BigDecimal.TEN);
        when(dto.dataInicio()).thenReturn(null);
        when(dto.cnpjEmpresa()).thenReturn(null);
        when(dto.nomeEmpresa()).thenReturn(null);
        when(dto.descricao()).thenReturn(null);

        // equipamento existe
        EquipamentoExternalDTO equipamento = mock(EquipamentoExternalDTO.class);
        when(equipamentoClient.buscarPorId(equipamentoId)).thenReturn(equipamento);

        when(manutencaoRepository.save(any(Manutencao.class))).thenAnswer(inv -> inv.getArgument(0));

        var resp = service.registrarManutencao(dto);

        verify(equipamentoClient, times(1)).buscarPorId(equipamentoId);
        verify(manutencaoRepository, times(1)).save(any(Manutencao.class));
        assertNotNull(resp);
    }

    @Test
    void buscarResumoParaTransparencia_aggregatesAndFlagsOciosidade() {
        UUID id = UUID.randomUUID();

        when(usoRepository.countByEquipamentoId(id)).thenReturn(5L);
        // último uso há 10 dias -> ocioso (limite 5 dias no código)
        when(usoRepository.findDataUltimoUso(id)).thenReturn(LocalDateTime.now().minusDays(10));
        when(manutencaoRepository.somarGastosNoPeriodo(eq(id), any())).thenReturn(BigDecimal.valueOf(1500));
        when(manutencaoRepository.contarManutencoesNoPeriodo(eq(id), any())).thenReturn(2L);

        Map<UUID, ?> result = service.buscarResumoParaTransparencia(List.of(id));

        assertNotNull(result);
        assertTrue(result.containsKey(id));
        verify(usoRepository, times(1)).countByEquipamentoId(id);
        verify(usoRepository, times(1)).findDataUltimoUso(id);
        verify(manutencaoRepository, times(1)).somarGastosNoPeriodo(eq(id), any());
        verify(manutencaoRepository, times(1)).contarManutencoesNoPeriodo(eq(id), any());
    }

    @Test
    void listarUsosPorEquipamento_delegatesAndMaps() {
        UUID id = UUID.randomUUID();
        Uso uso = Uso.builder().equipamentoId(id).operadorId("OP-XYZ").build();
        when(usoRepository.findByEquipamentoIdOrderByDataHoraDesc(id)).thenReturn(List.of(uso));

        var list = service.listarUsosPorEquipamento(id);

        assertNotNull(list);
        assertEquals(1, list.size());
        verify(usoRepository, times(1)).findByEquipamentoIdOrderByDataHoraDesc(id);
    }

    @Test
    void listarManutencoesPorEquipamento_delegatesAndMaps() {
        UUID id = UUID.randomUUID();
        Manutencao m = Manutencao.builder().equipamentoId(id).valor(BigDecimal.valueOf(200)).build();
        when(manutencaoRepository.findByEquipamentoIdOrderByDataInicioDesc(id)).thenReturn(List.of(m));

        var list = service.listarManutencoesPorEquipamento(id);

        assertNotNull(list);
        assertEquals(1, list.size());
        verify(manutencaoRepository, times(1)).findByEquipamentoIdOrderByDataInicioDesc(id);
    }
}