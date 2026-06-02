package com.abarrotes.pos.model.repository;

import com.abarrotes.pos.model.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
}
