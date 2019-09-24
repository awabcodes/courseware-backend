package com.awabcodes.courseware.web.rest;

import com.awabcodes.courseware.domain.Favorite;
import com.awabcodes.courseware.service.FavoriteService;
import com.awabcodes.courseware.web.rest.errors.BadRequestAlertException;
import com.awabcodes.courseware.service.dto.FavoriteCriteria;
import com.awabcodes.courseware.service.FavoriteQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.awabcodes.courseware.domain.Favorite}.
 */
@RestController
@RequestMapping("/api")
public class FavoriteResource {

    private final Logger log = LoggerFactory.getLogger(FavoriteResource.class);

    private static final String ENTITY_NAME = "favorite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FavoriteService favoriteService;

    private final FavoriteQueryService favoriteQueryService;

    public FavoriteResource(FavoriteService favoriteService, FavoriteQueryService favoriteQueryService) {
        this.favoriteService = favoriteService;
        this.favoriteQueryService = favoriteQueryService;
    }

    /**
     * {@code POST  /favorites} : Create a new favorite.
     *
     * @param favorite the favorite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new favorite, or with status {@code 400 (Bad Request)} if the favorite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/favorites")
    public ResponseEntity<Favorite> createFavorite(@Valid @RequestBody Favorite favorite) throws URISyntaxException {
        log.debug("REST request to save Favorite : {}", favorite);
        if (favorite.getId() != null) {
            throw new BadRequestAlertException("A new favorite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Favorite result = favoriteService.save(favorite);
        return ResponseEntity.created(new URI("/api/favorites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /favorites} : Updates an existing favorite.
     *
     * @param favorite the favorite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favorite,
     * or with status {@code 400 (Bad Request)} if the favorite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the favorite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/favorites")
    public ResponseEntity<Favorite> updateFavorite(@Valid @RequestBody Favorite favorite) throws URISyntaxException {
        log.debug("REST request to update Favorite : {}", favorite);
        if (favorite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Favorite result = favoriteService.save(favorite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, favorite.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /favorites} : get all the favorites.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of favorites in body.
     */
    @GetMapping("/favorites")
    public ResponseEntity<List<Favorite>> getAllFavorites(FavoriteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Favorites by criteria: {}", criteria);
        Page<Favorite> page = favoriteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /favorites/count} : count all the favorites.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/favorites/count")
    public ResponseEntity<Long> countFavorites(FavoriteCriteria criteria) {
        log.debug("REST request to count Favorites by criteria: {}", criteria);
        return ResponseEntity.ok().body(favoriteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /favorites/:id} : get the "id" favorite.
     *
     * @param id the id of the favorite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the favorite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/favorites/{id}")
    public ResponseEntity<Favorite> getFavorite(@PathVariable Long id) {
        log.debug("REST request to get Favorite : {}", id);
        Optional<Favorite> favorite = favoriteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(favorite);
    }

    /**
     * {@code DELETE  /favorites/:id} : delete the "id" favorite.
     *
     * @param id the id of the favorite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        log.debug("REST request to delete Favorite : {}", id);
        favoriteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
