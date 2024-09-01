import { Course } from './course.interface';
import { LessonPlan } from './lesson-plan.interface';
import { StudentLesson } from './student-lesson.interface';
import { Teacher } from './teacher.interface';

export interface Lesson {
  id?: string;
  description: string;
  spot: string;
  date: string;
  court: string;
  course?: Course;
  teacher?: Teacher;
  secondTeacher?: Teacher;
  lessonPlan?: LessonPlan;
  courseStudents: StudentLesson[];
  extraStudents: StudentLesson[];
}
