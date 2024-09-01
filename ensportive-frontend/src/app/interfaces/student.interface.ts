import { License } from './license.interface';

export interface Student {
  id: string | null;
  name: string;
  email: string;
  cellPhone: string;
  username: string | null;
  level:
    | 'BEGINNER_1'
    | 'BEGINNER_2'
    | 'INTERMEDIATE_1'
    | 'INTERMEDIATE_2'
    | 'ADVANCED'
    | 'ELITE';
  license: License;
}
