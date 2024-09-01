import { AsyncPipe, DecimalPipe } from '@angular/common';
import { Component, OnInit, PipeTransform } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Observable, map, startWith, switchMap } from 'rxjs';
import { CourseModalComponent } from '../course-modal/course-modal.component';
import { LevelEnum } from '../../enums/levels';
import { SportEnum } from '../../enums/sports';
import { WeekDaysEnum } from '../../enums/week-days';
import { NgbHighlight } from '@ng-bootstrap/ng-bootstrap';
import { IconsModule } from '../../icons/icons.module';
import { AdminCoursesService } from '../../services/course.service';
import { Course } from '../../interfaces/course.interface';
import { HttpClientModule } from '@angular/common/http';
import { PlanTypeEnum } from '../../enums/plan-types';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';

function getLevel(level: string): string {
  const selectedLevel = LevelEnum.find(obj => obj.key == level);
  return selectedLevel?.value || '';
}

function getSport(sport: string): string {
  const selectedSport = SportEnum.find(obj => obj.key == sport);
  return selectedSport?.value || '';
}

function getWeekDay(weekDay: string | null): string | null {
  if (!weekDay) {
    return null;
  }
  const selectedWeekDay = WeekDaysEnum.find(obj => obj.key == weekDay);
  return selectedWeekDay?.value || '';
}

function getPlanType(planType: string): string {
  const selectedPlanType = PlanTypeEnum.find(obj => obj.key == planType);
  return selectedPlanType?.value || '';
}

@Component({
  selector: 'app-admin-courses',
  standalone: true,
  imports: [
    IconsModule,
    AsyncPipe,
    NgbHighlight,
    ReactiveFormsModule,
    DecimalPipe,
    CourseModalComponent,
    ConfirmModalComponent,
    HttpClientModule,
  ],
  templateUrl: './admin-courses.component.html',
  styleUrl: './admin-courses.component.scss',
  providers: [DecimalPipe, AdminCoursesService],
})
export class AdminCoursesComponent implements OnInit {
  courses$!: Observable<Course[]>;
  filteredCourses$!: Observable<Course[]>;
  filter = new FormControl('', { nonNullable: true });

  constructor(
    private pipe: DecimalPipe,
    private adminCoursesService: AdminCoursesService
  ) {}

  ngOnInit() {
    this.loadCourses();
  }

  loadCourses() {
    this.courses$ = this.adminCoursesService.findAllCourses();

    this.filteredCourses$ = this.filter.valueChanges.pipe(
      startWith(''),
      switchMap(text => this.search(text, this.pipe))
    );
  }

  getLevel(level: string) {
    return getLevel(level);
  }

  getSport(sport: string) {
    return getSport(sport);
  }

  getWeekDay(weekDay: string | null) {
    return getWeekDay(weekDay);
  }

  getPlanType(planType: string) {
    return getPlanType(planType);
  }

  search(text: string, pipe: PipeTransform): Observable<Course[]> {
    const term = text.toLowerCase();
    return this.courses$.pipe(
      map(courses =>
        courses.filter(course => {
          return (
            getSport(course.sport).toLowerCase().includes(term) ||
            getLevel(course.level).toLowerCase().includes(term) ||
            pipe.transform(course.studentsSize).includes(term) ||
            getWeekDay(course.weekDay)?.toLowerCase().includes(term) ||
            course.hour.includes(term) ||
            getPlanType(course.planType).toLowerCase().includes(term)
          );
        })
      )
    );
  }

  handleDelete(id: string | null) {
    if (id === null) {
      return;
    }
    this.adminCoursesService.deleteCourse(id).subscribe({
      next: () => {
        this.loadCourses();
      },
      error: error => console.error('Error deleting course:', error),
    });
  }

  handleSave(course: Course) {
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
}
