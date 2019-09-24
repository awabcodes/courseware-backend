import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IFavorite, Favorite } from 'app/shared/model/favorite.model';
import { FavoriteService } from './favorite.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';

@Component({
  selector: 'jhi-favorite-update',
  templateUrl: './favorite-update.component.html'
})
export class FavoriteUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  categories: ICategory[];

  editForm = this.fb.group({
    id: [],
    user: [null, Validators.required],
    categories: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected favoriteService: FavoriteService,
    protected userService: UserService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ favorite }) => {
      this.updateForm(favorite);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.categoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICategory[]>) => response.body)
      )
      .subscribe((res: ICategory[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(favorite: IFavorite) {
    this.editForm.patchValue({
      id: favorite.id,
      user: favorite.user,
      categories: favorite.categories
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const favorite = this.createFromForm();
    if (favorite.id !== undefined) {
      this.subscribeToSaveResponse(this.favoriteService.update(favorite));
    } else {
      this.subscribeToSaveResponse(this.favoriteService.create(favorite));
    }
  }

  private createFromForm(): IFavorite {
    return {
      ...new Favorite(),
      id: this.editForm.get(['id']).value,
      user: this.editForm.get(['user']).value,
      categories: this.editForm.get(['categories']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFavorite>>) {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackCategoryById(index: number, item: ICategory) {
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
