package org.fzengin.app.route.repository.entity;

import jakarta.persistence.*;

import java.util.Objects;
@Entity
@Table(name = "transportations")
public class Transportation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "destination_location_id")
    private Location destinationLocation;

    @ManyToOne
    @JoinColumn(name = "origin_location_id")
    private Location originLocation;

    @Column(name = "transportation_type" , nullable = false, length = 50)
    private String transportationType;

    @Column(name = "operating_day_bit_mask" , nullable = false)
    private Integer operatingDayBitMask;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(Location originLocation) {
        this.originLocation = originLocation;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(String transportationType) {
        this.transportationType = transportationType;
    }

    public Integer getOperatingDayBitMask() {
        return operatingDayBitMask;
    }

    @PrePersist
    @PreUpdate
    private void formatFields() {
        if (transportationType != null) {
            transportationType = transportationType.toUpperCase();
        }
    }

    public void setOperatingDayBitMask(Integer operatingDayBitMask) {
        this.operatingDayBitMask = operatingDayBitMask;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Transportation t && id == t.id;
    }
}