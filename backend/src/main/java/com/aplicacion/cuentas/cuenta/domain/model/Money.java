package com.aplicacion.cuentas.cuenta.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {

	private static final int SCALE = 2;
	private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

	private final BigDecimal amount;
	private final String currencyCode;

	private Money(BigDecimal amount, String currencyCode) {
		this.amount = normalize(amount);
		this.currencyCode = Objects.requireNonNull(currencyCode, "currencyCode").toUpperCase();
	}

	public static Money cero(String currencyCode) {
		return new Money(BigDecimal.ZERO, currencyCode);
	}

	public static Money of(BigDecimal amount, String currencyCode) {
		Objects.requireNonNull(amount, "amount");
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("El monto no puede ser negativo");
		}
		return new Money(amount, currencyCode);
	}

	private static BigDecimal normalize(BigDecimal value) {
		return value.setScale(SCALE, ROUNDING);
	}

	public Money add(Money other) {
		validarMismaMoneda(other);
		return new Money(this.amount.add(other.amount), currencyCode);
	}

	public Money subtract(Money other) {
		validarMismaMoneda(other);
		BigDecimal result = this.amount.subtract(other.amount);
		if (result.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Resultado negativo no permitido en resta de saldo");
		}
		return new Money(result, currencyCode);
	}

	public boolean isLessThan(Money other) {
		validarMismaMoneda(other);
		return this.amount.compareTo(other.amount) < 0;
	}

	public BigDecimal amount() {
		return amount;
	}

	public String currencyCode() {
		return currencyCode;
	}

	private void validarMismaMoneda(Money other) {
		Objects.requireNonNull(other, "other");
		if (!this.currencyCode.equals(other.currencyCode)) {
			throw new IllegalArgumentException("Operacion entre monedas distintas");
		}
	}
}
