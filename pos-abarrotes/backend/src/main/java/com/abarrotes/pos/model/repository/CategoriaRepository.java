package com.abarrotes.pos.model.repository;

import com.abarrotes.pos.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
