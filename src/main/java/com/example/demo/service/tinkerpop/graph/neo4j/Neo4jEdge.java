package com.example.demo.service.tinkerpop.graph.neo4j;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.tinkerpop.graph.Edge;
import com.example.demo.service.tinkerpop.graph.Property;
import com.example.demo.service.tinkerpop.graph.Vertex;
import com.example.demo.service.tinkerpop.traversal.Bytecode;
import com.example.demo.service.tinkerpop.traversal.Neo4jHttpClient;
import com.example.demo.service.tinkerpop.traversal.Symbols;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author liwei
 *
 */
public class Neo4jEdge implements Edge {
	public String id;
	public String label;
	public String type;
	public String inV;
	public String inVLabel;
	public String outV;
	public String outVLabel;
	
	private JSONObject properties;
	
	public void setId(String id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setProperties(JSONObject properties) {
		this.properties = properties;
	}
	
	public void setInV(String inV) {
		this.inV = inV;
	}

	public void setInVLabel(String inVLabel) {
		this.inVLabel = inVLabel;
	}

	public void setOutV(String outV) {
		this.outV = outV;
	}

	public void setOutVLabel(String outVLabel) {
		this.outVLabel = outVLabel;
	}

	public Neo4jEdge() {
		super();
	}
	
	@Override
	public Object id() {
		return this.id;
	}

	@Override
	public String label() {
		return this.label;
	}

	@Override
	public String type() {
		return this.type;
	}

	@Override
	public <V> Iterator<Property<V>> properties(String... propertyKeys) {
		List<Property<V>> propertyList = new ArrayList<>();
		if(propertyKeys.length>0) {
			for(String key : propertyKeys) {
				if(this.properties.containsKey(key)) {
					@SuppressWarnings("unchecked")
					V o = (V) this.properties.get(key);
					Property<V> neo4jProperty = new Neo4jProperty<V>(this, key, o);
					propertyList.add(neo4jProperty);
				}
			}
		} else {
			Iterator<?> it = this.properties.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next().toString();
				V o = (V) this.properties.get(key);
				Property<V> neo4jProperty = new Neo4jProperty<V>(this, key, o);
				propertyList.add(neo4jProperty);
			}
		}
		
		return propertyList.iterator();
	}

	@Override
	public Vertex outVertex() {
    	Bytecode bytecode = new Bytecode();
    	bytecode.addStep(Symbols.V, this.outV);
    	bytecode.addStep(Symbols.next);
    	
    	Object result = Neo4jHttpClient.execute(bytecode.toString());
    	JSONArray jsonArray = JSONArray.parseArray((String) result);
    	
		JSONObject object = JSONObject.parseObject(jsonArray.get(0).toString());
		Neo4jVertex v = (Neo4jVertex) JSONObject.parseObject(object.toString(), Neo4jVertex.class);
    	
    	return v;
	}

	@Override
	public Vertex inVertex() {
		Bytecode bytecode = new Bytecode();
    	bytecode.addStep(Symbols.V, this.inV);
    	bytecode.addStep(Symbols.next);
    	
    	Object result = Neo4jHttpClient.execute(bytecode.toString());
    	JSONArray jsonArray = JSONArray.parseArray((String) result);
    	
		JSONObject object = JSONObject.parseObject(jsonArray.get(0).toString());
		Neo4jVertex v = (Neo4jVertex) JSONObject.parseObject(object.toString(), Neo4jVertex.class);
    	
    	return v;
	}
}
