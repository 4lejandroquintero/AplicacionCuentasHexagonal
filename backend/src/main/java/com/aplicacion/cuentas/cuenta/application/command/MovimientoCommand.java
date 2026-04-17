package com.aplicacion.cuentas.cuenta.application.command;

import java.math.BigDecimal;
import java.util.UUID;

public record MovimientoCommand(UUID cuentaId, BigDecimal monto) {
}
