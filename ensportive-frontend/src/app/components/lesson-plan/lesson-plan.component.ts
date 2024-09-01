import { Component, OnInit } from '@angular/core';
import { AsyncPipe, DecimalPipe } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { IconsModule } from '../../icons/icons.module';
import { Observable } from 'rxjs';
import { map, startWith, switchMap } from 'rxjs/operators';
import { NgbHighlight } from '@ng-bootstrap/ng-bootstrap';
import { LessonPlan } from '../../interfaces/lesson-plan.interface';
import { LessonPlanModalComponent } from '../lesson-plan-modal/lesson-plan-modal.component';
import { LessonPlanService } from '../../services/lesson-plan.service';
import { HttpClientModule } from '@angular/common/http';
import { LevelEnum } from '../../enums/levels';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';

@Component({
  selector: 'app-lesson-plan',
  standalone: true,
  imports: [
    DecimalPipe,
    AsyncPipe,
    ReactiveFormsModule,
    NgbHighlight,
    IconsModule,
    LessonPlanModalComponent,
    ConfirmModalComponent,
    HttpClientModule,
  ],
  templateUrl: './lesson-plan.component.html',
  styleUrl: './lesson-plan.component.scss',
  providers: [LessonPlanService],
})
export class LessonPlanComponent implements OnInit {
  lessonPlans$!: Observable<LessonPlan[]>;
  filteredLessonPlans$!: Observable<LessonPlan[]>;
  filter = new FormControl('', { nonNullable: true });

  constructor(private lessonPlanService: LessonPlanService) {}

  ngOnInit() {
    this.getAllLessonPlans();
  }

  getAllLessonPlans() {
    this.lessonPlans$ = this.lessonPlanService.getLessonPlans();

    this.filteredLessonPlans$ = this.filter.valueChanges.pipe(
      startWith(''),
      switchMap(text => this.search(text))
    );
  }

  search(text: string): Observable<LessonPlan[]> {
    const term = text.toLowerCase();
    return this.lessonPlans$.pipe(
      map(lessonPlans =>
        lessonPlans.filter(lessonPlan => {
          return lessonPlan.modality.toLowerCase().includes(term);
        })
      )
    );
  }

  translateLevels(key: string) {
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
    this.lessonPlanService.deleteLessonPlan(id).subscribe({
      next: () => {
        this.getAllLessonPlans();
      },
      error: error => console.error('Error deleting lesson plan:', error),
    });
  }

  handleSave(lessonPlan: LessonPlan) {
    if (lessonPlan.id) {
      this.lessonPlanService.updateLessonPlan(lessonPlan).subscribe({
        next: () => this.getAllLessonPlans(),
        error: error => console.error('Error updating lesson plan', error),
      });
    } else {
      this.lessonPlanService.saveLessonPlan(lessonPlan).subscribe({
        next: () => this.getAllLessonPlans(),
        error: error => console.error('Error saving lesson plan', error),
      });
    }
  }
}
