import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { Teacher } from '../../interfaces/teacher.interface';
import { HeaderService } from '../../utils/header.service';
import { EnvironmentService } from '../../enviroment/enviroment.service';

@Injectable({
  providedIn: 'root',
})
export class TeacherService {
  url = this.enviromentService.apiUrl + 'teachers';

  constructor(
    private httpClient: HttpClient,
    private headerService: HeaderService,
    private enviromentService: EnvironmentService
  ) {}

  getTeachers(): Observable<Teacher[]> {
    return this.httpClient
      .get<Teacher[]>(this.url, {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  getTeacherById(id: number): Observable<Teacher> {
    return this.httpClient
      .get<Teacher>(this.url + '/' + id, {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  saveTeacher(teacher: Teacher): Observable<number> {
    return this.httpClient
      .post<number>(this.url, JSON.stringify(teacher), {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  updateTeacher(teacher: Teacher): Observable<number> {
    return this.httpClient
      .put<number>(this.url + '/' + teacher.id, JSON.stringify(teacher), {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(1), catchError(this.handleError));
  }

  deleteTeacher(id: string) {
    return this.httpClient
      .delete<Teacher>(this.url + '/' + id, {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(1), catchError(this.handleError));
  }

  handleError(error: HttpErrorResponse) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      errorMessage = error.error.message;
    } else {
      errorMessage =
        `Código do erro: ${error.status}, ` + `mensagem: ${error.message}`;
    }
    console.log(errorMessage);
    return throwError(errorMessage);
  }
}
