package com.aplicacion.cuentas.cuenta.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.aplicacion.cuentas.cuenta.domain.model.Account;
import com.aplicacion.cuentas.cuenta.domain.model.AccountId;

public interface AccountRepositoryPort {

	Account guardar(Account cuenta);

	Optional<Account> buscarPorId(AccountId id);

	List<Account> listarTodas();

	boolean existeNumeroCuenta(String numeroCuenta);
}
