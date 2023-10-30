package com.utilities.tokenizers.IdentifierEncryptor;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.utilities.interfaces.IdentifierEncryptorMaker;
import java.util.ArrayList;
import java.util.List;
import com.utilities.classes.Queue;
import java.util.Random;
import java.util.function.Function;

public class IdentifierEncryptorBravo implements IdentifierEncryptorMaker
{
    private static Queue<Short> indexes = new Queue<>();
    private static List<Short> newIndexes = new ArrayList<>();
    private static Queue<Character> chars = new Queue<>();
    private static short paddingShort = -1;

    @Override
    public short paddingMaker()
    {
        Random random = new Random();
        short padding = -1;

        for (int i = 0; i < 5; i++)
        {
            padding = Short.parseShort(String.valueOf(random.nextInt(5)));
        }

        return padding;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Queue<Short> initialIndexes()
    {
        Random random = new Random();
        int i = 0;

        Function<Integer, Short> getRandomNumber = (s) -> Short.parseShort(String.valueOf(s + 1));

        while (i <= 8)
        {
            short number = getRandomNumber.apply(random.nextInt(17));
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
    public Queue<Character> chars() {
        return null;
    }

    @Override
    public String newIdentifierToken(String oldToken, String date)
    {
        return null;
    }
}