package com.qc.demo.service;

import com.qc.demo.domain.*; // for static metamodels
import com.qc.demo.domain.SampleUuid;
import com.qc.demo.repository.SampleUuidRepository;
import com.qc.demo.service.criteria.SampleUuidCriteria;
import com.qc.demo.service.dto.SampleUuidDTO;
import com.qc.demo.service.mapper.SampleUuidMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SampleUuid} entities in the database.
 * The main input is a {@link SampleUuidCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SampleUuidDTO} or a {@link Page} of {@link SampleUuidDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SampleUuidQueryService extends QueryService<SampleUuid> {

    private final Logger log = LoggerFactory.getLogger(SampleUuidQueryService.class);

    private final SampleUuidRepository sampleUuidRepository;

    private final SampleUuidMapper sampleUuidMapper;

    public SampleUuidQueryService(SampleUuidRepository sampleUuidRepository, SampleUuidMapper sampleUuidMapper) {
        this.sampleUuidRepository = sampleUuidRepository;
        this.sampleUuidMapper = sampleUuidMapper;
    }

    /**
     * Return a {@link List} of {@link SampleUuidDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SampleUuidDTO> findByCriteria(SampleUuidCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SampleUuid> specification = createSpecification(criteria);
        return sampleUuidMapper.toDto(sampleUuidRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SampleUuidDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SampleUuidDTO> findByCriteria(SampleUuidCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SampleUuid> specification = createSpecification(criteria);
        return sampleUuidRepository.findAll(specification, page).map(sampleUuidMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SampleUuidCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SampleUuid> specification = createSpecification(criteria);
        return sampleUuidRepository.count(specification);
    }

    /**
     * Function to convert {@link SampleUuidCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SampleUuid> createSpecification(SampleUuidCriteria criteria) {
        Specification<SampleUuid> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SampleUuid_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), SampleUuid_.uuid));
            }
        }
        return specification;
    }
}
