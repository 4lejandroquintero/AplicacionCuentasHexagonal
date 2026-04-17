import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';

import { Cuenta, EstadoCuenta } from '../../domain/cuenta.model';
import { CUENTA_REPOSITORY } from '../../ports/cuenta.repository.port';

@Component({
  selector: 'app-cuenta-detalle',
  imports: [RouterLink, ReactiveFormsModule, CurrencyPipe, DatePipe],
  templateUrl: './cuenta-detalle.component.html',
  styleUrl: './cuenta-detalle.component.scss'
})
export class CuentaDetalleComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly fb = inject(FormBuilder);
  private readonly repositorio = inject(CUENTA_REPOSITORY);

  protected readonly cuenta = signal<Cuenta | null>(null);
  protected readonly cargando = signal(true);
  protected readonly error = signal<string | null>(null);
  protected readonly operando = signal(false);
  protected readonly mensaje = signal<string | null>(null);

  protected id: string | null = null;

  protected readonly consignacion = this.fb.nonNullable.group({
    monto: [null as number | null, [Validators.required, Validators.min(0.01)]]
  });

  protected readonly retiro = this.fb.nonNullable.group({
    monto: [null as number | null, [Validators.required, Validators.min(0.01)]]
  });

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    if (!this.id) {
      this.error.set('Identificador invalido.');
      this.cargando.set(false);
      return;
    }
    this.cargar(this.id);
  }

  private cargar(id: string): void {
    this.cargando.set(true);
    this.repositorio.obtener(id).subscribe({
      next: (c) => {
        this.cuenta.set(c);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No fue posible obtener la cuenta.');
        this.cargando.set(false);
      }
    });
  }

  consignar(): void {
    if (!this.id || this.consignacion.invalid || this.operando()) {
      this.consignacion.markAllAsTouched();
      return;
    }
    const monto = this.consignacion.controls.monto.value;
    if (monto === null) {
      return;
    }
    this.ejecutar(this.repositorio.consignar(this.id, { monto }), this.consignacion);
  }

  retirar(): void {
    if (!this.id || this.retiro.invalid || this.operando()) {
      this.retiro.markAllAsTouched();
      return;
    }
    const monto = this.retiro.controls.monto.value;
    if (monto === null) {
      return;
    }
    this.ejecutar(this.repositorio.retirar(this.id, { monto }), this.retiro);
  }

  inactivarCuenta(): void {
    if (!this.id || this.operando() || this.cuenta()?.estado === 'CERRADA') {
      return;
    }
    const ok = confirm(
      'La cuenta quedara cerrada y no podras hacer movimientos. ¿Deseas continuar?'
    );
    if (!ok) {
      return;
    }
    this.operando.set(true);
    this.mensaje.set(null);
    this.repositorio.inactivar(this.id).subscribe({
      next: (c) => {
        this.cuenta.set(c);
        this.operando.set(false);
      },
      error: () => {
        this.operando.set(false);
        this.mensaje.set('No fue posible inactivar la cuenta.');
      }
    });
  }

  protected cuentaActiva(c: Cuenta): boolean {
    return c.estado === 'ACTIVA';
  }

  protected etiquetaEstado(estado: EstadoCuenta): string {
    return estado === 'ACTIVA' ? 'Activa' : 'Cerrada';
  }

  private ejecutar(
    accion: Observable<Cuenta>,
    formulario: typeof this.consignacion | typeof this.retiro
  ): void {
    this.operando.set(true);
    this.mensaje.set(null);
    accion.subscribe({
      next: (c) => {
        this.cuenta.set(c);
        this.operando.set(false);
        formulario.reset({ monto: null });
      },
      error: () => {
        this.operando.set(false);
        this.mensaje.set('No se pudo completar el movimiento. Revisa el monto o intenta de nuevo.');
      }
    });
  }
}
