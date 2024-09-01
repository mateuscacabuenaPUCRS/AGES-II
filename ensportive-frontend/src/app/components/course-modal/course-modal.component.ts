import {
  AfterViewInit,
  Component,
  EventEmitter,
  inject,
  Output,
  TemplateRef,
  ViewChild,
} from '@angular/core';

import { Course } from '../../interfaces/course.interface';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
  FormControl,
} from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Input } from '@angular/core';
import { IconsModule } from '../../icons/icons.module';
import { LevelEnum } from '../../enums/levels';
import { SportEnum } from '../../enums/sports';
import { WeekDaysEnum } from '../../enums/week-days';
import { PlanTypeEnum } from '../../enums/plan-types';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';
import { DateClickArg } from '@fullcalendar/interaction';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import {
  trigger,
  state,
  style,
  transition,
  animate,
} from '@angular/animations';
import { TeacherService } from '../../services/teacher/teacher.service';
import { map, Observable, startWith, switchMap } from 'rxjs';
import { Teacher } from '../../interfaces/teacher.interface';
import { Student } from '../../interfaces/student.interface';
import { StudentService } from '../../services/student/student.service';
import { NgSelectModule } from '@ng-select/ng-select';

@Component({
  selector: 'app-course-modal',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    IconsModule,
    ConfirmModalComponent,
    CommonModule,
    NgSelectModule,
    NgFor,
    NgIf,
    IconsModule,
  ],
  animations: [
    trigger('enterTrigger', [
      state(
        'fadeIn',
        style({
          opacity: '1',
        })
      ),
      transition('void => *', [style({ opacity: '0' }), animate('500ms')]),
    ]),
  ],
  templateUrl: './course-modal.component.html',
  styleUrls: ['./course-modal.component.scss'],
  providers: [TeacherService],
})
export class CourseModalComponent implements AfterViewInit {
  @ViewChild('content') content: TemplateRef<unknown> | undefined;

  levels = LevelEnum;
  sports = SportEnum.filter(sport => sport.key !== 'ALL');
  weekDays = WeekDaysEnum;
  planTypes = PlanTypeEnum;

  teachers$: Observable<Teacher[]> = this.teacherService.getTeachers();
  students$: Observable<Student[]> = this.studentService.getStudents();

  form: FormGroup = new FormGroup({});
  isEdit: boolean = false;

  filteredTeachers$!: Observable<Teacher[]>;
  filterTeacher = new FormControl('', { nonNullable: true });

  filteredStudents: Student[] = [];
  filterStudent = new FormControl('', { nonNullable: true });

  additionalStudents: Student[] = [];
  addStudentButtonText = 'Adicionar Aluno';
  showStudentsToAdd = false;

  @Input() course: Course = {
    id: null,
    sport: '',
    level: '',
    planType: '',
    studentsSize: 0,
    weekDay: null,
    hour: '00:00',
    teacher: {
      id: null,
      name: '',
      cellPhone: '',
      sport: '',
      email: '',
    },
    teacherId: '',
    studentsIds: [],
    court: 0,
    uniqueLesson: false,
    uniqueDate: null,
  };

  private modalService = inject(NgbModal);

  constructor(
    private formBuilder: FormBuilder,
    private teacherService: TeacherService,
    private studentService: StudentService
  ) {
    this.form = this.formBuilder.group({
      level: ['', Validators.required],
      uniqueLesson: '',
      uniqueDate: '',
      weekDay: '',
      sport: ['', Validators.required],
      studentsSize: ['', [Validators.required, Validators.min(0)]],
      hour: ['', Validators.required],
      planType: ['', Validators.required],
      teacherId: ['', Validators.required],
      studentsIds: [],
      court: ['', Validators.required],
      selectedStudent: [],
    });
  }

  ngAfterViewInit() {
    this.getAllTeachers();
    this.getAllStudents();

    if (this.form !== undefined) {
      this.form?.get('uniqueLesson')?.valueChanges.subscribe(value => {
        if (value) {
          this.form?.get('uniqueDate')?.setValidators(Validators.required);
          this.form?.get('weekDay')?.clearValidators();
        } else {
          this.form?.get('weekDay')?.setValidators(Validators.required);
          this.form?.get('uniqueDate')?.clearValidators();
        }
        this.form?.get('uniqueDate')?.updateValueAndValidity();
        this.form?.get('weekDay')?.updateValueAndValidity();
      });
    }

    this.setFormData();
    this.isEdit = this.course.id ? true : false;
  }

