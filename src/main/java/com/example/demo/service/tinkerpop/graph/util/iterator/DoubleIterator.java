package com.example.demo.service.tinkerpop.graph.util.iterator;


import com.example.demo.service.tinkerpop.graph.util.FastNoSuchElementException;

import java.io.Serializable;
import java.util.Iterator;


final class DoubleIterator<T> implements Iterator<T>, Serializable {

    private T a;
    private T b;
    private char current = 'a';

    protected DoubleIterator(final T a, final T b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean hasNext() {
        return this.current != 'x';
    }

    @Override
    public void remove() {
        if (this.current == 'b')
            this.a = null;
        else if (this.current == 'x')
            this.b = null;
    }

    @Override
    public T next() {
        if (this.current == 'x')
            throw FastNoSuchElementException.instance();
        else {
            if (this.current == 'a') {
                this.current = 'b';
                return this.a;
            } else {
                this.current = 'x';
                return this.b;
            }
        }
    }
}
