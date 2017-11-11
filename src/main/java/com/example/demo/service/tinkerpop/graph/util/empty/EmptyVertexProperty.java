
package com.example.demo.service.tinkerpop.graph.util.empty;



import com.example.demo.service.tinkerpop.graph.Property;
import com.example.demo.service.tinkerpop.graph.Vertex;
import com.example.demo.service.tinkerpop.graph.VertexProperty;
import com.example.demo.service.tinkerpop.graph.util.StringFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;


public final class EmptyVertexProperty<V> implements VertexProperty<V> {

    private static final EmptyVertexProperty INSTANCE = new EmptyVertexProperty();

    public static <U> VertexProperty<U> instance() {
        return INSTANCE;
    }

    @Override
    public Vertex element() {
        throw Property.Exceptions.propertyDoesNotExist();
    }

    @Override
    public Object id() {
        throw Property.Exceptions.propertyDoesNotExist();
    }

    @Override
    public <U> Property<U> property(String key) {
        return Property.<U>empty();
    }

    @Override
    public String key() {
        throw Property.Exceptions.propertyDoesNotExist();
    }

    @Override
    public V value() throws NoSuchElementException {
        throw Property.Exceptions.propertyDoesNotExist();
    }

    @Override
    public boolean isPresent() {
        return false;
    }


    @Override
    public String toString() {
        return StringFactory.propertyString(this);
    }

    @Override
    public <U> Iterator<Property<U>> properties(String... propertyKeys) {
        return Collections.emptyIterator();
    }

	@Override
	public String label() {
		return "";
	}

	@Override
	public String type() {
		return "vertex";
	}
}
