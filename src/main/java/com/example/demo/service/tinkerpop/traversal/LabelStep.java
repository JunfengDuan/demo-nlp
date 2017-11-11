package com.example.demo.service.tinkerpop.traversal;

import com.example.demo.service.tinkerpop.graph.Element;

import java.util.Collections;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class LabelStep<S extends Element> extends MapStep<S, String> {

    public LabelStep(final Traversal.Admin traversal) {
        super(traversal);
    }

    @Override
    protected String map(final Traverser.Admin<S> traverser) {
        return traverser.get().label();
    }

   /* @Override
    public Set<TraverserRequirement> getRequirements() {
        return Collections.singleton(TraverserRequirement.OBJECT);
    }*/
}