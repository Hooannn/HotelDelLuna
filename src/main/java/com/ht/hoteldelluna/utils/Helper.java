package com.ht.hoteldelluna.utils;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class Helper {
    public static String formatCurrency(double amount) {
        Locale vietnamLocale = new Locale("vi", "VN");
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vietnamLocale);
        vndFormat.setCurrency(Currency.getInstance("VND"));
        return vndFormat.format(amount);
    }
}
