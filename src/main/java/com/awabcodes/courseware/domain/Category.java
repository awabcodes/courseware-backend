package com.awabcodes.courseware.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Favorite> favorites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Category name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Category courses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    public Category addCourses(Course course) {
        this.courses.add(course);
        course.setCategory(this);
        return this;
    }

    public Category removeCourses(Course course) {
        this.courses.remove(course);
        course.setCategory(null);
        return this;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Set<Favorite> getFavorites() {
        return favorites;
    }

    public Category favorites(Set<Favorite> favorites) {
        this.favorites = favorites;
        return this;
    }

    public Category addFavorites(Favorite favorite) {
        this.favorites.add(favorite);
        favorite.getCategories().add(this);
        return this;
    }

    public Category removeFavorites(Favorite favorite) {
        this.favorites.remove(favorite);
        favorite.getCategories().remove(this);
        return this;
    }

    public void setFavorites(Set<Favorite> favorites) {
        this.favorites = favorites;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
