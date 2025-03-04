package org.fzengin.app.route.application;

import org.fzengin.app.route.data.dto.LocationDto;
import org.fzengin.app.route.data.dto.TransportationDto;
import org.fzengin.app.route.data.dto.enums.TransportationType;
import org.fzengin.app.route.data.mapper.LocationMapper;
import org.fzengin.app.route.data.mapper.TransportationMapper;
import org.fzengin.app.route.repository.dal.RouteAppLocationRepositoryHelper;
import org.fzengin.app.route.repository.dal.RouteAppTransportationRepositoryHelper;
import org.fzengin.app.route.repository.entity.Location;
import org.fzengin.app.route.repository.entity.Transportation;
import org.fzengin.app.route.repository.exception.RepositoryException;
import org.fzengin.app.route.application.exception.DataServiceException;
import org.fzengin.app.route.application.exception.DataServiceExceptionType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RouteAppTransportationDataService {
    private final RouteAppTransportationRepositoryHelper routeAppTransportationRepositoryHelper;

    private final RouteAppLocationRepositoryHelper routeAppLocationRepositoryHelper;

    private final TransportationMapper transportationMapper;

    private final LocationMapper locationMapper;

    public RouteAppTransportationDataService(RouteAppTransportationRepositoryHelper routeAppTransportationRepositoryHelper,
                                             RouteAppLocationRepositoryHelper routeAppLocationRepositoryHelper,
                                             TransportationMapper transportationMapper, LocationMapper locationMapper) {
        this.routeAppTransportationRepositoryHelper = routeAppTransportationRepositoryHelper;
        this.routeAppLocationRepositoryHelper = routeAppLocationRepositoryHelper;
        this.transportationMapper = transportationMapper;
        this.locationMapper = locationMapper;
    }

    public void saveTransportation(TransportationDto transportationDto) {
        try {
            checkTransportationDtoTransportationType(transportationDto);
            checkTransportationDtoDayInfo(transportationDto);
            checkTransportationDtoOriginAndDestinationLocations(transportationDto);
            routeAppTransportationRepositoryHelper.saveTransportation(transportationMapper.toTransportation(transportationDto));
        } catch (DataServiceException dataServiceException) {
            throw dataServiceException;
        } catch (Throwable ex) {
            throw new DataServiceException("RouteAppDataService.saveTransportation", ex);
        }
    }

    public Optional<LocationDto> findLocationByLocationName(String username) {
        try {
            Location location = routeAppLocationRepositoryHelper.findLocationByName(username).orElse(null);
            return location == null ? Optional.empty() : Optional.of(locationMapper.toLocationDto(location));
        } catch (Throwable ex) {
            throw new DataServiceException("RouteAppDataService.findLocationByLocationName", ex);
        }
    }

    public List<TransportationDto> findAllTransportations() {
        try {
            return routeAppTransportationRepositoryHelper.findAllTransportations().stream().map(transportationMapper::toTransportationDto).toList();
        } catch (Throwable ex) {
            throw new DataServiceException("RouteAppDataService.findAllTransportations", ex);
        }
    }

    public Optional<TransportationDto> findTransportationById(Integer transportationId) {
        try {
            Transportation transportation = routeAppTransportationRepositoryHelper.findTransportationById(transportationId);
            return transportation == null ? Optional.empty() : Optional.of(transportationMapper.toTransportationDto(transportation));
        } catch (Throwable ex) {
            throw new DataServiceException("RouteAppDataService.findTransportationById", ex);
        }
    }

    public Optional<TransportationDto> findTransportationByTransportationRequest(TransportationDto transportationDto) {
        try {
            LocationDto originLocationDto = findLocationByLocationName(transportationDto.getOriginLocation()).orElse(null);
            LocationDto destinationLocationDto = findLocationByLocationName(transportationDto.getDestinationLocation()).orElse(null);
            if (originLocationDto == null || destinationLocationDto == null)
                return Optional.empty();
            return routeAppTransportationRepositoryHelper.findTransportationByOriginLocationAndDestinationLocationAndTransportationType(locationMapper.toLocation(originLocationDto), locationMapper.toLocation(destinationLocationDto), transportationDto.getTransportationType()).map(transportationMapper::toTransportationDto);
        } catch (RepositoryException ex) {
            throw new DataServiceException("RouteAppDataService.findTransportationByTransportationRequest", ex.getCause());
        } catch (Throwable t) {
            throw new DataServiceException("RouteAppDataService.findTransportationByTransportationRequest", t);
        }
    }

    public void deleteTransportationById(Integer id) {
        try {
            routeAppTransportationRepositoryHelper.deleteTransportationById(id);
        } catch (Throwable ex) {
            throw new DataServiceException("", ex);
        }
    }

    private void checkTransportationDtoTransportationType(TransportationDto transportationDto) {
        if (TransportationType.getByName(transportationDto.getTransportationType()) == null)
            throw new DataServiceException("Invalid transportation type: " + transportationDto.getTransportationType(),
                    new IllegalArgumentException(), DataServiceExceptionType.ILLEGAL_ARGUMENT);
    }

    private void checkTransportationDtoDayInfo(TransportationDto transportationDto) {
        if (!isValidOperationDayInfo(transportationDto.getOperationDays())) {
            throw new DataServiceException("Invalid operation day info: " + transportationDto.getOperationDays(),
                    new IllegalArgumentException(), DataServiceExceptionType.ILLEGAL_ARGUMENT);
        }
    }

    private void checkTransportationDtoOriginAndDestinationLocations(TransportationDto transportationDto) {
        Optional<Location> originLocation = routeAppLocationRepositoryHelper.findLocationByName(transportationDto.getOriginLocation());
        Optional<Location> destinationLocation = routeAppLocationRepositoryHelper.findLocationByName(transportationDto.getDestinationLocation());
        if (originLocation.isEmpty() || destinationLocation.isEmpty())
            throw new DataServiceException("Origin location or destination location could not found", new IllegalArgumentException(),
                    DataServiceExceptionType.ILLEGAL_ARGUMENT);
        if (TransportationType.getByName(transportationDto.getTransportationType()) == TransportationType.FLIGHT && (originLocation.get().getLocationCode().length() != 3 || destinationLocation.get().getLocationCode().length() != 3))
            throw new DataServiceException("Origin location code for Flight Transportation had be size 3: " + transportationDto.getOriginLocation(), new IllegalArgumentException(), DataServiceExceptionType.ILLEGAL_ARGUMENT);

    }

    private boolean isValidOperationDayInfo(Set<Integer> days) {
        for (int day : days) {
            if (day < 1 || day > 7)
                return false;
        }
        return true;
    }
}
