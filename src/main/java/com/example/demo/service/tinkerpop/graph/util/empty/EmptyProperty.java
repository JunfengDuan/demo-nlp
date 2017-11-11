package com.example.demo.service.tinkerpop.graph.util.empty;

import com.example.demo.service.tinkerpop.graph.Element;
import com.example.demo.service.tinkerpop.graph.Property;
import com.example.demo.service.tinkerpop.graph.util.StringFactory;

import java.util.NoSuchElementException;


public final class EmptyProperty<V> implements Property<V> {

    private static final EmptyProperty INSTANCE = new EmptyProperty();

    private EmptyProperty() {

    }

    @Override
    public String key() {
        throw Exceptions.propertyDoesNotExist();
    }

    @Override
    public V value() throws NoSuchElementException {
        throw Exceptions.propertyDoesNotExist();
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public Element element() {
        throw Exceptions.propertyDoesNotExist();
    }

    @Override
    public String toString() {
        return StringFactory.propertyString(this);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(final Object object) {
        return object instanceof EmptyProperty;
    }

    public int hashCode() {
        return 1281483122;
    }

    public static <V> Property<V> instance() {
        return INSTANCE;
    }
}
