package com.example.demo.service.tinkerpop.traversal;

import com.example.demo.service.tinkerpop.graph.util.FastNoSuchElementException;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class EmptyStep<S, E> implements Step<S, E> {

    private static final EmptyStep INSTANCE = new EmptyStep<>();

    public static <S, E> EmptyStep<S, E> instance() {
        return INSTANCE;
    }

    private EmptyStep() {
    }

    @Override
    public void addStarts(final Iterator<Traverser.Admin<S>> starts) {

    }

    @Override
    public void addStart(final Traverser.Admin<S> start) {

    }

    @Override
    public void setPreviousStep(final Step<?, S> step) {

    }

    @Override
    public void reset() {

    }

    @Override
    public Step<?, S> getPreviousStep() {
        return INSTANCE;
    }

    @Override
    public void setNextStep(final Step<E, ?> step) {

    }

    @Override
    public Step<E, ?> getNextStep() {
        return INSTANCE;
    }

    @Override
    public <A, B> Traversal.Admin<A, B> getTraversal() {
        return EmptyTraversal.instance();
    }

    @Override
    public void setTraversal(final Traversal.Admin<?, ?> traversal) {

    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public EmptyStep<S, E> clone() {
        return INSTANCE;
    }

    @Override
    public Set<String> getLabels() {
        return Collections.emptySet();
    }

    @Override
    public void addLabel(final String label) {

    }

    @Override
    public void removeLabel(final String label) {

    }

    @Override
    public void setId(final String id) {

    }

    @Override
    public String getId() {
        return Traverser.Admin.HALT;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Traverser.Admin<E> next() {
        throw FastNoSuchElementException.instance();
    }

    @Override
    public int hashCode() {
        return -1691648095;
    }

    @Override
    public boolean equals(final Object object) {
        return object instanceof EmptyStep;
    }

    @Override
    public Set getRequirements() {
        return Collections.emptySet();
    }
}
