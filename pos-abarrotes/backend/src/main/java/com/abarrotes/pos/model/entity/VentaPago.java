package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "venta_pago")
public class VentaPago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_venta_pago") public Long idVentaPago;
    @ManyToOne(optional=false) @JoinColumn(name="id_venta") public Venta venta;
    @ManyToOne(optional=false) @JoinColumn(name="id_metodo_pago") public MetodoPago metodoPago;
    @Column(nullable=false, precision=12, scale=2) public BigDecimal monto;
    public String referencia;
    @Column(precision=12, scale=2) public BigDecimal cambio = BigDecimal.ZERO;
    public LocalDateTime fecha = LocalDateTime.now();
}
