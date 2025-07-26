package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.exception.CustomerArchivedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException(){
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Customer(
                        new CustomerId(),
                        new FullName("John", "Doe"),
                        new BirthDate(LocalDate.of(2000, 6, 15)),
                        new Email("invalid-email"),
                        new Phone("1234567890"),
                        new Document("12345678901"),
                        false,
                        OffsetDateTime.now(),
                        Address.builder()
                                .street("Bourbon Street")
                                .complement("Apartment 1")
                                .neighborhood("French Quarter")
                                .number("123")
                                .city("Los Angeles")
                                .state("LA")
                                .zipCode(new ZipCode("70112"))
                                .build()
                ));
    }

    @Test
    void given_invalidEmail_whenTryUpdateCustomerEmail_shouldGenerateException(){
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(2000, 6, 15)),
                new Email("john.doe@gmail.com"),
                new Phone("1234567890"),
                new Document("12345678901"),
                false,
                OffsetDateTime.now(),
                Address.builder()
                        .street("Bourbon Street")
                        .complement("Apartment 1")
                        .neighborhood("French Quarter")
                        .number("123")
                        .city("Los Angeles")
                        .state("LA")
                        .zipCode(new ZipCode("70112"))
                        .build()
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("invalid-email")));
    }

    @Test
    void given_unarchivedCustomer_whenArchive_shouldAnonymize(){
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(2000, 6, 15)),
                new Email("john.doe@gmail.com"),
                new Phone("1234567890"),
                new Document("12345678901"),
                false,
                OffsetDateTime.now(),
                Address.builder()
                        .street("Bourbon Street")
                        .complement("Apartment 1")
                        .neighborhood("French Quarter")
                        .number("123")
                        .city("Los Angeles")
                        .state("LA")
                        .zipCode(new ZipCode("70112"))
                        .build()
        );
        customer.archive();

        Assertions.assertWith(customer,
                c -> assertThat(c.fullName().toString()).isEqualTo(new FullName("Anonymous", "User").toString()),
                c -> assertThat(c.email().toString()).isNotEqualTo("john.doe@gmail.com"),
                c -> assertThat(c.phone().toString()).isEqualTo("000-000-0000"),
                c -> assertThat(c.document().toString()).isEqualTo("000-00-0000"),
                c -> assertThat(c.birthDate()).isNull(),
                c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse(),
                c -> assertThat(c.address()).isEqualTo(
                        Address.builder()
                                .street("Bourbon Street")
                                .complement(null)
                                .neighborhood("French Quarter")
                                .number("Anonymized")
                                .city("Los Angeles")
                                .state("LA")
                                .zipCode(new ZipCode("70112"))
                                .build()
                )
        );

    }

    @Test
    public void given_archivedCustomer_whenTryToUpdate_shouldGenerateException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Anonymous", "Anonymous"),
                new BirthDate(LocalDate.of(2000, 6, 15)),
                new Email("anonymous@anonymous.com"),
                new Phone("000-000-0000"),
                new Document("000-00-0000"),
                false,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                new LoyaltyPoints(10),
                Address.builder()
                        .street("Bourbon Street")
                        .complement("Apartment 1")
                        .neighborhood("French Quarter")
                        .number("123")
                        .city("Los Angeles")
                        .state("LA")
                        .zipCode(new ZipCode("70112"))
                        .build());

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("new-email@anonymous.com")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeName(new FullName("New", "Name")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone(new Phone("123-456-7890")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::enablePromotionNotifications);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::disablePromotionNotifications);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(10)));
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints(){
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(2000, 6, 15)),
                new Email("john.doe@gmail.com"),
                new Phone("1234567890"),
                new Document("12345678901"),
                false,
                OffsetDateTime.now(),
                Address.builder()
                        .street("Bourbon Street")
                        .complement("Apartment 1")
                        .neighborhood("French Quarter")
                        .number("123")
                        .city("Los Angeles")
                        .state("LA")
                        .zipCode(new ZipCode("70112"))
                        .build()
        );
        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(20));

        Assertions.assertWith(customer,
                c -> assertThat(c.fullName().toString()).isEqualTo("John Doe"),
                c -> assertThat(c.email().toString()).isEqualTo("john.doe@gmail.com"),
                c -> assertThat(c.phone().toString()).isEqualTo("1234567890"),
                c -> assertThat(c.document().toString()).isEqualTo("12345678901"),
                c -> assertThat(c.birthDate().toString()).isEqualTo(LocalDate.of(2000, 6, 15).toString()),
                c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse(),
                c -> assertThat(c.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30))
        );
    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException(){
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(2000, 6, 15)),
                new Email("john.doe@gmail.com"),
                new Phone("1234567890"),
                new Document("12345678901"),
                false,
                OffsetDateTime.now(),
                Address.builder()
                        .street("Bourbon Street")
                        .complement("Apartment 1")
                        .neighborhood("French Quarter")
                        .number("123")
                        .city("Los Angeles")
                        .state("LA")
                        .zipCode(new ZipCode("70112"))
                        .build()
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(LoyaltyPoints.ZERO));
    }

}