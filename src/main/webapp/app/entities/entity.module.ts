import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'course',
        loadChildren: () => import('./course/course.module').then(m => m.CoursewareCourseModule)
      },
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.CoursewareCategoryModule)
      },
      {
        path: 'tag',
        loadChildren: () => import('./tag/tag.module').then(m => m.CoursewareTagModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: []
})
export class CoursewareEntityModule {}
