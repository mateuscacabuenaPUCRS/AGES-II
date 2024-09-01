import { Component, OnInit } from '@angular/core';
import { Observable, map, startWith, switchMap } from 'rxjs';
import { SportEnum } from '../../enums/sports';
import { AsyncPipe, DecimalPipe } from '@angular/common';
import { IconsModule } from '../../icons/icons.module';
import { NgbHighlight } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { LessonService } from '../../services/lesson/lesson.service';
import { HttpClientModule } from '@angular/common/http';
import { Lesson } from '../../interfaces/lesson.interface';
import { DetailLessonModalComponent } from '../detail-lesson-modal/detail-lesson-modal.component';

@Component({
  selector: 'app-lessons',
  standalone: true,
  imports: [
    DecimalPipe,
    AsyncPipe,
    ReactiveFormsModule,
    NgbHighlight,
    IconsModule,
    HttpClientModule,
    DetailLessonModalComponent,
  ],
  templateUrl: './lessons.component.html',
  styleUrl: './lessons.component.scss',
  providers: [LessonService],
})
export class LessonsComponent implements OnInit {
  lessons$!: Observable<Lesson[]>;
  filteredLessons$!: Observable<Lesson[]>;
  filter = new FormControl('', { nonNullable: true });

  constructor(private lessonService: LessonService) {}

  ngOnInit() {
    this.getLessons();
  }

  getLessons() {
    this.lessons$ = this.lessonService.getLessons();

    this.filteredLessons$ = this.filter.valueChanges.pipe(
      startWith(''),
      switchMap(text => this.search(text))
    );
  }

  search(text: string): Observable<Lesson[]> {
    const term = text.toLowerCase();

    return this.lessons$.pipe(
      map(lessons =>
        lessons.filter(lesson => {
          return (
            lesson.teacher?.name.toLowerCase().includes(term) ||
            lesson.spot?.toString().toLowerCase().includes(term) ||
            lesson.course?.sport.includes(term) ||
            lesson.date.includes(term)
          );
        })
      )
    );
  }

  getSportName(sportKey?: string): string {
    const sport = SportEnum.find(s => s.key === sportKey);
    return sport ? sport.value : 'Unknown';
  }
  getDate(date: string): string {
    if (!date) return '';
    const [datePart] = date.split('T');
    const [year, month, day] = datePart.split('-');
    return `${day}/${month}/${year}`;
  }
}
