import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DetailLessonModalComponent } from './detail-lesson-modal.component';

describe('DetailClassModalComponent', () => {
  let component: DetailLessonModalComponent;
  let fixture: ComponentFixture<DetailLessonModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailLessonModalComponent],
    }).compileComponents();
    fixture = TestBed.createComponent(DetailLessonModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
