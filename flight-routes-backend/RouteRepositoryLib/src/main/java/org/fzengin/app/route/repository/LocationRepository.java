package org.fzengin.app.route.repository;

import org.fzengin.app.route.repository.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository()
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findLocationByNameIgnoreCase(@Param("name") String locationName);

    boolean existsByNameIgnoreCase(@Param("name") String locationName);

    void deleteLocationByNameIgnoreCase(@Param("name") String locationName);
}
