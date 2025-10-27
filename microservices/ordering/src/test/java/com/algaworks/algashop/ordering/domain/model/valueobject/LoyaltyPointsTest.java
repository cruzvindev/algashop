package com.algaworks.algashop.ordering.domain.model.valueobject;

import com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LoyaltyPointsTest {

    @Test
    void shouldGenerateWithValue() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        Assertions.assertThat(loyaltyPoints.value()).isEqualTo(10);
    }

    @Test
    void shouldAddValue() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        LoyaltyPoints result = loyaltyPoints.add(5);
        Assertions.assertThat(result.value()).isEqualTo(15);
    }

    @Test
    void shouldNotAddValue(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        Assertions.assertThatThrownBy(() -> loyaltyPoints.add(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessages.VALIDATION_ERROR_LOYALTY_POINTS_CANNOT_BE_NEGATIVE);
    }

    @Test
    void shouldNotAddZeroValue(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        Assertions.assertThatThrownBy(() -> loyaltyPoints.add(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessages.VALIDATION_ERROR_LOYALTY_POINTS_CANNOT_BE_NEGATIVE);
    }

}