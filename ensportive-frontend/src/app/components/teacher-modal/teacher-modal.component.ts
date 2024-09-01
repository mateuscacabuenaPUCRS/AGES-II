import {
  Component,
  EventEmitter,
  inject,
  OnInit,
  Output,
  TemplateRef,
} from '@angular/core';
import { Teacher } from '../../interfaces/teacher.interface';
import { FormsModule } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Input } from '@angular/core';
import { IconsModule } from '../../icons/icons.module';
import { TeacherService } from '../../services/teacher/teacher.service';
import { CommonModule } from '@angular/common';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';

@Component({
  selector: 'app-teacher-modal',
  standalone: true,
  imports: [FormsModule, IconsModule, CommonModule, ConfirmModalComponent],
  templateUrl: './teacher-modal.component.html',
  styleUrl: './teacher-modal.component.scss',
  providers: [TeacherService],
})
export class TeacherModalComponent implements OnInit {
  isEdit: boolean = false;
  idTeacher: string | null = null;
  name: string = '';
  cellPhone: string = '';
  sport: string = '';
  email: string = '';
  beachTennisChecked: boolean = false;
  tennisChecked: boolean = false;
  @Input() teacher: Teacher = {
    id: null,
    name: '',
    cellPhone: '',
    sport: '',
    email: '',
  };
  private modalService = inject(NgbModal);

  ngOnInit() {
    this.idTeacher = this.teacher.id;
    this.name = this.teacher.name;
    this.cellPhone = this.teacher.cellPhone;
    this.email = this.teacher.email;
    this.sport = this.teacher.sport;
    this.getTeacherSports();
    this.isEdit = this.teacher.id ? true : false;
  }
  getTeacherSports() {
    if (this.teacher.sport == 'ALL') {
      this.tennisChecked = true;
      this.beachTennisChecked = true;
    } else if (this.teacher.sport == 'BEACH_TENNIS') {
      this.beachTennisChecked = true;
    } else if (this.teacher.sport == 'TENNIS') {
      this.tennisChecked = true;
    }
  }

  open(content: TemplateRef<unknown>) {
    if (this.isEdit) {
      this.teacher = { ...this.teacher };
    } else {
      this.resetModal();
      this.isEdit = false;
    }
    this.modalService.open(content, {
      ariaLabelledBy: 'teacher-modal',
    });
  }

  resetModal() {
    if (this.isEdit) {
      this.teacher = { ...this.teacher };
    } else {
      this.idTeacher = null;
      this.isEdit = false;
      this.name = '';
      this.cellPhone = '';
      this.email = '';
      this.sport = '';
      this.beachTennisChecked = false;
      this.tennisChecked = false;
    }
  }

  @Output() saveTeacher = new EventEmitter<Teacher>();

  setSports() {
    if (this.tennisChecked && this.beachTennisChecked) {
      this.sport = 'ALL';
    } else if (this.beachTennisChecked && !this.tennisChecked) {
      this.sport = 'BEACH_TENNIS';
    } else if (!this.beachTennisChecked && this.tennisChecked) {
      this.sport = 'TENNIS';
    }
  }

  save() {
    this.setSports();
    const teacherData = {
      id: this.idTeacher,
      name: this.name,
      cellPhone: this.cellPhone,
      sport: this.sport,
      email: this.email,
    };
    this.saveTeacher.emit(teacherData);
    this.modalService.dismissAll();
  }
}
