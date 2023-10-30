package com.utilities.tokenizers;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.google.firebase.database.FirebaseDatabase;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import static com.utilities.classes.UtilityValues.Hex;

public class LoginTokenizer
{
    private final String email;

    public LoginTokenizer(String Email)
    {
        this.email = Email;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createLoginToken()
    {
        Supplier<String> valueCreator = () ->
        {
            Random random = new Random();
            StringBuilder string = new StringBuilder(16);
            for(int i = 0; i < 16; i++)
            {
                int n = random.nextInt(16);
                string.append(Hex.get(n));
            }

            return string.toString();
        };

        Map<String, Object> token = new HashMap<>();
        token.put("TokenValue", valueCreator.get());
        token.put("TokenDate", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        FirebaseDatabase.getInstance("https://openpos-logintoken.europe-west1.firebasedatabase.app/")
                .getReference().child(this.email).push().setValue(token);
    }
}
