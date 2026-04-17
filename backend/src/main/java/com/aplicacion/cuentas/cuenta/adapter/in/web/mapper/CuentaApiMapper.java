package com.aplicacion.cuentas.cuenta.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import com.aplicacion.cuentas.cuenta.adapter.in.web.dto.CuentaResponse;
import com.aplicacion.cuentas.cuenta.domain.model.Account;

@Component
public class CuentaApiMapper {

	public CuentaResponse aRespuesta(Account cuenta) {
		return new CuentaResponse(cuenta.id().value(), cuenta.numeroCuenta(), cuenta.titular(), cuenta.saldo().amount(),
				cuenta.saldo().currencyCode(), cuenta.estado(), cuenta.creadoEn());
	}
}
