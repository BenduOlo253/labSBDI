package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "metodo_pago")
public class MetodoPago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_metodo_pago") public Long idMetodoPago;
    @Column(nullable=false, unique=true) public String nombre;
    @Column(name="requiere_referencia") public Boolean requiereReferencia = false;
    public Boolean activo = true;
}
