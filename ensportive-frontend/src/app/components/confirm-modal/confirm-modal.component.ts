import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  TemplateRef,
  inject,
} from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IconsModule } from '../../icons/icons.module';

@Component({
  selector: 'app-confirm-modal',
  standalone: true,
  imports: [IconsModule],
  templateUrl: './confirm-modal.component.html',
  styleUrl: './confirm-modal.component.scss',
})
export class ConfirmModalComponent implements OnInit {
  @Input() isEdit: boolean = false;
  @Input() item: string = '';

  ngOnInit(): void {
    this.isEdit;
    this.item;
  }

  private modalService = inject(NgbModal);

  open(content: TemplateRef<unknown>) {
    this.modalService.open(content, {
      ariaLabelledBy: 'app-confirm-modal-title',
    });
  }

  @Output() deleteItem = new EventEmitter<void>();
  @Output() saveItem = new EventEmitter<void>();

  delete() {
    this.deleteItem.emit();
    this.modalService.dismissAll();
  }

  save() {
    this.saveItem.emit();
    this.modalService.dismissAll();
  }
}
