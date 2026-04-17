package com.aplicacion.cuentas.cuenta.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aplicacion.cuentas.cuenta.application.command.CrearCuentaCommand;
import com.aplicacion.cuentas.cuenta.application.command.MovimientoCommand;
import com.aplicacion.cuentas.cuenta.application.exception.CuentaNoEncontradaException;
import com.aplicacion.cuentas.cuenta.domain.model.Account;
import com.aplicacion.cuentas.cuenta.domain.model.AccountId;
import com.aplicacion.cuentas.cuenta.domain.model.Account.MontoOperacion;
import com.aplicacion.cuentas.cuenta.domain.model.Money;
import com.aplicacion.cuentas.cuenta.domain.port.in.ConsignarUseCase;
import com.aplicacion.cuentas.cuenta.domain.port.in.ConsultarCuentaUseCase;
import com.aplicacion.cuentas.cuenta.domain.port.in.CrearCuentaUseCase;
import com.aplicacion.cuentas.cuenta.domain.port.in.RetirarUseCase;
import com.aplicacion.cuentas.cuenta.domain.port.out.AccountNumberGeneratorPort;
import com.aplicacion.cuentas.cuenta.domain.port.out.AccountRepositoryPort;

@Service
@Transactional
public class CuentaApplicationService
		implements CrearCuentaUseCase, ConsultarCuentaUseCase, ConsignarUseCase, RetirarUseCase {

	private final AccountRepositoryPort cuentas;
	private final AccountNumberGeneratorPort numerosCuenta;

	public CuentaApplicationService(AccountRepositoryPort cuentas, AccountNumberGeneratorPort numerosCuenta) {
		this.cuentas = cuentas;
		this.numerosCuenta = numerosCuenta;
	}

	@Override
	public Account crear(CrearCuentaCommand command) {
		String numero = generarNumeroUnico();
		AccountId id = AccountId.nuevo();
		Account cuenta = Account.abrir(id, numero, command.nombreTitular(), command.codigoMoneda());
		return cuentas.guardar(cuenta);
	}

	@Override
	@Transactional(readOnly = true)
	public Account obtenerPorId(UUID id) {
		return cuentas.buscarPorId(AccountId.of(id)).orElseThrow(() -> new CuentaNoEncontradaException(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Account> listar() {
		return cuentas.listarTodas();
	}

	@Override
	public Account consignar(MovimientoCommand command) {
		Account cuenta = cuentas.buscarPorId(AccountId.of(command.cuentaId()))
				.orElseThrow(() -> new CuentaNoEncontradaException(command.cuentaId()));
		Money monto = Money.of(command.monto(), cuenta.saldo().currencyCode());
		cuenta.consignar(monto);
		return cuentas.guardar(cuenta);
	}

	@Override
	public Account retirar(MovimientoCommand command) {
		Account cuenta = cuentas.buscarPorId(AccountId.of(command.cuentaId()))
				.orElseThrow(() -> new CuentaNoEncontradaException(command.cuentaId()));
		Money monto = Money.of(command.monto(), cuenta.saldo().currencyCode());
		cuenta.retirar(new MontoOperacion(monto));
		return cuentas.guardar(cuenta);
	}

	private String generarNumeroUnico() {
		String candidato = numerosCuenta.siguienteNumero();
		int intentos = 0;
		while (cuentas.existeNumeroCuenta(candidato) && intentos < 50) {
			candidato = numerosCuenta.siguienteNumero();
			intentos++;
		}
		if (cuentas.existeNumeroCuenta(candidato)) {
			throw new IllegalStateException("No fue posible generar un numero de cuenta unico");
		}
		return candidato;
	}
}
