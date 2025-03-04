package org.fzengin.app.route.data.dto.enums;

public enum TransportationType {
    BUS, FLIGHT, SUBWAY, UBER;

    public static TransportationType getByName(String name) {
        for (TransportationType type : TransportationType.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
