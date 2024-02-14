package com.qc.demo.service;

import com.qc.demo.domain.IpMac;
import com.qc.demo.repository.IpMacRepository;
import com.qc.demo.service.dto.IpMacDTO;
import com.qc.demo.service.mapper.IpMacMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IpMac}.
 */
@Service
@Transactional
public class IpMacService {

    private final Logger log = LoggerFactory.getLogger(IpMacService.class);

    private final IpMacRepository ipMacRepository;

    private final IpMacMapper ipMacMapper;

    public IpMacService(IpMacRepository ipMacRepository, IpMacMapper ipMacMapper) {
        this.ipMacRepository = ipMacRepository;
        this.ipMacMapper = ipMacMapper;
    }

    /**
     * Save a ipMac.
     *
     * @param ipMacDTO the entity to save.
     * @return the persisted entity.
     */
    public IpMacDTO save(IpMacDTO ipMacDTO) {
        log.debug("Request to save IpMac : {}", ipMacDTO);
        IpMac ipMac = ipMacMapper.toEntity(ipMacDTO);
        ipMac = ipMacRepository.save(ipMac);
        return ipMacMapper.toDto(ipMac);
    }

    /**
     * Update a ipMac.
     *
     * @param ipMacDTO the entity to save.
     * @return the persisted entity.
     */
    public IpMacDTO update(IpMacDTO ipMacDTO) {
        log.debug("Request to update IpMac : {}", ipMacDTO);
        IpMac ipMac = ipMacMapper.toEntity(ipMacDTO);
        ipMac = ipMacRepository.save(ipMac);
        return ipMacMapper.toDto(ipMac);
    }

    /**
     * Partially update a ipMac.
     *
     * @param ipMacDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IpMacDTO> partialUpdate(IpMacDTO ipMacDTO) {
        log.debug("Request to partially update IpMac : {}", ipMacDTO);

        return ipMacRepository
            .findById(ipMacDTO.getId())
            .map(existingIpMac -> {
                ipMacMapper.partialUpdate(existingIpMac, ipMacDTO);

                return existingIpMac;
            })
            .map(ipMacRepository::save)
            .map(ipMacMapper::toDto);
    }

    /**
     * Get all the ipMacs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IpMacDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IpMacs");
        return ipMacRepository.findAll(pageable).map(ipMacMapper::toDto);
    }

    /**
     * Get all the ipMacs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<IpMacDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ipMacRepository.findAllWithEagerRelationships(pageable).map(ipMacMapper::toDto);
    }

    /**
     * Get one ipMac by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IpMacDTO> findOne(Long id) {
        log.debug("Request to get IpMac : {}", id);
        return ipMacRepository.findOneWithEagerRelationships(id).map(ipMacMapper::toDto);
    }

    /**
     * Delete the ipMac by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete IpMac : {}", id);
        ipMacRepository.deleteById(id);
    }
}
