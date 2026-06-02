package com.abarrotes.pos.model.repository;

import com.abarrotes.pos.model.entity.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
}
