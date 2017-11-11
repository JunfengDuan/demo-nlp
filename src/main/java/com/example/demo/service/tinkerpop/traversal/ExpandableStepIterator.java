package com.example.demo.service.tinkerpop.traversal;

import java.io.Serializable;
import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class ExpandableStepIterator<S> implements Iterator<Traverser.Admin<S>>, Serializable {

    private final TraverserSet<S> traverserSet = new TraverserSet<>();
    private final Step<S, ?> hostStep;

    public ExpandableStepIterator(final Step<S, ?> hostStep) {
        this.hostStep = hostStep;
    }

    @Override
    public boolean hasNext() {
        return !this.traverserSet.isEmpty() || this.hostStep.getPreviousStep().hasNext();
    }

    @Override
    public Traverser.Admin<S> next() {
        if (!this.traverserSet.isEmpty())
            return this.traverserSet.remove();
        /////////////
        if (this.hostStep.getPreviousStep().hasNext())
            return this.hostStep.getPreviousStep().next();
        /////////////
        return this.traverserSet.remove();
    }

    public void add(final Iterator<Traverser.Admin<S>> iterator) {
        iterator.forEachRemaining(this.traverserSet::add);
    }

    public void add(final Traverser.Admin<S> traverser) {
        this.traverserSet.add(traverser);
    }

    @Override
    public String toString() {
        return this.traverserSet.toString();
    }

    public void clear() {
        this.traverserSet.clear();
    }
}
