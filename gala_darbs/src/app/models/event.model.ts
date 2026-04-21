export interface EventModel {
  id: number;
  title: string;
  description: string;
  date: string;
  time: string;
  location: string;
  maxParticipants: number;
  cancelled: boolean;
  registeredCount: number;
  full: boolean;
  joined: boolean;
  createdByUsername: string | null;
}
