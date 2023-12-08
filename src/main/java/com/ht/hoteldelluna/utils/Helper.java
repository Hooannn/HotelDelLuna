package com.ht.hoteldelluna.utils;

import io.github.palexdev.materialfx.enums.SortState;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Currency;
import java.util.Locale;

public class Helper {
    public static String formatCurrency(double amount) {
        Locale vietnamLocale = new Locale("vi", "VN");
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vietnamLocale);
        vndFormat.setCurrency(Currency.getInstance("VND"));
        return vndFormat.format(amount);
    }

    public static LocalDateTime getStartOfDay() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    public static LocalDateTime getEndOfDay() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    public static LocalDateTime getStartOfWeek() {
        LocalDateTime now = LocalDateTime.now();
        return now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
    }

    public static LocalDateTime getEndOfWeek() {
        LocalDateTime now = LocalDateTime.now();
        return now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);
    }

    public static LocalDateTime getStartOfMonth() {
        LocalDateTime now = LocalDateTime.now();
        return now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    }

    public static LocalDateTime getEndOfMonth() {
        LocalDateTime now = LocalDateTime.now();
        return now.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    }

    public static LocalDateTime getStartOfYear() {
        LocalDateTime now = LocalDateTime.now();
        return now.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
    }

    public static LocalDateTime getEndOfYear() {
        LocalDateTime now = LocalDateTime.now();
        return now.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
    }

    public static LocalDateTime getStartOfPreviousDay() {
        LocalDateTime now = LocalDateTime.now();
        return now.minusDays(1).with(LocalTime.MIN);
    }

    public static LocalDateTime getEndOfPreviousDay() {
        LocalDateTime now = LocalDateTime.now();
        return now.with(LocalTime.MAX).minusDays(1);
    }

    public static LocalDateTime getStartOfPreviousWeek() {
        LocalDateTime now = LocalDateTime.now();
        return now.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
    }

    public static LocalDateTime getEndOfPreviousWeek() {
        LocalDateTime now = LocalDateTime.now();
        return now.minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);
    }

    public static LocalDateTime getStartOfPreviousMonth() {
        LocalDateTime now = LocalDateTime.now();
        return now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    }

    public static LocalDateTime getEndOfPreviousMonth() {
        LocalDateTime now = LocalDateTime.now();
        return now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    }
    public static LocalDateTime getStartOfPreviousYear() {
        LocalDateTime now = LocalDateTime.now();
        return now.minusYears(1).with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
    }

    public static LocalDateTime getEndOfPreviousYear() {
        LocalDateTime now = LocalDateTime.now();
        return now.minusYears(1).with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
    }

    public static StringConverter<SortState> sortEnumConverter = new StringConverter<SortState>() {
        @Override
        public String toString(SortState state) {
            if (state != null) {
                switch (state) {
                    case ASCENDING:
                        return "Tăng dần";
                    case DESCENDING:
                        return "Giảm dần";
                    case UNSORTED:
                        return "Mặc định";
                    default:
                        return state.toString().toLowerCase();
                }
            }
            return null;
        }

        @Override
        public SortState fromString(String string) {
            return SortState.valueOf(string.toUpperCase());
        }
    };
}
