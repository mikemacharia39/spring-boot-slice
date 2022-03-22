package com.mikehenry.springbootslice.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class AddressPK implements Serializable {
    @Column(name = "subLocation", nullable = false)
    private String subLocation;

    @Column(name = "location")
    private String location;

    @Column(name = "county", nullable = false)
    private String county;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        } else {
            AddressPK that = (AddressPK) o;

            return this.subLocation.equals(that.subLocation) &&
                    this.location.equals(that.location) &&
                    this.county.equals(that.county);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(subLocation, location, county);
    }
}
