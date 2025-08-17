package com.algaworks.algashop.ordering.exception;

public class ErrorMessages {
    public static final String VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST = "BirthDate must be a past date";
    public static final String VALIDATION_ERROR_FULLNAME_IS_NULL = "FullName cannot be null";
    public static final String VALIDATION_ERROR_FULLNAME_IS_BLANK = "FullName cannot be blank";
    public static final String VALIDATION_ERROR_EMAIL_IS_INVALID = "Email is invalid";

    public static final String ERROR_CUSTOMER_ARCHIVED = "Customer is archived and cannot be changed";
    public static final String VALIDATION_ERROR_LOYALTY_POINTS_CANNOT_BE_NEGATIVE = "Loyalty points cannot be negative";
    public static final String VALIDATION_ERROR_LOYALTY_POINTS_CANNOT_BE_ZERO = "Loyalty points cannot be zero";
    public static final String ERROR_ORDER_STATUS_CANNOT_BE_CHANGED = "Cannot change order %s status from %s to %s";
    public static final String ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST = "Order %s expected delivery date cannot be in the past";
    public static final String ERROR_ORDER_CANNOT_BE_PLACED_HAS_NO_ITEMS = "Order %s cannot be closed, it has no items";
}
