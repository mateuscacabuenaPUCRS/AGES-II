import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CourseModalComponent } from './course-modal.component';
import { FormsModule } from '@angular/forms'; // Importar FormsModule para trabalhar com ngModel

describe('SeuComponenteComponent', () => {
  let component: CourseModalComponent;
  let fixture: ComponentFixture<CourseModalComponent>;
  // let modalService: NgbModal;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CourseModalComponent],
      providers: [NgbModal],
      imports: [FormsModule], // Importar FormsModule para trabalhar com ngModel
    }).compileComponents();

    fixture = TestBed.createComponent(CourseModalComponent);
    component = fixture.componentInstance;
    // modalService = TestBed.inject(NgbModal);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render dropdowns for level, dayOfWeek, and sport fields', () => {
    const compiled = fixture.nativeElement;
    const levelDropdown = compiled.querySelector('#nivel-dropdown');
    const dayOfWeekDropdown = compiled.querySelector('#diaSemana-dropdown');
    const sportDropdown = compiled.querySelector('#esporte-dropdown');

    expect(levelDropdown).toBeTruthy();
    expect(dayOfWeekDropdown).toBeTruthy();
    expect(sportDropdown).toBeTruthy();
  });

  it('should render input fields for numberOfStudents, time, and court fields', () => {
    const compiled = fixture.nativeElement;
    const numberOfStudentsInput = compiled.querySelector('#numeroAlunos-input');
    const timeInput = compiled.querySelector('#horario-input');
    const courtInput = compiled.querySelector('#quadra-input');

    expect(numberOfStudentsInput).toBeTruthy();
    expect(timeInput).toBeTruthy();
    expect(courtInput).toBeTruthy();
  });
});
