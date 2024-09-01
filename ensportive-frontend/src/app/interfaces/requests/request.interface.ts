import { RequestStatus } from '../../enums/request-status';
import { RequestType } from '../../enums/request-type';
import { Lesson } from '../lesson.interface';
import { Student } from '../student.interface';

export interface Request {
  id?: string;
  description: string;
  requestStatus: RequestStatus;
  requestType: RequestType;
  lessonId: string;
  studentId: string;
  lesson?: Lesson;
  student?: Student;
  userEmail: string;
  userName: string;
  userPhone: string;
  creationDate: string;
}
