package com.aplicacion.cuentas.cuenta.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.aplicacion.cuentas.cuenta.domain.model.AccountStatus;

public record CuentaResponse(UUID id, String numeroCuenta, String titular, BigDecimal saldo, String moneda,
		AccountStatus estado, Instant creadoEn) {
}
