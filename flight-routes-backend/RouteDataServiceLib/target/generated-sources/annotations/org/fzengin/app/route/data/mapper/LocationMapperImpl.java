package org.fzengin.app.route.data.mapper;

import javax.annotation.processing.Generated;
import org.fzengin.app.route.data.dto.LocationDto;
import org.fzengin.app.route.repository.entity.Location;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-05T02:18:40+0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class LocationMapperImpl implements LocationMapper {

    @Override
    public Location toLocation(LocationDto locationDto) {
        if ( locationDto == null ) {
            return null;
        }

        Location location = new Location();

        location.setName( locationDto.getName() );
        location.setCountry( locationDto.getCountry() );
        location.setLocationCode( locationDto.getLocationCode() );
        location.setId( locationDto.getId() );

        return location;
    }

    @Override
    public LocationDto toLocationDto(Location location) {
        if ( location == null ) {
            return null;
        }

        LocationDto locationDto = new LocationDto();

        locationDto.setName( location.getName() );
        locationDto.setCountry( location.getCountry() );
        locationDto.setLocationCode( location.getLocationCode() );
        locationDto.setId( location.getId() );

        return locationDto;
    }
}
