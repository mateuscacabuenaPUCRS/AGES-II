import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbModalModule, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NewStudentModalComponent } from './new-student-modal';

describe('NewStudentModalComponent', () => {
  let component: NewStudentModalComponent;
  let fixture: ComponentFixture<NewStudentModalComponent>;
  let modalService: NgbModal;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NgbModalModule],
      declarations: [NewStudentModalComponent],
      providers: [NgbModal],
    }).compileComponents();

    fixture = TestBed.createComponent(NewStudentModalComponent);
    component = fixture.componentInstance;
    modalService = TestBed.inject(NgbModal);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should open modal when calling openNewStudentModal', () => {
    const openSpy = spyOn(modalService, 'open').and.callThrough();

    component.openNewStudentModal();

    expect(openSpy).toHaveBeenCalled();
  });
});
