package com.ifillbrito.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gjib on 12.01.18.
 */
public class DateUtils
{
    public static Date parseDate(String date)
    {
        try
        {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        }
        catch ( ParseException e )
        {
            e.printStackTrace();
        }
        return null;
    }
}
