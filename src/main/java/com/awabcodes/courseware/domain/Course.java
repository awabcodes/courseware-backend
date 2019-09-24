package com.awabcodes.courseware.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.awabcodes.courseware.domain.enumeration.CourseLevel;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private CourseLevel level;

    @NotNull
    @Column(name = "tutor", nullable = false)
    private String tutor;

    @NotNull
    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @NotNull
    @Column(name = "requirements", nullable = false)
    private String requirements;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "hours")
    private Double hours;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("courses")
    private Category category;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "course_tags",
               joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public Course picture(byte[] picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public Course pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public String getTitle() {
        return title;
    }

    public Course title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Course subtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Course startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Course endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getPrice() {
        return price;
    }

    public Course price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CourseLevel getLevel() {
        return level;
    }

    public Course level(CourseLevel level) {
        this.level = level;
        return this;
    }

    public void setLevel(CourseLevel level) {
        this.level = level;
    }

    public String getTutor() {
        return tutor;
    }

    public Course tutor(String tutor) {
        this.tutor = tutor;
        return this;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public Course contactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
        return this;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getRequirements() {
        return requirements;
    }

    public Course requirements(String requirements) {
        this.requirements = requirements;
        return this;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getDescription() {
        return description;
    }

    public Course description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public Course location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getHours() {
        return hours;
    }

    public Course hours(Double hours) {
        this.hours = hours;
        return this;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Category getCategory() {
        return category;
    }

    public Course category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Course tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Course addTags(Tag tag) {
        this.tags.add(tag);
        tag.getCourses().add(this);
        return this;
    }

    public Course removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getCourses().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", title='" + getTitle() + "'" +
            ", subtitle='" + getSubtitle() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", price=" + getPrice() +
            ", level='" + getLevel() + "'" +
            ", tutor='" + getTutor() + "'" +
            ", contactInfo='" + getContactInfo() + "'" +
            ", requirements='" + getRequirements() + "'" +
            ", description='" + getDescription() + "'" +
            ", location='" + getLocation() + "'" +
            ", hours=" + getHours() +
            "}";
    }
}
