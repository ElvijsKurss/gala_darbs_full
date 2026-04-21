import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { Router } from '@angular/router';
import { AuthResponse } from '../../models/auth-response';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private api = 'http://localhost:8080/api/auth';

  register(data: { username: string; password: string }) {
    return this.http.post<AuthResponse>(`${this.api}/register`, data).pipe(
      tap((res) => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('username', res.username);
      }),
    );
  }

  login(data: { username: string; password: string }) {
    return this.http.post<AuthResponse>(`${this.api}/login`, data).pipe(
      tap((res) => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('username', res.username);
      }),
    );
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getUsername(): string {
    return localStorage.getItem('username') || '';
  }
}
