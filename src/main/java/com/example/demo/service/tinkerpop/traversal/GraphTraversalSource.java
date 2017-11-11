package com.example.demo.service.tinkerpop.traversal;


import com.example.demo.service.tinkerpop.graph.Edge;
import com.example.demo.service.tinkerpop.graph.Vertex;

/**
 * @author liwei
 *
 */
public class GraphTraversalSource implements TraversalSource {
    private Bytecode bytecode;
    
    @Override
    public Bytecode getBytecode() {
        return this.bytecode;
    }
    
    public GraphTraversalSource(Bytecode bytecode) {
    	this.bytecode = bytecode;
    }
    
    @Override
    public GraphTraversal<Vertex, Vertex> V(final Object... vertexIds) {
    	Bytecode codeBytecode = this.getBytecode().clone();
    	codeBytecode.addStep(Symbols.V, vertexIds);
        return new DefaultTraversal(codeBytecode);
    }

    @Override
    public GraphTraversal<Edge, Edge> E(final Object... edgesIds) {
    	Bytecode codeBytecode = this.getBytecode().clone();
    	codeBytecode.addStep(Symbols.E, edgesIds);
        return new DefaultTraversal(codeBytecode);
    }
}
