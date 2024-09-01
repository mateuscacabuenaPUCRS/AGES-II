import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, retry, throwError } from 'rxjs';
import { HeaderService } from '../../utils/header.service';
import { Request } from '../../interfaces/requests/request.interface';
import { AbsenceRequest } from '../../interfaces/requests/absence-request.interface';
import { RegisterRequest } from '../../interfaces/requests/register-request.interface';
import { ApiResponse } from '../../interfaces/api-response.interface';
import { EnvironmentService } from '../../enviroment/enviroment.service';

@Injectable({
  providedIn: 'root',
})
export class RequestService {
  base_url = this.enviromentService.apiUrl + 'requests';

  constructor(
    private httpClient: HttpClient,
    private headerService: HeaderService,
    private enviromentService: EnvironmentService
  ) {}

  getRequests(): Observable<Request[]> {
    return this.httpClient
      .get<Request[]>(this.base_url, {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  getRequestByUserId(userId: number): Observable<Request[]> {
    return this.httpClient
      .get<Request[]>(`${this.base_url}/student/${userId}`, {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  createAbsenceRequest(request: AbsenceRequest): Observable<number> {
    return this.httpClient
      .post<number>(`${this.base_url}/absence`, JSON.stringify(request), {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  createRegisterRequest(request: RegisterRequest): Observable<number> {
    return this.httpClient
      .post<number>(`${this.base_url}/register`, JSON.stringify(request), {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  denyRequest(id: string): Observable<ApiResponse> {
    return this.httpClient
      .put<ApiResponse>(
        `${this.base_url}/deny/${id}`,
        {},
        {
          headers: this.headerService.getHeaders(),
        }
      )
      .pipe(retry(1), catchError(this.handleError));
  }

  approveRequest(id: string): Observable<ApiResponse> {
    return this.httpClient
      .put<ApiResponse>(
        `${this.base_url}/approve/${id}`,
        {},
        {
          headers: this.headerService.getHeaders(),
        }
      )
      .pipe(retry(1), catchError(this.handleError));
  }

  deleteRequest(id: string) {
    return this.httpClient
      .delete(`${this.base_url}${id}`, {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(1), catchError(this.handleError));
  }

  handleError(error: HttpErrorResponse) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Erro ocorreu no lado do client
      errorMessage = error.error.message;
    } else {
      // Erro ocorreu no lado do servidor
      errorMessage =
        `Código do erro: ${error.status}, ` + `mensagem: ${error.message}`;
    }
    console.log(errorMessage);
    return throwError(errorMessage);
  }
}
