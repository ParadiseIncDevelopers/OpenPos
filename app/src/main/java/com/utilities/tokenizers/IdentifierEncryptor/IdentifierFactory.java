package com.utilities.tokenizers.IdentifierEncryptor;

import com.utilities.classes.EncryptorKeyContainer;
import com.utilities.interfaces.IdentifierEncryptorMaker;
import java.util.Random;

public class IdentifierFactory
{
    private EncryptorKeyContainer container;
    private int identifierIndex;

    public IdentifierEncryptorMaker createIdentifierToken()
    {
        int[] Alpha = new int[5];
        int[] Bravo = new int[5];
        int[] Charlie = new int[5];

        Random random = new Random();

        for (int i = 0; i < 5; i++)
        {
            Alpha[i] = random.nextInt(5);
            Bravo[i] = random.nextInt(5);
            Charlie[i] = random.nextInt(5);
        }

        int a = Alpha[random.nextInt(Alpha.length)];
        int b = Bravo[random.nextInt(Bravo.length)];
        int c = Charlie[random.nextInt(Charlie.length)];

        if(a == b && a == c)
        {
            return new IdentifierEncryptorAlpha();
        }
        else{
            if(a > b)
            {
                if(a > c)
                {
                    return new IdentifierEncryptorAlpha();
                }
                return null;
            }
            else {
                if(a < c)
                {
                    if(b < c)
                    {
                        return new IdentifierEncryptorCharlie();
                    }
                    else if(b > c)
                    {
                        return new IdentifierEncryptorBravo();
                    }
                    else {
                        return new IdentifierEncryptorCharlie();
                    }
                }
                else {
                    return new IdentifierEncryptorBravo();
                }
            }
        }
    }

    public EncryptorKeyContainer getContainer() {
        return container;
    }
    public void setContainer(EncryptorKeyContainer container) {
        this.container = container;
    }

    public int getIdentifierIndex() {
        return identifierIndex;
    }
    public void setIdentifierIndex(int identifierIndex) {
        this.identifierIndex = identifierIndex;
    }
}
