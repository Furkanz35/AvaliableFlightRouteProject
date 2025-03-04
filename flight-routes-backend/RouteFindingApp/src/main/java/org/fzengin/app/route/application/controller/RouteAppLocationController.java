package org.fzengin.app.route.application.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.fzengin.app.route.service.RouteAppLocationDataService;
import org.fzengin.app.route.application.apiresponse.ApiResponse;
import org.fzengin.app.route.data.dto.LocationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route/location")
public class RouteAppLocationController {
    private final RouteAppLocationDataService routeAppLocationDataService;

    public RouteAppLocationController(RouteAppLocationDataService routeAppLocationDataService) {
        this.routeAppLocationDataService = routeAppLocationDataService;
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<LocationDto>> saveLocation(@Valid @RequestBody LocationDto locationDto) {
        boolean isRecordedLocation = routeAppLocationDataService.existsLocationByLocationId(locationDto.getId());
        if (!isRecordedLocation) {
            LocationDto savedLocation = routeAppLocationDataService.saveLocation(locationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Location saved successfully", savedLocation));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(false, "Location already exists"));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<LocationDto>> getLocation(@PathVariable @NotNull(message = "Id can not be null") Integer id) {
        LocationDto locationDto = routeAppLocationDataService.findLocationById(id).orElse(null);
        return locationDto == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Location not found")) :
                ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Location found", locationDto));
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<Iterable<LocationDto>>> getAllLocations() {
        List<LocationDto> locations = routeAppLocationDataService.findAllLocations();
        return locations.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Locations not found")) :
                ResponseEntity.ok(new ApiResponse<>(true, "Locations found", locations));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<LocationDto>> deleteLocation(@PathVariable @NotNull(message = "Id can not be null") Integer id) {
        boolean isRecordedLocation = routeAppLocationDataService.existsLocationByLocationId(id);
        if (!isRecordedLocation) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Location not found"));
        } else {
            routeAppLocationDataService.deleteLocationById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Location deleted successfully"));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<LocationDto>> updateLocation(@PathVariable @NotNull(message = "Id can not be null") Integer id,
                                                                   @Valid @RequestBody LocationDto locationDto) {
        boolean isRecordedLocation = routeAppLocationDataService.existsLocationByLocationId(id);
        if (!isRecordedLocation) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Location not found"));
        } else {
           LocationDto savedLocation = routeAppLocationDataService.saveLocation(locationDto);
            return ResponseEntity.ok().body(new ApiResponse<>(true, "Location updated successfully", savedLocation));
        }
    }
}
