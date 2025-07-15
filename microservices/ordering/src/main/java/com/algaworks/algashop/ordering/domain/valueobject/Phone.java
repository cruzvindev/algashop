package com.algaworks.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record Phone(String value) {

    public Phone(String value) {
        Objects.requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException("Phone value cannot be null or blank.");
        }
        this.value = value.trim();
    }

    @Override
    public String toString() {
        return value;
    }
}
