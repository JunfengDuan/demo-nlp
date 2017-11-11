package com.example.demo.service.tinkerpop.graph.neo4j;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.tinkerpop.graph.Edge;
import com.example.demo.service.tinkerpop.graph.Vertex;
import com.example.demo.service.tinkerpop.graph.VertexProperty;
import com.example.demo.service.tinkerpop.traversal.Bytecode;
import com.example.demo.service.tinkerpop.traversal.DefaultTraversal;
import com.example.demo.service.tinkerpop.traversal.GraphTraversal;
import com.example.demo.service.tinkerpop.traversal.Symbols;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author liwei
 *
 */
public class Neo4jVertex implements Vertex {
	public Object id;
	public String label;
	private JSONObject properties;
	public String type;

	public Neo4jVertex() {
		super();
	}
	
	public void setId(Object id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setProperties(JSONObject properties) {
		this.properties = properties;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public Object id() {
		return this.id;
	}

	public String type() {
		return this.type;
	}
	
	@Override
	public String label() {
		return this.label;
	}

	@Override
	public <V> Iterator<VertexProperty<V>> properties(String... propertyKeys) {
		List<VertexProperty<V>> vertexPropertyList = new ArrayList<>();
		if(propertyKeys.length>0) {
			for(String key : propertyKeys) {
				if(this.properties.containsKey(key)) {
					JSONArray jsonArray = JSONArray.parseArray((String) this.properties.get(key));
					JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(0).toString());
					V o =(V)jsonObject.get("value");
					
					VertexProperty<V> neo4jVertexProperty = new Neo4jVertexProperty<V>(this, key, o);
					vertexPropertyList.add(neo4jVertexProperty);
				}
			}
		} else {
            Iterator<?> it = this.properties.keySet().iterator();

            while(it.hasNext()) {
				String key = it.next().toString();
				JSONArray jsonArray = JSONArray.parseArray((String) this.properties.get(key));
				JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(0).toString());
				@SuppressWarnings("unchecked")
				V o =(V)jsonObject.get("value");
				
				VertexProperty<V> neo4jVertexProperty = new Neo4jVertexProperty<V>(this, key, o);
				vertexPropertyList.add(neo4jVertexProperty);
			}
		}
		
		return vertexPropertyList.iterator();
	}

	@Override
	public GraphTraversal<Edge, Edge> outE(String... edgeLabels) {
		
    	Bytecode bytecode = new Bytecode();
    	bytecode.addStep(Symbols.V, this.id);
    	bytecode.addStep(Symbols.outE, edgeLabels);

    	return new DefaultTraversal(bytecode);
	}

	@Override
	public GraphTraversal<Vertex, Vertex> out(String... edgeLabels) {
    	
    	Bytecode bytecode = new Bytecode();
    	bytecode.addStep(Symbols.V, this.id);
    	bytecode.addStep(Symbols.out, edgeLabels);

    	return new DefaultTraversal(bytecode);
	}

	@Override
	public GraphTraversal<Edge, Edge> inE(String... edgeLabels) {
		Bytecode bytecode = new Bytecode();
    	bytecode.addStep(Symbols.V, this.id);
    	bytecode.addStep(Symbols.inE, edgeLabels);

    	return new DefaultTraversal(bytecode);
	}

	@Override
	public GraphTraversal<Vertex, Vertex> in(String... edgeLabels) {
		Bytecode bytecode = new Bytecode();
    	bytecode.addStep(Symbols.V, this.id);
    	bytecode.addStep(Symbols.in, edgeLabels);

    	return new DefaultTraversal(bytecode);
	}

	@Override
	public GraphTraversal<Vertex, Vertex> self() {
		Bytecode bytecode = new Bytecode();
    	bytecode.addStep(Symbols.V, this.id);

    	return new DefaultTraversal(bytecode);
	}
   
}
