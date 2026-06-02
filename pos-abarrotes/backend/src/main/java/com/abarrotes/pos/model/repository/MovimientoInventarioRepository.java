package com.abarrotes.pos.model.repository;

import com.abarrotes.pos.model.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
}
