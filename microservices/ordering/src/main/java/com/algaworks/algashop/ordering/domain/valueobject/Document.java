package com.algaworks.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record Document(String value) {

    public Document(String value) {
        Objects.requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException("Document value cannot be null or blank.");
        }
        this.value = value.trim();
    }

    @Override
    public String toString() {
        return value;
    }
}
