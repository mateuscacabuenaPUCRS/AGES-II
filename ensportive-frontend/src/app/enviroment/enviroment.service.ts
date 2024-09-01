import { Injectable } from '@angular/core';
import { environment } from './enviroment';
export interface Environment {
  production: boolean;
  baseUrl: string;
}

@Injectable({
  providedIn: 'root',
})
export class EnvironmentService {
  constructor() {}

  get apiUrl(): string {
    return environment.baseUrl;
  }
}
