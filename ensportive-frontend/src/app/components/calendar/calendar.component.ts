import { CommonModule } from '@angular/common';
import { CalendarOptions, EventApi, EventInput } from '@fullcalendar/core';
import { Component, AfterViewInit, ViewChild } from '@angular/core';

import interactionPlugin, { DateClickArg } from '@fullcalendar/interaction';
import dayGridPlugin from '@fullcalendar/daygrid';
import listPlugin from '@fullcalendar/list';
import timeGridPlugin from '@fullcalendar/timegrid';
import { RouterOutlet } from '@angular/router';
import { FullCalendarModule } from '@fullcalendar/angular';
import ptBrLocale from '@fullcalendar/core/locales/pt-br';
import { Observable } from 'rxjs';
import { Lesson } from '../../interfaces/lesson.interface';
import { AuthApiService } from '../../services/auth/auth-api.service';
import {
  AuthContextService,
  AuthUser,
} from '../../services/auth/auth-context.service';
import { LessonService } from '../../services/lesson/lesson.service';
import { StudentService } from '../../services/student/student.service';
import { DetailLessonModalComponent } from '../detail-lesson-modal/detail-lesson-modal.component';
import { CourseModalComponent } from '../course-modal/course-modal.component';
import { Course } from '../../interfaces/course.interface';
import { AdminCoursesService } from '../../services/course.service';

let lessonsEvents: EventInput[] = [];

function lessonToEvent(lesson: Lesson): EventInput {
  return {
    id: lesson.id || '',
    title: lesson.course?.sport || '',
    start: lesson.date || '',
    end: lesson.date || '',
  };
}

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    FullCalendarModule,
    DetailLessonModalComponent,
    CourseModalComponent,
  ],
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.scss',
  providers: [
    LessonService,
    AuthApiService,
    StudentService,
    AdminCoursesService,
  ],
})
export class CalendarComponent implements AfterViewInit {
  user: AuthUser | null = null;
  isAdmin: boolean = false;

  constructor(
    private lessonsService: LessonService,
    private authContextService: AuthContextService,
    private adminCoursesService: AdminCoursesService
  ) {}

  @ViewChild('courseModal') courseModal: CourseModalComponent | undefined;

  lessons$!: Observable<Lesson[]>;
  lessons: Lesson[] = [];

  ngAfterViewInit() {
    this.authContextService.getUser().subscribe((user: AuthUser) => {
      this.user = user;
      this.isAdmin = user.roles.includes('ADMIN');
      this.loadCourses();
    });
  }

  getLesson(id: string) {
    let lesson: Lesson = {
      description: '',
      spot: '',
      date: '',
      court: '',
      courseStudents: [],
      extraStudents: [],
    };
    this.lessons.forEach(l => {
      if (l.id && l.id == id) {
        lesson = l;
      }
    });
    return lesson;
  }

  title = 'ensportive-frontend';
  calendarVisible = true;

  calendarOptions: CalendarOptions = {
    locales: [ptBrLocale],
    locale: 'pt-br',
    plugins: [interactionPlugin, dayGridPlugin, timeGridPlugin, listPlugin],
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek',
    },
    initialView: this.isMobileView() ? 'listWeek' : 'dayGridMonth',
    weekends: true,
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEventRows: 3,
    dateClick: this.handleDateClick.bind(this),
    events: [],
    eventMaxStack: 2,
  };

  currentEvents: EventApi[] = [];

  isMobileView() {
    return window.matchMedia('(max-width: 767px)').matches;
  }

  handleCalendarToggle() {
    this.calendarVisible = !this.calendarVisible;
  }

  handleWeekendsToggle() {
    const { calendarOptions } = this;
    calendarOptions.weekends = !calendarOptions.weekends;
  }

  handleDateClick(arg: DateClickArg) {
    if (this.user?.roles.includes('ADMIN')) {
      this.courseModal?.clickOpen(arg);
    }
  }

  onSave(course: Course) {
    if (course.id) {
      this.adminCoursesService.updateCourse(course.id, course).subscribe({
        next: () => this.loadCourses(),
        error: error => console.error('Error updating course', error),
      });
    } else {
      this.adminCoursesService.createCourse(course).subscribe({
        next: () => this.loadCourses(),
        error: error => console.error('Error saving course', error),
      });
    }
  }

  loadCourses() {
    lessonsEvents = [];
    this.lessons$ = this.lessonsService.getLessons();
    this.lessons$.subscribe(fetchedLessons => {
      fetchedLessons.forEach(l => {
        lessonsEvents.push(lessonToEvent(l));
      });
      this.calendarOptions.events = [...lessonsEvents];
      this.lessons = fetchedLessons;
    });
  }
}
