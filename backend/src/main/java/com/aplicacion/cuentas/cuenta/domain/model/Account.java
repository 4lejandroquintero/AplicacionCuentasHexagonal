package com.aplicacion.cuentas.cuenta.domain.model;

import java.time.Instant;
import java.util.Objects;

import com.aplicacion.cuentas.cuenta.domain.exception.InsufficientFundsException;
import com.aplicacion.cuentas.cuenta.domain.exception.InvalidAccountStateException;

public final class Account {

	private final AccountId id;
	private final String numeroCuenta;
	private String titular;
	private Money saldo;
	private AccountStatus estado;
	private final Instant creadoEn;

	private Account(AccountId id, String numeroCuenta, String titular, Money saldo, AccountStatus estado,
			Instant creadoEn) {
		this.id = Objects.requireNonNull(id, "id");
		this.numeroCuenta = Objects.requireNonNull(numeroCuenta, "numeroCuenta");
		this.titular = Objects.requireNonNull(titular, "titular");
		this.saldo = Objects.requireNonNull(saldo, "saldo");
		this.estado = Objects.requireNonNull(estado, "estado");
		this.creadoEn = Objects.requireNonNull(creadoEn, "creadoEn");
	}

	public static Account abrir(AccountId id, String numeroCuenta, String titular, String moneda) {
		Money saldoInicial = Money.cero(moneda);
		return new Account(id, numeroCuenta, titular, saldoInicial, AccountStatus.ACTIVA, Instant.now());
	}

	public static Account reconstruir(AccountId id, String numeroCuenta, String titular, Money saldo, AccountStatus estado,
			Instant creadoEn) {
		return new Account(id, numeroCuenta, titular, saldo, estado, creadoEn);
	}

	public void consignar(Money monto) {
		asegurarActiva();
		this.saldo = this.saldo.add(monto);
	}

	public void retirar(MontoOperacion monto) {
		asegurarActiva();
		if (this.saldo.isLessThan(monto.valor())) {
			throw new InsufficientFundsException(id.value(), monto.valor().amount(), saldo.amount());
		}
		this.saldo = this.saldo.subtract(monto.valor());
	}

	public void cerrar() {
		if (this.estado == AccountStatus.CERRADA) {
			return;
		}
		this.estado = AccountStatus.CERRADA;
	}

	private void asegurarActiva() {
		if (this.estado != AccountStatus.ACTIVA) {
			throw new InvalidAccountStateException("La cuenta no esta activa");
		}
	}

	public AccountId id() {
		return id;
	}

	public String numeroCuenta() {
		return numeroCuenta;
	}

	public String titular() {
		return titular;
	}

	public Money saldo() {
		return saldo;
	}

	public AccountStatus estado() {
		return estado;
	}

	public Instant creadoEn() {
		return creadoEn;
	}

	public record MontoOperacion(Money valor) {
		public MontoOperacion {
			Objects.requireNonNull(valor, "valor");
		}
	}
}
