import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AbsenceRequestModalComponent } from './absence-request-modal.component';

describe('AbsenceRequestModalComponent', () => {
  let component: AbsenceRequestModalComponent;
  let fixture: ComponentFixture<AbsenceRequestModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AbsenceRequestModalComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AbsenceRequestModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
