package com.example.demo.service.tinkerpop.graph;



import com.example.demo.service.tinkerpop.graph.util.iterator.IteratorUtils;

import java.util.*;

/**
 * @author liwei
 *
 */
public abstract interface Element {

    /**
     * Gets the unique identifier for the graph {@code Element}.
     *
     * @return The id of the element
     */
    public Object id();

    /**
     * Gets the label for the graph {@code Element} which helps categorize it.
     *
     * @return The label of the element
     */
    public String label();
    
    public String type();

    /**
     * Get the keys of the properties associated with this element.
     * The default implementation iterators the properties and stores the keys into a {@link HashSet}.
     *
     * @return The property key set
     */
    public default Set<String> keys() {
        final Set<String> keys = new HashSet<>();
        this.properties().forEachRemaining(property -> keys.add(property.key()));
        return Collections.unmodifiableSet(keys);
    }

    /**
     * Get a {@link Property} for the {@code Element} given its key.
     * The default implementation calls the raw {@link Element#properties}.
     */
    public default <V> Property<V> property(final String key) {
        final Iterator<? extends Property<V>> iterator = this.properties(key);
        return iterator.hasNext() ? iterator.next() : Property.<V>empty();
    }
    
    /**
     * Get the value of a {@link Property} given it's key.
     * The default implementation calls {@link Element#property} and then returns the associated value.
     *
     * @throws NoSuchElementException if the property does not exist on the {@code Element}.
     */
    public default <V> V value(final String key) throws NoSuchElementException {
        return this.<V>property(key).orElseThrow(() -> Property.Exceptions.propertyDoesNotExist(this,key));
    }

    /**
     * Get the values of properties as an {@link Iterator}.
     */
    public default <V> Iterator<V> values(final String... propertyKeys) {
        return IteratorUtils.map(this.<V>properties(propertyKeys), property -> property.value());
    }

    /**
     * Get an {@link Iterator} of properties.
     */
    public <V> Iterator<? extends Property<V>> properties(final String... propertyKeys);


    /**
     * Common exceptions to use with an element.
     */
    public static class Exceptions {

        protected Exceptions() {
        }

        public static IllegalArgumentException providedKeyValuesMustBeAMultipleOfTwo() {
            return new IllegalArgumentException("The provided key/value array length must be a multiple of two");
        }

        public static IllegalArgumentException providedKeyValuesMustHaveALegalKeyOnEvenIndices() {
            return new IllegalArgumentException("The provided key/value array must have a String or T on even array indices");
        }

        public static IllegalStateException propertyAdditionNotSupported() {
            return new IllegalStateException("Property addition is not supported");
        }

        public static IllegalArgumentException labelCanNotBeNull() {
            return new IllegalArgumentException("Label can not be null");
        }

        public static IllegalArgumentException labelCanNotBeEmpty() {
            return new IllegalArgumentException("Label can not be empty");
        }

        public static IllegalArgumentException labelCanNotBeAHiddenKey(final String label) {
            return new IllegalArgumentException("Label can not be a hidden key: " + label);
        }

        @Deprecated
        public static IllegalStateException elementAlreadyRemoved(final Class<? extends Element> clazz, final Object id) {
            return new IllegalStateException(String.format("%s with id %s was removed.", clazz.getSimpleName(), id));
        }
    }
}
