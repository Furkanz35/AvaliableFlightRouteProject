package org.fzengin.app.route.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations")
public class Location implements Serializable {
    @Id
    @Column(nullable = false, length = 512)
    private String name;

    @Basic
    @Column(nullable = false, length = 512)
    private String country;

    @Basic
    @Column(name = "location_code", nullable = false, length = 10)
    private String locationCode;

    @OneToMany(mappedBy = "originLocation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transportation> originTransportations = new ArrayList<>();

    @OneToMany(mappedBy = "destinationLocation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transportation> destinationTransportations = new ArrayList<>();

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

    @PrePersist
    @PreUpdate
    private void formatFields() {
        if (name != null) {
            name = name.toUpperCase();
        }
        if (country != null) {
            country = country.toUpperCase();
        }
        if (locationCode != null) {
            locationCode = locationCode.toUpperCase();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Location location = (Location) other;
        return Objects.equals(name, location.name);
    }
}