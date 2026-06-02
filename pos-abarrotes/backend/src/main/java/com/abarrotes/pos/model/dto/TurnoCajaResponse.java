package com.abarrotes.pos.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TurnoCajaResponse(Long idTurno, Long idCaja, String caja, Long idUsuarioApertura, String usuarioApertura, LocalDateTime fechaApertura, LocalDateTime fechaCierre, BigDecimal montoInicial, BigDecimal montoFinalSistema, BigDecimal montoFinalContado, BigDecimal diferencia, String estado) {}
