package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_usuario") public Long idUsuario;
    @ManyToOne(optional=false) @JoinColumn(name="id_rol") public Rol rol;
    @Column(nullable=false) public String nombre;
    @Column(nullable=false, unique=true) public String username;
    @Column(nullable=false) public String password;
    public Boolean activo = true;
    @Column(name="fecha_creacion") public LocalDateTime fechaCreacion = LocalDateTime.now();
}
