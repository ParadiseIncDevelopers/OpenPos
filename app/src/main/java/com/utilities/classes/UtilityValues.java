package com.utilities.classes;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.utilities.classes.UserUtility.userEmail;
import static com.utilities.classes.UserUtility.userEncryptorKeyContainer;

public class UtilityValues
{
    public static List<Integer> Days = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30);
    public static List<Integer> Months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    public static List<Character> Hex = Arrays.asList('0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F');
    public final static String REGISTERED = "REGISTERED";
    public static List<String> Currencies = Arrays.asList("TRY", "USD", "EUR", "CAD", "AUD", "JPY", "CNY",
            "CHF", "MAD", "QAR", "RUB");

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Integer> Years() {
        List<Integer> years = new ArrayList<>();
        int i = 2022;

        while(i <= LocalDateTime.now().getYear())
        {
            years.add(i);
            i++;
        }

        return years;
    }

    public static void createUserEncryptorKey()
    {
        FirebaseDatabase.getInstance("https://openpos-logintoken.europe-west1.firebasedatabase.app/")
                .getReference()
                .child(userEmail)
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                    {
                        List<Map<String, Object>> getData = (List<Map<String, Object>>) snapshot.getValue();
                        Map<String, Object> theLastElement = getData.get(getData.size() - 1);
                        String tokenValue = theLastElement.get("TokenValue").toString();
                        String tokenDate = theLastElement.get("TokenDate").toString();
                        userEncryptorKeyContainer = new EncryptorKeyContainer.Builder()
                                .SetTokenValue(tokenValue)
                                .SetTokenDate(tokenDate)
                                .Build();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error)
                    {

                    }
                });
    }
}
