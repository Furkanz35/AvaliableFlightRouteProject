package org.fzengin.app.route.data.dto;

import java.util.List;

public class ValidRouteDto {
    private List<TransportationDto> transportationDtoList;

    public ValidRouteDto(List<TransportationDto> transportationDtoList) {
        this.transportationDtoList = transportationDtoList;
    }

    public List<TransportationDto> getTransportationDtoList() {
        return transportationDtoList;
    }

    public void setTransportationDtoList(List<TransportationDto> transportationDtoList) {
        this.transportationDtoList = transportationDtoList;
    }
}
