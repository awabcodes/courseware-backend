package com.awabcodes.courseware.service;

import com.awabcodes.courseware.domain.Favorite;
import com.awabcodes.courseware.repository.FavoriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Favorite}.
 */
@Service
@Transactional
public class FavoriteService {

    private final Logger log = LoggerFactory.getLogger(FavoriteService.class);

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * Save a favorite.
     *
     * @param favorite the entity to save.
     * @return the persisted entity.
     */
    public Favorite save(Favorite favorite) {
        log.debug("Request to save Favorite : {}", favorite);
        return favoriteRepository.save(favorite);
    }

    /**
     * Get all the favorites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Favorite> findAll(Pageable pageable) {
        log.debug("Request to get all Favorites");
        return favoriteRepository.findAll(pageable);
    }

    /**
     * Get all the favorites with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Favorite> findAllWithEagerRelationships(Pageable pageable) {
        return favoriteRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one favorite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Favorite> findOne(Long id) {
        log.debug("Request to get Favorite : {}", id);
        return favoriteRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the favorite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Favorite : {}", id);
        favoriteRepository.deleteById(id);
    }
}
