package com.abarrotes.pos.model.repository;

import com.abarrotes.pos.model.entity.TurnoCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TurnoCajaRepository extends JpaRepository<TurnoCaja, Long> {
    Optional<TurnoCaja> findByCaja_IdCajaAndEstado(Long idCaja, String estado);
}
