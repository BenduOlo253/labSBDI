package com.abarrotes.pos.model.repository;

import com.abarrotes.pos.model.entity.Caja;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CajaRepository extends JpaRepository<Caja, Long> {
}
