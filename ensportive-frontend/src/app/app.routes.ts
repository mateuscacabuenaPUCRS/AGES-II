import { Routes } from '@angular/router';
import { AdminCoursesComponent } from './components/admin-courses/admin-courses.component';
import { StudentsComponent } from './components/students/students.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { TeachersComponent } from './components/teachers/teachers.component';
import { LoginComponent } from './components/login/login.component';
import { LessonPlanComponent } from './components/lesson-plan/lesson-plan.component';
import { LessonsComponent } from './components/lessons/lessons.component';
import { adminAuthGuard } from './services/auth/guard/admin-auth.guard';
import { loginAuthGuard } from './services/auth/guard/login-auth.guard';
import { roleAuthGuard } from './services/auth/guard/role-auth.guard';
import { ExperimentalClassComponent } from './components/trial-class/trial-class.component';
import { AdminComponent } from './components/admin/admin.component';
import { RequestsComponent } from './components/requests/requests.component';

export const routes: Routes = [
  {
    path: 'alunos',
    data: { name: 'alunos' },
    component: StudentsComponent,
    canActivate: [adminAuthGuard],
  },
  {
    path: 'solicitacoes',
    data: { name: 'solicitacoes' },
    component: RequestsComponent,
    canActivate: [roleAuthGuard],
  },
  {
    path: 'calendario',
    data: { name: 'calendario' },
    component: CalendarComponent,
    canActivate: [roleAuthGuard],
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'turmas',
    data: { name: 'turmas' },
    component: AdminCoursesComponent,
    canActivate: [adminAuthGuard],
  },
  {
    path: 'professores',
    data: { name: 'professores' },
    component: TeachersComponent,
    canActivate: [adminAuthGuard],
  },
  {
    path: 'planos-de-ensino',
    data: { name: 'planos-de-ensino' },
    component: LessonPlanComponent,
    canActivate: [adminAuthGuard],
  },
  {
    path: 'aulas',
    data: { name: 'aulas' },
    component: LessonsComponent,
    canActivate: [adminAuthGuard],
  },
  {
    path: 'login',
    data: { name: 'login' },
    component: LoginComponent,
    canActivate: [loginAuthGuard],
  },
  {
    path: 'aula-experimental',
    data: { name: 'aula-experimental' },
    component: ExperimentalClassComponent,
    canActivate: [loginAuthGuard],
  },
  {
    path: 'admin',
    data: { name: 'admin' },
    component: AdminComponent,
    canActivate: [adminAuthGuard],
  },
];
