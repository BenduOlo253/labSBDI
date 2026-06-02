package com.abarrotes.pos.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventarioResponse(Long idInventario, Long idSucursal, String sucursal, Long idProducto, String codigoBarras, String producto, BigDecimal existencia, BigDecimal stockMinimo, LocalDateTime fechaActualizacion) {}
