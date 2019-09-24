package com.awabcodes.courseware.web.rest;

import com.awabcodes.courseware.CoursewareApp;
import com.awabcodes.courseware.domain.Course;
import com.awabcodes.courseware.domain.Category;
import com.awabcodes.courseware.domain.Tag;
import com.awabcodes.courseware.repository.CourseRepository;
import com.awabcodes.courseware.service.CourseService;
import com.awabcodes.courseware.web.rest.errors.ExceptionTranslator;
import com.awabcodes.courseware.service.dto.CourseCriteria;
import com.awabcodes.courseware.service.CourseQueryService;

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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.awabcodes.courseware.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.awabcodes.courseware.domain.enumeration.CourseLevel;
/**
 * Integration tests for the {@link CourseResource} REST controller.
 */
@SpringBootTest(classes = CoursewareApp.class)
public class CourseResourceIT {

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBTITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUBTITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final CourseLevel DEFAULT_LEVEL = CourseLevel.BEGINNER;
    private static final CourseLevel UPDATED_LEVEL = CourseLevel.INTERMEDIATE;

    private static final String DEFAULT_TUTOR = "AAAAAAAAAA";
    private static final String UPDATED_TUTOR = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_REQUIREMENTS = "AAAAAAAAAA";
    private static final String UPDATED_REQUIREMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Double DEFAULT_HOURS = 1D;
    private static final Double UPDATED_HOURS = 2D;
    private static final Double SMALLER_HOURS = 1D - 1D;

    @Autowired
    private CourseRepository courseRepository;

    @Mock
    private CourseRepository courseRepositoryMock;

    @Mock
    private CourseService courseServiceMock;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseQueryService courseQueryService;

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

    private MockMvc restCourseMockMvc;

    private Course course;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseResource courseResource = new CourseResource(courseService, courseQueryService);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
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
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .title(DEFAULT_TITLE)
            .subtitle(DEFAULT_SUBTITLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .price(DEFAULT_PRICE)
            .level(DEFAULT_LEVEL)
            .tutor(DEFAULT_TUTOR)
            .contactInfo(DEFAULT_CONTACT_INFO)
            .requirements(DEFAULT_REQUIREMENTS)
            .description(DEFAULT_DESCRIPTION)
            .location(DEFAULT_LOCATION)
            .hours(DEFAULT_HOURS);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        course.setCategory(category);
        return course;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course()
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .price(UPDATED_PRICE)
            .level(UPDATED_LEVEL)
            .tutor(UPDATED_TUTOR)
            .contactInfo(UPDATED_CONTACT_INFO)
            .requirements(UPDATED_REQUIREMENTS)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .hours(UPDATED_HOURS);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        course.setCategory(category);
        return course;
    }

