import {
  Component,
  EventEmitter,
  inject,
  OnInit,
  Output,
  TemplateRef,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Input } from '@angular/core';
import { IconsModule } from '../../icons/icons.module';
import { LessonPlan } from '../../interfaces/lesson-plan.interface';
import { LessonPlanService } from '../../services/lesson-plan.service';
import { CommonModule } from '@angular/common';
import { LevelEnum } from '../../enums/levels';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';

@Component({
  selector: 'app-lesson-plan-modal',
  standalone: true,
  imports: [FormsModule, IconsModule, CommonModule, ConfirmModalComponent],
  templateUrl: './lesson-plan-modal.component.html',
  styleUrl: './lesson-plan-modal.component.scss',
  providers: [LessonPlanService],
})
export class LessonPlanModalComponent implements OnInit {
  levelEnum = LevelEnum;
  isEdit: boolean = false;
  idLessonPlan: string = '';
  modality: string = '';
  warmUp: string = '';
  technique1: string = '';
  technique2: string = '';
  tactic: string = '';
  serve: string = '';
  social: string = '';
  level: string = 'BEGINNER_1';
  @Input() lessonPlan: LessonPlan = {
    id: null,
    modality: '',
    warmUp: '',
    technique1: '',
    technique2: '',
    tactic: '',
    serve: '',
    social: '',
    level: 'BEGINNER_1',
  };

  @Input() viewOnly: boolean = false;
  private modalService = inject(NgbModal);

  ngOnInit() {
    if (this.lessonPlan) {
      this.idLessonPlan = this.lessonPlan.id || '';
      this.modality = this.lessonPlan.modality;
      this.warmUp = this.lessonPlan.warmUp;
      this.technique1 = this.lessonPlan.technique1;
      this.technique2 = this.lessonPlan.technique2;
      this.tactic = this.lessonPlan.tactic;
      this.serve = this.lessonPlan.serve;
      this.social = this.lessonPlan.social;
      this.level = this.lessonPlan.level;
      this.isEdit = this.lessonPlan.id ? true : false;
    }
  }

  open(content: TemplateRef<unknown>) {
    if (this.isEdit) {
      this.lessonPlan = { ...this.lessonPlan };
    } else {
      this.resetModal();
      this.isEdit = false;
    }
    this.modalService.open(content, {
      ariaLabelledBy: 'lesson-plan-modal',
    });
  }

  resetModal() {
    if (this.isEdit) {
      this.lessonPlan = { ...this.lessonPlan };
    } else {
      this.idLessonPlan = '';
      this.isEdit = false;
      this.modality = '';
      this.warmUp = '';
      this.technique1 = '';
      this.technique2 = '';
      this.tactic = '';
      this.serve = '';
      this.social = '';
      this.level = 'BEGINNER_1';
    }
  }

  @Output() saveLessonPlan = new EventEmitter<LessonPlan>();

  save() {
    const lessonPlanData = {
      id: this.idLessonPlan,
      modality: this.modality,
      warmUp: this.warmUp,
      technique1: this.technique1,
      technique2: this.technique2,
      tactic: this.tactic,
      serve: this.serve,
      social: this.social,
      level: this.level,
    };
    this.saveLessonPlan.emit(lessonPlanData);
    this.modalService.dismissAll();
  }
}
