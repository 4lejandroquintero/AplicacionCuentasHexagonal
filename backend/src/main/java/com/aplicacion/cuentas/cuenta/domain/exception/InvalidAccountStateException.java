package com.aplicacion.cuentas.cuenta.domain.exception;

public class InvalidAccountStateException extends DomainRuleException {

	public InvalidAccountStateException(String message) {
		super(message);
	}
}
