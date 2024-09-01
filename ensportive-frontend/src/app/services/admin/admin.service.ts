import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, retry, throwError } from 'rxjs';
import { Admin } from '../../interfaces/admin.interface';
import { HeaderService } from '../../utils/header.service';
import { StudentUserRegister } from '../../interfaces/student-user-register';
import { EnvironmentService } from '../../enviroment/enviroment.service';
@Injectable({
  providedIn: 'root',
})
export class AdminService {
  base_url = this.enviromentService.apiUrl + 'api/auth';

  constructor(
    private httpClient: HttpClient,
    private headerService: HeaderService,
    private enviromentService: EnvironmentService
  ) {}

  getAdmins(): Observable<Admin[]> {
    return this.httpClient
      .get<
        Admin[]
      >(`${this.base_url}/admins`, { headers: this.headerService.getHeaders() })
      .pipe(retry(2), catchError(this.handleError));
  }

  deleteAdmin(id: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.base_url}/${id}`);
  }

  saveAdmin(admin: Admin): Observable<Admin> {
    return this.httpClient.post<Admin>(
      `${this.base_url}/admin/register`,
      admin
    );
  }

  updateAdmin(admin: Admin): Observable<Admin> {
    return this.httpClient.put<Admin>(`${this.base_url}/${admin.id}`, admin);
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
  createStudentUser(studentUserData: StudentUserRegister): Observable<number> {
    return this.httpClient
      .post<number>(
        this.base_url + '/register',
        JSON.stringify(studentUserData),
        { headers: this.headerService.getHeaders() }
      )
      .pipe(retry(2), catchError(this.handleError));
  }
}
