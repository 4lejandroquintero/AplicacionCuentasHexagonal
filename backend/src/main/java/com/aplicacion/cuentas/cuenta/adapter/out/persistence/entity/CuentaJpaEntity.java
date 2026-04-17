package com.aplicacion.cuentas.cuenta.adapter.out.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.aplicacion.cuentas.cuenta.domain.model.AccountStatus;

@Entity
@Table(name = "cuenta")
public class CuentaJpaEntity {

	@Id
	@Column(nullable = false, updatable = false)
	private UUID id;

	@Column(name = "numero_cuenta", nullable = false, unique = true, length = 32)
	private String numeroCuenta;

	@Column(name = "titular", nullable = false, length = 200)
	private String titular;

	@Column(name = "saldo", nullable = false, precision = 19, scale = 2)
	private BigDecimal saldo;

	@Column(name = "moneda", nullable = false, length = 3)
	private String moneda;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, length = 20)
	private AccountStatus estado;

	@Column(name = "creado_en", nullable = false)
	private Instant creadoEn;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public AccountStatus getEstado() {
		return estado;
	}

	public void setEstado(AccountStatus estado) {
		this.estado = estado;
	}

	public Instant getCreadoEn() {
		return creadoEn;
	}

	public void setCreadoEn(Instant creadoEn) {
		this.creadoEn = creadoEn;
	}
}
