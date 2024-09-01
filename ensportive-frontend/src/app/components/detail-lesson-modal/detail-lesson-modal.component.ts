import {
  AfterViewInit,
  Component,
  EventEmitter,
  inject,
  Input,
  Output,
  TemplateRef,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LevelEnum } from '../../enums/levels';
import { Lesson } from '../../interfaces/lesson.interface';
import { LessonPlanModalComponent } from '../lesson-plan-modal/lesson-plan-modal.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { Student } from '../../interfaces/student.interface';
import { Observable } from 'rxjs';
import { StudentService } from '../../services/student/student.service';
import { NgFor, NgIf, CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { IconsModule } from '../../icons/icons.module';
import { LessonService } from '../../services/lesson/lesson.service';
import {
  AuthContextService,
  AuthUser,
} from '../../services/auth/auth-context.service';
import { RequestService } from '../../services/request/request.service';
import { AbsenceRequest } from '../../interfaces/requests/absence-request.interface';
import { Attendence } from '../../enums/attendence';
import { AbsenceRequestModalComponent } from '../absence-request-modal/absence-request-modal.component';

@Component({
  selector: 'app-detail-lesson',
  standalone: true,
  templateUrl: './detail-lesson-modal.component.html',
  styleUrl: './detail-lesson-modal.component.scss',
  imports: [
    FormsModule,
    NgFor,
    NgIf,
    NgSelectModule,
    CommonModule,
    HttpClientModule,
    IconsModule,
    LessonPlanModalComponent,
    AbsenceRequestModalComponent,
  ],
})
export class DetailLessonModalComponent implements AfterViewInit {
  @Output() refreshLesson = new EventEmitter<void>();
  @Input({ required: true }) lesson!: Lesson;
  title = '';
  lessonTime: Date | null = null;
  lessonDate: string = '';

  user: AuthUser | null = null;
  isAdmin: boolean = false;
  isAbsent: boolean = false;

  showStudentsToAdd = false;
  students: Student[] = [];
  additionalStudents: Student[] = [];
  description = '';
  hasFormChanged = false;
  selectedStudent: Student | null = null;
  addStudentButtonText = 'Adicionar Aluno de Presença Única';
  allStudents$!: Observable<Student[]>;

  constructor(
    private studentService: StudentService,
    private lessonService: LessonService,
    private requestService: RequestService,
    private authContextService: AuthContextService
  ) {}
  private modalService = inject(NgbModal);

  ngAfterViewInit(): void {
    this.authContextService.getUser().subscribe((user: AuthUser) => {
      this.user = user;
      this.isAdmin = user.roles.includes('ADMIN');
    });

    this.getAllStudents();
    this.setExtraStudents();
    this.description = this.lesson.description || '';
    this.lessonTime = new Date(this.lesson.date);
    const day = String(this.lessonTime.getDate()).padStart(2, '0');
    const month = String(this.lessonTime.getMonth() + 1).padStart(2, '0');
    const year = this.lessonTime.getFullYear();
    this.lessonDate = `${day}/${month}/${year}`;
    this.title =
      this.lessonTime.getHours() +
      'H - ' +
      this.getLevelName(this.lesson.course?.level);
  }

  open(content: TemplateRef<unknown>) {
    this.verifyIsAbsent();
    this.modalService.open(content, { ariaLabelledBy: 'Detalhar Aula' });
  }

  setExtraStudents() {
    this.additionalStudents = this.lesson.extraStudents.map(s => {
      return s.student;
    });
  }

  getLevelName(levelKey: string | undefined): string {
    const level = LevelEnum.find(s => s.key === levelKey);
    return level ? level.value : 'Unknown';
  }

  setFormChanged() {
    this.hasFormChanged =
      (this.lesson.description !== null &&
        this.description !== this.lesson.description) ||
      this.description !== '';
  }

  handleSaveAbsence(request: AbsenceRequest) {
    this.requestService.createAbsenceRequest(request).subscribe(() => {
      this.modalService.dismissAll();
      this.refreshLesson.emit();
    });
  }

  addStudent() {
    if (
      this.selectedStudent?.id &&
      !this.additionalStudents.find(s => s.id === this.selectedStudent?.id)
    ) {
      this.additionalStudents.push(this.selectedStudent);
      this.selectedStudent = null;
    }
  }
  removeStudent(student: Student) {
    this.lessonService.deleteExtraStudent(this.lesson, student).subscribe({
      next: () => {
        this.additionalStudents = this.additionalStudents.filter(
          s => s.id !== student.id
        );
      },
      error: (error: unknown) =>
        console.error('Error deleting extra student', error),
    });
  }
  getAllStudents() {
    this.allStudents$ = this.studentService.getStudents();
    this.allStudents$.subscribe({
      next: students => {
        this.students = students.filter(s => {
          return (
            !this.lesson.courseStudents.find(cs => cs.student.id === s.id) &&
            !this.lesson.extraStudents.find(es => es.student.id === s.id)
          );
        });
      },
      error: error => console.error('Error getting students', error),
    });
  }
  toggleShowStudentsToAdd() {
    if (this.showStudentsToAdd && this.additionalStudents.length > 0) {
      const studentsIds = this.additionalStudents
        .filter(s => s.id)
        .map(s => s.id || '');
      this.lessonService.addExtraStudent(this.lesson, studentsIds);
    }

    if (this.showStudentsToAdd) {
      this.addStudentButtonText = 'Adicionar Aluno de Presença Única';
    } else {
      this.addStudentButtonText = 'Concluído';
    }
    this.showStudentsToAdd = !this.showStudentsToAdd;
  }

  handleSave() {
    this.lesson.description = this.description;
    this.lessonService.updateLesson(this.lesson).subscribe({
      next: () => {
        this.lessonService.getLessons();
        this.hasFormChanged = false;
      },
      error: (error: unknown) => console.error('Error updating lesson', error),
    });
  }

  verifyIsAbsent() {
    if (this.user?.username) {
      this.studentService.getStudentByUsername(this.user.username).subscribe({
        next: student => {
          this.isAbsent = this.verifyIsStudentAbsent(student);
        },
        error: error => console.error('Error getting student', error),
      });
    }
  }

  verifyIsStudentAbsent(student: Student) {
    const studentCourse = this.lesson.courseStudents.find(
      s => s.student.id === student.id
    );
    if (studentCourse && studentCourse.attendance == Attendence.ABSENT) {
      return true;
    }

    const studentLesson = this.lesson.extraStudents.find(
      s => s.student.id === student.id
    );
    if (studentLesson && studentLesson.attendance == Attendence.ABSENT) {
      return true;
    }
    return false;
  }
}
