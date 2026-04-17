import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { CrearCuentaPayload, Cuenta, MovimientoPayload } from '../domain/cuenta.model';
import { CuentaRepositoryPort } from '../ports/cuenta.repository.port';
import { API_BASE_URL } from './api-base-url';

@Injectable()
export class CuentaHttpRepository implements CuentaRepositoryPort {
  private readonly base = `${API_BASE_URL}/api/v1/cuentas`;

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Cuenta[]> {
    return this.http.get<Cuenta[]>(this.base);
  }

  obtener(id: string): Observable<Cuenta> {
    return this.http.get<Cuenta>(`${this.base}/${id}`);
  }

  crear(payload: CrearCuentaPayload): Observable<Cuenta> {
    return this.http.post<Cuenta>(this.base, payload);
  }

  consignar(id: string, payload: MovimientoPayload): Observable<Cuenta> {
    return this.http.post<Cuenta>(`${this.base}/${id}/consignaciones`, payload);
  }

  retirar(id: string, payload: MovimientoPayload): Observable<Cuenta> {
    return this.http.post<Cuenta>(`${this.base}/${id}/retiros`, payload);
  }

  inactivar(id: string): Observable<Cuenta> {
    return this.http.post<Cuenta>(`${this.base}/${id}/inactivacion`, {});
  }
}
