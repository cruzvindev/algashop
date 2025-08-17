package com.algaworks.algashop.ordering.exception;

import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;

public class OrderCannotBePlacedException extends DomainException{

    public OrderCannotBePlacedException(OrderId orderId) {
        super(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_ITEMS, orderId));
    }
}
