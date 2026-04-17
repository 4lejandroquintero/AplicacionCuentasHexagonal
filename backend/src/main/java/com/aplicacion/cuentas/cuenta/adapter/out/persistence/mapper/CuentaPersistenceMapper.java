package com.aplicacion.cuentas.cuenta.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import com.aplicacion.cuentas.cuenta.adapter.out.persistence.entity.CuentaJpaEntity;
import com.aplicacion.cuentas.cuenta.domain.model.Account;
import com.aplicacion.cuentas.cuenta.domain.model.AccountId;
import com.aplicacion.cuentas.cuenta.domain.model.Money;

@Component
public class CuentaPersistenceMapper {

	public CuentaJpaEntity aEntidad(Account cuenta) {
		CuentaJpaEntity e = new CuentaJpaEntity();
		e.setId(cuenta.id().value());
		e.setNumeroCuenta(cuenta.numeroCuenta());
		e.setTitular(cuenta.titular());
		e.setSaldo(cuenta.saldo().amount());
		e.setMoneda(cuenta.saldo().currencyCode());
		e.setEstado(cuenta.estado());
		e.setCreadoEn(cuenta.creadoEn());
		return e;
	}

	public Account aDominio(CuentaJpaEntity e) {
		AccountId id = AccountId.of(e.getId());
		Money saldo = Money.of(e.getSaldo(), e.getMoneda());
		return Account.reconstruir(id, e.getNumeroCuenta(), e.getTitular(), saldo, e.getEstado(), e.getCreadoEn());
	}
}
