import { Component, OnInit, ElementRef } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { ICourse, Course } from 'app/shared/model/course.model';
import { CourseService } from './course.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag/tag.service';

@Component({
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html'
})
export class CourseUpdateComponent implements OnInit {
  isSaving: boolean;

  categories: ICategory[];

  tags: ITag[];
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    picture: [],
    pictureContentType: [],
    title: [null, [Validators.required]],
    subtitle: [],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    price: [null, [Validators.required]],
    level: [null, [Validators.required]],
    tutor: [null, [Validators.required]],
    contactInfo: [null, [Validators.required]],
    requirements: [null, [Validators.required]],
    description: [],
    location: [],
    hours: [],
    category: [null, Validators.required],
    tags: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected courseService: CourseService,
    protected categoryService: CategoryService,
    protected tagService: TagService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ course }) => {
      this.updateForm(course);
    });
    this.categoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICategory[]>) => response.body)
      )
      .subscribe((res: ICategory[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.tagService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITag[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITag[]>) => response.body)
      )
      .subscribe((res: ITag[]) => (this.tags = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(course: ICourse) {
    this.editForm.patchValue({
      id: course.id,
      picture: course.picture,
      pictureContentType: course.pictureContentType,
      title: course.title,
      subtitle: course.subtitle,
      startDate: course.startDate,
      endDate: course.endDate,
      price: course.price,
      level: course.level,
      tutor: course.tutor,
      contactInfo: course.contactInfo,
      requirements: course.requirements,
      description: course.description,
      location: course.location,
      hours: course.hours,
      category: course.category,
      tags: course.tags
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file: File = event.target.files[0];
        if (isImage && !file.type.startsWith('image/')) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      // eslint-disable-next-line no-console
      () => console.log('blob added'), // success
      this.onError
    );
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string) {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const course = this.createFromForm();
    if (course.id !== undefined) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
  }

  private createFromForm(): ICourse {
    return {
      ...new Course(),
      id: this.editForm.get(['id']).value,
      pictureContentType: this.editForm.get(['pictureContentType']).value,
      picture: this.editForm.get(['picture']).value,
      title: this.editForm.get(['title']).value,
      subtitle: this.editForm.get(['subtitle']).value,
      startDate: this.editForm.get(['startDate']).value,
      endDate: this.editForm.get(['endDate']).value,
      price: this.editForm.get(['price']).value,
      level: this.editForm.get(['level']).value,
      tutor: this.editForm.get(['tutor']).value,
      contactInfo: this.editForm.get(['contactInfo']).value,
      requirements: this.editForm.get(['requirements']).value,
      description: this.editForm.get(['description']).value,
      location: this.editForm.get(['location']).value,
      hours: this.editForm.get(['hours']).value,
      category: this.editForm.get(['category']).value,
      tags: this.editForm.get(['tags']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackCategoryById(index: number, item: ICategory) {
    return item.id;
  }

  trackTagById(index: number, item: ITag) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
