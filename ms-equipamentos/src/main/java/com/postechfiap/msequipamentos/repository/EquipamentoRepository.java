package com.postechfiap.msequipamentos.repository;

import com.postechfiap.msequipamentos.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EquipamentoRepository extends
        JpaRepository<Equipamento, UUID>,
        JpaSpecificationExecutor<Equipamento> {
}