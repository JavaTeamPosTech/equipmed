package com.postechfiap.msequipamentos.service.impl;

import com.postechfiap.msequipamentos.dto.EquipamentoRequestDTO;
import com.postechfiap.msequipamentos.dto.EquipamentoResponseDTO;
import com.postechfiap.msequipamentos.enums.StatusEquipamentoEnum;
import com.postechfiap.msequipamentos.enums.TipoEquipamentoEnum;
import com.postechfiap.msequipamentos.exception.SecurityInconsistencyException;
import com.postechfiap.msequipamentos.model.Equipamento;
import com.postechfiap.msequipamentos.repository.EquipamentoRepository;
import com.postechfiap.msequipamentos.service.EquipamentoService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquipamentoServiceImpl implements EquipamentoService {

    private final EquipamentoRepository repository;

    @Override
    @Transactional
    public EquipamentoResponseDTO criar(EquipamentoRequestDTO dto,
                                        String headerUnidade,
                                        String headerCidade) {
        log.info("Operacao=Criação TagPatrimonio={} Unidade={}", dto.tagPatrimonio(), dto.unidadeDeSaude());

        if (!dto.unidadeDeSaude().equalsIgnoreCase(headerUnidade)) {
            throw new SecurityInconsistencyException(
                    "VIOLAÇÃO DE JURISDIÇÃO: Esta API KEY pertence ao [" + headerUnidade +
                            "], mas tentou cadastrar um ativo para [" + dto.unidadeDeSaude() + "]."
            );
        }

        if (!dto.cidade().equalsIgnoreCase(headerCidade)) {
            throw new SecurityInconsistencyException(
                    "VIOLAÇÃO TERRITORIAL: Esta chave só tem permissão para operar na cidade de " + headerCidade
            );
        }

        Equipamento equipamento = dto.toEntity();
        Equipamento salvo = repository.save(equipamento);

        log.info("Status=Sucesso Id={} TagPatrimonio={}", salvo.getId(), salvo.getTagPatrimonio());
        return EquipamentoResponseDTO.fromEntity(salvo);
    }

    @Override
    @Transactional
    public EquipamentoResponseDTO criar(EquipamentoRequestDTO dto) {
        log.info("Operacao=Criação TagPatrimonio={} Unidade={}", dto.tagPatrimonio(), dto.unidadeDeSaude());

        Equipamento equipamento = dto.toEntity();
        Equipamento salvo = repository.save(equipamento);

        log.info("Status=Sucesso Id={} TagPatrimonio={}", salvo.getId(), salvo.getTagPatrimonio());
        return EquipamentoResponseDTO.fromEntity(salvo);
    }

    @Override
    public EquipamentoResponseDTO buscarPorId(UUID id) {
        log.debug("Operacao=BuscaPorId Id={}", id);
        return repository.findById(id)
                .map(EquipamentoResponseDTO::fromEntity)
                .orElseThrow(() -> {
                    log.warn("Status=Erro Id={} Mensagem='Equipamento não encontrado'", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento nao encontrado");
                });
    }

    @Override
    public Page<EquipamentoResponseDTO> listarComFiltros(
            String cidade, String estado, TipoEquipamentoEnum tipo,
            String unidadeDeSaude, StatusEquipamentoEnum status, Pageable pageable) {

        log.info("Operacao=ListagemFiltro Filtros[Cidade={}, Estado={}, Tipo={}, Unidade={}, Status={}]",
                cidade, estado, tipo, unidadeDeSaude, status);

        Specification<Equipamento> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (cidade != null && !cidade.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("cidade")), cidade.toLowerCase()));
            }
            if (estado != null && !estado.isBlank()) {
                predicates.add(cb.equal(cb.upper(root.get("estado")), estado.toUpperCase()));
            }
            if (tipo != null) {
                predicates.add(cb.equal(root.get("tipo"), tipo));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (unidadeDeSaude != null && !unidadeDeSaude.isBlank()) {
                // Aplicamos trim() no parâmetro e garantimos o lower case
                String termoBusca = "%" + unidadeDeSaude.trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("unidadeDeSaude")), termoBusca));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return repository.findAll(spec, pageable).map(EquipamentoResponseDTO::fromEntity);
    }

    @Override
    @Transactional
    public EquipamentoResponseDTO atualizarStatus(UUID id, StatusEquipamentoEnum novoStatus) {
        log.info("Operacao=AtualizacaoStatus Id={} NovoStatus={}", id, novoStatus);

        Equipamento e = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento nao encontrado"));

        e.setStatus(novoStatus);
        return EquipamentoResponseDTO.fromEntity(repository.save(e));
    }

    @Override
    @Transactional
    public EquipamentoResponseDTO atualizarLocalizacao(UUID id, String cidade, String estado) {
        log.info("Operacao=AtualizacaoLocalizacao Id={} NovaCidade={} NovoEstado={}", id, cidade, estado);

        Equipamento e = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento nao encontrado"));

        e.setCidade(cidade);
        e.setEstado(estado);
        return EquipamentoResponseDTO.fromEntity(repository.save(e));
    }
}