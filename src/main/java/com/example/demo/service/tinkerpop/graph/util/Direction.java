package com.example.demo.service.tinkerpop.graph.util;

public enum Direction {

    /**
     * Refers to an outgoing direction.
     */
    OUT,

    /**
     * Refers to an incoming direction.
     */
    IN,

    /**
     * Refers to either direction ({@link #IN} or {@link #OUT}).
     */
    BOTH;

    /**
     * The actual direction of an {@link Neo4jEdge} may only be {@link #IN} or {@link #OUT}, as defined in this array.
     */
    public static final Direction[] proper = new Direction[]{OUT, IN};

    /**
     * Produce the opposite representation of the current {@code Direction} enum.
     */
    public Direction opposite() {
        if (this.equals(OUT))
            return IN;
        else if (this.equals(IN))
            return OUT;
        else
            return BOTH;
    }
}
