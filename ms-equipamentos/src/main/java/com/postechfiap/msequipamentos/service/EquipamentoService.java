package com.postechfiap.msequipamentos.service;

import com.postechfiap.msequipamentos.dto.EquipamentoRequestDTO;
import com.postechfiap.msequipamentos.dto.EquipamentoResponseDTO;
import com.postechfiap.msequipamentos.enums.StatusEquipamentoEnum;
import com.postechfiap.msequipamentos.enums.TipoEquipamentoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EquipamentoService {
    EquipamentoResponseDTO criar(EquipamentoRequestDTO dto);
    EquipamentoResponseDTO buscarPorId(UUID id);
    Page<EquipamentoResponseDTO> listarComFiltros(
            String cidade,
            String estado,
            TipoEquipamentoEnum tipo,
            String unidadeDeSaude,
            StatusEquipamentoEnum status,
            Pageable pageable);
    EquipamentoResponseDTO atualizarStatus(UUID id, StatusEquipamentoEnum novoStatus);
    EquipamentoResponseDTO atualizarLocalizacao(UUID id, String cidade, String estado);
}