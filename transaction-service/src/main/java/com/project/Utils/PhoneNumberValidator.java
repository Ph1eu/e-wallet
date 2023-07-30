package com.project.Utils;

public class PhoneNumberValidator {

    private static final String PHONE_NUMBER_REGEX = "^\\d{10}$";

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches(PHONE_NUMBER_REGEX);
    }


}
