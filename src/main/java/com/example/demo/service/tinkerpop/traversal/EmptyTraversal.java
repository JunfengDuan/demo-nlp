package com.example.demo.service.tinkerpop.traversal;

import com.example.demo.service.tinkerpop.graph.util.FastNoSuchElementException;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class EmptyTraversal<S, E> implements Traversal.Admin<S, E> {

    private static final EmptyTraversal INSTANCE = new EmptyTraversal();
//    private static final TraversalSideEffects SIDE_EFFECTS = EmptyTraversalSideEffects.instance();
//    private static final TraversalStrategies STRATEGIES = EmptyTraversalStrategies.instance();

    public static <A, B> EmptyTraversal<A, B> instance() {
        return INSTANCE;
    }

    protected EmptyTraversal() {

    }

    public Bytecode getBytecode() {
        return new Bytecode();
    }

    @Override
    public Traversal.Admin<S, E> asAdmin() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public E next() {
        throw FastNoSuchElementException.instance();
    }

    /*@Override
    public TraversalSideEffects getSideEffects() {
        return SIDE_EFFECTS;
    }

    @Override
    public void applyStrategies() {

    }

    @Override
    public void addStarts(final Iterator<Traverser.Admin<S>> starts) {

    }

    @Override
    public void addStart(final Traverser.Admin<S> start) {

    }*/

    @Override
    public <E2> Traversal.Admin<S, E2> addStep(final Step<?, E2> step) {
        return instance();
    }

    @Override
    public List<Step> getSteps() {
        return Collections.emptyList();
    }

    @Override
    public EmptyTraversal<S, E> clone() {
        return instance();
    }

   /* @Override
    public boolean isLocked() {
        return true;
    }

    @Override
    public TraverserGenerator getTraverserGenerator() {
        return null;
    }

    @Override
    public void setSideEffects(final TraversalSideEffects sideEffects) {
    }

    @Override
    public TraversalStrategies getStrategies() {
        return STRATEGIES;
    }

    @Override
    public void setParent(final TraversalParent step) {

    }

    @Override
    public TraversalParent getParent() {
        return EmptyStep.instance();
    }

    @Override
    public void setStrategies(final TraversalStrategies traversalStrategies) {

    }*/

    @Override
    public <S2, E2> Traversal.Admin<S2, E2> addStep(final int index, final Step<?, ?> step) throws IllegalStateException {
        return (Traversal.Admin) this;
    }

   /* @Override
    public <S2, E2> Traversal.Admin<S2, E2> removeStep(final int index) throws IllegalStateException {
        return (Traversal.Admin) this;
    }*/

    @Override
    public boolean equals(final Object object) {
        return object instanceof EmptyTraversal;
    }

    @Override
    public int hashCode() {
        return -343564565;
    }

   /* @Override
    public Set<TraverserRequirement> getTraverserRequirements() {
        return Collections.emptySet();
    }

    @Override
    public Optional<Graph> getGraph() {
        return Optional.empty();
    }

    @Override
    public void setGraph(final Graph graph) {

    }*/
}
