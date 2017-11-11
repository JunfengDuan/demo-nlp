package com.example.demo.service.tinkerpop.traversal;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
* Created by jfd on 11/10/17.
*/
public interface Step<S, E> extends Iterator<Traverser.Admin<E>>, Serializable, Cloneable {

    /**
     * Add a iterator of {@link Traverser.Admin} objects of type S to the step.
     *
     * @param starts The iterator of objects to add
     */
    public void addStarts(final Iterator<Traverser.Admin<S>> starts);

    /**
     * Add a single {@link Traverser.Admin} to the step.
     *
     * @param start The traverser to add
     */
    public void addStart(final Traverser.Admin<S> start);

    /**
     * Set the step that is previous to the current step.
     * Used for linking steps together to form a function chain.
     *
     * @param step the previous step of this step
     */
    public void setPreviousStep(final Step<?, S> step);

    /**
     * Get the step prior to the current step.
     *
     * @return The previous step
     */
    public Step<?, S> getPreviousStep();

    /**
     * Set the step that is next to the current step.
     * Used for linking steps together to form a function chain.
     *
     * @param step the next step of this step
     */
    public void setNextStep(final Step<E, ?> step);

    /**
     * Get the next step to the current step.
     *
     * @return The next step
     */
    public Step<E, ?> getNextStep();

    /**
     * Get the {@link Traversal.Admin} that this step is contained within.
     *
     * @param <A> The incoming object type of the traversal
     * @param <B> The outgoing object type of the traversal
     * @return The traversal of this step
     */
    public <A, B> Traversal.Admin<A, B> getTraversal();

    /**
     * Set the {@link Traversal} that this step is contained within.
     *
     * @param traversal the new traversal for this step
     */
    public void setTraversal(final Traversal.Admin<?, ?> traversal);

    /**
     * Reset the state of the step such that it has no incoming starts.
     * Internal states are to be reset, but any sideEffect data structures are not to be recreated.
     */
    public void reset();

    /**
     * Cloning is used to duplicate steps for the purpose of traversal optimization and OLTP replication.
     * When cloning a step, it is important that the steps, the cloned step is equivalent to the state of the step when reset() is called.
     * Moreover, the previous and next steps should be set to {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.EmptyStep}.
     *
     * @return The cloned step
     */
    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    public Step<S, E> clone();

    /**
     * Get the labels of this step.
     * The labels are ordered by the order of the calls to {@link Step#addLabel}.
     *
     * @return the set of labels for this step
     */
    public Set<String> getLabels();

    /**
     * Add a label to this step.
     *
     * @param label the label to add to this step
     */
    public void addLabel(final String label);

    /**
     * Remove a label from this step.
     *
     * @param label the label to remove from this step
     */
    public void removeLabel(final String label);

    /**
     * Get the unique id of the step.
     * These ids can change when strategies are applied and anonymous traversals are embedded in the parent traversal.
     * A developer should typically not need to call this method.
     *
     * @param id the unique id of the step
     */
    public void setId(final String id);

    /**
     * Get the unique id of this step.
     *
     * @return the unique id of the step
     */
    public String getId();

    /**
     * Provide the necessary {@link org.apache.tinkerpop.gremlin.process.traversal.traverser.TraverserRequirement} that must be met by the traverser in order for the step to function properly.
     * The provided default implements returns an empty set.
     *
     * @return the set of requirements
     */
    public default Set getRequirements() {
        return Collections.emptySet();
    }

    /**
     * Compare the current step with another step.
     *
     * @param other      the other step
     * @param compareIds whether to compare step IDs or not
     * @return true if the steps are equal, otherwise false
     */
    public default boolean equals(final Step other, final boolean compareIds) {
        return (!compareIds || (other != null && this.getId().equals(other.getId()))) && this.equals(other);
    }
}
