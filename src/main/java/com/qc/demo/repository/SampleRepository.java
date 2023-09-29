package com.qc.demo.repository;

import com.qc.demo.domain.Sample;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sample entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SampleRepository extends JpaRepository<Sample, Long>, JpaSpecificationExecutor<Sample> {}
