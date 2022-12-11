package com.utilities.classes;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.money.Money;
import java.util.function.Function;

public class CurrencyConverter
{
    private static final double TRY = 16.5;
    private static final double EUR = 0.95;
    private static final double CNY = 6.65;
    private static final double JPY = 130.85;
    private static final double CHF = 0.95;
    private static final double CAD = 1.25;
    private static final double MAD = 9.88;
    private static final double GBP = 0.80;
    private static final double RUB = 63.35;
    private static final double AUD = 1.39;
    private static final double QAR = 3.65;

    private static final Function<String, Double> getCurrency = (s) ->
    {
        switch (s)
        {
            case "TRY":
                return TRY;
            case "EUR":
                return EUR;
            case "CNY":
                return CNY;
            case "JPY":
                return JPY;
            case "CHF":
                return CHF;
            case "CAD":
                return CAD;
            case "MAD":
                return MAD;
            case "GBP":
                return GBP;
            case "RUB":
                return RUB;
            case "AUD":
                return AUD;
            case "QAR":
                return QAR;
        }
        return 1.0;
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Money ConvertMoneyByAny(String currency, Money quote)
    {
        Money returnMoney = new Money();
        returnMoney.setCurrency(quote.getCurrency());
        double converter = getCurrency.apply(currency) / getCurrency.apply(quote.getCurrency());
        double totalMoney = converter * quote.getMoneyCase();
        returnMoney.setMoneyCase(totalMoney);
        return returnMoney;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Money ConvertMoneyByDollar(Money quote)
    {
        Money returnMoney = new Money();
        returnMoney.setCurrency(quote.getCurrency());
        double totalMoney = quote.getMoneyCase() / getCurrency.apply(quote.getCurrency());
        returnMoney.setMoneyCase(totalMoney);
        return returnMoney;
    }
}
