package com.example.C196.utilities;

import java.text.SimpleDateFormat;

public class TextFormatter {

    public static String datePickerPattern = "MM/dd/yyyy";
    public static String appPattern = "MMM d, yyyy";
    public static SimpleDateFormat appFormat = new SimpleDateFormat(appPattern);
    public static SimpleDateFormat dpFormat = new SimpleDateFormat(datePickerPattern);

}
