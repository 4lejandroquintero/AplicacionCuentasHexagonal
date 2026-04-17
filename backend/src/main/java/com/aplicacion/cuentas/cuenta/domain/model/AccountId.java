package com.aplicacion.cuentas.cuenta.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class AccountId {

	private final UUID value;

	private AccountId(UUID value) {
		this.value = Objects.requireNonNull(value, "value");
	}

	public static AccountId nuevo() {
		return new AccountId(UUID.randomUUID());
	}

	public static AccountId of(UUID value) {
		return new AccountId(value);
	}

	public UUID value() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AccountId accountId)) {
			return false;
		}
		return value.equals(accountId.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
