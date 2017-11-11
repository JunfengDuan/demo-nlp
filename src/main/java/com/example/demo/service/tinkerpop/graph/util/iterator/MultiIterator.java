package com.example.demo.service.tinkerpop.graph.util.iterator;


import com.example.demo.service.tinkerpop.graph.util.FastNoSuchElementException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public final class MultiIterator<T> implements Iterator<T>, Serializable {

    private final List<Iterator<T>> iterators = new ArrayList<>();
    private int current = 0;

    public void addIterator(final Iterator<T> iterator) {
        this.iterators.add(iterator);
    }

    @Override
    public boolean hasNext() {
        if (this.current >= this.iterators.size())
            return false;

        Iterator<T> currentIterator = this.iterators.get(this.current);

        while (true) {
            if (currentIterator.hasNext()) {
                return true;
            } else {
                this.current++;
                if (this.current >= iterators.size())
                    break;
                currentIterator = iterators.get(this.current);
            }
        }
        return false;
    }

    @Override
    public void remove() {
        this.iterators.get(this.current).remove();
    }

    @Override
    public T next() {
        if (this.iterators.isEmpty()) throw FastNoSuchElementException.instance();

        Iterator<T> currentIterator = iterators.get(this.current);
        while (true) {
            if (currentIterator.hasNext()) {
                return currentIterator.next();
            } else {
                this.current++;
                if (this.current >= iterators.size())
                    break;
                currentIterator = iterators.get(current);
            }
        }
        throw FastNoSuchElementException.instance();
    }

    public void clear() {
        this.iterators.clear();
        this.current = 0;
    }

}
