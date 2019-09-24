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
 * Criteria class for the {@link com.awabcodes.courseware.domain.Favorite} entity. This class is used
 * in {@link com.awabcodes.courseware.web.rest.FavoriteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /favorites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FavoriteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userId;

    private LongFilter categoriesId;

    public FavoriteCriteria(){
    }

    public FavoriteCriteria(FavoriteCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.categoriesId = other.categoriesId == null ? null : other.categoriesId.copy();
    }

    @Override
    public FavoriteCriteria copy() {
        return new FavoriteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(LongFilter categoriesId) {
        this.categoriesId = categoriesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FavoriteCriteria that = (FavoriteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(categoriesId, that.categoriesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        userId,
        categoriesId
        );
    }

    @Override
    public String toString() {
        return "FavoriteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (categoriesId != null ? "categoriesId=" + categoriesId + ", " : "") +
            "}";
    }

}
