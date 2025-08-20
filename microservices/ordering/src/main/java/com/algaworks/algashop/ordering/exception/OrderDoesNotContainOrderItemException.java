package com.algaworks.algashop.ordering.exception;

import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;

public class OrderDoesNotContainOrderItemException extends DomainException{

    public OrderDoesNotContainOrderItemException(OrderId id, OrderItemId orderItemId) {
        super(ErrorMessages.ERROR_ORDER_DOES_NOT_CONTAIN_ITEM.formatted(id, orderItemId));
    }
}
