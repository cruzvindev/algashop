package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.builders.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderPersistenceEntityAssemblerTest {

    private final OrderPersistenceEntityAssembler assembler = new OrderPersistenceEntityAssembler();

    @Test
    void shouldConvertToDomain() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(order);

        Assertions.assertThat(orderPersistenceEntity).satisfies(
                e -> assertEquals(order.id().value().toLong(), e.getId()),
                e -> assertEquals(order.customerId().value(), e.getCustomerId()),
                e -> assertEquals(order.totalAmount().value(), e.getTotalAmount()),
                e -> assertEquals(order.totalItems().value(), e.getTotalItems()),
                e -> assertEquals(order.status().name(), e.getStatus()),
                e -> assertEquals(order.paymentMethod().name(), e.getPaymentMethod()),
                e -> assertEquals(order.placedAt(), e.getPlacedAt()),
                e -> assertEquals(order.paidAt(), e.getPaidAt()),
                e -> assertEquals(order.canceledAt(), e.getCanceledAt()),
                e -> assertEquals(order.readyAt(), e.getReadyAt())
        );
    }

}