package com.aplicacion.cuentas.cuenta.adapter.in.web.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovimientoRequest(@NotNull @Positive BigDecimal monto) {
}
