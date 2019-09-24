package com.awabcodes.courseware.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.awabcodes.courseware.domain.enumeration.CourseLevel;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.awabcodes.courseware.domain.Course} entity. This class is used
 * in {@link com.awabcodes.courseware.web.rest.CourseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /courses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CourseCriteria implements Serializable, Criteria {
    /**
     * Class for filtering CourseLevel
     */
    public static class CourseLevelFilter extends Filter<CourseLevel> {

        public CourseLevelFilter() {
        }

        public CourseLevelFilter(CourseLevelFilter filter) {
            super(filter);
        }

        @Override
        public CourseLevelFilter copy() {
            return new CourseLevelFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter subtitle;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private DoubleFilter price;

    private CourseLevelFilter level;

    private StringFilter tutor;

    private StringFilter contactInfo;

    private StringFilter requirements;

    private StringFilter description;

    private StringFilter location;

    private DoubleFilter hours;

    private LongFilter categoryId;

    private LongFilter tagsId;

    public CourseCriteria(){
    }

    public CourseCriteria(CourseCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.subtitle = other.subtitle == null ? null : other.subtitle.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.level = other.level == null ? null : other.level.copy();
        this.tutor = other.tutor == null ? null : other.tutor.copy();
        this.contactInfo = other.contactInfo == null ? null : other.contactInfo.copy();
        this.requirements = other.requirements == null ? null : other.requirements.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.hours = other.hours == null ? null : other.hours.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.tagsId = other.tagsId == null ? null : other.tagsId.copy();
    }

    @Override
    public CourseCriteria copy() {
        return new CourseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(StringFilter subtitle) {
        this.subtitle = subtitle;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public CourseLevelFilter getLevel() {
        return level;
    }

    public void setLevel(CourseLevelFilter level) {
        this.level = level;
    }

    public StringFilter getTutor() {
        return tutor;
    }

    public void setTutor(StringFilter tutor) {
        this.tutor = tutor;
    }

    public StringFilter getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(StringFilter contactInfo) {
        this.contactInfo = contactInfo;
    }

    public StringFilter getRequirements() {
        return requirements;
    }

    public void setRequirements(StringFilter requirements) {
        this.requirements = requirements;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public DoubleFilter getHours() {
        return hours;
    }

    public void setHours(DoubleFilter hours) {
        this.hours = hours;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getTagsId() {
        return tagsId;
    }

    public void setTagsId(LongFilter tagsId) {
        this.tagsId = tagsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CourseCriteria that = (CourseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(subtitle, that.subtitle) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(price, that.price) &&
            Objects.equals(level, that.level) &&
            Objects.equals(tutor, that.tutor) &&
            Objects.equals(contactInfo, that.contactInfo) &&
            Objects.equals(requirements, that.requirements) &&
            Objects.equals(description, that.description) &&
            Objects.equals(location, that.location) &&
            Objects.equals(hours, that.hours) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(tagsId, that.tagsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        subtitle,
        startDate,
        endDate,
        price,
        level,
        tutor,
        contactInfo,
        requirements,
        description,
        location,
        hours,
        categoryId,
        tagsId
        );
    }

    @Override
    public String toString() {
        return "CourseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (subtitle != null ? "subtitle=" + subtitle + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (level != null ? "level=" + level + ", " : "") +
                (tutor != null ? "tutor=" + tutor + ", " : "") +
                (contactInfo != null ? "contactInfo=" + contactInfo + ", " : "") +
                (requirements != null ? "requirements=" + requirements + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (hours != null ? "hours=" + hours + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (tagsId != null ? "tagsId=" + tagsId + ", " : "") +
            "}";
    }

}
