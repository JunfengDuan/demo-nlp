package com.example.demo.service.tinkerpop.graph;

import java.util.Iterator;


public interface Edge extends Element {

    /**
     * The default label to use for an edge.
     * This is typically never used as when an edge is created, an edge label is required to be specified.
     */
    public static final String DEFAULT_LABEL = "edge";

    /**
     * Get the outgoing/tail vertex of this edge.
     *
     * @return the outgoing vertex of the edge
     */
    public Vertex outVertex();

    /**
     * Get the incoming/head vertex of this edge.
     *
     * @return the incoming vertex of the edge
     */
    public Vertex inVertex();
//
//    /**
//     * Get both the outgoing and incoming vertices of this edge.
//     * The first vertex in the iterator is the outgoing vertex.
//     * The second vertex in the iterator is the incoming vertex.
//     *
//     * @return an iterator of the two vertices of this edge
//     */
//    public default Iterator<Vertex> bothVertices() {
//        return this.vertices(Direction.BOTH);
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> Iterator<Property<V>> properties(final String... propertyKeys);


    /**
     * Common exceptions to use with an edge.
     */
    public static class Exceptions extends Element.Exceptions {

        private Exceptions() {
        }

        public static UnsupportedOperationException userSuppliedIdsNotSupported() {
            return new UnsupportedOperationException("Edge does not support user supplied identifiers");
        }

        public static UnsupportedOperationException userSuppliedIdsOfThisTypeNotSupported() {
            return new UnsupportedOperationException("Edge does not support user supplied identifiers of this type");
        }

        public static IllegalStateException edgeRemovalNotSupported() {
            return new IllegalStateException("Edge removal are not supported");
        }
    }
}
