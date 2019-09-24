package com.awabcodes.courseware.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.awabcodes.courseware.domain.Category} entity. This class is used
 * in {@link com.awabcodes.courseware.web.rest.CategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter coursesId;

    private LongFilter favoritesId;

    public CategoryCriteria(){
    }

    public CategoryCriteria(CategoryCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.coursesId = other.coursesId == null ? null : other.coursesId.copy();
        this.favoritesId = other.favoritesId == null ? null : other.favoritesId.copy();
    }

    @Override
    public CategoryCriteria copy() {
        return new CategoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(LongFilter coursesId) {
        this.coursesId = coursesId;
    }

    public LongFilter getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(LongFilter favoritesId) {
        this.favoritesId = favoritesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CategoryCriteria that = (CategoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(coursesId, that.coursesId) &&
            Objects.equals(favoritesId, that.favoritesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        coursesId,
        favoritesId
        );
    }

    @Override
    public String toString() {
        return "CategoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (coursesId != null ? "coursesId=" + coursesId + ", " : "") +
                (favoritesId != null ? "favoritesId=" + favoritesId + ", " : "") +
            "}";
    }

}
