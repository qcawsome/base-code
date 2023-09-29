package com.qc.demo.web.rest;

import com.qc.demo.repository.SampleUuidRepository;
import com.qc.demo.service.SampleUuidQueryService;
import com.qc.demo.service.SampleUuidService;
import com.qc.demo.service.criteria.SampleUuidCriteria;
import com.qc.demo.service.dto.SampleUuidDTO;
import com.qc.demo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
 * REST controller for managing {@link com.qc.demo.domain.SampleUuid}.
 */
@RestController
@RequestMapping("/api")
public class SampleUuidResource {

    private final Logger log = LoggerFactory.getLogger(SampleUuidResource.class);

    private static final String ENTITY_NAME = "sampleUuid";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SampleUuidService sampleUuidService;

    private final SampleUuidRepository sampleUuidRepository;

    private final SampleUuidQueryService sampleUuidQueryService;

    public SampleUuidResource(
        SampleUuidService sampleUuidService,
        SampleUuidRepository sampleUuidRepository,
        SampleUuidQueryService sampleUuidQueryService
    ) {
        this.sampleUuidService = sampleUuidService;
        this.sampleUuidRepository = sampleUuidRepository;
        this.sampleUuidQueryService = sampleUuidQueryService;
    }

    /**
     * {@code POST  /sample-uuids} : Create a new sampleUuid.
     *
     * @param sampleUuidDTO the sampleUuidDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sampleUuidDTO, or with status {@code 400 (Bad Request)} if the sampleUuid has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sample-uuids")
    public ResponseEntity<SampleUuidDTO> createSampleUuid(@RequestBody SampleUuidDTO sampleUuidDTO) throws URISyntaxException {
        log.debug("REST request to save SampleUuid : {}", sampleUuidDTO);
        if (sampleUuidDTO.getId() != null) {
            throw new BadRequestAlertException("A new sampleUuid cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SampleUuidDTO result = sampleUuidService.save(sampleUuidDTO);
        return ResponseEntity
            .created(new URI("/api/sample-uuids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sample-uuids/:id} : Updates an existing sampleUuid.
     *
     * @param id the id of the sampleUuidDTO to save.
     * @param sampleUuidDTO the sampleUuidDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sampleUuidDTO,
     * or with status {@code 400 (Bad Request)} if the sampleUuidDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sampleUuidDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sample-uuids/{id}")
    public ResponseEntity<SampleUuidDTO> updateSampleUuid(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody SampleUuidDTO sampleUuidDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SampleUuid : {}, {}", id, sampleUuidDTO);
        if (sampleUuidDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sampleUuidDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sampleUuidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SampleUuidDTO result = sampleUuidService.update(sampleUuidDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sampleUuidDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sample-uuids/:id} : Partial updates given fields of an existing sampleUuid, field will ignore if it is null
     *
     * @param id the id of the sampleUuidDTO to save.
     * @param sampleUuidDTO the sampleUuidDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sampleUuidDTO,
     * or with status {@code 400 (Bad Request)} if the sampleUuidDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sampleUuidDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sampleUuidDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sample-uuids/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SampleUuidDTO> partialUpdateSampleUuid(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody SampleUuidDTO sampleUuidDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SampleUuid partially : {}, {}", id, sampleUuidDTO);
        if (sampleUuidDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sampleUuidDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sampleUuidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SampleUuidDTO> result = sampleUuidService.partialUpdate(sampleUuidDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sampleUuidDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sample-uuids} : get all the sampleUuids.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sampleUuids in body.
     */
    @GetMapping("/sample-uuids")
    public ResponseEntity<List<SampleUuidDTO>> getAllSampleUuids(
        SampleUuidCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SampleUuids by criteria: {}", criteria);
        Page<SampleUuidDTO> page = sampleUuidQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sample-uuids/count} : count all the sampleUuids.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sample-uuids/count")
    public ResponseEntity<Long> countSampleUuids(SampleUuidCriteria criteria) {
        log.debug("REST request to count SampleUuids by criteria: {}", criteria);
        return ResponseEntity.ok().body(sampleUuidQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sample-uuids/:id} : get the "id" sampleUuid.
     *
     * @param id the id of the sampleUuidDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sampleUuidDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sample-uuids/{id}")
    public ResponseEntity<SampleUuidDTO> getSampleUuid(@PathVariable UUID id) {
        log.debug("REST request to get SampleUuid : {}", id);
        Optional<SampleUuidDTO> sampleUuidDTO = sampleUuidService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sampleUuidDTO);
    }

    /**
     * {@code DELETE  /sample-uuids/:id} : delete the "id" sampleUuid.
     *
     * @param id the id of the sampleUuidDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sample-uuids/{id}")
    public ResponseEntity<Void> deleteSampleUuid(@PathVariable UUID id) {
        log.debug("REST request to delete SampleUuid : {}", id);
        sampleUuidService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
