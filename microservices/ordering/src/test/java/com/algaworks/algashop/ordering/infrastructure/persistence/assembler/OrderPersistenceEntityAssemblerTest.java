package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.builders.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void givenOrderWithNoItems_shouldRemovePersistenceEntityItems() {
        Order order = OrderTestDataBuilder.anOrder().withItems(false).build();
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();

        Assertions.assertThat(order.items()).isEmpty();
        Assertions.assertThat(persistenceEntity.getItems()).isNotEmpty();

        assembler.merge(persistenceEntity, order);

        Assertions.assertThat(persistenceEntity.getItems()).isEmpty();
    }

    @Test
    public void givenOrderWithItems_shouldAddToPersistenceEntity() {
        Order order = OrderTestDataBuilder.anOrder().withItems(true).build();
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().items(new HashSet<>()).build();

        Assertions.assertThat(order.items()).isNotEmpty();
        Assertions.assertThat(persistenceEntity.getItems()).isEmpty();

        assembler.merge(persistenceEntity, order);

        Assertions.assertThat(persistenceEntity.getItems()).isNotEmpty();
        Assertions.assertThat(persistenceEntity.getItems().size()).isEqualTo(order.items().size());
    }

    @Test
    public void givenOrderWithItems_whenMerge_shouldRemoveMergeCorrectly() {
        Order order = OrderTestDataBuilder.anOrder().withItems(true).build();

        Assertions.assertThat(order.items().size()).isEqualTo(2);

        Set<OrderItemPersistenceEntity> orderItemPersistenceEntities = order.items().stream()
                .map(assembler::fromDomain)
                .collect(Collectors.toSet());

        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder
                .existingOrder()
                .items(orderItemPersistenceEntities)
                .build();

        order.removeItem(order.items().iterator().next().orderItemId());

        assembler.merge(persistenceEntity, order);

        Assertions.assertThat(persistenceEntity.getItems()).isNotEmpty();
        Assertions.assertThat(persistenceEntity.getItems().size()).isEqualTo(order.items().size());
    }
}