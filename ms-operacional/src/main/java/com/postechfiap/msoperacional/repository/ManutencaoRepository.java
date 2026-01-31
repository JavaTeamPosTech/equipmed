package com.postechfiap.msoperacional.repository;

import com.postechfiap.msoperacional.model.Manutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ManutencaoRepository extends JpaRepository<Manutencao, UUID> {
    List<Manutencao> findByEquipamentoIdIn(List<UUID> ids);

    @Query(value = """
        SELECT COALESCE(SUM(valor), 0) 
        FROM tb_manutencoes 
        WHERE equipamento_id = :id 
        AND data_inicio >= :dataInicio
    """, nativeQuery = true)
    BigDecimal somarGastosNoPeriodo(@Param("id") UUID id, @Param("dataInicio") LocalDate dataInicio);

    @Query(value = """
        SELECT COUNT(*) 
        FROM tb_manutencoes 
        WHERE equipamento_id = :id 
        AND data_inicio >= :dataInicio
    """, nativeQuery = true)
    long contarManutencoesNoPeriodo(@Param("id") UUID id, @Param("dataInicio") LocalDate dataInicio);

    // Novo: Busca histórico ordenado
    List<Manutencao> findByEquipamentoIdOrderByDataInicioDesc(UUID equipamentoId);
}