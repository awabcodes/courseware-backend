import { ICourse } from 'app/shared/model/course.model';
import { IFavorite } from 'app/shared/model/favorite.model';

export interface ICategory {
  id?: number;
  name?: string;
  courses?: ICourse[];
  favorites?: IFavorite[];
}

export class Category implements ICategory {
  constructor(public id?: number, public name?: string, public courses?: ICourse[], public favorites?: IFavorite[]) {}
}
