package com.abarrotes.pos.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record VentaRequest(@NotNull Long idSucursal, @NotNull Long idCaja, @NotNull Long idTurno, @NotNull Long idUsuario, @NotEmpty List<@Valid VentaDetalleRequest> detalles, @NotEmpty List<@Valid VentaPagoRequest> pagos) {}
