package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.exception.ErrorMessages;

import java.util.Objects;

public record LoyaltyPoints(Integer points) implements Comparable<LoyaltyPoints> {

    public static LoyaltyPoints ZERO = new LoyaltyPoints(0);

    public LoyaltyPoints() {
        this(0);
    }

    public LoyaltyPoints(Integer points) {
        Objects.requireNonNull(points);
        if (points < 0) {
            throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_LOYALTY_POINTS_CANNOT_BE_NEGATIVE);
        }
        this.points = points;
    }

    public LoyaltyPoints add(Integer value) {
        return add(new LoyaltyPoints(value));
    }

    public LoyaltyPoints add(LoyaltyPoints loyaltyPoints){
        Objects.requireNonNull(loyaltyPoints);
        if(loyaltyPoints.points <= 0) {
            throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_LOYALTY_POINTS_CANNOT_BE_NEGATIVE);
        }
        return new LoyaltyPoints(this.points + loyaltyPoints.points);
    }

    @Override
    public String toString() {
        return points.toString();
    }

    @Override
    public int compareTo(LoyaltyPoints o) {
        return this.points().compareTo(o.points);
    }
}
