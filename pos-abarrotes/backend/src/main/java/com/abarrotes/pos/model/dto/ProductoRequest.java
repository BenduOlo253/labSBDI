package com.abarrotes.pos.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductoRequest(String codigoBarras, @NotBlank String nombre, String descripcion, @NotNull Long idCategoria, @NotNull Long idUnidad, @NotNull Long idImpuesto, @NotNull @DecimalMin("0.00") BigDecimal precioCompra, @NotNull @DecimalMin("0.01") BigDecimal precioVenta, @NotNull @DecimalMin("0.000") BigDecimal stockMinimo, @NotNull @DecimalMin("0.000") BigDecimal stockMaximo, Boolean controlaInventario, Boolean activo) {}
