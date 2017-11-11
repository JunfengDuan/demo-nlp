package com.example.demo.service.tinkerpop.graph;


import com.example.demo.service.tinkerpop.traversal.GraphTraversal;

import java.util.Iterator;

public interface Vertex extends Element {

    /**
     * The default label to use for a vertex.
     */
    public static final String DEFAULT_LABEL = "vertex";

    static final Object[] EMPTY_ARGS = new Object[0];
    
    @Override
    public default <V> VertexProperty<V> property(final String key) {
        final Iterator<VertexProperty<V>> iterator = this.properties(key);
        if (iterator.hasNext()) {
            final VertexProperty<V> property = iterator.next();
            if (iterator.hasNext())
                throw Vertex.Exceptions.multiplePropertiesExistForProvidedKey(key);
            else
                return property;
        } else {
            return VertexProperty.<V>empty();
        }
    }
  
    public GraphTraversal<Edge, Edge> outE(final String... edgeLabels);

    public GraphTraversal<Vertex, Vertex> out(final String... edgeLabels);
    
    public GraphTraversal<Edge, Edge> inE(final String... edgeLabels);

    public GraphTraversal<Vertex, Vertex> in(final String... edgeLabels);
    
    public GraphTraversal<Vertex, Vertex> self();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <V> Iterator<VertexProperty<V>> properties(final String... propertyKeys);

    /**
     * Common exceptions to use with a vertex.
     */
    public static class Exceptions {

        private Exceptions() {
        }

        public static UnsupportedOperationException userSuppliedIdsNotSupported() {
            return new UnsupportedOperationException("Vertex does not support user supplied identifiers");
        }

        public static UnsupportedOperationException userSuppliedIdsOfThisTypeNotSupported() {
            return new UnsupportedOperationException("Vertex does not support user supplied identifiers of this type");
        }

        public static IllegalStateException vertexRemovalNotSupported() {
            return new IllegalStateException("Vertex removal are not supported");
        }

        public static IllegalStateException edgeAdditionsNotSupported() {
            return new IllegalStateException("Edge additions not supported");
        }

        public static IllegalStateException multiplePropertiesExistForProvidedKey(final String propertyKey) {
            return new IllegalStateException("Multiple properties exist for the provided key, use Vertex.properties(" + propertyKey + ')');
        }
    }
}
