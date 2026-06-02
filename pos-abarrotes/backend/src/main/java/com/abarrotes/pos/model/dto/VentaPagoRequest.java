package com.abarrotes.pos.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record VentaPagoRequest(@NotNull Long idMetodoPago, @NotNull @DecimalMin("0.00") BigDecimal monto, String referencia, BigDecimal cambio) {}
