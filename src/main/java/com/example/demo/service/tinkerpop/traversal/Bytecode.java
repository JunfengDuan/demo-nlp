package com.example.demo.service.tinkerpop.traversal;


import com.example.demo.service.tinkerpop.graph.util.StringFactory;

import java.io.Serializable;
import java.util.*;

public final class Bytecode implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;

	private static final Object[] EMPTY_ARRAY = new Object[]{};

    private List<Instruction> stepInstructions = new ArrayList<>();

    /**
     * Add a {@link Traversal} instruction to the bytecode.
     *
     * @param stepName  the traversal method name (e.g. out())
     * @param arguments the traversal method arguments
     */
    public void addStep(final String stepName, final Object... arguments) {
        this.stepInstructions.add(new Instruction(stepName, flattenArguments(arguments)));
        Bindings.clear();
    }

    /**
     * Get the {@link Traversal} instructions associated with this bytecode.
     *
     * @return an iterable of instructions
     */
    public Iterable<Instruction> getStepInstructions() {
        return this.stepInstructions;
    }

    /**
     * Get all the bindings (in a nested, recursive manner) from all the arguments of all the instructions of this bytecode.
     *
     * @return a map of string variable and object value bindings
     */
    public Map<String, Object> getBindings() {
        final Map<String, Object> bindingsMap = new HashMap<>();
       
        for (final Instruction instruction : this.stepInstructions) {
            for (final Object argument : instruction.getArguments()) {
                addArgumentBinding(bindingsMap, argument);
            }
        }
        return bindingsMap;
    }

    private static final void addArgumentBinding(final Map<String, Object> bindingsMap, final Object argument) {
        if (argument instanceof Binding)
            bindingsMap.put(((Binding) argument).key, ((Binding) argument).value);
        else if (argument instanceof Map) {
            for (final Map.Entry<?, ?> entry : ((Map<?, ?>) argument).entrySet()) {
                addArgumentBinding(bindingsMap, entry.getKey());
                addArgumentBinding(bindingsMap, entry.getValue());
            }
        } else if (argument instanceof Collection) {
            for (final Object item : (Collection) argument) {
                addArgumentBinding(bindingsMap, item);
            }
        } else if (argument instanceof Bytecode) {
        	bindingsMap.putAll(((Bytecode) argument).getBindings());
        }
    }

    @Override
    public String toString() {
    	String script = "";
    	for(final Instruction instruction : this.stepInstructions) {
    		script += instruction.toString() + "." ;
		 }
    	
    	if(script=="") {
    		return script;
    	} else {
    		return script.substring(0, script.length()-1);
    	}
    }

    @Override
    public boolean equals(final Object object) {
        return object instanceof Bytecode &&
                this.stepInstructions.equals(((Bytecode) object).stepInstructions);
    }

    @Override
    public int hashCode() {
        return this.stepInstructions.hashCode();
    }

    @Override
    public Bytecode clone() {
        try {
            final Bytecode clone = (Bytecode) super.clone();
            clone.stepInstructions = new ArrayList<>(this.stepInstructions);
            return clone;
        } catch (final CloneNotSupportedException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static class Instruction implements Serializable {

        private final String operator;
        private final Object[] arguments;

        private Instruction(final String operator, final Object... arguments) {
            this.operator = operator;
            this.arguments = arguments;
        }

        public String getOperator() {
            return this.operator;
        }

        public Object[] getArguments() {
            return this.arguments;
        }

        @Override
        public String toString() {
            return this.operator + "(" + StringFactory.removeEndBrackets(Arrays.asList(this.arguments)) + ")";
        }

        @Override
        public boolean equals(final Object object) {
            return object instanceof Instruction &&
                    this.operator.equals(((Instruction) object).operator) &&
                    Arrays.equals(this.arguments, ((Instruction) object).arguments);
        }

        @Override
        public int hashCode() {
            return this.operator.hashCode() + Arrays.hashCode(this.arguments);
        }
    }

    public static class Binding<V> implements Serializable {

        private final String key;
        private final V value;

        public Binding(final String key, final V value) {
            this.key = key;
            this.value = value;
        }

        public String variable() {
            return this.key;
        }

        public V value() {
            return this.value;
        }

        @Override
        public String toString() {
            return "binding[" + this.key + "=" + this.value + "]";
        }

        @Override
        public boolean equals(final Object object) {
            return object instanceof Binding &&
                    this.key.equals(((Binding) object).key) &&
                    this.value.equals(((Binding) object).value);
        }

        @Override
        public int hashCode() {
            return this.key.hashCode() + this.value.hashCode();
        }
    }

    /////

    private final Object[] flattenArguments(final Object... arguments) {
        if (arguments.length == 0)
            return EMPTY_ARRAY;
        final List<Object> flatArguments = new ArrayList<>();
        for (final Object object : arguments) {
            if (object instanceof Object[]) {
                for (final Object nestObject : (Object[]) object) {
                    flatArguments.add(convertArgument(nestObject, true));
                }
            } else
                flatArguments.add(convertArgument(object, true));
        }
        return flatArguments.toArray();
    }

    private final Object convertArgument(final Object argument, final boolean searchBindings) {
        if (searchBindings) {
            final String variable = Bindings.getBoundVariable(argument);
            if (null != variable)
                return new Binding<>(variable, convertArgument(argument, false));
        }
        //
        if (argument instanceof Traversal)
            return ((Traversal) argument).getBytecode();
        else if (argument instanceof Map) {
            final Map<Object, Object> map = new LinkedHashMap<>(((Map) argument).size());
            for (final Map.Entry<?, ?> entry : ((Map<?, ?>) argument).entrySet()) {
                map.put(convertArgument(entry.getKey(), true), convertArgument(entry.getValue(), true));
            }
            return map;
        } else if (argument instanceof List) {
            final List<Object> list = new ArrayList<>(((List) argument).size());
            for (final Object item : (List) argument) {
                list.add(convertArgument(item, true));
            }
            return list;
        } else if (argument instanceof Set) {
            final Set<Object> set = new LinkedHashSet<>(((Set) argument).size());
            for (final Object item : (Set) argument) {
                set.add(convertArgument(item, true));
            }
            return set;
        } else if(argument instanceof Long){
        	return argument;
        } else {
        	return "\"" + argument + "\"";
        }
            
    }
}