    @BeforeEach
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testCourse.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCourse.getSubtitle()).isEqualTo(DEFAULT_SUBTITLE);
        assertThat(testCourse.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCourse.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCourse.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCourse.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testCourse.getTutor()).isEqualTo(DEFAULT_TUTOR);
        assertThat(testCourse.getContactInfo()).isEqualTo(DEFAULT_CONTACT_INFO);
        assertThat(testCourse.getRequirements()).isEqualTo(DEFAULT_REQUIREMENTS);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCourse.getHours()).isEqualTo(DEFAULT_HOURS);
    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setTitle(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setStartDate(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setEndDate(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setPrice(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setLevel(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTutorIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setTutor(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContactInfoIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setContactInfo(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRequirementsIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setRequirements(null);

        // Create the Course, which fails.

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].subtitle").value(hasItem(DEFAULT_SUBTITLE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].tutor").value(hasItem(DEFAULT_TUTOR.toString())))
            .andExpect(jsonPath("$.[*].contactInfo").value(hasItem(DEFAULT_CONTACT_INFO.toString())))
            .andExpect(jsonPath("$.[*].requirements").value(hasItem(DEFAULT_REQUIREMENTS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].hours").value(hasItem(DEFAULT_HOURS.doubleValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCoursesWithEagerRelationshipsIsEnabled() throws Exception {
        CourseResource courseResource = new CourseResource(courseServiceMock, courseQueryService);
        when(courseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCourseMockMvc.perform(get("/api/courses?eagerload=true"))
        .andExpect(status().isOk());

        verify(courseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCoursesWithEagerRelationshipsIsNotEnabled() throws Exception {
        CourseResource courseResource = new CourseResource(courseServiceMock, courseQueryService);
            when(courseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCourseMockMvc.perform(get("/api/courses?eagerload=true"))
        .andExpect(status().isOk());

            verify(courseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.subtitle").value(DEFAULT_SUBTITLE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.tutor").value(DEFAULT_TUTOR.toString()))
            .andExpect(jsonPath("$.contactInfo").value(DEFAULT_CONTACT_INFO.toString()))
            .andExpect(jsonPath("$.requirements").value(DEFAULT_REQUIREMENTS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.hours").value(DEFAULT_HOURS.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllCoursesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where title equals to DEFAULT_TITLE
        defaultCourseShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the courseList where title equals to UPDATED_TITLE
        defaultCourseShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCoursesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCourseShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the courseList where title equals to UPDATED_TITLE
        defaultCourseShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCoursesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where title is not null
        defaultCourseShouldBeFound("title.specified=true");

        // Get all the courseList where title is null
        defaultCourseShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesBySubtitleIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where subtitle equals to DEFAULT_SUBTITLE
        defaultCourseShouldBeFound("subtitle.equals=" + DEFAULT_SUBTITLE);

        // Get all the courseList where subtitle equals to UPDATED_SUBTITLE
        defaultCourseShouldNotBeFound("subtitle.equals=" + UPDATED_SUBTITLE);
    }

    @Test
    @Transactional
    public void getAllCoursesBySubtitleIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where subtitle in DEFAULT_SUBTITLE or UPDATED_SUBTITLE
        defaultCourseShouldBeFound("subtitle.in=" + DEFAULT_SUBTITLE + "," + UPDATED_SUBTITLE);

        // Get all the courseList where subtitle equals to UPDATED_SUBTITLE
        defaultCourseShouldNotBeFound("subtitle.in=" + UPDATED_SUBTITLE);
    }

    @Test
    @Transactional
    public void getAllCoursesBySubtitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where subtitle is not null
        defaultCourseShouldBeFound("subtitle.specified=true");

        // Get all the courseList where subtitle is null
        defaultCourseShouldNotBeFound("subtitle.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate equals to DEFAULT_START_DATE
        defaultCourseShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate equals to UPDATED_START_DATE
        defaultCourseShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultCourseShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the courseList where startDate equals to UPDATED_START_DATE
        defaultCourseShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate is not null
        defaultCourseShouldBeFound("startDate.specified=true");

        // Get all the courseList where startDate is null
        defaultCourseShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultCourseShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate is greater than or equal to UPDATED_START_DATE
        defaultCourseShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate is less than or equal to DEFAULT_START_DATE
        defaultCourseShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate is less than or equal to SMALLER_START_DATE
        defaultCourseShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate is less than DEFAULT_START_DATE
        defaultCourseShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate is less than UPDATED_START_DATE
        defaultCourseShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate is greater than DEFAULT_START_DATE
        defaultCourseShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate is greater than SMALLER_START_DATE
        defaultCourseShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }


    @Test
    @Transactional
    public void getAllCoursesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where endDate equals to DEFAULT_END_DATE
        defaultCourseShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the courseList where endDate equals to UPDATED_END_DATE
        defaultCourseShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultCourseShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the courseList where endDate equals to UPDATED_END_DATE
        defaultCourseShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where endDate is not null
        defaultCourseShouldBeFound("endDate.specified=true");

        // Get all the courseList where endDate is null
        defaultCourseShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultCourseShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the courseList where endDate is greater than or equal to UPDATED_END_DATE
        defaultCourseShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where endDate is less than or equal to DEFAULT_END_DATE
        defaultCourseShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the courseList where endDate is less than or equal to SMALLER_END_DATE
        defaultCourseShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where endDate is less than DEFAULT_END_DATE
        defaultCourseShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the courseList where endDate is less than UPDATED_END_DATE
        defaultCourseShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where endDate is greater than DEFAULT_END_DATE
        defaultCourseShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the courseList where endDate is greater than SMALLER_END_DATE
        defaultCourseShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }


    @Test
    @Transactional
    public void getAllCoursesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price equals to DEFAULT_PRICE
        defaultCourseShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the courseList where price equals to UPDATED_PRICE
        defaultCourseShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultCourseShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the courseList where price equals to UPDATED_PRICE
        defaultCourseShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price is not null
        defaultCourseShouldBeFound("price.specified=true");

        // Get all the courseList where price is null
        defaultCourseShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price is greater than or equal to DEFAULT_PRICE
        defaultCourseShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the courseList where price is greater than or equal to UPDATED_PRICE
        defaultCourseShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price is less than or equal to DEFAULT_PRICE
        defaultCourseShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the courseList where price is less than or equal to SMALLER_PRICE
        defaultCourseShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price is less than DEFAULT_PRICE
        defaultCourseShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the courseList where price is less than UPDATED_PRICE
        defaultCourseShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price is greater than DEFAULT_PRICE
        defaultCourseShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the courseList where price is greater than SMALLER_PRICE
        defaultCourseShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllCoursesByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where level equals to DEFAULT_LEVEL
        defaultCourseShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the courseList where level equals to UPDATED_LEVEL
        defaultCourseShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllCoursesByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultCourseShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the courseList where level equals to UPDATED_LEVEL
        defaultCourseShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllCoursesByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where level is not null
        defaultCourseShouldBeFound("level.specified=true");

        // Get all the courseList where level is null
        defaultCourseShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByTutorIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where tutor equals to DEFAULT_TUTOR
        defaultCourseShouldBeFound("tutor.equals=" + DEFAULT_TUTOR);

        // Get all the courseList where tutor equals to UPDATED_TUTOR
        defaultCourseShouldNotBeFound("tutor.equals=" + UPDATED_TUTOR);
    }

    @Test
    @Transactional
    public void getAllCoursesByTutorIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where tutor in DEFAULT_TUTOR or UPDATED_TUTOR
        defaultCourseShouldBeFound("tutor.in=" + DEFAULT_TUTOR + "," + UPDATED_TUTOR);

        // Get all the courseList where tutor equals to UPDATED_TUTOR
        defaultCourseShouldNotBeFound("tutor.in=" + UPDATED_TUTOR);
    }

    @Test
    @Transactional
    public void getAllCoursesByTutorIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where tutor is not null
        defaultCourseShouldBeFound("tutor.specified=true");

        // Get all the courseList where tutor is null
        defaultCourseShouldNotBeFound("tutor.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByContactInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where contactInfo equals to DEFAULT_CONTACT_INFO
        defaultCourseShouldBeFound("contactInfo.equals=" + DEFAULT_CONTACT_INFO);

        // Get all the courseList where contactInfo equals to UPDATED_CONTACT_INFO
        defaultCourseShouldNotBeFound("contactInfo.equals=" + UPDATED_CONTACT_INFO);
    }

    @Test
    @Transactional
    public void getAllCoursesByContactInfoIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where contactInfo in DEFAULT_CONTACT_INFO or UPDATED_CONTACT_INFO
        defaultCourseShouldBeFound("contactInfo.in=" + DEFAULT_CONTACT_INFO + "," + UPDATED_CONTACT_INFO);

        // Get all the courseList where contactInfo equals to UPDATED_CONTACT_INFO
        defaultCourseShouldNotBeFound("contactInfo.in=" + UPDATED_CONTACT_INFO);
    }

    @Test
    @Transactional
    public void getAllCoursesByContactInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where contactInfo is not null
        defaultCourseShouldBeFound("contactInfo.specified=true");

        // Get all the courseList where contactInfo is null
        defaultCourseShouldNotBeFound("contactInfo.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByRequirementsIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where requirements equals to DEFAULT_REQUIREMENTS
        defaultCourseShouldBeFound("requirements.equals=" + DEFAULT_REQUIREMENTS);

        // Get all the courseList where requirements equals to UPDATED_REQUIREMENTS
        defaultCourseShouldNotBeFound("requirements.equals=" + UPDATED_REQUIREMENTS);
    }

    @Test
    @Transactional
    public void getAllCoursesByRequirementsIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where requirements in DEFAULT_REQUIREMENTS or UPDATED_REQUIREMENTS
        defaultCourseShouldBeFound("requirements.in=" + DEFAULT_REQUIREMENTS + "," + UPDATED_REQUIREMENTS);

        // Get all the courseList where requirements equals to UPDATED_REQUIREMENTS
        defaultCourseShouldNotBeFound("requirements.in=" + UPDATED_REQUIREMENTS);
    }

    @Test
    @Transactional
    public void getAllCoursesByRequirementsIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where requirements is not null
        defaultCourseShouldBeFound("requirements.specified=true");

        // Get all the courseList where requirements is null
        defaultCourseShouldNotBeFound("requirements.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description equals to DEFAULT_DESCRIPTION
        defaultCourseShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the courseList where description equals to UPDATED_DESCRIPTION
        defaultCourseShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCourseShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the courseList where description equals to UPDATED_DESCRIPTION
        defaultCourseShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description is not null
        defaultCourseShouldBeFound("description.specified=true");

        // Get all the courseList where description is null
        defaultCourseShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where location equals to DEFAULT_LOCATION
        defaultCourseShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the courseList where location equals to UPDATED_LOCATION
        defaultCourseShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultCourseShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the courseList where location equals to UPDATED_LOCATION
        defaultCourseShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where location is not null
        defaultCourseShouldBeFound("location.specified=true");

        // Get all the courseList where location is null
        defaultCourseShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours equals to DEFAULT_HOURS
        defaultCourseShouldBeFound("hours.equals=" + DEFAULT_HOURS);

        // Get all the courseList where hours equals to UPDATED_HOURS
        defaultCourseShouldNotBeFound("hours.equals=" + UPDATED_HOURS);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours in DEFAULT_HOURS or UPDATED_HOURS
        defaultCourseShouldBeFound("hours.in=" + DEFAULT_HOURS + "," + UPDATED_HOURS);

        // Get all the courseList where hours equals to UPDATED_HOURS
        defaultCourseShouldNotBeFound("hours.in=" + UPDATED_HOURS);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours is not null
        defaultCourseShouldBeFound("hours.specified=true");

        // Get all the courseList where hours is null
        defaultCourseShouldNotBeFound("hours.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours is greater than or equal to DEFAULT_HOURS
        defaultCourseShouldBeFound("hours.greaterThanOrEqual=" + DEFAULT_HOURS);

        // Get all the courseList where hours is greater than or equal to UPDATED_HOURS
        defaultCourseShouldNotBeFound("hours.greaterThanOrEqual=" + UPDATED_HOURS);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours is less than or equal to DEFAULT_HOURS
        defaultCourseShouldBeFound("hours.lessThanOrEqual=" + DEFAULT_HOURS);

        // Get all the courseList where hours is less than or equal to SMALLER_HOURS
        defaultCourseShouldNotBeFound("hours.lessThanOrEqual=" + SMALLER_HOURS);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours is less than DEFAULT_HOURS
        defaultCourseShouldNotBeFound("hours.lessThan=" + DEFAULT_HOURS);

        // Get all the courseList where hours is less than UPDATED_HOURS
        defaultCourseShouldBeFound("hours.lessThan=" + UPDATED_HOURS);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours is greater than DEFAULT_HOURS
        defaultCourseShouldNotBeFound("hours.greaterThan=" + DEFAULT_HOURS);

        // Get all the courseList where hours is greater than SMALLER_HOURS
        defaultCourseShouldBeFound("hours.greaterThan=" + SMALLER_HOURS);
    }


    @Test
    @Transactional
    public void getAllCoursesByCategoryIsEqualToSomething() throws Exception {
        // Get already existing entity
        Category category = course.getCategory();
        courseRepository.saveAndFlush(course);
        Long categoryId = category.getId();

        // Get all the courseList where category equals to categoryId
        defaultCourseShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the courseList where category equals to categoryId + 1
        defaultCourseShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }


    @Test
    @Transactional
    public void getAllCoursesByTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Tag tags = TagResourceIT.createEntity(em);
        em.persist(tags);
        em.flush();
        course.addTags(tags);
        courseRepository.saveAndFlush(course);
        Long tagsId = tags.getId();

        // Get all the courseList where tags equals to tagsId
        defaultCourseShouldBeFound("tagsId.equals=" + tagsId);

        // Get all the courseList where tags equals to tagsId + 1
        defaultCourseShouldNotBeFound("tagsId.equals=" + (tagsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCourseShouldBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subtitle").value(hasItem(DEFAULT_SUBTITLE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].tutor").value(hasItem(DEFAULT_TUTOR)))
            .andExpect(jsonPath("$.[*].contactInfo").value(hasItem(DEFAULT_CONTACT_INFO)))
            .andExpect(jsonPath("$.[*].requirements").value(hasItem(DEFAULT_REQUIREMENTS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].hours").value(hasItem(DEFAULT_HOURS.doubleValue())));

        // Check, that the count call also returns 1
        restCourseMockMvc.perform(get("/api/courses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCourseShouldNotBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCourseMockMvc.perform(get("/api/courses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        // Initialize the database
        courseService.save(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .price(UPDATED_PRICE)
            .level(UPDATED_LEVEL)
            .tutor(UPDATED_TUTOR)
            .contactInfo(UPDATED_CONTACT_INFO)
            .requirements(UPDATED_REQUIREMENTS)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .hours(UPDATED_HOURS);

        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourse)))
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testCourse.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testCourse.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCourse.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
        assertThat(testCourse.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCourse.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCourse.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCourse.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testCourse.getTutor()).isEqualTo(UPDATED_TUTOR);
        assertThat(testCourse.getContactInfo()).isEqualTo(UPDATED_CONTACT_INFO);
        assertThat(testCourse.getRequirements()).isEqualTo(UPDATED_REQUIREMENTS);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCourse.getHours()).isEqualTo(UPDATED_HOURS);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Create the Course

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
        courseService.save(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);
        course2.setId(2L);
        assertThat(course1).isNotEqualTo(course2);
        course1.setId(null);
        assertThat(course1).isNotEqualTo(course2);
    }
}
