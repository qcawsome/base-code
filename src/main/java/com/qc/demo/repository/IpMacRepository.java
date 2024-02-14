package com.qc.demo.repository;

import com.qc.demo.domain.IpMac;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IpMac entity.
 */
@Repository
public interface IpMacRepository extends JpaRepository<IpMac, Long>, JpaSpecificationExecutor<IpMac> {
    default Optional<IpMac> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<IpMac> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<IpMac> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct ipMac from IpMac ipMac left join fetch ipMac.device",
        countQuery = "select count(distinct ipMac) from IpMac ipMac"
    )
    Page<IpMac> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct ipMac from IpMac ipMac left join fetch ipMac.device")
    List<IpMac> findAllWithToOneRelationships();

    @Query("select ipMac from IpMac ipMac left join fetch ipMac.device where ipMac.id =:id")
    Optional<IpMac> findOneWithToOneRelationships(@Param("id") Long id);
}
