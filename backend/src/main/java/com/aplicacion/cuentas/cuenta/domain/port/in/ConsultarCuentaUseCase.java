package com.aplicacion.cuentas.cuenta.domain.port.in;

import java.util.List;
import java.util.UUID;

import com.aplicacion.cuentas.cuenta.domain.model.Account;

public interface ConsultarCuentaUseCase {

	Account obtenerPorId(UUID id);

	List<Account> listar();
}
