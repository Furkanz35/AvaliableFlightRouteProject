package org.fzengin.app.route.data.mapper;

import org.fzengin.app.route.data.dto.TransportationDto;
import org.fzengin.app.route.repository.entity.Transportation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(implementationName = "TransportationMapperImpl", componentModel = "spring")
public interface TransportationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "originLocation", target = "originLocation.name")
    @Mapping(source = "destinationLocation", target = "destinationLocation.name")
    @Mapping(source = "transportationType", target = "transportationType")
    @Mapping(source = "operationDays", target = "operatingDayBitMask", qualifiedByName = "convertDaysToBitwise")
    Transportation toTransportation(TransportationDto transportationDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "originLocation.name", target = "originLocation")
    @Mapping(source = "destinationLocation.name", target = "destinationLocation")
    @Mapping(source = "transportationType", target = "transportationType")
    @Mapping(source = "operatingDayBitMask", target = "operationDays", qualifiedByName = "convertBitwiseToDays")
    TransportationDto toTransportationDto(Transportation transportation);

    @Named("convertDaysToBitwise")
    default int convertDaysToBitwise(Set<Integer> days) {
        if (days == null || days.isEmpty()) {
            return 0;
        }
        return days.stream().map(integer -> {
                    Day day = Day.getByOrder(integer);
                    if (day != null) {
                        return day.getBitMasking();
                    } else {
                        return 0;
                    }
                })
                .reduce(0, (a, b) -> a | b);
    }

    @Named("convertBitwiseToDays")
    default Set<Integer> convertBitwiseToDays(int bitMask) {
        if (bitMask == 0) {
            return Set.of();
        }
        return Arrays.stream(Day.values())
                .filter(day -> (bitMask & day.getBitMasking()) != 0).map(Day::getDayOrder).collect(Collectors.toSet());
    }
}


enum Day {
    MONDAY(1, 1),
    TUESDAY(2, 2),
    WEDNESDAY(3, 4),
    THURSDAY(4, 8),
    FRIDAY(5, 16),
    SATURDAY(6, 32),
    SUNDAY(7, 64);

    private final int dayOrder;

    private final int bitMasking;

    Day(int dayOrder, int bitMasking) {
        this.dayOrder = dayOrder;
        this.bitMasking = bitMasking;
    }

    public static Day getByOrder(int dayOrder) {
        for (Day day : Day.values()) {
            if (day.dayOrder == dayOrder)
                return day;
        }
        return null;
    }

    public int getDayOrder() {
        return dayOrder;
    }

    public int getBitMasking() {
        return bitMasking;
    }
}
