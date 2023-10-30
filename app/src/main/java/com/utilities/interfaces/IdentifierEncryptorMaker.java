package com.utilities.interfaces;

import com.utilities.classes.Queue;
import java.util.List;

public interface IdentifierEncryptorMaker
{
    public abstract short paddingMaker();
    public abstract Queue<Short> initialIndexes();
    public abstract List<Short> newIndexes();
    public abstract Queue<Character> chars();
    public abstract String newIdentifierToken(String oldToken, String date);
}
