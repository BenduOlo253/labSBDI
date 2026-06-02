package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventario", uniqueConstraints=@UniqueConstraint(columnNames={"id_sucursal", "id_producto"}))
public class Inventario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_inventario") public Long idInventario;
    @ManyToOne(optional=false) @JoinColumn(name="id_sucursal") public Sucursal sucursal;
    @ManyToOne(optional=false) @JoinColumn(name="id_producto") public Producto producto;
    @Column(nullable=false, precision=12, scale=3) public BigDecimal existencia = BigDecimal.ZERO;
    @Column(name="fecha_actualizacion") public LocalDateTime fechaActualizacion = LocalDateTime.now();
}
