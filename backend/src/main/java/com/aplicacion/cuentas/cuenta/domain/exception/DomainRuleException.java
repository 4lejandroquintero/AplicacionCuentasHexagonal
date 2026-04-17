package com.aplicacion.cuentas.cuenta.domain.exception;

public abstract class DomainRuleException extends RuntimeException {

	protected DomainRuleException(String message) {
		super(message);
	}
}
