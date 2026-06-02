package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "categoria")
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_categoria") public Long idCategoria;
    @Column(nullable=false, unique=true) public String nombre;
    public String descripcion;
    public Boolean activo = true;
}
