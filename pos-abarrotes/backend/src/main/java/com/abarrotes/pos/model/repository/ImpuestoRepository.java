package com.abarrotes.pos.model.repository;

import com.abarrotes.pos.model.entity.Impuesto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpuestoRepository extends JpaRepository<Impuesto, Long> {
}
