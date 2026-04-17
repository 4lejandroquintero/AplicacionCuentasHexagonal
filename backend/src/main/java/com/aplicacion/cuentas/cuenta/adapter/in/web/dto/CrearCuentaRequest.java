package com.aplicacion.cuentas.cuenta.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearCuentaRequest(
		@NotBlank @Size(max = 200) String nombreTitular,
		@NotBlank @Size(min = 3, max = 3) String codigoMoneda) {
}
