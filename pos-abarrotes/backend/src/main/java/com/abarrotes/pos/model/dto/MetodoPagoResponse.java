package com.abarrotes.pos.model.dto;

public record MetodoPagoResponse(Long idMetodoPago, String nombre, Boolean requiereReferencia, Boolean activo) {}
