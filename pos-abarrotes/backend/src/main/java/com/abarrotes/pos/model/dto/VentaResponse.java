package com.abarrotes.pos.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VentaResponse(Long idVenta, String folio, BigDecimal subtotal, BigDecimal impuestoTotal, BigDecimal total, String estado, LocalDateTime fecha) {}
