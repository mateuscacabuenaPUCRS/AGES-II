import { Teacher } from './teacher.interface';

export interface Course {
  id: string | null;
  sport: string;
  level: string;
  planType: string;
  studentsSize: number;
  weekDay: string | null;
  hour: string;
  uniqueLesson: boolean;
  uniqueDate: string | null;
  studentsIds: [];
  court: number;
  teacher: Teacher;
  teacherId: string;
}
