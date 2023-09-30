package com.qc.demo.service;

import com.qc.demo.service.dto.PrimeNgTableDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.qc.demo.domain.PrimeNgTable}.
 */
public interface PrimeNgTableService {
    /**
     * Save a primeNgTable.
     *
     * @param primeNgTableDTO the entity to save.
     * @return the persisted entity.
     */
    PrimeNgTableDTO save(PrimeNgTableDTO primeNgTableDTO);

    /**
     * Updates a primeNgTable.
     *
     * @param primeNgTableDTO the entity to update.
     * @return the persisted entity.
     */
    PrimeNgTableDTO update(PrimeNgTableDTO primeNgTableDTO);

    /**
     * Partially updates a primeNgTable.
     *
     * @param primeNgTableDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrimeNgTableDTO> partialUpdate(PrimeNgTableDTO primeNgTableDTO);

    /**
     * Get all the primeNgTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrimeNgTableDTO> findAll(Pageable pageable);

    /**
     * Get the "id" primeNgTable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PrimeNgTableDTO> findOne(Long id);

    /**
     * Delete the "id" primeNgTable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
