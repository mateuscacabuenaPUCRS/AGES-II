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
  AbstractControl,
  ValidatorFn,
} from '@angular/forms';
import { Student } from '../../interfaces/student.interface';
import { IconsModule } from '../../icons/icons.module';
import { CommonModule } from '@angular/common';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';
import { PlanTypeEnum } from '../../enums/plan-types';
import { Frequency } from '../../enums/frequency';
import { dateToYYYYMMDD } from '../../utils/date-utils';

@Component({
  selector: 'app-student-modal',
  templateUrl: './new-student-modal.html',
  styleUrls: ['./new-student-modal.scss'],
  imports: [
    ReactiveFormsModule,
    IconsModule,
    CommonModule,
    ConfirmModalComponent,
  ],
  standalone: true,
})
export class NewStudentModalComponent implements OnInit {
  PlanTypeEnum = PlanTypeEnum;
  translateFrequency = [
    { key: 'ONE', value: 'Uma' },
    { key: 'TWO', value: 'Duas' },
    { key: 'THREE', value: 'Três' },
  ];
  translateSport = [
    { key: 'TENNIS', value: 'TÊNIS' },
    { key: 'BEACH_TENNIS', value: 'BEACH TENNIS' },
    { key: 'ALL', value: 'TODOS' },
  ];

  form: FormGroup = new FormGroup({});
  isEdit: boolean = false;
  planTypes = PlanTypeEnum;

  @Input() student: Student = {
    id: null,
    name: '',
    username: '',
    cellPhone: '',
    level: 'BEGINNER_1',
    email: '',
    license: {
      id: 0,
      startDate: dateToYYYYMMDD(new Date()),
      endDate: null,
      planType: PlanTypeEnum[0].key,
      frequency: Frequency.ONE,
      sport: 'TENNIS',
      coursesPerWeek: 0,
      active: true,
    },
  };

  @Output() saveStudent = new EventEmitter<Student>();

  private modalService = inject(NgbModal);
  private fb = inject(FormBuilder);

  ngOnInit() {
    this.initializeForm();
    this.setInitialFormValues();
    this.updateLicenseValidators();
  }

  initializeForm() {
    this.form = this.fb.group(
      {
        name: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        cellPhone: ['', Validators.required],
        level: ['', Validators.required],
        active: [true],
        startDate: [dateToYYYYMMDD(new Date())],
        endDate: [null],
        planType: [PlanTypeEnum[0].key],
        frequency: ['ONE'],
        coursesPerWeek: [0],
        sport: ['', Validators.required],
      },
      { validators: this.dateComparisonValidator }
    );
  }

  setInitialFormValues() {
    if (this.student.id) {
      this.isEdit = true;
      this.form.patchValue({
        ...this.student,
        ...this.student.license,
        startDate: this.student.license?.startDate,
        endDate: this.student.license?.endDate,
        sport: this.student.license?.sport,
      });
    } else {
      this.resetForm();
    }
  }

  resetForm() {
    this.isEdit = false;
    this.form.reset({
      name: '',
      email: '',
      cellPhone: '',
      level: '',
      tennisChecked: false,
      beachTennisChecked: false,
      active: true,
      startDate: dateToYYYYMMDD(new Date()),
      endDate: null,
      planType: PlanTypeEnum[0].key,
      frequency: Frequency.ONE,
      coursesPerWeek: 0,
    });
    this.updateLicenseValidators();
  }

  open(content: TemplateRef<unknown>) {
    this.setInitialFormValues();
    this.modalService.open(content, {
      ariaLabelledBy: 'cadastrar-aluno-modal',
    });
  }

  updateLicenseValidators() {
    const licenseActive = this.form.get('active')?.value;

    if (licenseActive) {
      this.form.get('startDate')?.setValidators(Validators.required);
      this.form
        .get('endDate')
        ?.setValidators([Validators.required, this.dateAfter('startDate')]);
      this.form.get('planType')?.setValidators(Validators.required);
      this.form.get('frequency')?.setValidators(Validators.required);
      this.form
        .get('coursesPerWeek')
        ?.setValidators([Validators.required, Validators.min(1)]);
    } else {
      this.form.get('startDate')?.clearValidators();
      this.form.get('endDate')?.clearValidators();
      this.form.get('planType')?.clearValidators();
      this.form.get('frequency')?.clearValidators();
      this.form.get('coursesPerWeek')?.clearValidators();
    }

    this.form.get('startDate')?.updateValueAndValidity();
    this.form.get('endDate')?.updateValueAndValidity();
    this.form.get('planType')?.updateValueAndValidity();
    this.form.get('frequency')?.updateValueAndValidity();
    this.form.get('coursesPerWeek')?.updateValueAndValidity();
  }

  dateAfter(startDateField: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: unknown } | null => {
      const formGroup = control.parent;
      if (!formGroup) return null;

      const startDate = formGroup.get(startDateField)?.value;
      const endDate = control.value;

      if (startDate && endDate && endDate <= startDate) {
        return { dateAfter: true };
      }
      return null;
    };
  }

  dateComparisonValidator(formGroup: FormGroup) {
    if (!formGroup.get('active')?.value) return null;

    const startDate = formGroup.get('startDate')?.value;
    const endDate = formGroup.get('endDate')?.value;

    return startDate && endDate && endDate <= startDate
      ? { dateAfter: true }
      : null;
  }

  save() {
    if (this.form.invalid) {
      return;
    }

    const formValues = this.form.value;

    const student: Student = {
      ...this.student,
      ...formValues,
      license: {
        ...this.student.license,
        startDate: formValues.startDate,
        endDate: formValues.endDate,
        planType: formValues.planType,
        frequency: formValues.frequency,
        coursesPerWeek: formValues.coursesPerWeek,
        active: formValues.active,
        sport: formValues.sport,
      },
    };

    this.saveStudent.emit(student);
    this.modalService.dismissAll();
  }
}
