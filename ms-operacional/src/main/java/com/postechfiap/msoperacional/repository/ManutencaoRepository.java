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

    @Query("SELECT SUM(m.valor) FROM Manutencao m WHERE m.equipamentoId = :id AND m.dataInicio >= :dataInicio")
    BigDecimal somarGastosNoPeriodo(@Param("id") UUID id, @Param("dataInicio") LocalDate dataInicio);

    // Novo: Busca histórico ordenado
    List<Manutencao> findByEquipamentoIdOrderByDataInicioDesc(UUID equipamentoId);
}