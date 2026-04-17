export type EstadoCuenta = 'ACTIVA' | 'CERRADA';

export interface Cuenta {
  id: string;
  numeroCuenta: string;
  titular: string;
  saldo: number;
  moneda: string;
  estado: EstadoCuenta;
  creadoEn: string;
}

export interface CrearCuentaPayload {
  nombreTitular: string;
  codigoMoneda: string;
}

export interface MovimientoPayload {
  monto: number;
}
