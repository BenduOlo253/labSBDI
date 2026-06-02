package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "venta_detalle")
public class VentaDetalle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_venta_detalle") public Long idVentaDetalle;
    @ManyToOne(optional=false) @JoinColumn(name="id_venta") public Venta venta;
    @ManyToOne(optional=false) @JoinColumn(name="id_producto") public Producto producto;
    @Column(nullable=false, precision=12, scale=3) public BigDecimal cantidad;
    @Column(name="precio_unitario", precision=12, scale=2) public BigDecimal precioUnitario;
    @Column(precision=12, scale=2) public BigDecimal descuento = BigDecimal.ZERO;
    @Column(precision=12, scale=2) public BigDecimal impuesto = BigDecimal.ZERO;
    @Column(precision=12, scale=2) public BigDecimal subtotal = BigDecimal.ZERO;
    @Column(precision=12, scale=2) public BigDecimal total = BigDecimal.ZERO;
}
