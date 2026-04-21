import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventService } from '../../core/services/event.service';
import { EventModel } from '../../models/event.model';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-events',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './events.html',
  styleUrl: './events.scss',
})
export class Events implements OnInit {
  private eventService = inject(EventService);
  authService = inject(AuthService);

  events: EventModel[] = [];
  error = '';

  ngOnInit(): void {
    this.loadEvents();
  }

  private getErrorMessage(err: any, fallback: string): string {
    if (!err) return fallback;
    if (typeof err.error === 'string') return err.error;
    if (err.error?.message) return err.error.message;
    if (err.message) return err.message;
    return fallback;
  }

  loadEvents() {
    const username = this.authService.getUsername();

    this.eventService.getEvents(username).subscribe({
      next: (res) => {
        this.events = res.filter((event) => !event.cancelled);
        this.error = '';
      },
      error: (err) => {
        console.log('LOAD EVENTS ERROR:', err);
        this.error = this.getErrorMessage(err, 'Neizdevās ielādēt pasākumus');
      },
    });
  }

  join(id: number) {
    const username = this.authService.getUsername();

    if (!username) {
      alert('Nav atrasts lietotājs. Lūdzu, pieslēdzies vēlreiz.');
      return;
    }

    this.eventService.joinEvent(id, username).subscribe({
      next: () => this.loadEvents(),
      error: (err) => {
        console.log('JOIN ERROR:', err);
        alert(this.getErrorMessage(err, 'Neizdevās pieteikties'));
      },
    });
  }

  leave(id: number) {
    const username = this.authService.getUsername();

    if (!username) {
      alert('Nav atrasts lietotājs. Lūdzu, pieslēdzies vēlreiz.');
      return;
    }

    this.eventService.leaveEvent(id, username).subscribe({
      next: () => this.loadEvents(),
      error: (err) => {
        console.log('LEAVE ERROR:', err);
        alert(this.getErrorMessage(err, 'Neizdevās atcelt dalību'));
      },
    });
  }

  cancelEvent(id: number) {
    const username = this.authService.getUsername();

    if (!username) {
      alert('Nav atrasts lietotājs. Lūdzu, pieslēdzies vēlreiz.');
      return;
    }

    this.eventService.cancelEvent(id, username).subscribe({
      next: () => this.loadEvents(),
      error: (err) => {
        console.log('CANCEL EVENT ERROR:', err);
        alert(this.getErrorMessage(err, 'Neizdevās atcelt pasākumu'));
      },
    });
  }
}
