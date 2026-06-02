package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_inventario")
public class MovimientoInventario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_movimiento") public Long idMovimiento;
    @ManyToOne(optional=false) @JoinColumn(name="id_sucursal") public Sucursal sucursal;
    @ManyToOne(optional=false) @JoinColumn(name="id_producto") public Producto producto;
    @ManyToOne(optional=false) @JoinColumn(name="id_usuario") public Usuario usuario;
    @Column(name="tipo_movimiento", nullable=false) public String tipoMovimiento;
    @Column(nullable=false, precision=12, scale=3) public BigDecimal cantidad;
    @Column(name="existencia_anterior", precision=12, scale=3) public BigDecimal existenciaAnterior;
    @Column(name="existencia_nueva", precision=12, scale=3) public BigDecimal existenciaNueva;
    @Column(name="referencia_tipo") public String referenciaTipo;
    @Column(name="referencia_id") public Long referenciaId;
    public String motivo;
    public LocalDateTime fecha = LocalDateTime.now();
}
