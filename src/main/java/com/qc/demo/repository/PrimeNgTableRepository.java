package com.qc.demo.repository;

import com.qc.demo.domain.PrimeNgTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PrimeNgTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrimeNgTableRepository extends JpaRepository<PrimeNgTable, Long>, JpaSpecificationExecutor<PrimeNgTable> {}
