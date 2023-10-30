package com.utilities.tokenizers.IdentifierEncryptor;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.utilities.interfaces.IdentifierEncryptorMaker;
import java.util.ArrayList;
import java.util.List;
import com.utilities.classes.Queue;
import java.util.Random;
import static com.utilities.classes.UtilityValues.Hex;

public class IdentifierEncryptorAlpha implements IdentifierEncryptorMaker
{
    private static Queue<Short> indexes = new Queue<>();
    private static List<Short> newIndexes = new ArrayList<>();
    private static Queue<Character> chars = new Queue<>();
    private static short paddingShort = -1;

    @Override
    public short paddingMaker()
    {
        Random random = new Random();
        paddingShort = Short.parseShort(String.valueOf(random.nextInt(5)));
        return paddingShort;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Queue<Short> initialIndexes()
    {
        Random random = new Random();
        int i = 0;

        while (i <= 8)
        {
            short number = Short.parseShort(String.valueOf(random.nextInt(17)));
            if(indexes.isEmpty())
            {
                indexes.enqueue(number);
                i++;
            }
            else{
                if(indexes.stream().noneMatch(x -> x == number))
                {
                    indexes.enqueue(number);
                    i++;
                }
            }
        }

        return indexes;
    }

    @Override
    public List<Short> newIndexes()
    {
        while (!indexes.isEmpty())
        {
            short newIndex = (short)(paddingShort + indexes.dequeue());

            if(newIndex > 16)
            {
                newIndex = (short) (newIndex - 16);
            }
            newIndexes.add((short) newIndex);
        }

        return newIndexes;
    }

    @Override
    public Queue<Character> chars()
    {
        Random random = new Random();

        for (int i = 0; i < 8; i++)
        {
            chars.enqueue(Hex.get(random.nextInt(Hex.size())));
        }

        return chars;
    }

    @Override
    public String newIdentifierToken(String oldToken, String date)
    {
        StringBuilder builder = new StringBuilder(oldToken);

        for (int i = 0; i < 8; i++)
        {
            builder.replace(newIndexes.get(i), newIndexes.get(i + 1), String.valueOf(chars.dequeue()));
        }

        return builder.toString();
    }
}
