package com.aplicacion.cuentas.cuenta.domain.port.in;

import com.aplicacion.cuentas.cuenta.application.command.CrearCuentaCommand;
import com.aplicacion.cuentas.cuenta.domain.model.Account;

public interface CrearCuentaUseCase {

	Account crear(CrearCuentaCommand command);
}
