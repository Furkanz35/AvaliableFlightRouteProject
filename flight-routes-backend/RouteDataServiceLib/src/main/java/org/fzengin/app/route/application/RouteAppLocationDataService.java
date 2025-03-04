package org.fzengin.app.route.application;

import jakarta.transaction.Transactional;
import org.fzengin.app.route.data.dto.LocationDto;
import org.fzengin.app.route.data.mapper.LocationMapper;
import org.fzengin.app.route.repository.dal.RouteAppLocationRepositoryHelper;
import org.fzengin.app.route.repository.entity.Location;
import org.fzengin.app.route.application.exception.DataServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public void saveLocation(LocationDto locationDto) {
        try {
            routeAppLocationRepositoryHelper.saveLocation(locationMapper.toLocation(locationDto));
        } catch (Throwable ex) {
            throw new DataServiceException("RouteAppDataService.saveLocation", ex);
        }
    }

    public Optional<LocationDto> findLocationByLocationName(String username) {
        try {
            Optional<Location> location = routeAppLocationRepositoryHelper.findLocationByName(username);
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

    public boolean existsLocationByLocationName(String locationName) {
        try {
            return routeAppLocationRepositoryHelper.existsLocationByLocationName(locationName);
        } catch (Throwable ex) {
            throw new DataServiceException("RouteAppDataService.existsLocationByLocationName", ex);
        }
    }

    @Transactional
    public void deleteLocationByName(String locationName) {
        try {
            routeAppLocationRepositoryHelper.deleteLocationByLocationName(locationName);
        } catch (Throwable ex) {
            throw new DataServiceException("", ex);
        }
    }

}
