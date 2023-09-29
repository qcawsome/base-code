package com.qc.demo.service;

import com.qc.demo.domain.SampleUuid;
import com.qc.demo.repository.SampleUuidRepository;
import com.qc.demo.service.dto.SampleUuidDTO;
import com.qc.demo.service.mapper.SampleUuidMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SampleUuid}.
 */
@Service
@Transactional
public class SampleUuidService {

    private final Logger log = LoggerFactory.getLogger(SampleUuidService.class);

    private final SampleUuidRepository sampleUuidRepository;

    private final SampleUuidMapper sampleUuidMapper;

    public SampleUuidService(SampleUuidRepository sampleUuidRepository, SampleUuidMapper sampleUuidMapper) {
        this.sampleUuidRepository = sampleUuidRepository;
        this.sampleUuidMapper = sampleUuidMapper;
    }

    /**
     * Save a sampleUuid.
     *
     * @param sampleUuidDTO the entity to save.
     * @return the persisted entity.
     */
    public SampleUuidDTO save(SampleUuidDTO sampleUuidDTO) {
        log.debug("Request to save SampleUuid : {}", sampleUuidDTO);
        SampleUuid sampleUuid = sampleUuidMapper.toEntity(sampleUuidDTO);
        sampleUuid = sampleUuidRepository.save(sampleUuid);
        return sampleUuidMapper.toDto(sampleUuid);
    }

    /**
     * Update a sampleUuid.
     *
     * @param sampleUuidDTO the entity to save.
     * @return the persisted entity.
     */
    public SampleUuidDTO update(SampleUuidDTO sampleUuidDTO) {
        log.debug("Request to update SampleUuid : {}", sampleUuidDTO);
        SampleUuid sampleUuid = sampleUuidMapper.toEntity(sampleUuidDTO);
        sampleUuid = sampleUuidRepository.save(sampleUuid);
        return sampleUuidMapper.toDto(sampleUuid);
    }

    /**
     * Partially update a sampleUuid.
     *
     * @param sampleUuidDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SampleUuidDTO> partialUpdate(SampleUuidDTO sampleUuidDTO) {
        log.debug("Request to partially update SampleUuid : {}", sampleUuidDTO);

        return sampleUuidRepository
            .findById(sampleUuidDTO.getId())
            .map(existingSampleUuid -> {
                sampleUuidMapper.partialUpdate(existingSampleUuid, sampleUuidDTO);

                return existingSampleUuid;
            })
            .map(sampleUuidRepository::save)
            .map(sampleUuidMapper::toDto);
    }

    /**
     * Get all the sampleUuids.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SampleUuidDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SampleUuids");
        return sampleUuidRepository.findAll(pageable).map(sampleUuidMapper::toDto);
    }

    /**
     * Get one sampleUuid by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SampleUuidDTO> findOne(Long id) {
        log.debug("Request to get SampleUuid : {}", id);
        return sampleUuidRepository.findById(id).map(sampleUuidMapper::toDto);
    }

    /**
     * Delete the sampleUuid by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SampleUuid : {}", id);
        sampleUuidRepository.deleteById(id);
    }
}
