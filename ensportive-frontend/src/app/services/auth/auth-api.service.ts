import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
  HttpResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { TokenResponse } from '../../interfaces/token-response';
import { EnvironmentService } from '../../enviroment/enviroment.service';

export class LoginDTO {
  username: string | undefined;
  password: string | undefined;
}

export let isPasswordCorrect = true;

@Injectable({
  providedIn: 'root',
})
export class AuthApiService {
  base_url = this.enviromentService.apiUrl;

  loginFields: LoginDTO;

  constructor(
    private httpClient: HttpClient,
    private router: Router,
    private enviromentService: EnvironmentService
  ) {
    this.loginFields = new LoginDTO();
  }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  onLogin(username: string, password: string) {
    console.log(this.base_url);
    this.loginFields.username = username;
    this.loginFields.password = password;
    this.httpClient
      .post<TokenResponse>(this.base_url + 'api/auth/login', this.loginFields, {
        headers: this.httpOptions.headers,
        observe: 'response',
      })
      .pipe(catchError(this.handleError))
      .subscribe((res: HttpResponse<TokenResponse>) => {
        if (res.status === 200 && res.body) {
          localStorage.setItem('userToken', res.body.access_token);
          localStorage.setItem('userRefreshToken', res.body.refresh_token);
          this.router.navigateByUrl('/calendario');
        } else {
          catchError(this.handleError);
        }
      });
  }

  isLoggedIn(): boolean {
    if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
      const token = this.getUserToken();
      if (token) {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  logOut() {
    localStorage.removeItem('userToken');
    localStorage.removeItem('userRefreshToken');
    this.router.navigateByUrl('/login');
  }

  getUserToken(): string {
    if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
      const token = localStorage.getItem('userToken');
      if (token) {
        return token;
      } else {
        return '';
      }
    } else {
      return '';
    }
  }

  handleError(error: HttpErrorResponse) {
    isPasswordCorrect = false;
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Erro ocorreu no lado do client
      errorMessage = error.error.message;
    } else {
      // Erro ocorreu no lado do servidor
      errorMessage =
        `Código do erro: ${error.status}, ` + `menssagem: ${error.message}`;
    }
    console.log(errorMessage);
    return throwError(errorMessage);
  }
}
