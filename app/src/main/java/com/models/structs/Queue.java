package com.models.structs;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Queue<T>
{
    private ArrayList<T> elements;
    private int index = 0;
    private int capacity = 0;

    public Queue()
    {
        elements = new ArrayList<>();
    }

    public Queue(int capacity)
    {
        elements = new ArrayList<>(capacity);
    }

    public boolean isEmpty()
    {
        return elements.size() == 0;
    }

    public boolean isFull()
    {
        return elements.size() == capacity;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Stream<T> stream()
    {
        return elements.stream();
    }

    public T dequeue()
    {
        try
        {
            T elem = elements.get(index);
            elements.remove(index);
            index++;
            return elem;
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    public void enqueue(T element)
    {
        elements.add(element);
    }

    public T peek()
    {
        return elements.get(index);
    }
}
