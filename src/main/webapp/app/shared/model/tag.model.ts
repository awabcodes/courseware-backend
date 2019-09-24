import { ICourse } from 'app/shared/model/course.model';

export interface ITag {
  id?: number;
  name?: string;
  courses?: ICourse[];
}

export class Tag implements ITag {
  constructor(public id?: number, public name?: string, public courses?: ICourse[]) {}
}
