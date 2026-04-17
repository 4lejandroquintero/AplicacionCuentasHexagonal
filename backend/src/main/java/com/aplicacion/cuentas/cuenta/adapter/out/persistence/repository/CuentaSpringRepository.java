package com.aplicacion.cuentas.cuenta.adapter.out.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aplicacion.cuentas.cuenta.adapter.out.persistence.entity.CuentaJpaEntity;

public interface CuentaSpringRepository extends JpaRepository<CuentaJpaEntity, UUID> {

	boolean existsByNumeroCuenta(String numeroCuenta);
}
