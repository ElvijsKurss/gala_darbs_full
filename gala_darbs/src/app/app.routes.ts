import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'events', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login').then((m) => m.Login),
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register').then((m) => m.Register),
  },
  {
    path: 'events',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/events/events').then((m) => m.Events),
  },
  {
    path: 'create-event',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/create-event/create-event').then((m) => m.CreateEvent),
  },
  { path: '**', redirectTo: 'events' },
];
