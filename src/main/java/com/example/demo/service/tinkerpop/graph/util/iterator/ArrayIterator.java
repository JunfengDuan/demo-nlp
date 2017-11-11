package com.example.demo.service.tinkerpop.graph.util.iterator;


import com.example.demo.service.tinkerpop.graph.util.FastNoSuchElementException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;


public final class ArrayIterator<T> implements Iterator<T>, Serializable {

    private final T[] array;
    private int current = 0;

    public ArrayIterator(final T[] array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return this.current < this.array.length;
    }

    @Override
    public T next() {
        if (this.hasNext()) {
            this.current++;
            return this.array[this.current - 1];
        } else {
            throw FastNoSuchElementException.instance();
        }
    }

    @Override
    public String toString() {
        return Arrays.asList(array).toString();
    }
}
