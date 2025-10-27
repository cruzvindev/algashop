package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;

@Component
public class OrderPersistenceEntityDisassembler {

    public Order toDomainEntity(OrderPersistenceEntity persistenceEntity) {
        return Order.existing()
                .id(new OrderId(persistenceEntity.getId()))
                .customerId(new CustomerId(persistenceEntity.getCustomerId()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(persistenceEntity.getTotalItems()))
                .status(OrderStatus.valueOf(persistenceEntity.getStatus()))
                .paymentMethod(PaymentMethod.valueOf(persistenceEntity.getPaymentMethod()))
                .placedAt(persistenceEntity.getPlacedAt())
                .paidAt(persistenceEntity.getPaidAt())
                .canceledAt(persistenceEntity.getCanceledAt())
                .readyAt(persistenceEntity.getReadyAt())
                .items(new HashSet<>())
                .version(persistenceEntity.getVersion())
                .build();
    }

    private Shipping toShippingValueObject(ShippingEmbeddable shippingEmbeddable) {
        RecipientEmbeddable recipientEmbeddable = shippingEmbeddable.getRecipient();
        return Shipping.builder()
                .cost(new Money(shippingEmbeddable.getCost()))
                .expectedDate(shippingEmbeddable.getExpectedDate())
                .recipient(
                        Recipient.builder()
                                .fullName(new FullName(recipientEmbeddable.getFirstName(), recipientEmbeddable.getLastName()))
                                .document(new Document(recipientEmbeddable.getDocument()))
                                .phone(new Phone(recipientEmbeddable.getPhone()))
                                .build()
                )
                .address(toAddressValueObject(shippingEmbeddable.getAddress()))
                .build();
    }

    private Billing toBillingValueObject(BillingEmbeddable billingEmbeddable) {
        return Billing.builder()
                .fullName(new FullName(billingEmbeddable.getFirstName(), billingEmbeddable.getLastName()))
                .document(new Document(billingEmbeddable.getDocument()))
                .phone(new Phone(billingEmbeddable.getPhone()))
                .address(toAddressValueObject(billingEmbeddable.getAddress()))
                .build();
    }

    private Address toAddressValueObject(AddressEmbeddable address) {
        return Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(new ZipCode(address.getZipCode()))
                .build();
    }

    public Order toDomain(OrderPersistenceEntity orderPersistence) {
        return Order.existing()
                .id(new OrderId(orderPersistence.getId()))
                .customerId(new CustomerId(orderPersistence.getCustomerId()))
                .totalAmount(new Money(orderPersistence.getTotalAmount()))
                .totalItems(new Quantity(orderPersistence.getTotalItems()))
                .paymentMethod(PaymentMethod.valueOf(orderPersistence.getPaymentMethod()))
                .placedAt(orderPersistence.getPlacedAt())
                .paidAt(orderPersistence.getPlacedAt())
                .readyAt(orderPersistence.getReadyAt())
                .canceledAt(orderPersistence.getCanceledAt())
                .status(OrderStatus.valueOf(orderPersistence.getStatus()))
                .items(new HashSet<>())
                .version(orderPersistence.getVersion())
                .billing(this.buildBilling(orderPersistence.getBilling()))
                .shipping(this.buildShipping(orderPersistence.getShipping()))
                .build();
    }

    private Billing buildBilling(BillingEmbeddable billing) {
        if (Objects.isNull(billing)) return null;

        return Billing.builder()
                .fullName(new FullName(billing.getFirstName(), billing.getLastName()))
                .document(new Document(billing.getDocument()))
                .phone(new Phone(billing.getPhone()))
                .address(this.buildAddress(billing.getAddress()))
                .build();
    }

    private Shipping buildShipping(ShippingEmbeddable shipping) {
        if (Objects.isNull(shipping)) return null;

        return Shipping.builder()
                .cost(new Money(shipping.getCost()))
                .expectedDate(shipping.getExpectedDate())
                .recipient(this.buildRecipient(shipping.getRecipient()))
                .address(this.buildAddress(shipping.getAddress()))
                .build();
    }

    private Address buildAddress(AddressEmbeddable address) {
        Objects.requireNonNull(address);

        return Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(new ZipCode(address.getZipCode()))
                .build();
    }

    private Recipient buildRecipient(RecipientEmbeddable recipient) {
        Objects.requireNonNull(recipient);

        return Recipient.builder()
                .fullName(new FullName(recipient.getFirstName(), recipient.getLastName()))
                .document(new Document(recipient.getDocument()))
                .phone(new Phone(recipient.getPhone()))
                .build();
    }
}
