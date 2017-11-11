package com.example.demo.service.tinkerpop;


import com.example.demo.service.tinkerpop.traversal.Bytecode;
import com.example.demo.service.tinkerpop.traversal.DefaultTraversal;
import com.example.demo.service.tinkerpop.traversal.GraphTraversalSource;

/**
 * @author liwei
 *
 */
public interface Neo4jGraph {
    /**
     * 
     * @return GraphTraversalSource
     */
    public GraphTraversalSource traversal();
    
    /**
     * 
     *@return DefaultTraversal
     */
    public DefaultTraversal<?, ?> it();
    
    /**
     * @param bytecode
     * @return GraphTraversalSource
     */
    public GraphTraversalSource traversal(Bytecode bytecode);
}
