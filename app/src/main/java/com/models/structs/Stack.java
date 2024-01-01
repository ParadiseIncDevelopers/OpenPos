package com.models.structs;

import java.util.EmptyStackException;

public class Stack<T> {
    private int maxSize;
    private T[] stackArray;
    private int top;

    public Stack(int size) {
        this.maxSize = size;
        this.stackArray = (T[]) (new Object[maxSize]);
        this.top = -1;
    }
    public Stack () {
        this.maxSize = Integer.MAX_VALUE;
        this.stackArray = (T[]) (new Object[maxSize]);
        this.top = -1;
    }
    public boolean isEmpty() {
        return (top == -1);
    }

    public boolean isFull() {
        return (top == maxSize - 1);
    }

    public void push(T value) {
        if (isFull()) {
            // System.out.println("Stack is full. Cannot push element.");
        } else {
            stackArray[++top] = value;
            //System.out.println(value + " pushed to stack");
        }
    }

    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        } else {
            T value = (T) stackArray[top--];
            //  System.out.println(value + " popped from stack");
            return value;
        }
    }

    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        } else {
            return (T) stackArray[top];
        }
    }

    public void displayStack() {
        if (isEmpty()) {
            //System.out.println("Stack is empty.");
        } else {
            //System.out.println("Stack elements:");
            for (int i = top; i >= 0; i--) {
                //   System.out.println(stackArray[i]);
            }
        }
    }

}