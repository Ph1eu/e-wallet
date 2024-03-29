package com.project.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringDateValidator {
    private static final String DATE_REGEX = "^\\d{2}-\\d{2}-\\d{4}$";

    public static boolean isValidDateFormat(String dateStr) {
        Pattern pattern = Pattern.compile(DATE_REGEX);
        Matcher matcher = pattern.matcher(dateStr);
        return matcher.matches();
    }
}
