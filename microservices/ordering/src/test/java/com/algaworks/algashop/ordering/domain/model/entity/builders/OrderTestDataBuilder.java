package com.algaworks.algashop.ordering.domain.model.entity.builders;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;

import java.time.LocalDate;

public class OrderTestDataBuilder {

    private CustomerId customerId = new CustomerId();
    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    private Money shippingCost = new Money("10.00");
    private LocalDate expectedDeliveryDate = LocalDate.now().plusWeeks(1);
    private Shipping shipping = aShipping();
    private Billing billing = aBilling();

    private boolean withItems = true;
    private OrderStatus status = OrderStatus.DRAFT;

    private OrderTestDataBuilder() {}

    public static OrderTestDataBuilder anOrder() {
        return new OrderTestDataBuilder();
    }

    public Order build() {
        Order order = Order.draft(customerId);
        order.changeShipping(shipping);
        order.changeBilling(billing);
        order.changePaymentMethod(paymentMethod);

        if (withItems) {
            order.addItem(ProductTestDataBuilder.aProduct().build(),
                    new Quantity(2)
            );

            order.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(),
                    new Quantity(1)
            );
        }

        switch (this.status) {
            case DRAFT -> {
            }
            case PLACED -> {
                order.place();
            }
            case PAID -> {
                order.place();
                order.markAsPaid();
            }
            case READY -> {
            }
            case CANCELED -> {
            }
        }

        return order;
    }

    public static Billing aBilling() {
        return Billing.builder()
                .address(anAddress())
                .document(new Document("225-09-1992"))
                .phone(new Phone("123-111-9911"))
                .email(new Email("john.doe@gmail.com"))
                .fullName(new FullName("John", "Doe")).build();
    }

    public static Shipping aShipping() {
        return Shipping.builder()
                .address(anAddress())
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .recipient(Recipient.builder()
                        .fullName(new FullName("John", "Doe"))
                        .document(new Document("112-33-2321"))
                        .phone(new Phone("111-441-1244"))
                        .build())
                .build();
    }

    public static Address anAddress() {
        return Address.builder()
                .street("Bourbon Street")
                .number("1234")
                .neighborhood("North Ville")
                .complement("apt. 11")
                .city("Montfort")
                .state("South Carolina")
                .zipCode(new ZipCode("79911")).build();
    }

    public static Address anAddressAlt() {
        return Address.builder()
                .street("Main Street")
                .number("5678")
                .neighborhood("Downtown")
                .complement("suite 22")
                .city("Springfield")
                .state("Illinois")
                .zipCode(new ZipCode("62701")).build();
    }

    public static Shipping aShippingAlt() {
        return Shipping.builder()
                .address(anAddressAlt())
                .cost(new Money("15.00"))
                .expectedDate(LocalDate.now().plusWeeks(2))
                .recipient(Recipient.builder()
                        .fullName(new FullName("Jane", "Smith"))
                        .document(new Document("987-65-4321"))
                        .phone(new Phone("555-123-4567"))
                        .build())
                .build();
    }

    public OrderTestDataBuilder customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestDataBuilder paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderTestDataBuilder shippingInfo(Shipping shipping) {
        this.shipping = shipping;
        return this;
    }

    public OrderTestDataBuilder billingInfo(Billing billing) {
        this.billing = billing;
        return this;
    }

    public OrderTestDataBuilder withItems(boolean withItems) {
        this.withItems = withItems;
        return this;
    }

    public OrderTestDataBuilder status(OrderStatus status) {
        this.status = status;
        return this;
    }
}
