package com.abarrotes.pos.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="id_producto") public Long idProducto;
    @Column(name="codigo_barras", unique=true) public String codigoBarras;
    @Column(nullable=false) public String nombre;
    public String descripcion;
    @ManyToOne(optional=false) @JoinColumn(name="id_categoria") public Categoria categoria;
    @ManyToOne(optional=false) @JoinColumn(name="id_unidad") public UnidadMedida unidad;
    @ManyToOne(optional=false) @JoinColumn(name="id_impuesto") public Impuesto impuesto;
    @Column(name="precio_compra", precision=12, scale=2) public BigDecimal precioCompra = BigDecimal.ZERO;
    @Column(name="precio_venta", nullable=false, precision=12, scale=2) public BigDecimal precioVenta = BigDecimal.ZERO;
    @Column(name="stock_minimo", precision=12, scale=3) public BigDecimal stockMinimo = BigDecimal.ZERO;
    @Column(name="stock_maximo", precision=12, scale=3) public BigDecimal stockMaximo = BigDecimal.ZERO;
    @Column(name="controla_inventario") public Boolean controlaInventario = true;
    public Boolean activo = true;
    @Column(name="fecha_creacion") public LocalDateTime fechaCreacion = LocalDateTime.now();
}
