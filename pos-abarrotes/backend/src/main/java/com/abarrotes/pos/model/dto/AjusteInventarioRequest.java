package com.abarrotes.pos.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record AjusteInventarioRequest(@NotNull Long idSucursal, @NotNull Long idProducto, @NotNull Long idUsuario, @NotBlank String tipoMovimiento, @NotNull @DecimalMin("0.001") BigDecimal cantidad, String motivo) {}
