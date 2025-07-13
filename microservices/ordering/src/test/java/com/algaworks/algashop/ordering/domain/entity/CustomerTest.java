package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import com.algaworks.algashop.ordering.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.exception.ErrorMessages;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException(){
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new Customer(
                            IdGenerator.generateTimeBasedUUID(),
                            "John Doe",
                            LocalDate.of(2000, 6, 15),
                            "invalid-email",
                            "1234567890",
                            "12345678901",
                            false,
                            OffsetDateTime.now()
                    );
                });
    }

    @Test
    void given_invalidEmail_whenTryUpdateCustomerEmail_shouldGenerateException(){
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "John Doe",
                LocalDate.of(2000, 6, 15),
                "john.doe@gmail.com",
                "1234567890",
                "12345678901",
                false,
                OffsetDateTime.now()
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    customer.changeEmail("invalid-email");
                });
    }

    @Test
    void given_unarchivedCustomer_whenArchive_shouldAnonymize(){
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "John Doe",
                LocalDate.of(2000, 6, 15),
                "john.doe@gmail.com",
                "1234567890",
                "12345678901",
                false,
                OffsetDateTime.now()
        );
        customer.archive();

        Assertions.assertWith(customer,
                c -> assertThat(c.fullName()).isEqualTo("Anonymous"),
                c -> assertThat(c.email()).isNotEqualTo("john.doe@gmail.com"),
                c -> assertThat(c.phone()).isEqualTo("000-000-0000"),
                c -> assertThat(c.document()).isEqualTo("000-00-0000"),
                c -> assertThat(c.birthDate()).isNull(),
                c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse()
        );

    }

    @Test
    public void given_archivedCustomer_whenTryToUpdate_shouldGenerateException() {
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "John Doe",
                LocalDate.of(2000, 6, 15),
                "anonymous@anonymous.com",
                "000-000-0000",
                "000-00-0000",
                false,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                10);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail("new-email@anonymous.com"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeName("New Name"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone("123-456-7890"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::enablePromotionNotifications);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::disablePromotionNotifications);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(10));
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints(){
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "John Doe",
                LocalDate.of(2000, 6, 15),
                "john.doe@gmail.com",
                "1234567890",
                "12345678901",
                false,
                OffsetDateTime.now()
        );
        customer.addLoyaltyPoints(10);
        customer.addLoyaltyPoints(20);

        Assertions.assertWith(customer,
                c -> assertThat(c.fullName()).isEqualTo("John Doe"),
                c -> assertThat(c.email()).isEqualTo("john.doe@gmail.com"),
                c -> assertThat(c.phone()).isEqualTo("1234567890"),
                c -> assertThat(c.document()).isEqualTo("12345678901"),
                c -> assertThat(c.birthDate()).isEqualTo(LocalDate.of(2000, 6, 15)),
                c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse(),
                c -> assertThat(c.loyaltyPoints()).isEqualTo(30)
        );
    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException(){
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "John Doe",
                LocalDate.of(2000, 6, 15),
                "john.doe@gmail.com",
                "1234567890",
                "12345678901",
                false,
                OffsetDateTime.now()
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(-10))
                .withMessage(ErrorMessages.VALIDATION_ERROR_LOYALTY_POINTS_CANNOT_BE_NEGATIVE);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(0))
                .withMessage(ErrorMessages.VALIDATION_ERROR_LOYALTY_POINTS_CANNOT_BE_ZERO);
    }

}