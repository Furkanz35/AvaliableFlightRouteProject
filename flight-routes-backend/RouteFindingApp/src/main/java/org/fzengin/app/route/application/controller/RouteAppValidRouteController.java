package org.fzengin.app.route.application.controller;

import org.fzengin.app.route.application.apiresponse.ApiResponse;
import org.fzengin.app.route.data.dto.ValidRouteDto;
import org.fzengin.app.route.application.RouteAppValidRouteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/route")
public class RouteAppValidRouteController {
    private final RouteAppValidRouteService routeAppValidRouteService;

    public RouteAppValidRouteController(RouteAppValidRouteService routeAppValidRouteService) {
        this.routeAppValidRouteService = routeAppValidRouteService;
    }


    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<ValidRouteDto>>> getTransportationById(@RequestParam String originLocationName,
                                                                                  @RequestParam String destinationLocationName,
                                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ValidRouteDto> validRoutes = routeAppValidRouteService.findValidRoutes(originLocationName, destinationLocationName,
                date.getDayOfWeek().getValue());
        return CollectionUtils.isEmpty(validRoutes) ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Can not found " +
                "valid route")) :
                ResponseEntity.ok(new ApiResponse<>(true, "Valid routes were found.", validRoutes));
    }
}
