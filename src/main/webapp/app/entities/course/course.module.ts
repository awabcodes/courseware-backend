import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CoursewareSharedModule } from 'app/shared/shared.module';
import { CourseComponent } from './course.component';
import { CourseDetailComponent } from './course-detail.component';
import { CourseUpdateComponent } from './course-update.component';
import { CourseDeletePopupComponent, CourseDeleteDialogComponent } from './course-delete-dialog.component';
import { courseRoute, coursePopupRoute } from './course.route';

const ENTITY_STATES = [...courseRoute, ...coursePopupRoute];

@NgModule({
  imports: [CoursewareSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [CourseComponent, CourseDetailComponent, CourseUpdateComponent, CourseDeleteDialogComponent, CourseDeletePopupComponent],
  entryComponents: [CourseComponent, CourseUpdateComponent, CourseDeleteDialogComponent, CourseDeletePopupComponent]
})
export class CoursewareCourseModule {}
