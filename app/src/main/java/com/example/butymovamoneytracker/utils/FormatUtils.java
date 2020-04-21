package com.example.butymovamoneytracker.utils;

import com.example.butymovamoneytracker.prefs.FilterDate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class FormatUtils {
    private static final String DECIMAL_FORMAT = "###,##0.00##";
    private static final String DATE_FORMAT = "dd MMM yyyy HH:mm";
    private static final String DATE_FORMAT_API = "yyyy-MM-dd HH:mm:ss";


    public String getDateStr(long filterDate, String formatDate) {
        SimpleDateFormat format = new SimpleDateFormat(formatDate, Locale.getDefault());
        return format.format(filterDate);
    }

    public String getDateStr(Long date){
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        format.setTimeZone(TimeZone.getDefault());
        return format.format(date);
    }

    public Long getStrToDateUTC(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_API, Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return (format.parse(dateStr)).getTime();
    }

    public String getPriceStr(Long price){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');

        DecimalFormat format = new DecimalFormat();
        format.setDecimalFormatSymbols(symbols);
        format.applyPattern(DECIMAL_FORMAT);
        return  format.format(price);
    }

    public FilterDate getFilterDate(long date, TimeZone timeZone){
        Calendar calendar = Calendar.getInstance(timeZone);

        if (date!= 0)
            calendar.setTimeInMillis(date);

        return new FilterDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    public long getDateMaxLong(FilterDate filterDate){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        calendar.set(filterDate.getYear(),
                filterDate.getMonth(),
                filterDate.getDayOfMonth(),
                calendar.getActualMaximum(Calendar.HOUR_OF_DAY),
                calendar.getActualMaximum(Calendar.MINUTE),
                calendar.getActualMaximum(Calendar.SECOND));
        return calendar.getTimeInMillis();
    }

    public long getDateMinLong(FilterDate filterDate){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        calendar.set(filterDate.getYear(),
                filterDate.getMonth(),
                filterDate.getDayOfMonth(),
                calendar.getActualMinimum(Calendar.HOUR_OF_DAY),
                calendar.getActualMinimum(Calendar.MINUTE),
                calendar.getActualMinimum(Calendar.SECOND));
        return calendar.getTimeInMillis();
    }
}
