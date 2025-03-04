package org.fzengin.app.route.repository.dal;
import org.fzengin.app.route.repository.TransportationRepository;
import org.fzengin.app.route.repository.entity.Location;
import org.fzengin.app.route.repository.entity.Transportation;
import org.fzengin.app.route.repository.exception.RepositoryException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RouteAppTransportationRepositoryHelper {
    private final TransportationRepository transportationRepository;

    public RouteAppTransportationRepositoryHelper(TransportationRepository transportationRepository)
    {
        this.transportationRepository = transportationRepository;
    }
    public void saveTransportation(Transportation transportation)
    {
        try {
            transportationRepository.save(transportation);
        }
        catch (Throwable throwable) {
            throw new RepositoryException("RouteRepositoryHelper.saveTransportation", throwable);
        }
    }

    public List<Transportation> findTransportationsByOriginLocationName(String originLocationName)
    {
        try {
            return transportationRepository.findAllByOriginLocationName(originLocationName.toUpperCase());
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.findTransportationByOriginLocationName", ex);
        }
    }

    public List<Transportation> findAllTransportations()
    {
        try {
            return transportationRepository.findAll();
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.findAllTransportations", ex);
        }
    }

    public Transportation findTransportationById(Integer transportationId)
    {
        try {
            return transportationRepository.findTransportationById(transportationId).orElse(null);
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.findTransportationById", ex);
        }
    }

    public void deleteTransportationById(Integer transportationId)
    {
        try {
            transportationRepository.deleteTransportationById(transportationId);
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.deleteTransportationById", ex);
        }
    }

    public Optional<Transportation> findTransportationByOriginLocationAndDestinationLocationAndTransportationType(Location originLocation, Location destinationLocation, String transportationType)
    {
        try {
            return transportationRepository.findTransportationByOriginLocationAndDestinationLocationAndTransportationType(originLocation, destinationLocation, transportationType);
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.findTransportationByOriginLocationAndDestinationLocationAndTransportationType", ex);
        }
    }

}