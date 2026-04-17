package com.aplicacion.cuentas.cuenta.adapter.in.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aplicacion.cuentas.cuenta.application.exception.CuentaNoEncontradaException;
import com.aplicacion.cuentas.cuenta.domain.exception.DomainRuleException;
import com.aplicacion.cuentas.cuenta.domain.exception.InsufficientFundsException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(CuentaNoEncontradaException.class)
	public ResponseEntity<Map<String, Object>> noEncontrada(CuentaNoEncontradaException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo("recurso_no_encontrado", ex.getMessage()));
	}

	@ExceptionHandler({ DomainRuleException.class })
	public ResponseEntity<Map<String, Object>> reglaDominio(DomainRuleException ex) {
		if (ex instanceof InsufficientFundsException ins) {
			Map<String, Object> body = cuerpo("fondos_insuficientes", ex.getMessage());
			body.put("cuentaId", ins.cuentaId().toString());
			body.put("solicitado", ins.solicitado());
			body.put("disponible", ins.disponible());
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cuerpo("regla_negocio", ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException ex) {
		Map<String, String> detalles = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			detalles.put(error.getField(), error.getDefaultMessage());
		}
		Map<String, Object> body = cuerpo("validacion", "Datos de entrada invalidos");
		body.put("detalles", detalles);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> ilegal(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo("peticion_invalida", ex.getMessage()));
	}

	private Map<String, Object> cuerpo(String codigo, String mensaje) {
		Map<String, Object> map = new HashMap<>();
		map.put("codigo", codigo);
		map.put("mensaje", mensaje);
		return map;
	}
}
