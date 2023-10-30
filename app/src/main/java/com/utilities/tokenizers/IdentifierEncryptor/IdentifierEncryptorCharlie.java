package com.utilities.tokenizers.IdentifierEncryptor;

import com.utilities.interfaces.IdentifierEncryptorMaker;
import java.util.List;
import com.utilities.classes.Queue;
import java.util.Random;

public class IdentifierEncryptorCharlie implements IdentifierEncryptorMaker
{
    @Override
    public short paddingMaker()
    {
        Random random = new Random();
        short[] shorts = new short[15];

        for (int i = 0; i < 15; i++)
        {
            shorts[i] = Short.parseShort(String.valueOf(random.nextInt(5)));
        }

        return shorts[random.nextInt(shorts.length)];
    }

    @Override
    public Queue<Short> initialIndexes() {
        return null;
    }

    @Override
    public List<Short> newIndexes() {
        return null;
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
