package com.models.structs;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class Stack<T> {
    private int maxSize = -1;
    private final List<T> stackArray;
    private int top;

    public Stack(int size) {
        this.maxSize = size;
        this.stackArray = new ArrayList<>(maxSize);
        this.top = -1;
    }
    public Stack () {
        this.stackArray = new ArrayList<>();
        this.top = 0;
    }

    public int size()
    {
        return stackArray.size();
    }

    public boolean isEmpty() {
        return (top == stackArray.size());
    }

    public boolean isFull() {
        return (top == maxSize - 1);
    }

    public void push(T value) {
        if (isFull()) {
            throw new StackOverflowError();
        } else {
            stackArray.add(value);
        }
    }

    public void fullyPush(List<T> values)
    {
        if(isFull())
        {
            throw new StackOverflowError();
        }
        else{
            stackArray.addAll(values);
        }
    }

    public T pop()
    {
        if (isEmpty())
        {
            throw new EmptyStackException();
        }
        else {
            return stackArray.remove(stackArray.size() - 1);
        }
    }

    public T peek() {
        if (isEmpty())
        {
            throw new EmptyStackException();
        }
        else {
            return stackArray.get(stackArray.size() - 1);
        }
    }

}