/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.example.demo.service.tinkerpop.graph.util;


import com.example.demo.service.tinkerpop.graph.Property;
import com.example.demo.service.tinkerpop.graph.Vertex;
import com.example.demo.service.tinkerpop.graph.VertexProperty;

import java.util.Collection;


/**
 * A collection of helpful methods for creating standard {@link Object#toString()} representations of graph-related
 * objects.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class StringFactory {

    private static final String V = "v";
    private static final String E = "e";
    private static final String P = "p";
    private static final String VP = "vp";
    private static final String L_BRACKET = "[";
    private static final String R_BRACKET = "]";
    private static final String COMMA_SPACE = ", ";
    private static final String COLON = ":";
    private static final String EMPTY_MAP = "{}";
    private static final String DOTS = "...";
    private static final String DASH = "-";
    private static final String ARROW = "->";
    private static final String STAR = "*";
    private static final String EMPTY_PROPERTY = "p[empty]";
    private static final String EMPTY_VERTEX_PROPERTY = "vp[empty]";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String STORAGE = "storage";

    private static final String featuresStartWith = "supports";
    private static final int prefixLength = featuresStartWith.length();

    private StringFactory() {
    }

    /**
     * Construct the representation for a {@link org.apache.tinkerpop.gremlin.structure.Vertex}.
     */
    public static String vertexString(final Vertex vertex) {
        return V + L_BRACKET + vertex.id() + R_BRACKET;
    }

    /**
     * Construct the representation for a {@link org.apache.tinkerpop.gremlin.structure.Property} or {@link org.apache.tinkerpop.gremlin.structure.Neo4jVertexProperty}.
     */
    public static String propertyString(final Property property) {
        if (property instanceof VertexProperty) {
            if (!property.isPresent()) return EMPTY_VERTEX_PROPERTY;
            final String valueString = String.valueOf(property.value());
            return VP + L_BRACKET + property.key() + ARROW + valueString.substring(0, Math.min(valueString.length(), 20)) + R_BRACKET;
        } else {
            if (!property.isPresent()) return EMPTY_PROPERTY;
            final String valueString = String.valueOf(property.value());
            return P + L_BRACKET + property.key() + ARROW + valueString.substring(0, Math.min(valueString.length(), 20)) + R_BRACKET;
        }
    }

    public static String storageString(final String internalString) {
        return STORAGE + L_BRACKET + internalString + R_BRACKET;
    }

    public static String removeEndBrackets(final Collection collection) {
        final String string = collection.toString();
        return string.substring(1, string.length() - 1);
    }

}
