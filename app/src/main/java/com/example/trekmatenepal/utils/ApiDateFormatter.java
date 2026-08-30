package com.example.trekmatenepal.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ApiDateFormatter {

    private ApiDateFormatter() {
        // Utility class
    }

    public static String format(String date) {
        try {
            SimpleDateFormat inputFormat =
                    new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

            inputFormat.setLenient(false);

            Date parsedDate = inputFormat.parse(date);

            SimpleDateFormat outputFormat =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            return outputFormat.format(parsedDate);

        } catch (ParseException e) {
            return null;
        }
    }
}