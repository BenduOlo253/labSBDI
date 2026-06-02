package com.abarrotes.pos.model.dto;

import java.math.BigDecimal;

public record ProductoResponse(Long idProducto, String codigoBarras, String nombre, String descripcion, Long idCategoria, String categoria, Long idUnidad, String unidad, Long idImpuesto, String impuesto, BigDecimal porcentajeImpuesto, BigDecimal precioCompra, BigDecimal precioVenta, BigDecimal stockMinimo, BigDecimal stockMaximo, Boolean controlaInventario, Boolean activo) {}
