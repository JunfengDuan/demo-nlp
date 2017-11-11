package com.example.demo.service.tinkerpop.traversal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liwei
 *
 */
public final class Bindings {

    private static final Bindings INSTANCE = new Bindings();
    private static final ThreadLocal<Map<Object, String>> MAP = new ThreadLocal<>();

    public <V> V of(final String variable, final V value) {
        Map<Object, String> map = MAP.get();
        if (null == map) {
            map = new HashMap<>();
            MAP.set(map);
        }
        map.put(value, variable);
        return value;
    }

    protected static <V> String getBoundVariable(final V value) {
        final Map<Object, String> map = MAP.get();
        return null == map ? null : map.get(value);
    }

    protected static void clear() {
        final Map<Object, String> map = MAP.get();
        if (null != map)
            map.clear();
    }

    public static Bindings instance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "bindings[" + Thread.currentThread().getName() + "]";
    }
}
