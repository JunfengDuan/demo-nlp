package com.example.demo.service.tinkerpop.internal;


import com.example.demo.service.tinkerpop.Neo4jGraph;
import com.example.demo.service.tinkerpop.traversal.Bytecode;
import com.example.demo.service.tinkerpop.traversal.DefaultTraversal;
import com.example.demo.service.tinkerpop.traversal.GraphTraversalSource;
import org.springframework.stereotype.Component;

@Component
public class Neo4jGraphImpl implements Neo4jGraph {
	@Override
	public GraphTraversalSource traversal() {
		Bytecode bytecode = new Bytecode();
		return new GraphTraversalSource(bytecode);
	}

	@Override
	public DefaultTraversal it() {
		Bytecode bytecode = new Bytecode();
		return new DefaultTraversal(bytecode);
	}

	@Override
	public GraphTraversalSource traversal(Bytecode bytecode) {
		return new GraphTraversalSource(bytecode);
	}
}