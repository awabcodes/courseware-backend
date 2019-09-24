import { Moment } from 'moment';
import { ICategory } from 'app/shared/model/category.model';
import { ITag } from 'app/shared/model/tag.model';
import { CourseLevel } from 'app/shared/model/enumerations/course-level.model';

export interface ICourse {
  id?: number;
  pictureContentType?: string;
  picture?: any;
  title?: string;
  subtitle?: string;
  startDate?: Moment;
  endDate?: Moment;
  price?: number;
  level?: CourseLevel;
  tutor?: string;
  contactInfo?: string;
  requirements?: string;
  description?: string;
  location?: string;
  hours?: number;
  category?: ICategory;
  tags?: ITag[];
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public pictureContentType?: string,
    public picture?: any,
    public title?: string,
    public subtitle?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public price?: number,
    public level?: CourseLevel,
    public tutor?: string,
    public contactInfo?: string,
    public requirements?: string,
    public description?: string,
    public location?: string,
    public hours?: number,
    public category?: ICategory,
    public tags?: ITag[]
  ) {}
}
