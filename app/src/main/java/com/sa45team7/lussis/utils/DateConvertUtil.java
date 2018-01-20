package com.sa45team7.lussis.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by nhatton on 1/20/18.
 */

public class DateConvertUtil {

    private static final SimpleDateFormat formatFromServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    private static final SimpleDateFormat formatForRequisitions = new SimpleDateFormat("MMM d", Locale.US);

    static {
        formatFromServer.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String convertForRequisitions(Date date){
        return formatForRequisitions.format(date);
    }
}
