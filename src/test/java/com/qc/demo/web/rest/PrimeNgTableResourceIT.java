package com.qc.demo.web.rest;

import static com.qc.demo.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qc.demo.IntegrationTest;
import com.qc.demo.domain.PrimeNgTable;
import com.qc.demo.repository.PrimeNgTableRepository;
import com.qc.demo.service.criteria.PrimeNgTableCriteria;
import com.qc.demo.service.dto.PrimeNgTableDTO;
import com.qc.demo.service.mapper.PrimeNgTableMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PrimeNgTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrimeNgTableResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;
    private static final Integer SMALLER_NUMBER = 1 - 1;

    private static final Double DEFAULT_FLOAT_NUMBER = 1D;
    private static final Double UPDATED_FLOAT_NUMBER = 2D;
    private static final Double SMALLER_FLOAT_NUMBER = 1D - 1D;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_ZONE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ZONE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ZONE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_LONG_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_LONG_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/prime-ng-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrimeNgTableRepository primeNgTableRepository;

    @Autowired
    private PrimeNgTableMapper primeNgTableMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrimeNgTableMockMvc;

    private PrimeNgTable primeNgTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrimeNgTable createEntity(EntityManager em) {
        PrimeNgTable primeNgTable = new PrimeNgTable()
            .text(DEFAULT_TEXT)
            .number(DEFAULT_NUMBER)
            .floatNumber(DEFAULT_FLOAT_NUMBER)
            .date(DEFAULT_DATE)
            .zoneDate(DEFAULT_ZONE_DATE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .longText(DEFAULT_LONG_TEXT);
        return primeNgTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrimeNgTable createUpdatedEntity(EntityManager em) {
        PrimeNgTable primeNgTable = new PrimeNgTable()
            .text(UPDATED_TEXT)
            .number(UPDATED_NUMBER)
            .floatNumber(UPDATED_FLOAT_NUMBER)
            .date(UPDATED_DATE)
            .zoneDate(UPDATED_ZONE_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .longText(UPDATED_LONG_TEXT);
        return primeNgTable;
    }

    @BeforeEach
    public void initTest() {
        primeNgTable = createEntity(em);
    }

    @Test
    @Transactional
    void createPrimeNgTable() throws Exception {
        int databaseSizeBeforeCreate = primeNgTableRepository.findAll().size();
        // Create the PrimeNgTable
        PrimeNgTableDTO primeNgTableDTO = primeNgTableMapper.toDto(primeNgTable);
        restPrimeNgTableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(primeNgTableDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PrimeNgTable in the database
        List<PrimeNgTable> primeNgTableList = primeNgTableRepository.findAll();
        assertThat(primeNgTableList).hasSize(databaseSizeBeforeCreate + 1);
        PrimeNgTable testPrimeNgTable = primeNgTableList.get(primeNgTableList.size() - 1);
        assertThat(testPrimeNgTable.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testPrimeNgTable.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPrimeNgTable.getFloatNumber()).isEqualTo(DEFAULT_FLOAT_NUMBER);
        assertThat(testPrimeNgTable.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPrimeNgTable.getZoneDate()).isEqualTo(DEFAULT_ZONE_DATE);
        assertThat(testPrimeNgTable.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPrimeNgTable.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testPrimeNgTable.getLongText()).isEqualTo(DEFAULT_LONG_TEXT);
    }

    @Test
    @Transactional
    void createPrimeNgTableWithExistingId() throws Exception {
        // Create the PrimeNgTable with an existing ID
        primeNgTable.setId(1L);
        PrimeNgTableDTO primeNgTableDTO = primeNgTableMapper.toDto(primeNgTable);

        int databaseSizeBeforeCreate = primeNgTableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrimeNgTableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(primeNgTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrimeNgTable in the database
        List<PrimeNgTable> primeNgTableList = primeNgTableRepository.findAll();
        assertThat(primeNgTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrimeNgTables() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList
        restPrimeNgTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(primeNgTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].floatNumber").value(hasItem(DEFAULT_FLOAT_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].zoneDate").value(hasItem(sameInstant(DEFAULT_ZONE_DATE))))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].longText").value(hasItem(DEFAULT_LONG_TEXT.toString())));
    }

    @Test
    @Transactional
    void getPrimeNgTable() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get the primeNgTable
        restPrimeNgTableMockMvc
            .perform(get(ENTITY_API_URL_ID, primeNgTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(primeNgTable.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.floatNumber").value(DEFAULT_FLOAT_NUMBER.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.zoneDate").value(sameInstant(DEFAULT_ZONE_DATE)))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.longText").value(DEFAULT_LONG_TEXT.toString()));
    }

    @Test
    @Transactional
    void getPrimeNgTablesByIdFiltering() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        Long id = primeNgTable.getId();

        defaultPrimeNgTableShouldBeFound("id.equals=" + id);
        defaultPrimeNgTableShouldNotBeFound("id.notEquals=" + id);

        defaultPrimeNgTableShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrimeNgTableShouldNotBeFound("id.greaterThan=" + id);

        defaultPrimeNgTableShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrimeNgTableShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where text equals to DEFAULT_TEXT
        defaultPrimeNgTableShouldBeFound("text.equals=" + DEFAULT_TEXT);

        // Get all the primeNgTableList where text equals to UPDATED_TEXT
        defaultPrimeNgTableShouldNotBeFound("text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByTextIsInShouldWork() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where text in DEFAULT_TEXT or UPDATED_TEXT
        defaultPrimeNgTableShouldBeFound("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT);

        // Get all the primeNgTableList where text equals to UPDATED_TEXT
        defaultPrimeNgTableShouldNotBeFound("text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where text is not null
        defaultPrimeNgTableShouldBeFound("text.specified=true");

        // Get all the primeNgTableList where text is null
        defaultPrimeNgTableShouldNotBeFound("text.specified=false");
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByTextContainsSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where text contains DEFAULT_TEXT
        defaultPrimeNgTableShouldBeFound("text.contains=" + DEFAULT_TEXT);

        // Get all the primeNgTableList where text contains UPDATED_TEXT
        defaultPrimeNgTableShouldNotBeFound("text.contains=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByTextNotContainsSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where text does not contain DEFAULT_TEXT
        defaultPrimeNgTableShouldNotBeFound("text.doesNotContain=" + DEFAULT_TEXT);

        // Get all the primeNgTableList where text does not contain UPDATED_TEXT
        defaultPrimeNgTableShouldBeFound("text.doesNotContain=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where number equals to DEFAULT_NUMBER
        defaultPrimeNgTableShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the primeNgTableList where number equals to UPDATED_NUMBER
        defaultPrimeNgTableShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultPrimeNgTableShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the primeNgTableList where number equals to UPDATED_NUMBER
        defaultPrimeNgTableShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where number is not null
        defaultPrimeNgTableShouldBeFound("number.specified=true");

        // Get all the primeNgTableList where number is null
        defaultPrimeNgTableShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where number is greater than or equal to DEFAULT_NUMBER
        defaultPrimeNgTableShouldBeFound("number.greaterThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the primeNgTableList where number is greater than or equal to UPDATED_NUMBER
        defaultPrimeNgTableShouldNotBeFound("number.greaterThanOrEqual=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where number is less than or equal to DEFAULT_NUMBER
        defaultPrimeNgTableShouldBeFound("number.lessThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the primeNgTableList where number is less than or equal to SMALLER_NUMBER
        defaultPrimeNgTableShouldNotBeFound("number.lessThanOrEqual=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where number is less than DEFAULT_NUMBER
        defaultPrimeNgTableShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the primeNgTableList where number is less than UPDATED_NUMBER
        defaultPrimeNgTableShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where number is greater than DEFAULT_NUMBER
        defaultPrimeNgTableShouldNotBeFound("number.greaterThan=" + DEFAULT_NUMBER);

        // Get all the primeNgTableList where number is greater than SMALLER_NUMBER
        defaultPrimeNgTableShouldBeFound("number.greaterThan=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByFloatNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where floatNumber equals to DEFAULT_FLOAT_NUMBER
        defaultPrimeNgTableShouldBeFound("floatNumber.equals=" + DEFAULT_FLOAT_NUMBER);

        // Get all the primeNgTableList where floatNumber equals to UPDATED_FLOAT_NUMBER
        defaultPrimeNgTableShouldNotBeFound("floatNumber.equals=" + UPDATED_FLOAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByFloatNumberIsInShouldWork() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where floatNumber in DEFAULT_FLOAT_NUMBER or UPDATED_FLOAT_NUMBER
        defaultPrimeNgTableShouldBeFound("floatNumber.in=" + DEFAULT_FLOAT_NUMBER + "," + UPDATED_FLOAT_NUMBER);

        // Get all the primeNgTableList where floatNumber equals to UPDATED_FLOAT_NUMBER
        defaultPrimeNgTableShouldNotBeFound("floatNumber.in=" + UPDATED_FLOAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByFloatNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where floatNumber is not null
        defaultPrimeNgTableShouldBeFound("floatNumber.specified=true");

        // Get all the primeNgTableList where floatNumber is null
        defaultPrimeNgTableShouldNotBeFound("floatNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByFloatNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where floatNumber is greater than or equal to DEFAULT_FLOAT_NUMBER
        defaultPrimeNgTableShouldBeFound("floatNumber.greaterThanOrEqual=" + DEFAULT_FLOAT_NUMBER);

        // Get all the primeNgTableList where floatNumber is greater than or equal to UPDATED_FLOAT_NUMBER
        defaultPrimeNgTableShouldNotBeFound("floatNumber.greaterThanOrEqual=" + UPDATED_FLOAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByFloatNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where floatNumber is less than or equal to DEFAULT_FLOAT_NUMBER
        defaultPrimeNgTableShouldBeFound("floatNumber.lessThanOrEqual=" + DEFAULT_FLOAT_NUMBER);

        // Get all the primeNgTableList where floatNumber is less than or equal to SMALLER_FLOAT_NUMBER
        defaultPrimeNgTableShouldNotBeFound("floatNumber.lessThanOrEqual=" + SMALLER_FLOAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByFloatNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where floatNumber is less than DEFAULT_FLOAT_NUMBER
        defaultPrimeNgTableShouldNotBeFound("floatNumber.lessThan=" + DEFAULT_FLOAT_NUMBER);

        // Get all the primeNgTableList where floatNumber is less than UPDATED_FLOAT_NUMBER
        defaultPrimeNgTableShouldBeFound("floatNumber.lessThan=" + UPDATED_FLOAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByFloatNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where floatNumber is greater than DEFAULT_FLOAT_NUMBER
        defaultPrimeNgTableShouldNotBeFound("floatNumber.greaterThan=" + DEFAULT_FLOAT_NUMBER);

        // Get all the primeNgTableList where floatNumber is greater than SMALLER_FLOAT_NUMBER
        defaultPrimeNgTableShouldBeFound("floatNumber.greaterThan=" + SMALLER_FLOAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where date equals to DEFAULT_DATE
        defaultPrimeNgTableShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the primeNgTableList where date equals to UPDATED_DATE
        defaultPrimeNgTableShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where date in DEFAULT_DATE or UPDATED_DATE
        defaultPrimeNgTableShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the primeNgTableList where date equals to UPDATED_DATE
        defaultPrimeNgTableShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where date is not null
        defaultPrimeNgTableShouldBeFound("date.specified=true");

        // Get all the primeNgTableList where date is null
        defaultPrimeNgTableShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where date is greater than or equal to DEFAULT_DATE
        defaultPrimeNgTableShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the primeNgTableList where date is greater than or equal to UPDATED_DATE
        defaultPrimeNgTableShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where date is less than or equal to DEFAULT_DATE
        defaultPrimeNgTableShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the primeNgTableList where date is less than or equal to SMALLER_DATE
        defaultPrimeNgTableShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where date is less than DEFAULT_DATE
        defaultPrimeNgTableShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the primeNgTableList where date is less than UPDATED_DATE
        defaultPrimeNgTableShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where date is greater than DEFAULT_DATE
        defaultPrimeNgTableShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the primeNgTableList where date is greater than SMALLER_DATE
        defaultPrimeNgTableShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByZoneDateIsEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where zoneDate equals to DEFAULT_ZONE_DATE
        defaultPrimeNgTableShouldBeFound("zoneDate.equals=" + DEFAULT_ZONE_DATE);

        // Get all the primeNgTableList where zoneDate equals to UPDATED_ZONE_DATE
        defaultPrimeNgTableShouldNotBeFound("zoneDate.equals=" + UPDATED_ZONE_DATE);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByZoneDateIsInShouldWork() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where zoneDate in DEFAULT_ZONE_DATE or UPDATED_ZONE_DATE
        defaultPrimeNgTableShouldBeFound("zoneDate.in=" + DEFAULT_ZONE_DATE + "," + UPDATED_ZONE_DATE);

        // Get all the primeNgTableList where zoneDate equals to UPDATED_ZONE_DATE
        defaultPrimeNgTableShouldNotBeFound("zoneDate.in=" + UPDATED_ZONE_DATE);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByZoneDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where zoneDate is not null
        defaultPrimeNgTableShouldBeFound("zoneDate.specified=true");

        // Get all the primeNgTableList where zoneDate is null
        defaultPrimeNgTableShouldNotBeFound("zoneDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByZoneDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where zoneDate is greater than or equal to DEFAULT_ZONE_DATE
        defaultPrimeNgTableShouldBeFound("zoneDate.greaterThanOrEqual=" + DEFAULT_ZONE_DATE);

        // Get all the primeNgTableList where zoneDate is greater than or equal to UPDATED_ZONE_DATE
        defaultPrimeNgTableShouldNotBeFound("zoneDate.greaterThanOrEqual=" + UPDATED_ZONE_DATE);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByZoneDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where zoneDate is less than or equal to DEFAULT_ZONE_DATE
        defaultPrimeNgTableShouldBeFound("zoneDate.lessThanOrEqual=" + DEFAULT_ZONE_DATE);

        // Get all the primeNgTableList where zoneDate is less than or equal to SMALLER_ZONE_DATE
        defaultPrimeNgTableShouldNotBeFound("zoneDate.lessThanOrEqual=" + SMALLER_ZONE_DATE);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByZoneDateIsLessThanSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where zoneDate is less than DEFAULT_ZONE_DATE
        defaultPrimeNgTableShouldNotBeFound("zoneDate.lessThan=" + DEFAULT_ZONE_DATE);

        // Get all the primeNgTableList where zoneDate is less than UPDATED_ZONE_DATE
        defaultPrimeNgTableShouldBeFound("zoneDate.lessThan=" + UPDATED_ZONE_DATE);
    }

    @Test
    @Transactional
    void getAllPrimeNgTablesByZoneDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        // Get all the primeNgTableList where zoneDate is greater than DEFAULT_ZONE_DATE
        defaultPrimeNgTableShouldNotBeFound("zoneDate.greaterThan=" + DEFAULT_ZONE_DATE);

        // Get all the primeNgTableList where zoneDate is greater than SMALLER_ZONE_DATE
        defaultPrimeNgTableShouldBeFound("zoneDate.greaterThan=" + SMALLER_ZONE_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrimeNgTableShouldBeFound(String filter) throws Exception {
        restPrimeNgTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(primeNgTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].floatNumber").value(hasItem(DEFAULT_FLOAT_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].zoneDate").value(hasItem(sameInstant(DEFAULT_ZONE_DATE))))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].longText").value(hasItem(DEFAULT_LONG_TEXT.toString())));

        // Check, that the count call also returns 1
        restPrimeNgTableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrimeNgTableShouldNotBeFound(String filter) throws Exception {
        restPrimeNgTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrimeNgTableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrimeNgTable() throws Exception {
        // Get the primeNgTable
        restPrimeNgTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrimeNgTable() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        int databaseSizeBeforeUpdate = primeNgTableRepository.findAll().size();

        // Update the primeNgTable
        PrimeNgTable updatedPrimeNgTable = primeNgTableRepository.findById(primeNgTable.getId()).get();
        // Disconnect from session so that the updates on updatedPrimeNgTable are not directly saved in db
        em.detach(updatedPrimeNgTable);
        updatedPrimeNgTable
            .text(UPDATED_TEXT)
            .number(UPDATED_NUMBER)
            .floatNumber(UPDATED_FLOAT_NUMBER)
            .date(UPDATED_DATE)
            .zoneDate(UPDATED_ZONE_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .longText(UPDATED_LONG_TEXT);
        PrimeNgTableDTO primeNgTableDTO = primeNgTableMapper.toDto(updatedPrimeNgTable);

        restPrimeNgTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, primeNgTableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(primeNgTableDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrimeNgTable in the database
        List<PrimeNgTable> primeNgTableList = primeNgTableRepository.findAll();
        assertThat(primeNgTableList).hasSize(databaseSizeBeforeUpdate);
        PrimeNgTable testPrimeNgTable = primeNgTableList.get(primeNgTableList.size() - 1);
        assertThat(testPrimeNgTable.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testPrimeNgTable.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPrimeNgTable.getFloatNumber()).isEqualTo(UPDATED_FLOAT_NUMBER);
        assertThat(testPrimeNgTable.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPrimeNgTable.getZoneDate()).isEqualTo(UPDATED_ZONE_DATE);
        assertThat(testPrimeNgTable.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPrimeNgTable.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testPrimeNgTable.getLongText()).isEqualTo(UPDATED_LONG_TEXT);
    }

    @Test
    @Transactional
    void putNonExistingPrimeNgTable() throws Exception {
        int databaseSizeBeforeUpdate = primeNgTableRepository.findAll().size();
        primeNgTable.setId(count.incrementAndGet());

        // Create the PrimeNgTable
        PrimeNgTableDTO primeNgTableDTO = primeNgTableMapper.toDto(primeNgTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrimeNgTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, primeNgTableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(primeNgTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrimeNgTable in the database
        List<PrimeNgTable> primeNgTableList = primeNgTableRepository.findAll();
        assertThat(primeNgTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrimeNgTable() throws Exception {
        int databaseSizeBeforeUpdate = primeNgTableRepository.findAll().size();
        primeNgTable.setId(count.incrementAndGet());

        // Create the PrimeNgTable
        PrimeNgTableDTO primeNgTableDTO = primeNgTableMapper.toDto(primeNgTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrimeNgTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(primeNgTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrimeNgTable in the database
        List<PrimeNgTable> primeNgTableList = primeNgTableRepository.findAll();
        assertThat(primeNgTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrimeNgTable() throws Exception {
        int databaseSizeBeforeUpdate = primeNgTableRepository.findAll().size();
        primeNgTable.setId(count.incrementAndGet());

        // Create the PrimeNgTable
        PrimeNgTableDTO primeNgTableDTO = primeNgTableMapper.toDto(primeNgTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrimeNgTableMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(primeNgTableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrimeNgTable in the database
        List<PrimeNgTable> primeNgTableList = primeNgTableRepository.findAll();
        assertThat(primeNgTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrimeNgTableWithPatch() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        int databaseSizeBeforeUpdate = primeNgTableRepository.findAll().size();

        // Update the primeNgTable using partial update
        PrimeNgTable partialUpdatedPrimeNgTable = new PrimeNgTable();
        partialUpdatedPrimeNgTable.setId(primeNgTable.getId());

        partialUpdatedPrimeNgTable
            .text(UPDATED_TEXT)
            .number(UPDATED_NUMBER)
            .floatNumber(UPDATED_FLOAT_NUMBER)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .longText(UPDATED_LONG_TEXT);

        restPrimeNgTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrimeNgTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrimeNgTable))
            )
            .andExpect(status().isOk());

        // Validate the PrimeNgTable in the database
        List<PrimeNgTable> primeNgTableList = primeNgTableRepository.findAll();
        assertThat(primeNgTableList).hasSize(databaseSizeBeforeUpdate);
        PrimeNgTable testPrimeNgTable = primeNgTableList.get(primeNgTableList.size() - 1);
        assertThat(testPrimeNgTable.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testPrimeNgTable.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPrimeNgTable.getFloatNumber()).isEqualTo(UPDATED_FLOAT_NUMBER);
        assertThat(testPrimeNgTable.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPrimeNgTable.getZoneDate()).isEqualTo(DEFAULT_ZONE_DATE);
        assertThat(testPrimeNgTable.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPrimeNgTable.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testPrimeNgTable.getLongText()).isEqualTo(UPDATED_LONG_TEXT);
    }

    @Test
    @Transactional
    void fullUpdatePrimeNgTableWithPatch() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        int databaseSizeBeforeUpdate = primeNgTableRepository.findAll().size();

        // Update the primeNgTable using partial update
        PrimeNgTable partialUpdatedPrimeNgTable = new PrimeNgTable();
        partialUpdatedPrimeNgTable.setId(primeNgTable.getId());

        partialUpdatedPrimeNgTable
            .text(UPDATED_TEXT)
            .number(UPDATED_NUMBER)
            .floatNumber(UPDATED_FLOAT_NUMBER)
            .date(UPDATED_DATE)
            .zoneDate(UPDATED_ZONE_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .longText(UPDATED_LONG_TEXT);

        restPrimeNgTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrimeNgTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrimeNgTable))
            )
            .andExpect(status().isOk());

        // Validate the PrimeNgTable in the database
        List<PrimeNgTable> primeNgTableList = primeNgTableRepository.findAll();
        assertThat(primeNgTableList).hasSize(databaseSizeBeforeUpdate);
        PrimeNgTable testPrimeNgTable = primeNgTableList.get(primeNgTableList.size() - 1);
        assertThat(testPrimeNgTable.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testPrimeNgTable.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPrimeNgTable.getFloatNumber()).isEqualTo(UPDATED_FLOAT_NUMBER);
        assertThat(testPrimeNgTable.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPrimeNgTable.getZoneDate()).isEqualTo(UPDATED_ZONE_DATE);
        assertThat(testPrimeNgTable.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPrimeNgTable.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testPrimeNgTable.getLongText()).isEqualTo(UPDATED_LONG_TEXT);
    }

    @Test
    @Transactional
    void patchNonExistingPrimeNgTable() throws Exception {
        int databaseSizeBeforeUpdate = primeNgTableRepository.findAll().size();
        primeNgTable.setId(count.incrementAndGet());

        // Create the PrimeNgTable
        PrimeNgTableDTO primeNgTableDTO = primeNgTableMapper.toDto(primeNgTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrimeNgTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, primeNgTableDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(primeNgTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrimeNgTable in the database
        List<PrimeNgTable> primeNgTableList = primeNgTableRepository.findAll();
        assertThat(primeNgTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrimeNgTable() throws Exception {
        int databaseSizeBeforeUpdate = primeNgTableRepository.findAll().size();
        primeNgTable.setId(count.incrementAndGet());

        // Create the PrimeNgTable
        PrimeNgTableDTO primeNgTableDTO = primeNgTableMapper.toDto(primeNgTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrimeNgTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(primeNgTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrimeNgTable in the database
        List<PrimeNgTable> primeNgTableList = primeNgTableRepository.findAll();
        assertThat(primeNgTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrimeNgTable() throws Exception {
        int databaseSizeBeforeUpdate = primeNgTableRepository.findAll().size();
        primeNgTable.setId(count.incrementAndGet());

        // Create the PrimeNgTable
        PrimeNgTableDTO primeNgTableDTO = primeNgTableMapper.toDto(primeNgTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrimeNgTableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(primeNgTableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrimeNgTable in the database
        List<PrimeNgTable> primeNgTableList = primeNgTableRepository.findAll();
        assertThat(primeNgTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrimeNgTable() throws Exception {
        // Initialize the database
        primeNgTableRepository.saveAndFlush(primeNgTable);

        int databaseSizeBeforeDelete = primeNgTableRepository.findAll().size();

        // Delete the primeNgTable
        restPrimeNgTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, primeNgTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrimeNgTable> primeNgTableList = primeNgTableRepository.findAll();
        assertThat(primeNgTableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
