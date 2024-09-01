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
import { AbsenceRequest } from '../../interfaces/requests/absence-request.interface';

@Component({
  selector: 'app-request-modal',
  standalone: true,
  imports: [FormsModule, CommonModule, ConfirmModalComponent],
  templateUrl: './absence-request-modal.component.html',
  styleUrl: './absence-request-modal.component.scss',
  providers: [],
})
export class AbsenceRequestModalComponent {
  description: string = '';

  @Input() lessonId: string = '';
  @Input() isAbsent: boolean = false;
  @Input() isAdmin: boolean = false;

  private modalService = inject(NgbModal);

  open(content: TemplateRef<unknown>) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }

  @Output() saveRequest = new EventEmitter<AbsenceRequest>();

  save() {
    const requestData: AbsenceRequest = {
      description: this.description,
      lessonId: this.lessonId,
    };
    this.saveRequest.emit(requestData);
  }
}
