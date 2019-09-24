package com.awabcodes.courseware.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.awabcodes.courseware.domain.Course;
import com.awabcodes.courseware.domain.*; // for static metamodels
import com.awabcodes.courseware.repository.CourseRepository;
import com.awabcodes.courseware.service.dto.CourseCriteria;

/**
 * Service for executing complex queries for {@link Course} entities in the database.
 * The main input is a {@link CourseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Course} or a {@link Page} of {@link Course} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CourseQueryService extends QueryService<Course> {

    private final Logger log = LoggerFactory.getLogger(CourseQueryService.class);

    private final CourseRepository courseRepository;

    public CourseQueryService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Return a {@link List} of {@link Course} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Course> findByCriteria(CourseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Course> specification = createSpecification(criteria);
        return courseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Course} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Course> findByCriteria(CourseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Course> specification = createSpecification(criteria);
        return courseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CourseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Course> specification = createSpecification(criteria);
        return courseRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<Course> createSpecification(CourseCriteria criteria) {
        Specification<Course> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Course_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Course_.title));
            }
            if (criteria.getSubtitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubtitle(), Course_.subtitle));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Course_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Course_.endDate));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Course_.price));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getLevel(), Course_.level));
            }
            if (criteria.getTutor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTutor(), Course_.tutor));
            }
            if (criteria.getContactInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactInfo(), Course_.contactInfo));
            }
            if (criteria.getRequirements() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRequirements(), Course_.requirements));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Course_.description));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Course_.location));
            }
            if (criteria.getHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHours(), Course_.hours));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Course_.category, JoinType.LEFT).get(Category_.id)));
            }
            if (criteria.getTagsId() != null) {
                specification = specification.and(buildSpecification(criteria.getTagsId(),
                    root -> root.join(Course_.tags, JoinType.LEFT).get(Tag_.id)));
            }
        }
        return specification;
    }
}
