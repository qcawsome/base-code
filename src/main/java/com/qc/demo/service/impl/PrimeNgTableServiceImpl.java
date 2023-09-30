package com.qc.demo.service.impl;

import com.qc.demo.domain.PrimeNgTable;
import com.qc.demo.repository.PrimeNgTableRepository;
import com.qc.demo.service.PrimeNgTableService;
import com.qc.demo.service.dto.PrimeNgTableDTO;
import com.qc.demo.service.mapper.PrimeNgTableMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PrimeNgTable}.
 */
@Service
@Transactional
public class PrimeNgTableServiceImpl implements PrimeNgTableService {

    private final Logger log = LoggerFactory.getLogger(PrimeNgTableServiceImpl.class);

    private final PrimeNgTableRepository primeNgTableRepository;

    private final PrimeNgTableMapper primeNgTableMapper;

    public PrimeNgTableServiceImpl(PrimeNgTableRepository primeNgTableRepository, PrimeNgTableMapper primeNgTableMapper) {
        this.primeNgTableRepository = primeNgTableRepository;
        this.primeNgTableMapper = primeNgTableMapper;
    }

    @Override
    public PrimeNgTableDTO save(PrimeNgTableDTO primeNgTableDTO) {
        log.debug("Request to save PrimeNgTable : {}", primeNgTableDTO);
        PrimeNgTable primeNgTable = primeNgTableMapper.toEntity(primeNgTableDTO);
        primeNgTable = primeNgTableRepository.save(primeNgTable);
        return primeNgTableMapper.toDto(primeNgTable);
    }

    @Override
    public PrimeNgTableDTO update(PrimeNgTableDTO primeNgTableDTO) {
        log.debug("Request to update PrimeNgTable : {}", primeNgTableDTO);
        PrimeNgTable primeNgTable = primeNgTableMapper.toEntity(primeNgTableDTO);
        primeNgTable = primeNgTableRepository.save(primeNgTable);
        return primeNgTableMapper.toDto(primeNgTable);
    }

    @Override
    public Optional<PrimeNgTableDTO> partialUpdate(PrimeNgTableDTO primeNgTableDTO) {
        log.debug("Request to partially update PrimeNgTable : {}", primeNgTableDTO);

        return primeNgTableRepository
            .findById(primeNgTableDTO.getId())
            .map(existingPrimeNgTable -> {
                primeNgTableMapper.partialUpdate(existingPrimeNgTable, primeNgTableDTO);

                return existingPrimeNgTable;
            })
            .map(primeNgTableRepository::save)
            .map(primeNgTableMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrimeNgTableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrimeNgTables");
        return primeNgTableRepository.findAll(pageable).map(primeNgTableMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrimeNgTableDTO> findOne(Long id) {
        log.debug("Request to get PrimeNgTable : {}", id);
        return primeNgTableRepository.findById(id).map(primeNgTableMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrimeNgTable : {}", id);
        primeNgTableRepository.deleteById(id);
    }
}
