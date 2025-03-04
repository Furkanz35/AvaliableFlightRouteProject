package org.fzengin.app.route.service;

import jakarta.transaction.Transactional;
import org.fzengin.app.route.service.exception.DataServiceExceptionType;
import org.fzengin.app.route.data.dto.LocationDto;
import org.fzengin.app.route.data.mapper.LocationMapper;
import org.fzengin.app.route.repository.dal.RouteAppLocationRepositoryHelper;
import org.fzengin.app.route.repository.entity.Location;
import org.fzengin.app.route.service.exception.DataServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RouteAppLocationDataService {
    private final RouteAppLocationRepositoryHelper routeAppLocationRepositoryHelper;

    private final LocationMapper locationMapper;

    public RouteAppLocationDataService(RouteAppLocationRepositoryHelper routeAppLocationRepositoryHelper, LocationMapper locationMapper) {
        this.routeAppLocationRepositoryHelper = routeAppLocationRepositoryHelper;
        this.locationMapper = locationMapper;
    }

    @Transactional
    public LocationDto saveLocation(LocationDto locationDto) {
        try {
            checkLocationNameAndLocationCodeIsUnique(locationDto);
           return locationMapper.toLocationDto(routeAppLocationRepositoryHelper.saveLocation(locationMapper.toLocation(locationDto)));
        } catch (DataServiceException dataServiceException) {
            throw dataServiceException;
        } catch (Throwable ex) {
            throw new DataServiceException("RouteAppDataService.saveLocation", ex);
        }
    }

    public Optional<LocationDto> findLocationById(Integer id) {
        try {
            if (id == null)
                return Optional.empty();
            Optional<Location> location = routeAppLocationRepositoryHelper.findLocationById(id);
            return location.map(locationMapper::toLocationDto);
        } catch (Throwable ex) {
            throw new DataServiceException("RouteAppDataService.findLocationByLocationName", ex);
        }
    }

    public Optional<LocationDto> findLocationByLocationName(String locationName) {
        try {
            Optional<Location> location = routeAppLocationRepositoryHelper.findLocationByName(locationName);
            return location.map(locationMapper::toLocationDto);
        } catch (Throwable ex) {
            throw new DataServiceException("RouteAppDataService.findLocationByLocationName", ex);
        }
    }

    public List<LocationDto> findAllLocations() {
        try {
            List<Location> locations = routeAppLocationRepositoryHelper.findAllLocations();
            return locations.isEmpty() ? new ArrayList<>() : locations.stream().map(locationMapper::toLocationDto).toList();
        } catch (Throwable ex) {
            throw new DataServiceException("RouteAppDataService.findAllLocations", ex);
        }
    }

    public boolean existsLocationByLocationId(Integer id) {
        try {
            if (id == null)
                return false;
            return routeAppLocationRepositoryHelper.existsLocationByLocationId(id);
        } catch (Throwable ex) {
            throw new DataServiceException("RouteAppDataService.existsLocationByLocationName", ex);
        }
    }

    @Transactional
    public void deleteLocationById(Integer id) {
        try {
            if (id == null)
                return;
            routeAppLocationRepositoryHelper.deleteLocationById(id);
        } catch (Throwable ex) {
            throw new DataServiceException("", ex);
        }
    }

    private void checkLocationNameAndLocationCodeIsUnique(LocationDto locationDto) {
        List<Location> locationsWithUniqueValues = routeAppLocationRepositoryHelper.findLocationByNameOrLocationCode(locationDto.getName(), locationDto.getLocationCode());
       if (locationsWithUniqueValues.isEmpty())
           return;
        if (locationDto.getId() == null || locationsWithUniqueValues.size() > 1 || !Objects.equals(locationsWithUniqueValues.get(0).getId(),
                locationDto.getId())) {
            throw new DataServiceException("Location Name and Location Code fields must be unique",
                    new IllegalArgumentException(), DataServiceExceptionType.ILLEGAL_ARGUMENT);
        }
    }
}
