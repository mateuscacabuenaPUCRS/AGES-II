export interface License {
  id: number | null;
  startDate: string | null;
  endDate: string | null;
  planType: string | null;
  frequency: string | null;
  sport: string | null;
  coursesPerWeek: number;
  active: boolean;
}
