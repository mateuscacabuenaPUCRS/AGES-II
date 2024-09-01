import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AdminCourseRequest } from '../interfaces/admin-course-request';
import { Course } from '../interfaces/course.interface';
import { HeaderService } from '../utils/header.service';
import { EnvironmentService } from '../enviroment/enviroment.service';

@Injectable({
  providedIn: 'root',
})
export class AdminCoursesService {
  private baseUrl = this.enviromentService.apiUrl + 'courses';

  constructor(
    private http: HttpClient,
    private headerService: HeaderService,
    private enviromentService: EnvironmentService
  ) {}

  findAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(this.baseUrl, {
      headers: this.headerService.getHeaders(),
    });
  }

  getCourseById(id: string): Observable<Course> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<Course>(url, {
      headers: this.headerService.getHeaders(),
    });
  }

  createCourse(course: AdminCourseRequest): Observable<number> {
    return this.http.post<number>(this.baseUrl, course, {
      headers: this.headerService.getHeaders(),
    });
  }

  updateCourse(id: string, course: AdminCourseRequest): Observable<number> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.put<number>(url, course, {
      headers: this.headerService.getHeaders(),
    });
  }

  patchCourse(id: string, course: AdminCourseRequest): Observable<number> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.patch<number>(url, course, {
      headers: this.headerService.getHeaders(),
    });
  }

  deleteCourse(id: string): Observable<number> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete<number>(url, {
      headers: this.headerService.getHeaders(),
    });
  }
}
