package com.qc.demo.web.rest;

import com.qc.demo.repository.PrimeNgTableRepository;
import com.qc.demo.service.PrimeNgTableQueryService;
import com.qc.demo.service.PrimeNgTableService;
import com.qc.demo.service.criteria.PrimeNgTableCriteria;
import com.qc.demo.service.dto.PrimeNgTableDTO;
import com.qc.demo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.qc.demo.domain.PrimeNgTable}.
 */
@RestController
@RequestMapping("/api")
public class PrimeNgTableResource {

    private final Logger log = LoggerFactory.getLogger(PrimeNgTableResource.class);

    private static final String ENTITY_NAME = "primeNgTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrimeNgTableService primeNgTableService;

    private final PrimeNgTableRepository primeNgTableRepository;

    private final PrimeNgTableQueryService primeNgTableQueryService;

    public PrimeNgTableResource(
        PrimeNgTableService primeNgTableService,
        PrimeNgTableRepository primeNgTableRepository,
        PrimeNgTableQueryService primeNgTableQueryService
    ) {
        this.primeNgTableService = primeNgTableService;
        this.primeNgTableRepository = primeNgTableRepository;
        this.primeNgTableQueryService = primeNgTableQueryService;
    }

    /**
     * {@code POST  /prime-ng-tables} : Create a new primeNgTable.
     *
     * @param primeNgTableDTO the primeNgTableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new primeNgTableDTO, or with status {@code 400 (Bad Request)} if the primeNgTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prime-ng-tables")
    public ResponseEntity<PrimeNgTableDTO> createPrimeNgTable(@RequestBody PrimeNgTableDTO primeNgTableDTO) throws URISyntaxException {
        log.debug("REST request to save PrimeNgTable : {}", primeNgTableDTO);
        if (primeNgTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new primeNgTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrimeNgTableDTO result = primeNgTableService.save(primeNgTableDTO);
        return ResponseEntity
            .created(new URI("/api/prime-ng-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prime-ng-tables/:id} : Updates an existing primeNgTable.
     *
     * @param id the id of the primeNgTableDTO to save.
     * @param primeNgTableDTO the primeNgTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated primeNgTableDTO,
     * or with status {@code 400 (Bad Request)} if the primeNgTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the primeNgTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prime-ng-tables/{id}")
    public ResponseEntity<PrimeNgTableDTO> updatePrimeNgTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PrimeNgTableDTO primeNgTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PrimeNgTable : {}, {}", id, primeNgTableDTO);
        if (primeNgTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, primeNgTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!primeNgTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrimeNgTableDTO result = primeNgTableService.update(primeNgTableDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, primeNgTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prime-ng-tables/:id} : Partial updates given fields of an existing primeNgTable, field will ignore if it is null
     *
     * @param id the id of the primeNgTableDTO to save.
     * @param primeNgTableDTO the primeNgTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated primeNgTableDTO,
     * or with status {@code 400 (Bad Request)} if the primeNgTableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the primeNgTableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the primeNgTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prime-ng-tables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrimeNgTableDTO> partialUpdatePrimeNgTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PrimeNgTableDTO primeNgTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PrimeNgTable partially : {}, {}", id, primeNgTableDTO);
        if (primeNgTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, primeNgTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!primeNgTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrimeNgTableDTO> result = primeNgTableService.partialUpdate(primeNgTableDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, primeNgTableDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prime-ng-tables} : get all the primeNgTables.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of primeNgTables in body.
     */
    @GetMapping("/prime-ng-tables")
    public ResponseEntity<List<PrimeNgTableDTO>> getAllPrimeNgTables(
        PrimeNgTableCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PrimeNgTables by criteria: {}", criteria);
        Page<PrimeNgTableDTO> page = primeNgTableQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prime-ng-tables/count} : count all the primeNgTables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prime-ng-tables/count")
    public ResponseEntity<Long> countPrimeNgTables(PrimeNgTableCriteria criteria) {
        log.debug("REST request to count PrimeNgTables by criteria: {}", criteria);
        return ResponseEntity.ok().body(primeNgTableQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prime-ng-tables/:id} : get the "id" primeNgTable.
     *
     * @param id the id of the primeNgTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the primeNgTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prime-ng-tables/{id}")
    public ResponseEntity<PrimeNgTableDTO> getPrimeNgTable(@PathVariable Long id) {
        log.debug("REST request to get PrimeNgTable : {}", id);
        Optional<PrimeNgTableDTO> primeNgTableDTO = primeNgTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(primeNgTableDTO);
    }

    /**
     * {@code DELETE  /prime-ng-tables/:id} : delete the "id" primeNgTable.
     *
     * @param id the id of the primeNgTableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prime-ng-tables/{id}")
    public ResponseEntity<Void> deletePrimeNgTable(@PathVariable Long id) {
        log.debug("REST request to delete PrimeNgTable : {}", id);
        primeNgTableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
