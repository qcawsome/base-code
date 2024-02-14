package com.qc.demo.web.rest;

import com.qc.demo.repository.IpMacRepository;
import com.qc.demo.service.IpMacQueryService;
import com.qc.demo.service.IpMacService;
import com.qc.demo.service.criteria.IpMacCriteria;
import com.qc.demo.service.dto.IpMacDTO;
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
 * REST controller for managing {@link com.qc.demo.domain.IpMac}.
 */
@RestController
@RequestMapping("/api")
public class IpMacResource {

    private final Logger log = LoggerFactory.getLogger(IpMacResource.class);

    private static final String ENTITY_NAME = "ipMac";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IpMacService ipMacService;

    private final IpMacRepository ipMacRepository;

    private final IpMacQueryService ipMacQueryService;

    public IpMacResource(IpMacService ipMacService, IpMacRepository ipMacRepository, IpMacQueryService ipMacQueryService) {
        this.ipMacService = ipMacService;
        this.ipMacRepository = ipMacRepository;
        this.ipMacQueryService = ipMacQueryService;
    }

    /**
     * {@code POST  /ip-macs} : Create a new ipMac.
     *
     * @param ipMacDTO the ipMacDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ipMacDTO, or with status {@code 400 (Bad Request)} if the ipMac has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ip-macs")
    public ResponseEntity<IpMacDTO> createIpMac(@RequestBody IpMacDTO ipMacDTO) throws URISyntaxException {
        log.debug("REST request to save IpMac : {}", ipMacDTO);
        if (ipMacDTO.getId() != null) {
            throw new BadRequestAlertException("A new ipMac cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IpMacDTO result = ipMacService.save(ipMacDTO);
        return ResponseEntity
            .created(new URI("/api/ip-macs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ip-macs/:id} : Updates an existing ipMac.
     *
     * @param id the id of the ipMacDTO to save.
     * @param ipMacDTO the ipMacDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ipMacDTO,
     * or with status {@code 400 (Bad Request)} if the ipMacDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ipMacDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ip-macs/{id}")
    public ResponseEntity<IpMacDTO> updateIpMac(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IpMacDTO ipMacDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IpMac : {}, {}", id, ipMacDTO);
        if (ipMacDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ipMacDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ipMacRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IpMacDTO result = ipMacService.update(ipMacDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ipMacDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ip-macs/:id} : Partial updates given fields of an existing ipMac, field will ignore if it is null
     *
     * @param id the id of the ipMacDTO to save.
     * @param ipMacDTO the ipMacDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ipMacDTO,
     * or with status {@code 400 (Bad Request)} if the ipMacDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ipMacDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ipMacDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ip-macs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IpMacDTO> partialUpdateIpMac(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IpMacDTO ipMacDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IpMac partially : {}, {}", id, ipMacDTO);
        if (ipMacDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ipMacDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ipMacRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IpMacDTO> result = ipMacService.partialUpdate(ipMacDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ipMacDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ip-macs} : get all the ipMacs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ipMacs in body.
     */
    @GetMapping("/ip-macs")
    public ResponseEntity<List<IpMacDTO>> getAllIpMacs(
        IpMacCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get IpMacs by criteria: {}", criteria);
        Page<IpMacDTO> page = ipMacQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ip-macs/count} : count all the ipMacs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ip-macs/count")
    public ResponseEntity<Long> countIpMacs(IpMacCriteria criteria) {
        log.debug("REST request to count IpMacs by criteria: {}", criteria);
        return ResponseEntity.ok().body(ipMacQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ip-macs/:id} : get the "id" ipMac.
     *
     * @param id the id of the ipMacDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ipMacDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ip-macs/{id}")
    public ResponseEntity<IpMacDTO> getIpMac(@PathVariable Long id) {
        log.debug("REST request to get IpMac : {}", id);
        Optional<IpMacDTO> ipMacDTO = ipMacService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ipMacDTO);
    }

    /**
     * {@code DELETE  /ip-macs/:id} : delete the "id" ipMac.
     *
     * @param id the id of the ipMacDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ip-macs/{id}")
    public ResponseEntity<Void> deleteIpMac(@PathVariable Long id) {
        log.debug("REST request to delete IpMac : {}", id);
        ipMacService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
