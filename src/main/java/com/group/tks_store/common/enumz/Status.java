package com.group.tks_store.common.enumz;

public enum Status {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    final String value;
    Status(String status) {
        this.value = status;
    }
    public String getValue() {
        return value;
    }
}
