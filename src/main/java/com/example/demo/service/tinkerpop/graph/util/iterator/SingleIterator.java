package com.example.demo.service.tinkerpop.graph.util.iterator;


import com.example.demo.service.tinkerpop.graph.util.FastNoSuchElementException;

import java.io.Serializable;
import java.util.Iterator;

final class SingleIterator<T> implements Iterator<T>, Serializable {

    private T t;
    private boolean alive = true;

    protected SingleIterator(final T t) {
        this.t = t;
    }

    @Override
    public boolean hasNext() {
        return this.alive;
    }

    @Override
    public void remove() {
        this.t = null;
    }

    @Override
    public T next() {
        if (!this.alive)
            throw FastNoSuchElementException.instance();
        else {
            this.alive = false;
            return t;
        }
    }
}
