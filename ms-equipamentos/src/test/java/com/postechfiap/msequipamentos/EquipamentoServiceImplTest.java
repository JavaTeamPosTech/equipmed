// java
package com.postechfiap.msequipamentos;

import com.postechfiap.msequipamentos.dto.EquipamentoRequestDTO;
import com.postechfiap.msequipamentos.dto.EquipamentoResponseDTO;
import com.postechfiap.msequipamentos.enums.StatusEquipamentoEnum;
import com.postechfiap.msequipamentos.exception.SecurityInconsistencyException;
import com.postechfiap.msequipamentos.model.Equipamento;
import com.postechfiap.msequipamentos.repository.EquipamentoRepository;
import com.postechfiap.msequipamentos.service.impl.EquipamentoServiceImpl;
import jakarta.persistence.criteria.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class EquipamentoServiceImplTest {

    @Mock
    EquipamentoRepository repository;

    @InjectMocks
    EquipamentoServiceImpl service;

    @Test
    void criar_withMatchingHeaders_savesAndReturns() {
        EquipamentoRequestDTO dto = mock(EquipamentoRequestDTO.class);
        when(dto.tagPatrimonio()).thenReturn("TAG123");
        when(dto.unidadeDeSaude()).thenReturn("UnidadeA");
        when(dto.cidade()).thenReturn("CidadeX");

        Equipamento entidade = mock(Equipamento.class);
        when(dto.toEntity()).thenReturn(entidade);

        Equipamento salvo = mock(Equipamento.class);
        UUID id = UUID.randomUUID();
        when(salvo.getId()).thenReturn(id);
        when(salvo.getTagPatrimonio()).thenReturn("TAG123");

        when(repository.save(entidade)).thenReturn(salvo);

        EquipamentoResponseDTO resp = service.criar(dto, "UnidadeA", "CidadeX");

        verify(repository, times(1)).save(entidade);
        assertNotNull(resp);
        assertEquals("TAG123", resp.tagPatrimonio());
    }

    @Test
    void criar_withMismatchedUnidade_throwsSecurityInconsistencyException() {
        EquipamentoRequestDTO dto = mock(EquipamentoRequestDTO.class);
        Mockito.lenient().when(dto.unidadeDeSaude()).thenReturn("OutraUnidade");
        Mockito.lenient().when(dto.cidade()).thenReturn("CidadeX");

        var ex = assertThrows(RuntimeException.class, () -> service.criar(dto, "UnidadeA", "CidadeX"));
        assertThrows(SecurityInconsistencyException.class, () ->
                service.criar(dto, "UnidadeA", "CidadeX")
        );

    }

    @Test
    void criar_withMismatchedCidade_throwsSecurityInconsistencyException() {
        EquipamentoRequestDTO dto = mock(EquipamentoRequestDTO.class);
        when(dto.unidadeDeSaude()).thenReturn("UnidadeA");
        when(dto.cidade()).thenReturn("OutraCidade");

        var ex = assertThrows(RuntimeException.class, () -> service.criar(dto, "UnidadeA", "CidadeX"));
        assertThrows(SecurityInconsistencyException.class, () ->
                service.criar(dto, "UnidadeA", "CidadeX")
        );

    }

    @Test
    void buscarPorId_found_returnsDto() {
        UUID id = UUID.randomUUID();
        Equipamento e = mock(Equipamento.class);
        when(e.getId()).thenReturn(id);
        when(e.getTagPatrimonio()).thenReturn("TAG-1");

        when(repository.findById(id)).thenReturn(Optional.of(e));

        EquipamentoResponseDTO resp = service.buscarPorId(id);

        assertNotNull(resp);
        assertEquals("TAG-1", resp.tagPatrimonio());
    }

    @Test
    void buscarPorId_notFound_throwsNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.buscarPorId(id));
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void listarComFiltros_delegatesToRepository_andMapsResult() {
        Equipamento e = mock(Equipamento.class);
        when(e.getTagPatrimonio()).thenReturn("TAG-10");

        Page<Equipamento> page = new PageImpl<>(List.of(e));
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<EquipamentoResponseDTO> result = service.listarComFiltros(
                "cidadeX", "estadoY", null, "uni", null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("TAG-10", result.getContent().get(0).tagPatrimonio());
    }

    @Test
    void atualizarStatus_existing_updatesAndReturns() {
        UUID id = UUID.randomUUID();
        Equipamento original = mock(Equipamento.class);
        Equipamento saved = mock(Equipamento.class);

        when(repository.findById(id)).thenReturn(Optional.of(original));
        when(repository.save(original)).thenReturn(saved);

        when(saved.getId()).thenReturn(id);
        when(saved.getTagPatrimonio()).thenReturn("TAG-STAT");
        when(saved.getStatus()).thenReturn(StatusEquipamentoEnum.ATIVO);

        var resp = service.atualizarStatus(id, StatusEquipamentoEnum.ATIVO);

        verify(original, times(1)).setStatus(StatusEquipamentoEnum.ATIVO);
        verify(repository, times(1)).save(original);
        assertEquals(StatusEquipamentoEnum.ATIVO, resp.status());
    }

    @Test
    void atualizarStatus_notFound_throwsNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.atualizarStatus(id, StatusEquipamentoEnum.ATIVO));
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void atualizarLocalizacao_existing_updatesAndReturns() {
        UUID id = UUID.randomUUID();
        Equipamento original = mock(Equipamento.class);
        Equipamento saved = mock(Equipamento.class);

        when(repository.findById(id)).thenReturn(Optional.of(original));
        when(repository.save(original)).thenReturn(saved);

        when(saved.getCidade()).thenReturn("NovaCidade");
        when(saved.getEstado()).thenReturn("NovoEstado");
        when(saved.getId()).thenReturn(id);

        var resp = service.atualizarLocalizacao(id, "NovaCidade", "NovoEstado");

        verify(original, times(1)).setCidade("NovaCidade");
        verify(original, times(1)).setEstado("NovoEstado");
        verify(repository, times(1)).save(original);
        String[] partes = resp.localizacaoCompleta().split("-");
        assertEquals("NovaCidade", partes[0].trim());
        assertEquals("NovoEstado", partes[1].trim());
    }

    @Test
    void atualizarLocalizacao_notFound_throwsNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.atualizarLocalizacao(id, "c", "e"));
        assertEquals(404, ex.getStatusCode().value());
    }
}