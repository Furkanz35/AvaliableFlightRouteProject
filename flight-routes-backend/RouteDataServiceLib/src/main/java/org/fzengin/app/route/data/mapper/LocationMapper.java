package org.fzengin.app.route.data.mapper;

import org.fzengin.app.route.data.dto.LocationDto;
import org.fzengin.app.route.repository.entity.Location;
import org.mapstruct.Mapper;

@Mapper(implementationName = "LocationMapperImpl", componentModel = "spring")
public interface LocationMapper {
    Location toLocation(LocationDto locationDto);

    LocationDto toLocationDto(Location location);

}
