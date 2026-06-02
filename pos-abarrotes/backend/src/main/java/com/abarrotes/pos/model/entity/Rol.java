package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rol")
public class Rol {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_rol") public Long idRol;
    @Column(nullable=false, unique=true) public String nombre;
    public String descripcion;
    public Boolean activo = true;
}
