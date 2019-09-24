package com.awabcodes.courseware.web.rest;

import com.awabcodes.courseware.CoursewareApp;
import com.awabcodes.courseware.domain.Favorite;
import com.awabcodes.courseware.domain.User;
import com.awabcodes.courseware.domain.Category;
import com.awabcodes.courseware.repository.FavoriteRepository;
import com.awabcodes.courseware.service.FavoriteService;
import com.awabcodes.courseware.web.rest.errors.ExceptionTranslator;
import com.awabcodes.courseware.service.dto.FavoriteCriteria;
import com.awabcodes.courseware.service.FavoriteQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.awabcodes.courseware.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FavoriteResource} REST controller.
 */
@SpringBootTest(classes = CoursewareApp.class)
public class FavoriteResourceIT {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Mock
    private FavoriteRepository favoriteRepositoryMock;

    @Mock
    private FavoriteService favoriteServiceMock;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteQueryService favoriteQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFavoriteMockMvc;

    private Favorite favorite;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FavoriteResource favoriteResource = new FavoriteResource(favoriteService, favoriteQueryService);
        this.restFavoriteMockMvc = MockMvcBuilders.standaloneSetup(favoriteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Favorite createEntity(EntityManager em) {
        Favorite favorite = new Favorite();
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        favorite.setUser(user);
        return favorite;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Favorite createUpdatedEntity(EntityManager em) {
        Favorite favorite = new Favorite();
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        favorite.setUser(user);
        return favorite;
    }

    @BeforeEach
    public void initTest() {
        favorite = createEntity(em);
    }

    @Test
    @Transactional
    public void createFavorite() throws Exception {
        int databaseSizeBeforeCreate = favoriteRepository.findAll().size();

        // Create the Favorite
        restFavoriteMockMvc.perform(post("/api/favorites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favorite)))
            .andExpect(status().isCreated());

        // Validate the Favorite in the database
        List<Favorite> favoriteList = favoriteRepository.findAll();
        assertThat(favoriteList).hasSize(databaseSizeBeforeCreate + 1);
        Favorite testFavorite = favoriteList.get(favoriteList.size() - 1);
    }

    @Test
    @Transactional
    public void createFavoriteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = favoriteRepository.findAll().size();

        // Create the Favorite with an existing ID
        favorite.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavoriteMockMvc.perform(post("/api/favorites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favorite)))
            .andExpect(status().isBadRequest());

        // Validate the Favorite in the database
        List<Favorite> favoriteList = favoriteRepository.findAll();
        assertThat(favoriteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFavorites() throws Exception {
        // Initialize the database
        favoriteRepository.saveAndFlush(favorite);

        // Get all the favoriteList
        restFavoriteMockMvc.perform(get("/api/favorites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favorite.getId().intValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllFavoritesWithEagerRelationshipsIsEnabled() throws Exception {
        FavoriteResource favoriteResource = new FavoriteResource(favoriteServiceMock, favoriteQueryService);
        when(favoriteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restFavoriteMockMvc = MockMvcBuilders.standaloneSetup(favoriteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFavoriteMockMvc.perform(get("/api/favorites?eagerload=true"))
        .andExpect(status().isOk());

        verify(favoriteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllFavoritesWithEagerRelationshipsIsNotEnabled() throws Exception {
        FavoriteResource favoriteResource = new FavoriteResource(favoriteServiceMock, favoriteQueryService);
            when(favoriteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restFavoriteMockMvc = MockMvcBuilders.standaloneSetup(favoriteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFavoriteMockMvc.perform(get("/api/favorites?eagerload=true"))
        .andExpect(status().isOk());

            verify(favoriteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getFavorite() throws Exception {
        // Initialize the database
        favoriteRepository.saveAndFlush(favorite);

        // Get the favorite
        restFavoriteMockMvc.perform(get("/api/favorites/{id}", favorite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(favorite.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllFavoritesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = favorite.getUser();
        favoriteRepository.saveAndFlush(favorite);
        Long userId = user.getId();

        // Get all the favoriteList where user equals to userId
        defaultFavoriteShouldBeFound("userId.equals=" + userId);

        // Get all the favoriteList where user equals to userId + 1
        defaultFavoriteShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllFavoritesByCategoriesIsEqualToSomething() throws Exception {
        // Initialize the database
        favoriteRepository.saveAndFlush(favorite);
        Category categories = CategoryResourceIT.createEntity(em);
        em.persist(categories);
        em.flush();
        favorite.addCategories(categories);
        favoriteRepository.saveAndFlush(favorite);
        Long categoriesId = categories.getId();

        // Get all the favoriteList where categories equals to categoriesId
        defaultFavoriteShouldBeFound("categoriesId.equals=" + categoriesId);

        // Get all the favoriteList where categories equals to categoriesId + 1
        defaultFavoriteShouldNotBeFound("categoriesId.equals=" + (categoriesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFavoriteShouldBeFound(String filter) throws Exception {
        restFavoriteMockMvc.perform(get("/api/favorites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favorite.getId().intValue())));

        // Check, that the count call also returns 1
        restFavoriteMockMvc.perform(get("/api/favorites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFavoriteShouldNotBeFound(String filter) throws Exception {
        restFavoriteMockMvc.perform(get("/api/favorites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFavoriteMockMvc.perform(get("/api/favorites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFavorite() throws Exception {
        // Get the favorite
        restFavoriteMockMvc.perform(get("/api/favorites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavorite() throws Exception {
        // Initialize the database
        favoriteService.save(favorite);

        int databaseSizeBeforeUpdate = favoriteRepository.findAll().size();

        // Update the favorite
        Favorite updatedFavorite = favoriteRepository.findById(favorite.getId()).get();
        // Disconnect from session so that the updates on updatedFavorite are not directly saved in db
        em.detach(updatedFavorite);

        restFavoriteMockMvc.perform(put("/api/favorites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFavorite)))
            .andExpect(status().isOk());

        // Validate the Favorite in the database
        List<Favorite> favoriteList = favoriteRepository.findAll();
        assertThat(favoriteList).hasSize(databaseSizeBeforeUpdate);
        Favorite testFavorite = favoriteList.get(favoriteList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingFavorite() throws Exception {
        int databaseSizeBeforeUpdate = favoriteRepository.findAll().size();

        // Create the Favorite

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoriteMockMvc.perform(put("/api/favorites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favorite)))
            .andExpect(status().isBadRequest());

        // Validate the Favorite in the database
        List<Favorite> favoriteList = favoriteRepository.findAll();
        assertThat(favoriteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFavorite() throws Exception {
        // Initialize the database
        favoriteService.save(favorite);

        int databaseSizeBeforeDelete = favoriteRepository.findAll().size();

        // Delete the favorite
        restFavoriteMockMvc.perform(delete("/api/favorites/{id}", favorite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Favorite> favoriteList = favoriteRepository.findAll();
        assertThat(favoriteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Favorite.class);
        Favorite favorite1 = new Favorite();
        favorite1.setId(1L);
        Favorite favorite2 = new Favorite();
        favorite2.setId(favorite1.getId());
        assertThat(favorite1).isEqualTo(favorite2);
        favorite2.setId(2L);
        assertThat(favorite1).isNotEqualTo(favorite2);
        favorite1.setId(null);
        assertThat(favorite1).isNotEqualTo(favorite2);
    }
}
