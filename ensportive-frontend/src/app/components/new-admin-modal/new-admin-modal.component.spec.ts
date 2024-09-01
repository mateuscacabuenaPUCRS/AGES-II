import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbModalModule, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NewAdminModalComponent } from './new-admin-modal.component';

describe('NewAdminModalComponent', () => {
  let component: NewAdminModalComponent;
  let fixture: ComponentFixture<NewAdminModalComponent>;
  let modalService: NgbModal;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NgbModalModule],
      declarations: [NewAdminModalComponent],
      providers: [NgbModal],
    }).compileComponents();

    fixture = TestBed.createComponent(NewAdminModalComponent);
    component = fixture.componentInstance;
    modalService = TestBed.inject(NgbModal);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should open modal when calling openNewAdminModal', () => {
    const openSpy = spyOn(modalService, 'open').and.callThrough();

    component.admin;

    expect(openSpy).toHaveBeenCalled();
  });
});
