package com.abarrotes.pos.model.repository;

import com.abarrotes.pos.model.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findByProducto_IdProducto(Long idProducto);
    Optional<Inventario> findBySucursal_IdSucursalAndProducto_IdProducto(Long idSucursal, Long idProducto);
    List<Inventario> findByProducto_ActivoTrueOrderByProducto_NombreAsc();
}
