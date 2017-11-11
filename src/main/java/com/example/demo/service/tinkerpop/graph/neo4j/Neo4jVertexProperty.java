package com.example.demo.service.tinkerpop.graph.neo4j;

import com.example.demo.service.tinkerpop.graph.Property;
import com.example.demo.service.tinkerpop.graph.Vertex;
import com.example.demo.service.tinkerpop.graph.VertexProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * @author liwei
 *
 * @param <V>
 */
public class Neo4jVertexProperty<V> implements VertexProperty<V> {
	private String key;
	private V value;
	private Vertex vertex;
	
	public Neo4jVertexProperty(Vertex vertex, String key, V value) {
		super();
		this.key = key;
		this.value = value;
		this.vertex = vertex;
	}

	@Override
	public String key() {
		return this.key;
	}

	@Override
	public V value() throws NoSuchElementException {
		return this.value;
	}

	@Override
	public boolean isPresent() {
		return  this.value!=null;
	}

	@Override
	public Object id() {
		return this.vertex.id();
	}

	@Override
	public String label() {
		return this.vertex.label();
	}

	@Override
	public String type() {
		return this.vertex.type();
	}
	
	@Override
	public Vertex element() {
		return this.vertex;
	}

	@Override
	public <U> Iterator<Property<U>> properties(String... propertyKeys) {
		List<Property<U>> PropertyList = new ArrayList<>();
		
		Iterator<VertexProperty<Object>> VertexPropertyList= this.vertex.properties(propertyKeys);
		while(VertexPropertyList.hasNext()) {
			VertexProperty<Object> VertexProperty = VertexPropertyList.next();
			PropertyList.add(new Neo4jProperty(this.vertex, VertexProperty.key(), VertexProperty.value()));
		}
		
		return PropertyList.iterator();
	}
}
