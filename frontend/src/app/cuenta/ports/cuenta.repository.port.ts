import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';

import { CrearCuentaPayload, Cuenta, MovimientoPayload } from '../domain/cuenta.model';

export interface CuentaRepositoryPort {
  listar(): Observable<Cuenta[]>;
  obtener(id: string): Observable<Cuenta>;
  crear(payload: CrearCuentaPayload): Observable<Cuenta>;
  consignar(id: string, payload: MovimientoPayload): Observable<Cuenta>;
  retirar(id: string, payload: MovimientoPayload): Observable<Cuenta>;
  inactivar(id: string): Observable<Cuenta>;
}

export const CUENTA_REPOSITORY = new InjectionToken<CuentaRepositoryPort>('CuentaRepositoryPort');
