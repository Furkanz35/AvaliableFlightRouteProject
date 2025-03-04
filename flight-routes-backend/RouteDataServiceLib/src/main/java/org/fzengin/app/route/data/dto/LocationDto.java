package org.fzengin.app.route.data.dto;

import java.util.Objects;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LocationDto {
    private Integer id;

    @NotBlank(message = "Location name can not be empty")
    @Size(min = 2, max = 100, message = "Location name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Country can not be empty")
    @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
    private String country;

    @Pattern(regexp = "^[A-Za-z]{3,}$", message = "Location code must be at least 3 characters and all characters must be alphanumeric")
    private String locationCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public LocationDto(String name, String country, String locationCode) {
        this.name = name;
        this.country = country;
        this.locationCode = locationCode;
    }

    public LocationDto(Integer id, String name, String country, String locationCode) {
        this(name, country, locationCode);
        this.id = id;
    }

    public LocationDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationDto that = (LocationDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(country, that.country) && Objects.equals(locationCode, that.locationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country, locationCode);
    }
}
