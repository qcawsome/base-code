package com.qc.demo.service;

import com.qc.demo.domain.*; // for static metamodels
import com.qc.demo.domain.IpMac;
import com.qc.demo.repository.IpMacRepository;
import com.qc.demo.service.criteria.IpMacCriteria;
import com.qc.demo.service.dto.IpMacDTO;
import com.qc.demo.service.mapper.IpMacMapper;
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
 * Service for executing complex queries for {@link IpMac} entities in the database.
 * The main input is a {@link IpMacCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IpMacDTO} or a {@link Page} of {@link IpMacDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IpMacQueryService extends QueryService<IpMac> {

    private final Logger log = LoggerFactory.getLogger(IpMacQueryService.class);

    private final IpMacRepository ipMacRepository;

    private final IpMacMapper ipMacMapper;

    public IpMacQueryService(IpMacRepository ipMacRepository, IpMacMapper ipMacMapper) {
        this.ipMacRepository = ipMacRepository;
        this.ipMacMapper = ipMacMapper;
    }

    /**
     * Return a {@link List} of {@link IpMacDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IpMacDTO> findByCriteria(IpMacCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IpMac> specification = createSpecification(criteria);
        return ipMacMapper.toDto(ipMacRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IpMacDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IpMacDTO> findByCriteria(IpMacCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IpMac> specification = createSpecification(criteria);
        return ipMacRepository.findAll(specification, page).map(ipMacMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IpMacCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IpMac> specification = createSpecification(criteria);
        return ipMacRepository.count(specification);
    }

    /**
     * Function to convert {@link IpMacCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IpMac> createSpecification(IpMacCriteria criteria) {
        Specification<IpMac> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), IpMac_.id));
            }
            if (criteria.getIp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIp(), IpMac_.ip));
            }
            if (criteria.getMac() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMac(), IpMac_.mac));
            }
            if (criteria.getNetworkStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNetworkStatus(), IpMac_.networkStatus));
            }
            if (criteria.getAgentStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAgentStatus(), IpMac_.agentStatus));
            }
            if (criteria.getDeviceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDeviceId(), root -> root.join(IpMac_.device, JoinType.LEFT).get(Device_.id))
                    );
            }
            if (criteria.getDeviceName() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDeviceName(), root -> root.join(IpMac_.device, JoinType.LEFT).get(Device_.name))
                    );
            }
        }
        return specification;
    }
}