  clickOpen(arg: DateClickArg) {
    this.form?.patchValue({
      uniqueDate: arg.date.toISOString().slice(0, 10),
      hour: arg.date.toTimeString().slice(0, 5),
      weekDay: WeekDaysEnum[arg.date.getDay()].key,
    });
    this.modalService.open(this.content, {
      ariaLabelledBy: 'app-course-modal-title',
    });
  }

  setFormData() {
    if (this.course?.id) {
      console.log(this.course);
      this.form?.patchValue({
        level: this.course.level,
        uniqueLesson: this.course.uniqueLesson,
        uniqueDate: this.course.uniqueDate,
        weekDay: this.course.weekDay,
        sport: this.course.sport,
        studentsSize: this.course.studentsSize,
        hour: this.course.hour,
        planType: this.course.planType,
        teacherId: this.course.teacher.id,
        studentsIds: this.course.studentsIds,
        court: this.course.court,
      });

      if (this.course.studentsIds?.length > 0) {
        for (const studentId of this.course.studentsIds) {
          this.students$.subscribe(students => {
            const student = students.find(student => student.id === studentId);
            if (student) {
              this.additionalStudents.push(student);
            }
          });
        }
      }
    }
  }

  open(content: TemplateRef<unknown>) {
    if (this.isEdit) {
      this.course = { ...this.course };
    } else {
      this.resetModal();
      this.isEdit = false;
    }
    this.modalService.open(content, {
      ariaLabelledBy: 'app-course-modal-title',
    });
  }

  resetModal() {
    this.form?.reset({
      level: '',
      uniqueLesson: false,
      uniqueDate: '',
      weekDay: null,
      sport: '',
      studentsSize: '',
      hour: '',
      planType: '',
      teacherId: '',
      studentsIds: [],
      court: '',
    });
  }

  getAllTeachers() {
    this.teachers$ = this.teacherService.getTeachers();

    this.filteredTeachers$ = this.filterTeacher.valueChanges.pipe(
      startWith(''),
      switchMap(text => this.searchTeacher(text))
    );
  }

  searchTeacher(text: string): Observable<Teacher[]> {
    const term = text.toLowerCase();
    return this.teachers$.pipe(
      map(teachers =>
        teachers.filter(teacher => {
          return teacher.name.toLowerCase().includes(term);
        })
      )
    );
  }

  addStudent() {
    const selectedStudentId = this.form.get('selectedStudent')?.value;
    const selectedStudent = this.students$.pipe(
      map(students =>
        students.find(student => student.id === selectedStudentId)
      )
    );

    selectedStudent.subscribe(student => {
      if (student && !this.additionalStudents.some(s => s.id === student.id)) {
        this.additionalStudents.push(student);
        const studentsIds = this.form.get('studentsIds')?.value || [];
        studentsIds.push(student.id);
        this.form.patchValue({ studentsIds });
        this.form.patchValue({ selectedStudent: null });
      }
    });
  }

  removeStudent(student: Student) {
    this.additionalStudents = this.additionalStudents.filter(
      s => s.id !== student.id
    );
    const studentsIds = this.form.get('studentsIds')?.value || [];
    const index = studentsIds.indexOf(student.id);
    if (index > -1) {
      studentsIds.splice(index, 1);
      this.form.patchValue({ studentsIds });
    }
  }

  getAllStudents() {
    this.students$ = this.studentService.getStudents();
    this.students$ = this.students$.pipe(
      map(students =>
        students.filter(student => {
          return !this.additionalStudents.find(as => as.id === student.id);
        })
      )
    );
  }

  searchStudent(text: string): Observable<Student[]> {
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

  onStudentSelect(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const selectedValue = selectElement.value;

    if (selectedValue) {
      const studentsIds = this.form.get('studentsIds')?.value || [];
      if (!studentsIds.includes(selectedValue)) {
        studentsIds.push(selectedValue);
        this.form.patchValue({ studentsIds });
      }
    }
  }

  @Output() saveCourse = new EventEmitter<Course>();

  save() {
    if (this.form?.valid) {
      const formValue = { ...this.form.value };
      delete formValue.selectedStudent;

      const courseData: Course = {
        id: this.course.id,
        ...this.form?.value,
      };

      console.log(courseData);

      this.saveCourse.emit(courseData);
      this.modalService.dismissAll();
    } else {
      this.form?.markAllAsTouched();
    }
  }
}
