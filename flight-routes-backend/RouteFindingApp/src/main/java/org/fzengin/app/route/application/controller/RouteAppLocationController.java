package org.fzengin.app.route.application.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.fzengin.app.route.application.apiresponse.ApiResponse;
import org.fzengin.app.route.data.dto.LocationDto;
import org.fzengin.app.route.application.RouteAppLocationDataService;
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
        boolean isRecordedLocation = routeAppLocationDataService.existsLocationByLocationName(locationDto.getName());
        if (!isRecordedLocation) {
            routeAppLocationDataService.saveLocation(locationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Location saved successfully", locationDto));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(false, "Location already exists"));
        }
    }

    @GetMapping("/get/{locationName}")
    public ResponseEntity<ApiResponse<LocationDto>> getLocation(@PathVariable @Size(min = 2, message = "Location name must be at " + "least 2 " +
            "characters") String locationName) {
        LocationDto locationDto = routeAppLocationDataService.findLocationByLocationName(locationName).orElse(null);
        return locationDto == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Location not found")) :
                ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Get " + locationName, locationDto));
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<Iterable<LocationDto>>> getAllLocations() {
        List<LocationDto> locations = routeAppLocationDataService.findAllLocations();
        return locations.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Locations not found")) :
                ResponseEntity.ok(new ApiResponse<>(true, "Locations found", locations));
    }

    @DeleteMapping("/delete/{locationName}")
    public ResponseEntity<ApiResponse<LocationDto>> deleteLocation(@PathVariable @Size(min = 2, message =
            "Location name must be at least 2 characters") String locationName) {
        boolean locationNotFound = routeAppLocationDataService.findLocationByLocationName(locationName).isEmpty();
        if (locationNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Location not found"));
        } else {
            routeAppLocationDataService.deleteLocationByName(locationName);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Location deleted successfully"));
        }
    }

    @PutMapping("/update/{locationName}")
    public ResponseEntity<ApiResponse<LocationDto>> updateLocation(@PathVariable @Size(min = 2, message = "Location name must be at least 2 " +
            "characters") String locationName, @Valid @RequestBody LocationDto locationDto) {
        if (!locationName.equalsIgnoreCase(locationDto.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false,
                    "Location name in the path and in the request body must be the same"));
        }
        boolean locationNotFound = routeAppLocationDataService.findLocationByLocationName(locationName).isEmpty();
        if (locationNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Location not found"));
        } else {
            routeAppLocationDataService.saveLocation(locationDto);
            return ResponseEntity.ok().body(new ApiResponse<>(true, "Location updated successfully", locationDto));
        }
    }
}
