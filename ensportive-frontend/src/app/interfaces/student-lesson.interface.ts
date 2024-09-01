import { Attendence } from '../enums/attendence';
import { Student } from './student.interface';

export interface StudentLesson {
  id?: string;
  student: Student;
  attendance: Attendence;
  extraStudent: boolean;
}
