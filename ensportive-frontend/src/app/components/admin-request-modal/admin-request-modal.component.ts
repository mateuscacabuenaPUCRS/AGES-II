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
import { Request } from '../../interfaces/requests/request.interface';

@Component({
  selector: 'app-admin-request-modal',
  standalone: true,
  imports: [FormsModule, CommonModule, ConfirmModalComponent],
  templateUrl: './admin-request-modal.component.html',
  styleUrl: './admin-request-modal.component.scss',
  providers: [],
})
export class AdminRequestModalComponent {
  action: string = '';
  @Input({ required: true }) request!: Request;

  private modalService = inject(NgbModal);

  open(content: TemplateRef<unknown>) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }

  @Output() saveRequest = new EventEmitter<{
    requestId: string;
    action: string;
  }>();

  setAction(action: string) {
    this.action = action;
  }

  save() {
    if (!this.request?.id) {
      return;
    }
    const requestId = this.request.id;
    this.saveRequest.emit({ requestId: requestId, action: this.action });
    this.modalService.dismissAll();
  }
}
