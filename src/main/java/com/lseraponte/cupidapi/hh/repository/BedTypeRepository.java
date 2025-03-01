package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.BedType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BedTypeRepository extends JpaRepository<BedType, Integer> {

    @Query("SELECT DISTINCT b FROM BedType b JOIN b.translations t WHERE t.bedTypeName = :bedTypeName")
    Optional<BedType> findByBedTypeName(@Param("bedTypeName") String bedTypeName);

    @Query("SELECT b FROM BedType b JOIN b.translations t WHERE t.bedTypeName = :bedTypeName AND t.bedSize = :bedSize")
    Optional<BedType> findByBedTypeAndBedSize(@Param("bedTypeName") String bedTypeName, @Param("bedSize") String bedSize);

}
