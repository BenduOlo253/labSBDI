package com.abarrotes.pos.model.repository;

import com.abarrotes.pos.model.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByTurno_IdTurnoOrderByFechaDesc(Long idTurno);
    long count();
}
