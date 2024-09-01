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
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { Admin } from '../../interfaces/admin.interface';
import { IconsModule } from '../../icons/icons.module';
import { CommonModule } from '@angular/common';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';

@Component({
  selector: 'app-admin-modal',
  templateUrl: './new-admin-modal.component.html',
  styleUrls: ['./new-admin-modal.component.scss'],
  imports: [
    ReactiveFormsModule,
    IconsModule,
    CommonModule,
    ConfirmModalComponent,
  ],
  standalone: true,
})
export class NewAdminModalComponent implements OnInit {
  form: FormGroup = new FormGroup({});
  isEdit: boolean = false;

  @Input() admin: Admin = {
    id: 0,
    username: '',
    password: '',
  };

  @Output() saveAdmin = new EventEmitter<Admin>();

  private modalService = inject(NgbModal);
  private fb = inject(FormBuilder);

  ngOnInit() {
    this.initializeForm();
    this.setInitialFormValues();
  }

  initializeForm() {
    this.form = this.fb.group({
      username: [this.admin.username, Validators.required],
      password: [this.admin.password, Validators.required],
    });
  }

  setInitialFormValues() {
    if (this.admin.id) {
      this.isEdit = true;
      this.form.patchValue({
        ...this.admin,
      });
    } else {
      this.resetForm();
    }
  }

  resetForm() {
    this.isEdit = false;
    this.form.reset({
      username: '',
      password: '',
    });
  }

  open(content: TemplateRef<unknown>) {
    this.setInitialFormValues();
    this.modalService.open(content, {
      ariaLabelledBy: 'cadastrar-admin-modal',
    });
  }

  save() {
    if (this.form.invalid) {
      return;
    }

    const formValues = this.form.value;

    const admin: Admin = {
      ...this.admin,
      ...formValues,
    };
    console.log(admin);

    this.saveAdmin.emit(admin);
    this.modalService.dismissAll();
  }
}
