package com.utilities.classes;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilityValues
{
    public static List<Integer> Days = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);

    public static List<Integer> Months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Integer> Years()
    {
        List<Integer> years = new ArrayList<>();
        int i = 2022;

        while(i <= LocalDateTime.now().getYear())
        {
            years.add(i);
            i++;
        }

        return years;
    }

    public static List<String> Currencies = Arrays.asList("TRY", "USD", "EUR", "CAD", "AUD", "JPY", "CNY",
            "CHF", "MAD", "QAR", "RUB");
    public final static String REGISTERED = "REGISTERED";
    public final static String ANONYMOUS = "REGISTERED";
    public static Integer COUNT_BACKGROUND = 0;
}
