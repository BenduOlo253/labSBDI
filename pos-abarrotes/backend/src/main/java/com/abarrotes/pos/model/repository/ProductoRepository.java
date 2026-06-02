package com.abarrotes.pos.model.repository;

import com.abarrotes.pos.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByCodigoBarrasAndActivoTrue(String codigoBarras);
    List<Producto> findByActivoTrueOrderByNombreAsc();
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
}
