package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.entity.builders.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages;
import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderDoesNotContainOrderItemException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderRemoveItemTest {

    @Test
    public void givenAnOrderItemInOrderItemsList_whenRemoveItem_ShouldRemoveOrderItemFromList() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.DRAFT)
                .withItems(true)
                .build();

        BigDecimal orderValueBefore = order.totalAmount().value();
        OrderItem first = order.items().stream().findFirst().orElse(null);

        Objects.requireNonNull(first);
        order.removeItem(first.orderItemId());

        Assertions.assertThat(order.items()).hasSize(1);
        Assertions.assertThat(order.totalAmount().value()).isNotEqualTo(orderValueBefore);
        Assertions.assertThatThrownBy(() -> order.removeItem(first.orderItemId()))
                .isInstanceOf(OrderDoesNotContainOrderItemException.class)
                .hasMessage(String.format(ErrorMessages.ERROR_ORDER_DOES_NOT_CONTAIN_ITEM, order.id(), first.orderItemId()));
    }

    @Test
    public void givenAnOrderWithStatusOtherThanDraft_whenRemoveItem_ShouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.PAID)
                .withItems(true)
                .build();

        OrderItem first = order.items().stream().findFirst().orElse(null);

        Objects.requireNonNull(first);
        Assertions.assertThatThrownBy(() -> order.removeItem(first.orderItemId()))
                .isInstanceOf(OrderCannotBeEditedException.class)
                .hasMessage(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED, order.id(), order.status()));
    }
}
