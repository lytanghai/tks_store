package com.group.tks_store.common.enumz;

public enum Condition {
    CONTAINS("CONTAINS"),
    GREATER_THAN("GREATER_THAN"),
    LESS_THAN("LESS_THAN"),
    EQUAL("EQUAL"),
    BETWEEN("BETWEEN");

    String value;

    Condition(String value) {
        this.value = value;
    }
}
