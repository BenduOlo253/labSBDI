package com.abarrotes.pos.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CerrarCajaRequest(@NotNull Long idTurno, @NotNull Long idUsuario, @NotNull @DecimalMin("0.00") BigDecimal montoFinalContado) {}
