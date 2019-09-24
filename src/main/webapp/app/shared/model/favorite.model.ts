import { IUser } from 'app/core/user/user.model';
import { ICategory } from 'app/shared/model/category.model';

export interface IFavorite {
  id?: number;
  user?: IUser;
  categories?: ICategory[];
}

export class Favorite implements IFavorite {
  constructor(public id?: number, public user?: IUser, public categories?: ICategory[]) {}
}
