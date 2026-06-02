package com.abarrotes.pos.model.repository;

import com.abarrotes.pos.model.entity.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Long> {
}
