import {
  HttpClient,
  HttpErrorResponse,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { Student } from '../../interfaces/student.interface';
import { HeaderService } from '../../utils/header.service';
import { EnvironmentService } from '../../enviroment/enviroment.service';

@Injectable({
  providedIn: 'root',
})
export class StudentService {
  url = this.enviromentService.apiUrl + 'students';

  constructor(
    private httpClient: HttpClient,
    private headerService: HeaderService,
    private enviromentService: EnvironmentService
  ) {}

  getStudents(): Observable<Student[]> {
    return this.httpClient
      .get<Student[]>(this.url, {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  getStudentById(id: number): Observable<Student> {
    return this.httpClient
      .get<Student>(this.url + '/' + id, {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  getStudentByUsername(username: string): Observable<Student> {
    const params = new HttpParams().set('username', username);
    return this.httpClient
      .get<Student>(this.url + '/user', {
        params,
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  saveStudent(student: Student): Observable<number> {
    return this.httpClient
      .post<number>(this.url, JSON.stringify(student), {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  updateStudent(student: Student): Observable<number> {
    return this.httpClient
      .put<number>(this.url + '/' + student.id, JSON.stringify(student), {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(1), catchError(this.handleError));
  }

  deleteStudent(id: string) {
    return this.httpClient
      .delete<Student>(this.url + '/' + id, {
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
