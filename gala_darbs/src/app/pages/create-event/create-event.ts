import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { Router } from '@angular/router';
import { EventService } from '../../core/services/event.service';

function pastDateValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value;

  if (!value) {
    return null;
  }

  const selectedDate = new Date(value);
  const today = new Date();

  today.setHours(0, 0, 0, 0);
  selectedDate.setHours(0, 0, 0, 0);

  return selectedDate < today ? { pastDate: true } : null;
}

@Component({
  selector: 'app-create-event',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-event.html',
  styleUrl: './create-event.scss',
})
export class CreateEvent {
  private fb = inject(FormBuilder);
  private eventService = inject(EventService);
  private router = inject(Router);

  error = '';

  today = new Date().toISOString().split('T')[0];

  form = this.fb.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    date: ['', [Validators.required, pastDateValidator]],
    time: ['', Validators.required],
    location: ['', Validators.required],
    maxParticipants: [1, [Validators.required, Validators.min(1)]],
  });

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const username = localStorage.getItem('username') || '';

    if (!username) {
      this.error = 'Nav atrasts lietotājs. Lūdzu, pieslēdzies vēlreiz.';
      return;
    }

    this.eventService
      .createEvent(
        this.form.value as {
          title: string;
          description: string;
          date: string;
          time: string;
          location: string;
          maxParticipants: number;
        },
        username,
      )
      .subscribe({
        next: () => this.router.navigate(['/events']),
        error: (err) => {
          this.error = err.error?.message || 'Pasākumu neizdevās izveidot';
        },
      });
  }
}
