import { Component, OnInit } from '@angular/core';
import { AsyncPipe, DecimalPipe } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { IconsModule } from '../../icons/icons.module';
import { Teacher } from '../../interfaces/teacher.interface';
import { TeacherModalComponent } from '../teacher-modal/teacher-modal.component';

import { Observable, map, startWith, switchMap } from 'rxjs';
import { NgbHighlight } from '@ng-bootstrap/ng-bootstrap';
import { TeacherService } from '../../services/teacher/teacher.service';
import { HttpClientModule } from '@angular/common/http';
import { SportEnum } from '../../enums/sports';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';

function getSport(sport: string): string {
  const selectedSport = SportEnum.find(obj => obj.key == sport);
  return selectedSport?.value || '';
}

@Component({
  selector: 'app-teachers',
  standalone: true,
  imports: [
    DecimalPipe,
    AsyncPipe,
    ReactiveFormsModule,
    NgbHighlight,
    IconsModule,
    TeacherModalComponent,
    ConfirmModalComponent,
    HttpClientModule,
  ],
  templateUrl: './teachers.component.html',
  styleUrl: './teachers.component.scss',
  providers: [TeacherService],
})
export class TeachersComponent implements OnInit {
  teachers$!: Observable<Teacher[]>;
  filteredTeachers$!: Observable<Teacher[]>;
  filter = new FormControl('', { nonNullable: true });

  constructor(private teacherService: TeacherService) {}

  ngOnInit() {
    this.getAllTeachers();
  }

  getAllTeachers() {
    this.teachers$ = this.teacherService.getTeachers();

    this.filteredTeachers$ = this.filter.valueChanges.pipe(
      startWith(''),
      switchMap(text => this.search(text))
    );
  }
  getSportName(sportKey: string): string {
    const sport = SportEnum.find(s => s.key === sportKey);
    return sport ? sport.value : 'Unknown';
  }

  getSport(sport: string) {
    return getSport(sport);
  }

  search(text: string): Observable<Teacher[]> {
    const term = text.toLowerCase();
    return this.teachers$.pipe(
      map(teachers =>
        teachers.filter(teacher => {
          return (
            getSport(teacher.sport).toLowerCase().includes(term) ||
            teacher.name.toLowerCase().includes(term) ||
            teacher.cellPhone.toLowerCase().includes(term) ||
            teacher.email.toLowerCase().includes(term)
          );
        })
      )
    );
  }

  handleDelete(id: string | null) {
    if (id === null) {
      return;
    }
    this.teacherService.deleteTeacher(id).subscribe({
      next: () => {
        this.getAllTeachers();
      },
      error: error => console.error('Error deleting teacher:', error),
    });
  }

  handleSave(teacher: Teacher) {
    if (teacher.id) {
      this.teacherService.updateTeacher(teacher).subscribe({
        next: () => this.getAllTeachers(),
        error: error => console.error('Error updating teacher', error),
      });
    } else {
      this.teacherService.saveTeacher(teacher).subscribe({
        next: () => this.getAllTeachers(),
        error: error => console.error('Error saving teacher', error),
      });
    }
  }
}
