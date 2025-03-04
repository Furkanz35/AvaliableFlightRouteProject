package org.fzengin.app.route.application.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.fzengin.app.route.application.apiresponse.ApiResponse;
import org.fzengin.app.route.data.dto.TransportationDto;
import org.fzengin.app.route.repository.entity.Transportation;
import org.fzengin.app.route.service.RouteAppTransportationDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/route/transportation")
public class RouteAppTransportationController {
    private final RouteAppTransportationDataService routeAppTransportationDataService;

    public RouteAppTransportationController(RouteAppTransportationDataService routeAppTransportationDataService) {
        this.routeAppTransportationDataService = routeAppTransportationDataService;
    }

    @GetMapping("get/all")
    public ResponseEntity<ApiResponse<Iterable<TransportationDto>>> getAllTransportations() {
        List<TransportationDto> transportations = routeAppTransportationDataService.findAllTransportations();
        return transportations.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(true, "Empty transportation")) :
                ResponseEntity.ok().body(new ApiResponse<>(true, "Transportations are listed successfully", transportations));
    }

    @GetMapping("/get/{transportationId}")
    public ResponseEntity<ApiResponse<TransportationDto>> getTransportationById(@PathVariable @NotNull(message = "Id can not be null") Integer transportationId) {
        Optional<TransportationDto> transportationRequestDto = routeAppTransportationDataService.findTransportationById(transportationId);
        return transportationRequestDto.map(transportationDto -> ResponseEntity.ok().body(new ApiResponse<>(false, "Transportation is listed " +
                "successfully", transportationDto))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(true,
                "Transportation not found")));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<TransportationDto>> deleteTransportationBuId(@PathVariable @NotNull(message = "Id can not be null for delete") Integer id) {
        boolean transportationNotFound = routeAppTransportationDataService.findTransportationById(id).isEmpty();
        if (transportationNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Transportation not found"));
        } else {
            routeAppTransportationDataService.deleteTransportationById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Transportation is deleted successfully"));
        }
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<TransportationDto>> saveTransportation(@Valid @RequestBody TransportationDto transportationDto) {
        boolean isRecordedLocation = routeAppTransportationDataService.findTransportationByTransportationRequest(transportationDto).isPresent();
        if (!isRecordedLocation) {
            TransportationDto savedTransportation = routeAppTransportationDataService.saveTransportation(transportationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Transportation saved successfully", savedTransportation));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(false, "Transportation already exists"));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<TransportationDto>> updateTransportation(@PathVariable @NotNull(message = "Id can not be null for update") Integer id,
                                                                               @Valid @RequestBody TransportationDto transportationDto) {
        if (!id.equals(transportationDto.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Transportation id mismatch"));
        }
        boolean transportationNotFound = routeAppTransportationDataService.findTransportationById(id).isEmpty();
        if (transportationNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Transportation not found"));
        } else {
            TransportationDto saveTransportationDto =  routeAppTransportationDataService.saveTransportation(transportationDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Transportation updated successfully", saveTransportationDto));
        }
    }
}
