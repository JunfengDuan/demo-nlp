package com.example.demo.service.tinkerpop.graph.neo4j;


import com.example.demo.service.tinkerpop.graph.Element;
import com.example.demo.service.tinkerpop.graph.Property;
import com.example.demo.service.tinkerpop.graph.util.StringFactory;

/**
 * @author liwei
 *
 * @param <V>
 */
public final class Neo4jProperty<V> implements Property<V> {

    protected final Element element;
    protected final String key;
    protected V value;
    protected boolean removed = false;

    public Neo4jProperty(final Element element, final String key, final V value) {
        this.element = element;
        this.key = key;
        this.value = value;
    }

    @Override
    public Element element() {
        return this.element;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public V value() {
        return this.value;
    }

    @Override
    public boolean isPresent() {
        return null != this.value;
    }

    @Override
    public String toString() {
        return StringFactory.propertyString(this);
    }
}