package com.example.demo.service.tinkerpop.graph;

import com.example.demo.service.tinkerpop.graph.util.empty.EmptyVertexProperty;

import java.util.Iterator;

public interface VertexProperty<V> extends Property<V>, Element {

    public static final String DEFAULT_LABEL = "vertexProperty";

    public enum Cardinality {
        single, list, set
    }

    /**
     * Gets the {@link Vertex} that owns this {@code VertexProperty}.
     */
    @Override
    public Vertex element();


    /**
     * {@inheritDoc}
     */
    @Override
    public default String label() {
        return this.key();
    }

    /**
     * Constructs an empty {@code VertexProperty}.
     */
    public static <V> VertexProperty<V> empty() {
        return EmptyVertexProperty.instance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Iterator<Property<U>> properties(final String... propertyKeys);


    /**
     * Common exceptions to use with a property.
     */
    public static class Exceptions {

        private Exceptions() {
        }

        public static UnsupportedOperationException userSuppliedIdsNotSupported() {
            return new UnsupportedOperationException("VertexProperty does not support user supplied identifiers");
        }

        public static UnsupportedOperationException userSuppliedIdsOfThisTypeNotSupported() {
            return new UnsupportedOperationException("VertexProperty does not support user supplied identifiers of this type");
        }

        public static UnsupportedOperationException multiPropertiesNotSupported() {
            return new UnsupportedOperationException("Multiple properties on a vertex is not supported");
        }

        public static UnsupportedOperationException identicalMultiPropertiesNotSupported() {
            return new UnsupportedOperationException("Multiple properties on a vertex is supported, but a single key may not hold the same value more than once");
        }

        public static UnsupportedOperationException metaPropertiesNotSupported() {
            return new UnsupportedOperationException("Properties on a vertex property is not supported");
        }
    }
}
