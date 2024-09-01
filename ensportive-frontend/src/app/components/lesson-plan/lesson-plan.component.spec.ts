import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LessonPlanComponent } from './lesson-plan.component';

describe('CoursePlanComponent', () => {
  let component: LessonPlanComponent;
  let fixture: ComponentFixture<LessonPlanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LessonPlanComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(LessonPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
