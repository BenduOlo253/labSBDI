package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "caja")
public class Caja {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_caja") public Long idCaja;
    @ManyToOne(optional=false) @JoinColumn(name="id_sucursal") public Sucursal sucursal;
    @Column(nullable=false) public String nombre;
    public Boolean activo = true;
}
