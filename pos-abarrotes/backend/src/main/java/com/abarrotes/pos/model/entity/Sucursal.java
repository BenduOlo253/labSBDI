package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sucursal")
public class Sucursal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_sucursal") public Long idSucursal;
    @Column(nullable=false) public String nombre;
    public String direccion;
    public String telefono;
    public Boolean activo = true;
}
