package com.aplicacion.cuentas.cuenta.domain.port.in;

import java.util.UUID;

import com.aplicacion.cuentas.cuenta.domain.model.Account;

public interface InactivarCuentaUseCase {

	Account inactivar(UUID id);
}
