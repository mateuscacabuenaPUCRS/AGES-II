import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, retry, throwError } from 'rxjs';
import { Lesson } from '../../interfaces/lesson.interface';
import { HeaderService } from './../../utils/header.service';
import { Student } from '../../interfaces/student.interface';
import { EnvironmentService } from '../../enviroment/enviroment.service';

@Injectable({
  providedIn: 'root',
})
export class LessonService {
  base_url = this.enviromentService.apiUrl + 'lessons';

  constructor(
    private httpClient: HttpClient,
    private headerService: HeaderService,
    private enviromentService: EnvironmentService
  ) {}

  getLessons(): Observable<Lesson[]> {
    return this.httpClient
      .get<
        Lesson[]
      >(this.base_url, { headers: this.headerService.getHeaders() })
      .pipe(retry(2), catchError(this.handleError));
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

  getLessonById(id: string): Observable<Lesson> {
    return this.httpClient
      .get<Lesson>(this.base_url + '/' + id, {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }
  saveLesson(lesson: Lesson): Observable<number> {
    return this.httpClient
      .post<number>(this.base_url, JSON.stringify(lesson), {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }
  updateLesson(lesson: Lesson): Observable<number> {
    return this.httpClient
      .put<number>(this.base_url + '/' + lesson.id, JSON.stringify(lesson), {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(1), catchError(this.handleError));
  }
  deleteLesson(id: string) {
    return this.httpClient
      .delete<Lesson>(this.base_url + '/' + id, {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(1), catchError(this.handleError));
  }
  addExtraStudent(lesson: Lesson, studentsIds: string[]) {
    const ret = this.httpClient
      .put<number>(
        this.base_url + '/' + lesson.id + '/students',
        JSON.stringify(studentsIds),
        {
          headers: this.headerService.getHeaders(),
        }
      )
      .pipe(retry(1), catchError(this.handleError));
    ret.subscribe(data => {
      console.log(data);
    });
    return ret;
  }
  deleteExtraStudent(lesson: Lesson, student: Student) {
    return this.httpClient
      .delete<Student>(
        this.base_url + '/' + lesson.id + '/students/' + student.id,
        {
          headers: this.headerService.getHeaders(),
        }
      )
      .pipe(retry(1), catchError(this.handleError));
  }
}
