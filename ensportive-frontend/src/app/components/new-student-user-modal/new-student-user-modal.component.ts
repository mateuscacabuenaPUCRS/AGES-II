import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  inject,
  Input,
  Output,
  TemplateRef,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';
import { StudentUserRegister } from '../../interfaces/student-user-register';

@Component({
  selector: 'app-new-student-user-modal',
  standalone: true,
  imports: [FormsModule, CommonModule, ConfirmModalComponent],
  templateUrl: './new-student-user-modal.component.html',
  styleUrl: './new-student-user-modal.component.scss',
  providers: [],
})
export class NewStudentUserModalComponent {
  username: string = '';
  password: string = '';
  @Input() studentId: string | null = '';
  @Input() studentUserRegister?: StudentUserRegister;

  private modalService = inject(NgbModal);

  open(content: TemplateRef<unknown>) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }

  @Output() saveStudentUserRegister = new EventEmitter<StudentUserRegister>();

  save() {
    const studentUserRegister: StudentUserRegister = {
      username: this.username,
      password: this.password,
      studentId: this.studentId,
    };

    this.saveStudentUserRegister.emit(studentUserRegister);
    this.modalService.dismissAll();
  }
}
