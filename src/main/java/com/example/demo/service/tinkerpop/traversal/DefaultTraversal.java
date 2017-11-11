package com.example.demo.service.tinkerpop.traversal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.tinkerpop.graph.Edge;
import com.example.demo.service.tinkerpop.graph.Vertex;
import com.example.demo.service.tinkerpop.graph.neo4j.Neo4jEdge;
import com.example.demo.service.tinkerpop.graph.neo4j.Neo4jVertex;

import java.util.List;

/**
 * @author liwei
 *
 * @param <S>
 * @param <E>
 */
public class DefaultTraversal<S, E> implements GraphTraversal.Admin<S, E>,Cloneable{
	private static final long serialVersionUID = 1L;
	protected Bytecode bytecode; 


    public DefaultTraversal(final Bytecode bytecode) {
        this.bytecode = bytecode;
    }
    
    public Bytecode getBytecode() {
        return this.bytecode;
    }

	@Override
	public boolean hasNext() {
		Bytecode cloneBytecode = this.bytecode.clone();
		cloneBytecode.addStep(Symbols.hasNext);
		
		String script = cloneBytecode.toString();
    	Object result = Neo4jHttpClient.execute(script);
    	
		return (boolean) result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E next() {
		Bytecode cloneBytecode = this.bytecode.clone();
		cloneBytecode.addStep(Symbols.next);
    	
    	String script = cloneBytecode.toString();
    	Object result = Neo4jHttpClient.execute(script);
    	JSONArray jsonArray = JSONArray.parseArray((String) result);
    	
    	if(jsonArray.size()==0) return null;
    	
		JSONObject object = JSONObject.parseObject((String) jsonArray.get(0));
		
		if(object.containsValue(Vertex.DEFAULT_LABEL)) {
			return (E) JSONObject.parseObject(object.toString(), Neo4jVertex.class);
		} else if(object.containsValue(Edge.DEFAULT_LABEL)){
			return (E) JSONObject.parseObject(object.toString(), Neo4jEdge.class);
		} else {
			return null;
		}
    	
	}
	
	@Override
	public GraphTraversal.Admin<S, E> clone() {
		DefaultTraversal<S, E> clone;
		try {
			clone = (DefaultTraversal<S, E>) super.clone();
			clone.bytecode = this.bytecode.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	public List<Step> getSteps() {
		return null;
	}

	@Override
	public <S2, E2> Traversal.Admin<S2, E2> addStep(int index, Step<?, ?> step) throws IllegalStateException {
		return null;
	}
}
