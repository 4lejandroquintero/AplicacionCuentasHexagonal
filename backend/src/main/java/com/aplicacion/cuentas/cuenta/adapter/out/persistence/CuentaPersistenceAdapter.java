package com.aplicacion.cuentas.cuenta.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.aplicacion.cuentas.cuenta.adapter.out.persistence.entity.CuentaJpaEntity;
import com.aplicacion.cuentas.cuenta.adapter.out.persistence.mapper.CuentaPersistenceMapper;
import com.aplicacion.cuentas.cuenta.adapter.out.persistence.repository.CuentaSpringRepository;
import com.aplicacion.cuentas.cuenta.domain.model.Account;
import com.aplicacion.cuentas.cuenta.domain.model.AccountId;
import com.aplicacion.cuentas.cuenta.domain.port.out.AccountRepositoryPort;

@Component
public class CuentaPersistenceAdapter implements AccountRepositoryPort {

	private final CuentaSpringRepository repositorio;
	private final CuentaPersistenceMapper mapper;

	public CuentaPersistenceAdapter(CuentaSpringRepository repositorio, CuentaPersistenceMapper mapper) {
		this.repositorio = repositorio;
		this.mapper = mapper;
	}

	@Override
	public Account guardar(Account cuenta) {
		CuentaJpaEntity entidad = mapper.aEntidad(cuenta);
		CuentaJpaEntity guardada = repositorio.save(entidad);
		return mapper.aDominio(guardada);
	}

	@Override
	public Optional<Account> buscarPorId(AccountId id) {
		return repositorio.findById(id.value()).map(mapper::aDominio);
	}

	@Override
	public List<Account> listarTodas() {
		return repositorio.findAll().stream().map(mapper::aDominio).toList();
	}

	@Override
	public boolean existeNumeroCuenta(String numeroCuenta) {
		return repositorio.existsByNumeroCuenta(numeroCuenta);
	}
}
