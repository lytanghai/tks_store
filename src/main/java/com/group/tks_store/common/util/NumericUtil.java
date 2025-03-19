package com.group.tks_store.common.util;

public class NumericUtil {
    public static Boolean isNumeric(String value) {
        boolean numeric = true;
        try {
            Double.parseDouble(value);
            return numeric;
        } catch (Exception e) {
            return false;
        }
    }
}
