package com.aplicacion.cuentas.cuenta.application.exception;

import java.util.UUID;

public class CuentaNoEncontradaException extends RuntimeException {

	public CuentaNoEncontradaException(UUID id) {
		super("No existe cuenta con id " + id);
	}
}
