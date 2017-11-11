package com.example.demo.service.tinkerpop.traversal;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.tinkerpop.graph.Edge;
import com.example.demo.service.tinkerpop.graph.Vertex;
import com.example.demo.service.tinkerpop.graph.neo4j.Neo4jEdge;
import com.example.demo.service.tinkerpop.graph.neo4j.Neo4jVertex;

import java.io.Serializable;
import java.util.*;

public interface Traversal<S, E> extends Iterator<E>, Serializable, Cloneable{

	public Bytecode getBytecode();

    /**
     * Get the next n-number of results from the traversal.
     * If the traversal has less than n-results, then only that number of results are returned.
     *
     * @param amount the number of results to get
     * @return the n-results in a {@link List}
     */
    public default List<E> next(final int amount) {
        final List<E> result = new ArrayList<>();
        int counter = 0;
        while (counter++ < amount && this.hasNext()) {
            result.add(this.next());
        }
        return result;
    }
    
    /**
     * Put all the results into an {@link ArrayList}.
     *
     * @return the results in a list
     */
    @SuppressWarnings("unchecked")
	public default List<E> toList() {
    	Bytecode cloneBytecode = this.getBytecode();
    	cloneBytecode.addStep(Symbols.toList);
    	
    	List<E> list = new ArrayList<>();
    	String script = cloneBytecode.toString();
    	Object result = Neo4jHttpClient.execute(script);
    	JSONArray jsonArray = (JSONArray) result;
    	
    	for(int i=0;i<jsonArray.size();i++) {
    		JSONObject object = JSONObject.parseObject(jsonArray.get(i).toString());
    		if(object.containsValue(Vertex.DEFAULT_LABEL)) {
    			Neo4jVertex v = (Neo4jVertex) JSONObject.parseObject(object.toString(), Neo4jVertex.class);
    			list.add((E) v);
    		} else if(object.containsValue(Edge.DEFAULT_LABEL)){
    			Neo4jEdge e = (Neo4jEdge) JSONObject.parseObject(object.toString(), Neo4jEdge.class);
    			list.add((E) e);
    		} else {
    			
    		}
    	}
    	
    	return list;
    }

    /**
     * Put all the results into a {@link HashSet}.
     *
     * @return the results in a set
     */
    @SuppressWarnings("unchecked")
	public default Set<E> toSet() {
    	
    	Bytecode cloneBytecode = this.getBytecode();
    	cloneBytecode.addStep(Symbols.toSet);
    	
    	String script = cloneBytecode.toString();
    	Object result = Neo4jHttpClient.execute(script);
    	
    	
    	Set<E> set = new HashSet<>();
    	
    	JSONArray jsonArray = (JSONArray) result;
    	
    	for(int i=0;i<jsonArray.size();i++) {
    		JSONObject object = JSONObject.parseObject(jsonArray.get(i).toString());
    		if(object.containsValue(Vertex.DEFAULT_LABEL)) {
    			Neo4jVertex v = (Neo4jVertex) JSONObject.parseObject(object.toString(), Neo4jVertex.class);
    			set.add((E) v);
    		} else if(object.containsValue(Edge.DEFAULT_LABEL)){
    			Neo4jEdge e = (Neo4jEdge) JSONObject.parseObject(object.toString(), Neo4jEdge.class);
    			set.add((E) e);
    		} else {
    			
    		}
    	}
    	return set;
    }
    
    /**
     * Get access to administrative methods of the traversal via its accompanying {@link Traversal.Admin}.
     *
     * @return the admin of this traversal
     */
    public default Traversal.Admin<S, E> asAdmin() {
        return (Traversal.Admin<S, E>) this;
    }

    public interface Admin<S, E> extends Traversal<S, E> {

        /**
         * Get the {@link Bytecode} associated with the construction of this traversal.
         *
         * @return the byte code representation of the traversal
         */
        public Bytecode getBytecode();

        /**
         * Cloning is used to duplicate the traversal typically in OLAP environments.
         *
         * @return The cloned traversal
         */
        public Traversal.Admin<S, E> clone();

        /**
         * Get the {@link Step} instances associated with this traversal.
         * The steps are ordered according to their linked list structure as defined by {@link Step#getPreviousStep()} and {@link Step#getNextStep()}.
         *
         * @return the ordered steps of the traversal
         */
        public List<Step> getSteps();

        /**
         * Add a {@link Step} to the end of the traversal. This method should link the step to its next and previous step accordingly.
         *
         * @param step the step to add
         * @param <E2> the output of the step
         * @return the updated traversal
         */
        public default <E2> Traversal.Admin<S, E2> addStep(final Step<?, E2> step) throws IllegalStateException {
            return this.addStep(this.getSteps().size(), step);
        }

        /**
         * Add a {@link Step} to an arbitrary point in the traversal.
         *
         * @param index the location in the traversal to insert the step
         * @param step  the step to add
         * @param <S2>  the new start type of the traversal (if the added step was a start step)
         * @param <E2>  the new end type of the traversal (if the added step was an end step)
         * @return the newly modulated traversal
         */
        public <S2, E2> Traversal.Admin<S2, E2> addStep(final int index, final Step<?, ?> step) throws IllegalStateException;
    }
}
