package org.fzengin.app.route.service;

import org.fzengin.app.route.data.dto.ValidRouteDto;
import org.fzengin.app.route.data.dto.enums.TransportationType;
import org.fzengin.app.route.data.mapper.TransportationMapper;
import org.fzengin.app.route.repository.dal.RouteAppLocationRepositoryHelper;
import org.fzengin.app.route.repository.dal.RouteAppTransportationRepositoryHelper;
import org.fzengin.app.route.repository.entity.Location;
import org.fzengin.app.route.repository.entity.Transportation;
import org.fzengin.app.route.service.exception.DataServiceException;
import org.fzengin.app.route.service.exception.DataServiceExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RouteAppValidRouteService {

    private final TransportationMapper transportationMapper;

    private final RouteAppLocationRepositoryHelper routeAppLocationRepositoryHelper;

    private final RouteAppTransportationRepositoryHelper routeAppTransportationRepositoryHelper;

    public RouteAppValidRouteService(TransportationMapper transportationMapper, RouteAppLocationRepositoryHelper routeAppLocationRepositoryHelper,
                                     RouteAppTransportationRepositoryHelper routeAppTransportationRepositoryHelper) {
        this.transportationMapper = transportationMapper;
        this.routeAppLocationRepositoryHelper = routeAppLocationRepositoryHelper;
        this.routeAppTransportationRepositoryHelper = routeAppTransportationRepositoryHelper;
    }

    public List<ValidRouteDto> findValidRoutes(String originLocationName, String destinationLocationName, int dateOrder) {
        if (dateOrder < 1 || dateOrder > 7) {
            throw new DataServiceException("Invalid Day information", new IllegalArgumentException("Invalid dateOrder"));
        }
        Location originLocation = routeAppLocationRepositoryHelper.findLocationByName(originLocationName)
                .orElseThrow(() -> new DataServiceException("Origin location not found", new IllegalArgumentException("Origin location not found"),
                        DataServiceExceptionType.ENTITY_NOT_FOUND));

        Location destinationLocation = routeAppLocationRepositoryHelper.findLocationByName(destinationLocationName)
                .orElseThrow(() -> new DataServiceException("Destination location not found", new IllegalArgumentException("Destination location not found"),
                        DataServiceExceptionType.ENTITY_NOT_FOUND));

       List<ValidRouteDto> validRoutes = new ArrayList<>();
       findRoutes(originLocation, destinationLocation, new ArrayList<>(), validRoutes, dateOrder);

        return validRoutes;
    }
    private void findRoutes(Location currentLocation, Location destinationLocation, List<Transportation> currentPath, List<ValidRouteDto> validRoutes, int dayOfWeek) {
        if (currentPath.size() > 3) {
            return;
        }
        if (currentLocation.equals(destinationLocation) && currentPath.size() > 0) {
            if (isValidRoute(currentPath, dayOfWeek)) {
                validRoutes.add(new ValidRouteDto((currentPath.stream().map(transportationMapper::toTransportationDto).collect(Collectors.toList()))));
            }
            return;
        }
        List<Transportation> availableTransportations = routeAppTransportationRepositoryHelper.findTransportationsByOriginLocationName(currentLocation.getName());
        for (Transportation transportation : availableTransportations) {
            if (!isTransportationAvailableOnSelectedDate(transportation, dayOfWeek)) {
                continue;
            }
            currentPath.add(transportation);

            findRoutes(transportation.getDestinationLocation(), destinationLocation, currentPath, validRoutes, dayOfWeek);

            currentPath.remove(currentPath.size() - 1);
        }
    }

    private boolean isValidRoute(List<Transportation> route, int dayOfWeek) {
        if (route.size() > 3) {
            return false;
        }
        int flightCount = 0;
        int beforeFlightTransfers = 0;
        int afterFlightTransfers = 0;
        boolean passedFlight = false;

        for (Transportation transportation : route) {
            if (!isTransportationAvailableOnSelectedDate(transportation, dayOfWeek)) {
                return false;
            }
            if (TransportationType.FLIGHT.name().equalsIgnoreCase(transportation.getTransportationType())) {
                flightCount++;
                passedFlight = true;
            } else {
                if (!passedFlight) {
                    beforeFlightTransfers++;
                } else {
                    afterFlightTransfers++;
                }
            }
        }
        return
                flightCount == 1 &&
                beforeFlightTransfers <= 1 &&
                afterFlightTransfers <= 1;
    }

    private boolean isTransportationAvailableOnSelectedDate(Transportation transportation, int dayOfWeek) {
        Set<Integer> workingDays = transportationMapper.convertBitwiseToDays(transportation.getOperatingDayBitMask());
        return !CollectionUtils.isEmpty(workingDays) && workingDays.contains(dayOfWeek);
    }
}