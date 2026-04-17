package com.aplicacion.cuentas.cuenta.adapter.in.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aplicacion.cuentas.cuenta.adapter.in.web.dto.CrearCuentaRequest;
import com.aplicacion.cuentas.cuenta.adapter.in.web.dto.CuentaResponse;
import com.aplicacion.cuentas.cuenta.adapter.in.web.dto.MovimientoRequest;
import com.aplicacion.cuentas.cuenta.adapter.in.web.mapper.CuentaApiMapper;
import com.aplicacion.cuentas.cuenta.application.command.CrearCuentaCommand;
import com.aplicacion.cuentas.cuenta.application.command.MovimientoCommand;
import com.aplicacion.cuentas.cuenta.domain.model.Account;
import com.aplicacion.cuentas.cuenta.domain.port.in.ConsignarUseCase;
import com.aplicacion.cuentas.cuenta.domain.port.in.ConsultarCuentaUseCase;
import com.aplicacion.cuentas.cuenta.domain.port.in.CrearCuentaUseCase;
import com.aplicacion.cuentas.cuenta.domain.port.in.RetirarUseCase;

@RestController
@RequestMapping("/api/v1/cuentas")
@Validated
public class CuentaRestController {

	private final CrearCuentaUseCase crearCuenta;
	private final ConsultarCuentaUseCase consultarCuenta;
	private final ConsignarUseCase consignar;
	private final RetirarUseCase retirar;
	private final CuentaApiMapper mapper;

	public CuentaRestController(CrearCuentaUseCase crearCuenta, ConsultarCuentaUseCase consultarCuenta,
			ConsignarUseCase consignar, RetirarUseCase retirar, CuentaApiMapper mapper) {
		this.crearCuenta = crearCuenta;
		this.consultarCuenta = consultarCuenta;
		this.consignar = consignar;
		this.retirar = retirar;
		this.mapper = mapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CuentaResponse crear(@RequestBody @Valid CrearCuentaRequest request) {
		CrearCuentaCommand command = new CrearCuentaCommand(request.nombreTitular(), request.codigoMoneda());
		Account cuenta = crearCuenta.crear(command);
		return mapper.aRespuesta(cuenta);
	}

	@GetMapping
	public List<CuentaResponse> listar() {
		return consultarCuenta.listar().stream().map(mapper::aRespuesta).toList();
	}

	@GetMapping("/{id}")
	public CuentaResponse obtener(@PathVariable UUID id) {
		return mapper.aRespuesta(consultarCuenta.obtenerPorId(id));
	}

	@PostMapping("/{id}/consignaciones")
	public CuentaResponse consignar(@PathVariable UUID id, @RequestBody @Valid MovimientoRequest request) {
		MovimientoCommand command = new MovimientoCommand(id, request.monto());
		return mapper.aRespuesta(consignar.consignar(command));
	}

	@PostMapping("/{id}/retiros")
	public CuentaResponse retirar(@PathVariable UUID id, @RequestBody @Valid MovimientoRequest request) {
		MovimientoCommand command = new MovimientoCommand(id, request.monto());
		return mapper.aRespuesta(retirar.retirar(command));
	}
}
