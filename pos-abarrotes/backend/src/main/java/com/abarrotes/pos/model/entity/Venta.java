package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "venta")
public class Venta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_venta") public Long idVenta;
    @Column(nullable=false, unique=true) public String folio;
    @ManyToOne(optional=false) @JoinColumn(name="id_sucursal") public Sucursal sucursal;
    @ManyToOne(optional=false) @JoinColumn(name="id_caja") public Caja caja;
    @ManyToOne(optional=false) @JoinColumn(name="id_turno") public TurnoCaja turno;
    @ManyToOne(optional=false) @JoinColumn(name="id_usuario") public Usuario usuario;
    public LocalDateTime fecha = LocalDateTime.now();
    @Column(precision=12, scale=2) public BigDecimal subtotal = BigDecimal.ZERO;
    @Column(name="descuento_total", precision=12, scale=2) public BigDecimal descuentoTotal = BigDecimal.ZERO;
    @Column(name="impuesto_total", precision=12, scale=2) public BigDecimal impuestoTotal = BigDecimal.ZERO;
    @Column(precision=12, scale=2) public BigDecimal total = BigDecimal.ZERO;
    public String estado = "PAGADA";
}
