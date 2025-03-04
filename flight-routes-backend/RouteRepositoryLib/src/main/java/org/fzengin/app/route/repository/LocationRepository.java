package org.fzengin.app.route.repository;

import org.fzengin.app.route.repository.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository()
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findLocationById(@Param("id") Integer id);

    Optional<Location> findLocationByNameIgnoreCase(@Param("name") String locationName);

    @Query("SELECT l FROM Location l WHERE l.name = :name OR l.locationCode = :locationCode")
    List<Location> findByNameOrLocationCode(@Param("name") String name, @Param("locationCode") String locationCode);

    boolean existsById(@Param("id") Integer id);

    void deleteLocationById(@Param("id") Integer id);
}
