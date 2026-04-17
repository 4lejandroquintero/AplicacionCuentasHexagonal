package com.aplicacion.cuentas.cuenta.adapter.out.numeracion;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.aplicacion.cuentas.cuenta.domain.port.out.AccountNumberGeneratorPort;

@Component
public class NumeroCuentaGeneradorAdapter implements AccountNumberGeneratorPort {

	private static final SecureRandom RANDOM = new SecureRandom();

	@Override
	public String siguienteNumero() {
		long parte = Math.abs(RANDOM.nextLong() % 1_000_000_000_000L);
		return String.format("BC%012d", parte);
	}
}
