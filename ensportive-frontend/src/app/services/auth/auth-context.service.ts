import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { AuthApiService } from './auth-api.service';
import {
  getRolesFromToken,
  getUserIdFromToken,
  getUsernameFromToken,
} from '../../utils/jwt-decoder';

export interface AuthUser {
  token: string | null;
  roles: string[];
  username: string | null;
  userId: number | null;
}

@Injectable({
  providedIn: 'root',
})
export class AuthContextService {
  private userSubject = new BehaviorSubject<AuthUser>({
    token: null,
    roles: [],
    username: null,
    userId: null,
  });

  constructor(private authApiService: AuthApiService) {
    this.loadUserFromLocalStorage();
  }

  private loadUserFromLocalStorage() {
    const token = this.authApiService.getUserToken();
    if (token) {
      this.userSubject.next({
        token,
        roles: getRolesFromToken(token),
        username: getUsernameFromToken(token),
        userId: getUserIdFromToken(token),
      });
    }
  }

  getUser(): Observable<AuthUser> {
    return this.userSubject.asObservable();
  }

  setUser(user: AuthUser) {
    this.userSubject.next(user);
  }

  isLoggedIn(): boolean {
    return this.userSubject.value.token !== null;
  }

  logOut() {
    this.authApiService.logOut();
    this.userSubject.next({
      token: null,
      roles: [],
      username: null,
      userId: null,
    });
  }
}
