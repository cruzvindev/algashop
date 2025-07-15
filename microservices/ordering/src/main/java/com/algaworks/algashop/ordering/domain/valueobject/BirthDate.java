package com.algaworks.algashop.ordering.domain.valueobject;

import java.time.LocalDate;
import java.util.Objects;

public record BirthDate(LocalDate birthDate) {

    public BirthDate(LocalDate birthDate) {
        Objects.requireNonNull(birthDate);

        if(birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future.");
        }
        this.birthDate = birthDate;
    }

    public Integer age() {
        return LocalDate.now().getYear() - birthDate.getYear() -
               (LocalDate.now().getDayOfYear() < birthDate.getDayOfYear() ? 1 : 0);
    }

    @Override
    public String toString() {
        return birthDate.toString();
    }
}
