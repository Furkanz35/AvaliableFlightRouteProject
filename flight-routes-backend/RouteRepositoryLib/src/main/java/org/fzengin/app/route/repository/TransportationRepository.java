package org.fzengin.app.route.repository;

import org.fzengin.app.route.repository.entity.Location;
import org.fzengin.app.route.repository.entity.Transportation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportationRepository extends JpaRepository<Transportation, Integer> {
    List<Transportation> findAllByOriginLocationName(String originLocationName);
    Optional<Transportation> findTransportationById(Integer transportationId);
    Optional<Transportation> findTransportationByOriginLocationAndDestinationLocationAndTransportationType(Location originLocation, Location destinationLocation, String transportationType);

    void deleteTransportationById(int id);
}