package com.qc.demo.service;

import com.qc.demo.domain.*; // for static metamodels
import com.qc.demo.domain.PrimeNgTable;
import com.qc.demo.repository.PrimeNgTableRepository;
import com.qc.demo.service.criteria.PrimeNgTableCriteria;
import com.qc.demo.service.dto.PrimeNgTableDTO;
import com.qc.demo.service.mapper.PrimeNgTableMapper;
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
 * Service for executing complex queries for {@link PrimeNgTable} entities in the database.
 * The main input is a {@link PrimeNgTableCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrimeNgTableDTO} or a {@link Page} of {@link PrimeNgTableDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrimeNgTableQueryService extends QueryService<PrimeNgTable> {

    private final Logger log = LoggerFactory.getLogger(PrimeNgTableQueryService.class);

    private final PrimeNgTableRepository primeNgTableRepository;

    private final PrimeNgTableMapper primeNgTableMapper;

    public PrimeNgTableQueryService(PrimeNgTableRepository primeNgTableRepository, PrimeNgTableMapper primeNgTableMapper) {
        this.primeNgTableRepository = primeNgTableRepository;
        this.primeNgTableMapper = primeNgTableMapper;
    }

    /**
     * Return a {@link List} of {@link PrimeNgTableDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrimeNgTableDTO> findByCriteria(PrimeNgTableCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PrimeNgTable> specification = createSpecification(criteria);
        return primeNgTableMapper.toDto(primeNgTableRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrimeNgTableDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrimeNgTableDTO> findByCriteria(PrimeNgTableCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrimeNgTable> specification = createSpecification(criteria);
        return primeNgTableRepository.findAll(specification, page).map(primeNgTableMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrimeNgTableCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrimeNgTable> specification = createSpecification(criteria);
        return primeNgTableRepository.count(specification);
    }

    /**
     * Function to convert {@link PrimeNgTableCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrimeNgTable> createSpecification(PrimeNgTableCriteria criteria) {
        Specification<PrimeNgTable> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PrimeNgTable_.id));
            }
            if (criteria.getText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getText(), PrimeNgTable_.text));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), PrimeNgTable_.number));
            }
            if (criteria.getFloatNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFloatNumber(), PrimeNgTable_.floatNumber));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), PrimeNgTable_.date));
            }
            if (criteria.getZoneDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getZoneDate(), PrimeNgTable_.zoneDate));
            }
        }
        return specification;
    }
}
