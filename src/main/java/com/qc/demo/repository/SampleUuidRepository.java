package com.qc.demo.repository;

import com.qc.demo.domain.SampleUuid;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SampleUuid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SampleUuidRepository extends JpaRepository<SampleUuid, Long>, JpaSpecificationExecutor<SampleUuid> {}
