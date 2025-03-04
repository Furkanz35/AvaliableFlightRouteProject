package org.fzengin.app.route.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class TransportationDto {

    private int id = -1;

    @NotBlank(message = "Origin Location name can not be empty")
    @Size(min = 2, max = 100, message = "Origin Location name must be between 2 and 100 characters")
    private String originLocation;

    @NotBlank(message = "Destination Location name can not be empty")
    @Size(min = 2, max = 100, message = "Destination Location name must be between 2 and 100 characters")
    private String destinationLocation;

    @NotBlank(message = "Transportation type can not be empty")
    private String transportationType;

    @NotEmpty(message = "Operation days can not be empty")
    private Set<Integer> operationDays;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(String transportationType) {
        this.transportationType = transportationType;
    }

    public Set<Integer> getOperationDays() {
        return operationDays;
    }

    public void setOperationDays(Set<Integer> operationDays) {
        this.operationDays = operationDays;
    }
}
