package com.aplicacion.cuentas.cuenta.domain.port.in;

import com.aplicacion.cuentas.cuenta.application.command.MovimientoCommand;
import com.aplicacion.cuentas.cuenta.domain.model.Account;

public interface ConsignarUseCase {

	Account consignar(MovimientoCommand command);
}
