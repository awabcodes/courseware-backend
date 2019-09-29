import { ICourse } from 'app/shared/model/course.model';

export interface ICategory {
  id?: number;
  name?: string;
  courses?: ICourse[];
}

export class Category implements ICategory {
  constructor(public id?: number, public name?: string, public courses?: ICourse[]) {}
}
