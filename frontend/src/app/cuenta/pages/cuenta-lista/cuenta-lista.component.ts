import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Cuenta } from '../../domain/cuenta.model';
import { CUENTA_REPOSITORY } from '../../ports/cuenta.repository.port';

@Component({
  selector: 'app-cuenta-lista',
  imports: [RouterLink, CurrencyPipe, DatePipe],
  templateUrl: './cuenta-lista.component.html',
  styleUrl: './cuenta-lista.component.scss'
})
export class CuentaListaComponent implements OnInit {
  private readonly repositorio = inject(CUENTA_REPOSITORY);

  protected readonly cuentas = signal<Cuenta[]>([]);
  protected readonly cargando = signal(true);
  protected readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.repositorio.listar().subscribe({
      next: (lista) => {
        this.cuentas.set(lista);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No fue posible cargar las cuentas. Verifica que el API este en ejecucion.');
        this.cargando.set(false);
      }
    });
  }
}
