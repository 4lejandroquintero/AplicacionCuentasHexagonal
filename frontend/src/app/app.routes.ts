import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./cuenta/pages/cuenta-lista/cuenta-lista.component').then((m) => m.CuentaListaComponent)
  },
  {
    path: 'cuentas/nueva',
    loadComponent: () =>
      import('./cuenta/pages/cuenta-nueva/cuenta-nueva.component').then((m) => m.CuentaNuevaComponent)
  },
  {
    path: 'cuentas/:id',
    loadComponent: () =>
      import('./cuenta/pages/cuenta-detalle/cuenta-detalle.component').then((m) => m.CuentaDetalleComponent)
  },
  { path: '**', redirectTo: '' }
];
