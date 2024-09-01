import { HeaderService } from './../utils/header.service';
import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { LessonPlan } from '../interfaces/lesson-plan.interface';
import { EnvironmentService } from '../enviroment/enviroment.service';

@Injectable({
  providedIn: 'root',
})
export class LessonPlanService {
  url = this.enviromentService.apiUrl + 'lessonPlan';

  constructor(
    private httpClient: HttpClient,
    private headerService: HeaderService,
    private enviromentService: EnvironmentService
  ) {}

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  getLessonPlans(): Observable<LessonPlan[]> {
    return this.httpClient
      .get<LessonPlan[]>(this.url, {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  getLessonPlanById(id: number): Observable<LessonPlan> {
    return this.httpClient
      .get<LessonPlan>(this.url + '/' + id, {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  saveLessonPlan(lessonPlan: LessonPlan): Observable<number> {
    return this.httpClient
      .post<number>(this.url, JSON.stringify(lessonPlan), {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(2), catchError(this.handleError));
  }

  updateLessonPlan(lessonPlan: LessonPlan): Observable<number> {
    return this.httpClient
      .put<number>(this.url + '/' + lessonPlan.id, JSON.stringify(lessonPlan), {
        headers: this.headerService.getHeaders(),
      })
      .pipe(retry(1), catchError(this.handleError));
  }

  deleteLessonPlan(id: string) {
    return this.httpClient
      .delete<LessonPlan>(this.url + '/' + id, {
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
