import { Component, OnInit } from '@angular/core';
import { AsyncPipe, DecimalPipe } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { IconsModule } from '../../icons/icons.module';
import { Observable } from 'rxjs';
import { map, startWith, switchMap } from 'rxjs/operators';
import { NgbHighlight } from '@ng-bootstrap/ng-bootstrap';
import { Student } from '../../interfaces/student.interface';
import { StudentService } from '../../services/student/student.service';
import { NewStudentModalComponent } from './../new-student-modal/new-student-modal.component';
import { HttpClientModule } from '@angular/common/http';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';
import { LevelEnum } from '../../enums/levels';
import { StudentUserRegister } from '../../interfaces/student-user-register';
import { NewStudentUserModalComponent } from '../new-student-user-modal/new-student-user-modal.component';
import { AdminService } from '../../services/admin/admin.service';

@Component({
  selector: 'app-students',
  standalone: true,
  imports: [
    DecimalPipe,
    AsyncPipe,
    ReactiveFormsModule,
    NgbHighlight,
    IconsModule,
    NewStudentModalComponent,
    NewStudentUserModalComponent,
    ConfirmModalComponent,
    HttpClientModule,
  ],
  templateUrl: './students.component.html',
  styleUrl: './students.component.scss',
  providers: [DecimalPipe, StudentService],
})
export class StudentsComponent implements OnInit {
  students$!: Observable<Student[]>;
  filteredStudents$!: Observable<Student[]>;
  filter = new FormControl('', { nonNullable: true });
  studentUserRegister: StudentUserRegister = {
    username: '',
    password: '',
    studentId: '',
  };

  constructor(
    private studentService: StudentService,
    private adminService: AdminService
  ) {}

  ngOnInit() {
    this.getAllStudents();
  }

  getAllStudents() {
    this.students$ = this.studentService.getStudents();

    this.filteredStudents$ = this.filter.valueChanges.pipe(
      startWith(''),
      switchMap(text => this.search(text))
    );
  }

  search(text: string): Observable<Student[]> {
    const term = text.toLowerCase();
    return this.students$.pipe(
      map(students =>
        students.filter(student => {
          return (
            student.name.toLowerCase().includes(term) ||
            student.email.toLowerCase().includes(term) ||
            student.cellPhone.toLowerCase().includes(term) ||
            student.level?.toLocaleLowerCase().includes(term)
          );
        })
      )
    );
  }

  translateLevels(key: string | undefined) {
    for (const level of LevelEnum) {
      if (level.key === key) {
        return level.value;
      }
    }
    return '';
  }

  handleDelete(id: string | null) {
    if (id === null) {
      return;
    }
    this.studentService.deleteStudent(id).subscribe({
      next: () => {
        this.getAllStudents();
      },
      error: error => console.error('Error deleting student:', error),
    });
  }

  handleCreateUser(studentUserRegister: StudentUserRegister) {
    this.adminService.createStudentUser(studentUserRegister).subscribe({
      next: () => this.getAllStudents(),
      error: error => console.error('Error creating student user:', error),
    });
  }

  handleSave(student: Student) {
    if (student.id) {
      this.studentService.updateStudent(student).subscribe({
        next: () => this.getAllStudents(),
        error: error => console.error('Erro ao atualizar aluno', error),
      });
    } else {
      this.studentService.saveStudent(student).subscribe({
        next: () => this.getAllStudents(),
        error: error => console.error('Erro ao criar aluno', error),
      });
    }
  }
}
