export interface AdminCourseRequest {
  sport: string;
  level: string;
  planType: string;
  studentsSize: number;
  weekDay: string | null;
  hour: string;
  uniqueLesson: boolean;
  uniqueDate: string | null;
}
