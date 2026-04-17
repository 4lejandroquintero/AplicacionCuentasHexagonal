import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { CUENTA_REPOSITORY } from '../../ports/cuenta.repository.port';

@Component({
  selector: 'app-cuenta-nueva',
  imports: [ReactiveFormsModule],
  templateUrl: './cuenta-nueva.component.html',
  styleUrl: './cuenta-nueva.component.scss'
})
export class CuentaNuevaComponent {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly repositorio = inject(CUENTA_REPOSITORY);

  protected readonly enviando = signal(false);
  protected readonly error = signal<string | null>(null);

  protected readonly formulario = this.fb.nonNullable.group({
    nombreTitular: ['', [Validators.required, Validators.maxLength(200)]],
    codigoMoneda: ['COP', [Validators.required, Validators.minLength(3), Validators.maxLength(3)]]
  });

  crear(): void {
    if (this.formulario.invalid || this.enviando()) {
      this.formulario.markAllAsTouched();
      return;
    }
    this.enviando.set(true);
    this.error.set(null);
    const v = this.formulario.getRawValue();
    this.repositorio.crear({ nombreTitular: v.nombreTitular, codigoMoneda: v.codigoMoneda.toUpperCase() }).subscribe({
      next: (c) => {
        this.enviando.set(false);
        void this.router.navigate(['/cuentas', c.id]);
      },
      error: () => {
        this.enviando.set(false);
        this.error.set('No fue posible crear la cuenta.');
      }
    });
  }
}
