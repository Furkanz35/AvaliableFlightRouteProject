package org.fzengin.app.route.repository.dal;
import org.fzengin.app.route.repository.LocationRepository;
import org.fzengin.app.route.repository.entity.Location;
import org.fzengin.app.route.repository.exception.RepositoryException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RouteAppLocationRepositoryHelper {
    private final LocationRepository locationRepository;

    public RouteAppLocationRepositoryHelper(LocationRepository locationRepository)
    {
        this.locationRepository = locationRepository;
    }

    public void saveLocation(Location location)
    {
        try {
            locationRepository.save(location);
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.saveLocation", ex);
        }
    }

    public Optional<Location> findLocationByName(String locationName)
    {
        try {
            return locationRepository.findLocationByNameIgnoreCase(locationName.toUpperCase());
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.findLocationByLocationName", ex);
        }
    }


    public List<Location> findAllLocations()
    {
        try {
            return locationRepository.findAll();
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.findAllLocations", ex);
        }
    }

    public boolean existsLocationByLocationName(String locationName)
    {
        try {
            return locationRepository.existsByNameIgnoreCase(locationName.toUpperCase());
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.existsLocationByLocationName", ex);
        }
    }


    public void deleteLocationByLocationName(String locationName)
    {
        try {
           locationRepository.deleteLocationByNameIgnoreCase(locationName.toUpperCase());
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.deleteLocationByLocationName", ex);
        }
    }


}