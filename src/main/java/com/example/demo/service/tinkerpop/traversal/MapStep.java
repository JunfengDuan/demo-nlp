package com.example.demo.service.tinkerpop.traversal;
/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class MapStep<S, E> extends AbstractStep<S, E> {

    public MapStep(final Traversal.Admin traversal) {
        super(traversal);
    }

    @Override
    protected Traverser.Admin<E> processNextStart() {
        final Traverser.Admin<S> traverser = this.starts.next();
        return traverser.split(this.map(traverser), this);
    }

    protected abstract E map(final Traverser.Admin<S> traverser);

}