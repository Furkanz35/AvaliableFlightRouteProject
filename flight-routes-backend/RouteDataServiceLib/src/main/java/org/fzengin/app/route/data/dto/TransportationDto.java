package org.fzengin.app.route.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class TransportationDto {

    private Integer id;

    @NotNull(message = "Origin Location Id can not be null")
    private Integer originLocationId;

    @NotNull(message = "Destination Location Id can not be null")
    private Integer destinationLocationId;

    @NotBlank(message = "Transportation type can not be empty")
    private String transportationType;

    @NotEmpty(message = "Operation days can not be empty")
    private Set<Integer> operationDays;

    private String originLocationName;

    private String destinationLocationName;

    public TransportationDto(Integer originLocationId, Integer destinationLocationId, String transportationType, Set<Integer> operationDays) {
        this.originLocationId = originLocationId;
        this.destinationLocationId = destinationLocationId;
        this.transportationType = transportationType;
        this.operationDays = operationDays;
    }

    public TransportationDto(Integer id, Integer originLocationId, Integer destinationLocationId, String transportationType,
                             Set<Integer> operationDays) {
        this(originLocationId, destinationLocationId, transportationType, operationDays);
        this.id = id;
    }

    public TransportationDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOriginLocationId() {
        return originLocationId;
    }

    public void setOriginLocationId(Integer originLocationId) {
        this.originLocationId = originLocationId;
    }

    public Integer getDestinationLocationId() {
        return destinationLocationId;
    }

    public void setDestinationLocationId(Integer destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
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

    public String getOriginLocationName() {
        return originLocationName;
    }

    public void setOriginLocationName(String originLocationName) {
        this.originLocationName = originLocationName;
    }

    public String getDestinationLocationName() {
        return destinationLocationName;
    }

    public void setDestinationLocationName(String destinationLocationName) {
        this.destinationLocationName = destinationLocationName;
    }
}
