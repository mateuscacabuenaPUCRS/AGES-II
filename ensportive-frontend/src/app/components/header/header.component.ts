import { Component, OnInit } from '@angular/core';
import { NavItem } from '../../interfaces/nav-item';
import { RouterLink } from '@angular/router';
import { IconsModule } from '../../icons/icons.module';
import { AuthApiService } from '../../services/auth/auth-api.service';
import {
  getRolesFromToken,
  getUsernameFromToken,
} from '../../utils/jwt-decoder';
import { StudentService } from '../../services/student/student.service';
import { Student } from '../../interfaces/student.interface';

const USER_TYPE_TO_NAV_ITEMS_MAP = {
  admin: [
    {
      route: '/calendario',
      text: 'Calendário',
    },
    {
      route: '/alunos',
      text: 'Alunos',
    },
    {
      route: '/professores',
      text: 'Professores',
    },
    {
      route: '/turmas',
      text: 'Turmas',
    },
    {
      route: '/planos-de-ensino',
      text: 'Planos de Ensino',
    },
    {
      route: '/aulas',
      text: 'Aulas',
    },
    {
      route: '/admin',
      text: 'Administradores',
    },
  ],
  student: [
    {
      route: '/calendario',
      text: 'Calendário',
    },
  ],
};

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, IconsModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  providers: [AuthApiService, StudentService],
})
export class HeaderComponent implements OnInit {
  navItems: NavItem[] = [];
  currentUser: string = '';
  abbreviation: string = '';

  constructor(
    private authApiService: AuthApiService,
    private studentService: StudentService
  ) {}

  ngOnInit(): void {
    this.buildHeaderItems();
  }

  isLoggedIn(): boolean {
    return this.authApiService.isLoggedIn();
  }

  getUserData() {}

  onLogOut() {
    this.authApiService.logOut();
  }

  getCurrentPath() {
    if (typeof window !== 'undefined') {
      return window.location.pathname;
    }
    return '';
  }

  buildHeaderItems() {
    const token = this.authApiService.getUserToken();
    const roles = getRolesFromToken(token);
    const username = getUsernameFromToken(token);
    if (roles.includes('ADMIN')) {
      this.navItems = USER_TYPE_TO_NAV_ITEMS_MAP['admin'];
      this.currentUser = 'Admin';
    } else {
      const student = this.studentService.getStudentByUsername(username);
      student.subscribe((data: Student) => {
        this.currentUser = data?.name;
        this.abbreviation = data?.name.charAt(0).toUpperCase();
      });
      this.navItems = USER_TYPE_TO_NAV_ITEMS_MAP['student'];
    }
  }
}
