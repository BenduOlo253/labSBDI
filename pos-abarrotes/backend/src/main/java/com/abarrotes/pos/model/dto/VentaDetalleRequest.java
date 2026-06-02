package com.abarrotes.pos.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record VentaDetalleRequest(@NotNull Long idProducto, @NotNull @DecimalMin("0.001") BigDecimal cantidad) {}
