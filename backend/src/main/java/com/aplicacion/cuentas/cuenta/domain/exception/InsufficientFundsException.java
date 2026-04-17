package com.aplicacion.cuentas.cuenta.domain.exception;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientFundsException extends DomainRuleException {

	private final UUID cuentaId;
	private final BigDecimal solicitado;
	private final BigDecimal disponible;

	public InsufficientFundsException(UUID cuentaId, BigDecimal solicitado, BigDecimal disponible) {
		super("Fondos insuficientes para la cuenta " + cuentaId);
		this.cuentaId = cuentaId;
		this.solicitado = solicitado;
		this.disponible = disponible;
	}

	public UUID cuentaId() {
		return cuentaId;
	}

	public BigDecimal solicitado() {
		return solicitado;
	}

	public BigDecimal disponible() {
		return disponible;
	}
}
