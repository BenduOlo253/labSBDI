package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "impuesto")
public class Impuesto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_impuesto") public Long idImpuesto;
    @Column(nullable=false) public String nombre;
    @Column(nullable=false, precision=10, scale=2) public BigDecimal porcentaje = BigDecimal.ZERO;
    public Boolean activo = true;
}
