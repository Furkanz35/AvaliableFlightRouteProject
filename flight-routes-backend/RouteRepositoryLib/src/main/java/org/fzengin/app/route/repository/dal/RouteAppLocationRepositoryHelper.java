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

    public Location saveLocation(Location location)
    {
        try {
           return locationRepository.save(location);
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.saveLocation", ex);
        }
    }

    public Optional<Location> findLocationById(Integer id)
    {
        try {
            return locationRepository.findLocationById(id);
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.findLocationById", ex);
        }
    }

    public Optional<Location> findLocationByName(String locationName)
    {
        try {
            return locationRepository.findLocationByNameIgnoreCase(locationName.toUpperCase());
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.findLocationByName", ex);
        }
    }

    public List<Location> findLocationByNameOrLocationCode(String name, String locationCode)
    {
        try {
            return locationRepository.findByNameOrLocationCode(name, locationCode);
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.findLocationByNameOrLocationCode", ex);
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

    public boolean existsLocationByLocationId(Integer id)
    {
        try {
            return locationRepository.existsById(id);
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.existsLocationByLocationName", ex);
        }
    }


    public void deleteLocationById(Integer id)
    {
        try {
           locationRepository.deleteLocationById(id);
        }
        catch (Throwable ex) {
            throw new RepositoryException("RouteRepositoryHelper.deleteLocationById", ex);
        }
    }


}