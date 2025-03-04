package org.fzengin.app.route.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "location_code"})
)
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Basic
    @Column(nullable = false, length = 100)
    private String country;

    @Basic
    @Column(name = "location_code", nullable = false, length = 10)
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Location location = (Location) other;
        return Objects.equals(id, location.id);
    }
}