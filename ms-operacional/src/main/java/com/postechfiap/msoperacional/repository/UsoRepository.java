package com.postechfiap.msoperacional.repository;

import com.postechfiap.msoperacional.model.Uso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UsoRepository extends JpaRepository<Uso, UUID> {
    List<Uso> findByEquipamentoIdIn(List<UUID> ids);
    List<Uso> findByEquipamentoIdAndDataHoraAfter(UUID id, LocalDateTime data);
    long countByEquipamentoId(UUID id);

    @Query("SELECT MAX(u.dataHora) FROM Uso u WHERE u.equipamentoId = :id")
    LocalDateTime findDataUltimoUso(@Param("id") UUID id);

    List<Uso> findByEquipamentoIdOrderByDataHoraDesc(UUID equipamentoId);
}