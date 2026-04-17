import { provideHttpClient, withFetch } from '@angular/common/http';
import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { CuentaHttpRepository } from './cuenta/adapters/cuenta-http.repository';
import { CUENTA_REPOSITORY } from './cuenta/ports/cuenta.repository.port';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(withFetch()),
    { provide: CUENTA_REPOSITORY, useClass: CuentaHttpRepository }
  ]
};
