package com.group.tks_store.common.util;

public class CurrencyFormatUtil {

    private static Double EXCHANGE_RATE = 4100.0;

    public static Double convertCurrency(Double price, String fromCurrency, String toCurrency) {
        Double result = 0.0;

        // If converting from USD to KHR
        if (fromCurrency.equals("USD") && toCurrency.equals("KHR")) {
            result = price * EXCHANGE_RATE;

        }
        // If converting from KHR to USD
        else {
            result = price / EXCHANGE_RATE;

            // Rounding based on the last two digits
            double shifted = result * 100;
            int lastTwoDigits = (int) (shifted % 100);
            if (lastTwoDigits == 0) {
                return result; // No rounding needed if last two digits are 00
            }
            if (lastTwoDigits < 50) {
                result = Math.floor(result * 100) / 100.0;  // Round down to two decimal places
            } else {
                result = Math.ceil(result * 100) / 100.0;  // Round up to two decimal places
            }
        }

        return result;
    }

}
