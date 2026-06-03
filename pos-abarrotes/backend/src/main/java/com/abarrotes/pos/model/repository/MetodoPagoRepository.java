package com.abarrotes.pos.model.repository;

import com.abarrotes.pos.model.entity.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
    List<MetodoPago> findByActivoTrueOrderByNombreAsc();
}
