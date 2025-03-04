package org.fzengin.app.route.data.mapper;

import javax.annotation.processing.Generated;
import org.fzengin.app.route.data.dto.TransportationDto;
import org.fzengin.app.route.repository.entity.Location;
import org.fzengin.app.route.repository.entity.Transportation;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-05T02:18:40+0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class TransportationMapperImpl implements TransportationMapper {

    @Override
    public Transportation toTransportation(TransportationDto transportationDto) {
        if ( transportationDto == null ) {
            return null;
        }

        Transportation transportation = new Transportation();

        transportation.setOriginLocation( transportationDtoToLocation( transportationDto ) );
        transportation.setDestinationLocation( transportationDtoToLocation1( transportationDto ) );
        if ( transportationDto.getId() != null ) {
            transportation.setId( transportationDto.getId() );
        }
        transportation.setTransportationType( transportationDto.getTransportationType() );
        transportation.setOperatingDayBitMask( convertDaysToBitwise( transportationDto.getOperationDays() ) );

        return transportation;
    }

    @Override
    public TransportationDto toTransportationDto(Transportation transportation) {
        if ( transportation == null ) {
            return null;
        }

        TransportationDto transportationDto = new TransportationDto();

        transportationDto.setId( transportation.getId() );
        transportationDto.setOriginLocationId( transportationOriginLocationId( transportation ) );
        transportationDto.setDestinationLocationId( transportationDestinationLocationId( transportation ) );
        transportationDto.setOriginLocationName( transportationOriginLocationName( transportation ) );
        transportationDto.setDestinationLocationName( transportationDestinationLocationName( transportation ) );
        transportationDto.setTransportationType( transportation.getTransportationType() );
        transportationDto.setOperationDays( convertBitwiseToDays( transportation.getOperatingDayBitMask().intValue() ) );

        return transportationDto;
    }

    protected Location transportationDtoToLocation(TransportationDto transportationDto) {
        if ( transportationDto == null ) {
            return null;
        }

        Location location = new Location();

        location.setId( transportationDto.getOriginLocationId() );

        return location;
    }

    protected Location transportationDtoToLocation1(TransportationDto transportationDto) {
        if ( transportationDto == null ) {
            return null;
        }

        Location location = new Location();

        location.setId( transportationDto.getDestinationLocationId() );

        return location;
    }

    private Integer transportationOriginLocationId(Transportation transportation) {
        Location originLocation = transportation.getOriginLocation();
        if ( originLocation == null ) {
            return null;
        }
        return originLocation.getId();
    }

    private Integer transportationDestinationLocationId(Transportation transportation) {
        Location destinationLocation = transportation.getDestinationLocation();
        if ( destinationLocation == null ) {
            return null;
        }
        return destinationLocation.getId();
    }

    private String transportationOriginLocationName(Transportation transportation) {
        Location originLocation = transportation.getOriginLocation();
        if ( originLocation == null ) {
            return null;
        }
        return originLocation.getName();
    }

    private String transportationDestinationLocationName(Transportation transportation) {
        Location destinationLocation = transportation.getDestinationLocation();
        if ( destinationLocation == null ) {
            return null;
        }
        return destinationLocation.getName();
    }
}
