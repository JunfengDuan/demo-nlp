package com.example.demo.service.tinkerpop.traversal;



import com.example.demo.service.tinkerpop.graph.Edge;
import com.example.demo.service.tinkerpop.graph.Vertex;

public interface TraversalSource {

    public Bytecode getBytecode();
    
    public GraphTraversal<Vertex, Vertex> V(final Object... vertexIds);

    public GraphTraversal<Edge, Edge> E(final Object... edgesIds);
}
