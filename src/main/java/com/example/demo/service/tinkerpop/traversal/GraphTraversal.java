package com.example.demo.service.tinkerpop.traversal;


import com.example.demo.service.tinkerpop.graph.Edge;
import com.example.demo.service.tinkerpop.graph.Vertex;

public interface GraphTraversal<S, E> extends Traversal<S, E> {
	
	public interface Admin<S, E> extends Traversal.Admin<S, E>, GraphTraversal<S, E> {

        @Override
        public default <E2> GraphTraversal.Admin<S, E2> addStep(final Step<?, E2> step) {
            return (GraphTraversal.Admin<S, E2>) Traversal.Admin.super.addStep((Step) step);
        }

        /*@Override
        public default GraphTraversal<S, E> iterate() {
            return GraphTraversal.super.iterate();
        }
*/
        @Override
        public GraphTraversal.Admin<S, E> clone();
    }

    @Override
    public default GraphTraversal.Admin<S, E> asAdmin() {
        return (GraphTraversal.Admin<S, E>) this;
    }
    
    ///////////////////// MAP STEPS /////////////////////

//    /**
//     * Map a {@link Traverser} referencing an object of type <code>E</code> to an object of type <code>E2</code>.
//     *
//     * @param function the lambda expression that does the functional mapping
//     * @return the traversal with an appended {@link LambdaMapStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#general-steps" target="_blank">Reference Documentation - General Steps</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> map(final Function<Traverser<E>, E2> function) {
//        this.getBytecode().addStep(Symbols.map, function);
//        return this.addStep(new LambdaMapStep<>(this.asAdmin(), function));
//    }
//
//    /**
//     * Map a {@link Traverser} referencing an object of type <code>E</code> to an object of type <code>E2</code>.
//     *
//     * @param mapTraversal the traversal expression that does the functional mapping
//     * @return the traversal with an appended {@link LambdaMapStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#general-steps" target="_blank">Reference Documentation - General Steps</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> map(final Traversal<?, E2> mapTraversal) {
//        this.getBytecode().addStep(Symbols.map, mapTraversal);
//        return this.addStep(new TraversalMapStep<>(this.asAdmin(), mapTraversal));
//    }
//
//    /**
//     * Map a {@link Traverser} referencing an object of type <code>E</code> to an iterator of objects of type <code>E2</code>.
//     * The resultant iterator is drained one-by-one before a new <code>E</code> object is pulled in for processing.
//     *
//     * @param function the lambda expression that does the functional mapping
//     * @param <E2>     the type of the returned iterator objects
//     * @return the traversal with an appended {@link LambdaFlatMapStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#general-steps" target="_blank">Reference Documentation - General Steps</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> flatMap(final Function<Traverser<E>, Iterator<E2>> function) {
//        this.getBytecode().addStep(Symbols.flatMap, function);
//        return this.addStep(new LambdaFlatMapStep<>(this.asAdmin(), function));
//    }
//
//    /**
//     * Map a {@link Traverser} referencing an object of type <code>E</code> to an iterator of objects of type <code>E2</code>.
//     * The internal traversal is drained one-by-one before a new <code>E</code> object is pulled in for processing.
//     *
//     * @param flatMapTraversal the traversal generating objects of type <code>E2</code>
//     * @param <E2>             the end type of the internal traversal
//     * @return the traversal with an appended {@link TraversalFlatMapStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#general-steps" target="_blank">Reference Documentation - General Steps</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> flatMap(final Traversal<?, E2> flatMapTraversal) {
//        this.getBytecode().addStep(Symbols.flatMap, flatMapTraversal);
//        return this.addStep(new TraversalFlatMapStep<>(this.asAdmin(), flatMapTraversal));
//    }
//
//    /**
//     * Map the {@link Element} to its {@link Element#id}.
//     *
//     * @return the traversal with an appended {@link IdStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#id-step" target="_blank">Reference Documentation - Id Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, Object> id() {
//        this.getBytecode().addStep(Symbols.id);
//        return this.addStep(new IdStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Map the {@link Element} to its {@link Element#label}.
//     *
//     * @return the traversal with an appended {@link LabelStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#label-step" target="_blank">Reference Documentation - Label Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, String> label() {
//        this.getBytecode().addStep(Symbols.label);
//        return this.addStep(new LabelStep<>(this.asAdmin()));
//    }

    public default GraphTraversal<S, E> label() {
        Admin<S, E> clone = (Admin<S, E>) this.asAdmin().clone();
        clone.getBytecode().addStep(Symbols.label);
        return clone;
    }
//
//    /**
//     * Map the <code>E</code> object to itself. In other words, a "no op."
//     *
//     * @return the traversal with an appended {@link IdentityStep}.
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> identity() {
//        this.getBytecode().addStep(Symbols.identity);
//        return this.addStep(new IdentityStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Map any object to a fixed <code>E</code> value.
//     *
//     * @return the traversal with an appended {@link ConstantStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#constant-step" target="_blank">Reference Documentation - Constant Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> constant(final E2 e) {
//        this.getBytecode().addStep(Symbols.constant, e);
//        return this.addStep(new ConstantStep<E, E2>(this.asAdmin(), e));
//    }
//
//    /**
//     * A {@code V} step is usually used to start a traversal but it may also be used mid-traversal.
//     *
//     * @param vertexIdsOrElements vertices to inject into the traversal
//     * @return the traversal with an appended {@link GraphStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#graph-step" target="_blank">Reference Documentation - Graph Step</a>
//     * @since 3.1.0-incubating
//     */
//    public default GraphTraversal<S, Vertex> V(final Object... vertexIdsOrElements) {
//        this.getBytecode().addStep(Symbols.V, vertexIdsOrElements);
//        return this.addStep(new GraphStep<>(this.asAdmin(), Vertex.class, false, vertexIdsOrElements));
//    }
//
//    /**
//     * Map the {@link Vertex} to its adjacent vertices given a direction and edge labels.
//     *
//     * @param direction  the direction to traverse from the current vertex
//     * @param edgeLabels the edge labels to traverse
//     * @return the traversal with an appended {@link VertexStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, Vertex> to(final Direction direction, final String... edgeLabels) {
//        this.getBytecode().addStep(Symbols.to, direction, edgeLabels);
//        return this.addStep(new VertexStep<>(this.asAdmin(), Vertex.class, direction, edgeLabels));
//    }

    /**
     * Map the {@link Vertex} to its outgoing adjacent vertices given the edge labels.
     *
     * @param edgeLabels the edge labels to traverse
     * @return the traversal with an appended {@link VertexStep}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, Vertex> out(final String... edgeLabels) {
    	Admin<S, Vertex> clone = (Admin<S, Vertex>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.out, edgeLabels);
        return clone;
    }

    /**
     * Map the {@link Vertex} to its incoming adjacent vertices given the edge labels.
     *
     * @param edgeLabels the edge labels to traverse
     * @return the traversal with an appended {@link VertexStep}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, Vertex> in(final String... edgeLabels) {
    	Admin<S, Vertex> clone = (Admin<S, Vertex>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.in, edgeLabels);
        return clone;
    }

    /**
     * Map the {@link Vertex} to its adjacent vertices given the edge labels.
     *
     * @param edgeLabels the edge labels to traverse
     * @return the traversal with an appended {@link VertexStep}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, Vertex> both(final String... edgeLabels) {
    	Admin<S, Vertex> clone = (Admin<S, Vertex>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.both, edgeLabels);
        return clone;
    }

//    /**
//     * Map the {@link Vertex} to its incident edges given the direction and edge labels.
//     *
//     * @param direction  the direction to traverse from the current vertex
//     * @param edgeLabels the edge labels to traverse
//     * @return the traversal with an appended {@link VertexStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, Edge> toE(final Direction direction, final String... edgeLabels) {
//        this.getBytecode().addStep(Symbols.toE, direction, edgeLabels);
//        return this.addStep(new VertexStep<>(this.asAdmin(), Edge.class, direction, edgeLabels));
//    }

    /**
     * Map the {@link Vertex} to its outgoing incident edges given the edge labels.
     *
     * @param edgeLabels the edge labels to traverse
     * @return the traversal with an appended {@link VertexStep}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
     * @since 3.0.0-incubating
     */
	public default GraphTraversal<S, Edge> outE(final String... edgeLabels) {
		Admin<S, Edge> clone = (Admin<S, Edge>) this.asAdmin().clone();
		clone.getBytecode().addStep(Symbols.outE, edgeLabels);
        return clone;
    }

    /**
     * Map the {@link Vertex} to its incoming incident edges given the edge labels.
     *
     * @param edgeLabels the edge labels to traverse
     * @return the traversal with an appended {@link VertexStep}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, Edge> inE(final String... edgeLabels) {
    	Admin<S, Edge> clone = (Admin<S, Edge>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.inE, edgeLabels);
        return clone;
    }

    /**
     * Map the {@link Vertex} to its incident edges given the edge labels.
     *
     * @param edgeLabels the edge labels to traverse
     * @return the traversal with an appended {@link VertexStep}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, Edge> bothE(final String... edgeLabels) {
    	Admin<S, Edge> clone = (Admin<S, Edge>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.bothE, edgeLabels);
        return clone;
    }

//    /**
//     * Map the {@link Edge} to its incident vertices given the direction.
//     *
//     * @param direction the direction to traverser from the current edge
//     * @return the traversal with an appended {@link EdgeVertexStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, Vertex> toV(final Direction direction) {
//        this.getBytecode().addStep(Symbols.toV, direction);
//        return this.addStep(new EdgeVertexStep(this.asAdmin(), direction));
//    }

    /**
     * Map the {@link Edge} to its incoming/head incident {@link Vertex}.
     *
     * @return the traversal with an appended {@link EdgeVertexStep}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, Vertex> inV() {
    	Admin<S, Vertex> clone = (Admin<S, Vertex>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.inV);
        return clone;
    }

    /**
     * Map the {@link Edge} to its outgoing/tail incident {@link Vertex}.
     *
     * @return the traversal with an appended {@link EdgeVertexStep}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
     * @since 3.0.0-incubating
     */
    @SuppressWarnings("unchecked")
	public default GraphTraversal<S, Vertex> outV() {
    	Admin<S, Vertex> clone = (Admin<S, Vertex>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.outV);
        return clone;
    }

    /**
     * Map the {@link Edge} to its incident vertices.
     *
     * @return the traversal with an appended {@link EdgeVertexStep}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
     * @since 3.0.0-incubating
     */
    @SuppressWarnings("unchecked")
	public default GraphTraversal<S, Vertex> bothV() {
    	Admin<S, Vertex> clone = (Admin<S, Vertex>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.bothV);
        return clone;
    }

    /**
     * Map the {@link Edge} to the incident vertex that was not just traversed from in the path history.
     *
     * @return the traversal with an appended {@link EdgeOtherVertexStep}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#vertex-steps" target="_blank">Reference Documentation - Vertex Step</a>
     * @since 3.0.0-incubating
     */
    @SuppressWarnings("unchecked")
	public default GraphTraversal<S, Vertex> otherV() {
    	Admin<S, Vertex> clone = (Admin<S, Vertex>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.otherV);
        return clone;
    }

    /**
     * Order all the objects in the traversal up to this point and then emit them one-by-one in their ordered sequence.
     *
     * @return the traversal with an appended {@link OrderGlobalStep}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#order-step" target="_blank">Reference Documentation - Order Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, E> order() {
    	Admin<S, E> clone = (Admin<S, E>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.order);
        return clone;
    }

    /**
     * Order either the {@link Scope#local} object (e.g. a list, map, etc.) or the entire {@link Scope#global} traversal stream.
     *
     * @param scope whether the ordering is the current local object or the entire global stream.
     * @return the traversal with an appended {@link OrderGlobalStep} or {@link OrderLocalStep} depending on the {@code scope}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#order-step" target="_blank">Reference Documentation - Order Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, E> order(final String scope) {
    	Admin<S, E> clone = (Admin<S, E>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.order, scope);
        return clone;
    }

//    /**
//     * Map the {@link Element} to its associated properties given the provide property keys.
//     * If no property keys are provided, then all properties are emitted.
//     *
//     * @param propertyKeys the properties to retrieve
//     * @param <E2>         the value type of the returned properties
//     * @return the traversal with an appended {@link PropertiesStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#properties-step" target="_blank">Reference Documentation - Properties Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, ? extends Property<E2>> properties(final String... propertyKeys) {
//        this.getBytecode().addStep(Symbols.properties, propertyKeys);
//        return this.addStep(new PropertiesStep<>(this.asAdmin(), PropertyType.PROPERTY, propertyKeys));
//    }
//
//    /**
//     * Map the {@link Element} to the values of the associated properties given the provide property keys.
//     * If no property keys are provided, then all property values are emitted.
//     *
//     * @param propertyKeys the properties to retrieve their value from
//     * @param <E2>         the value type of the properties
//     * @return the traversal with an appended {@link PropertiesStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#values-step" target="_blank">Reference Documentation - Values Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> values(final String... propertyKeys) {
//        this.getBytecode().addStep(Symbols.values, propertyKeys);
//        return this.addStep(new PropertiesStep<>(this.asAdmin(), PropertyType.VALUE, propertyKeys));
//    }
//
//    /**
//     * Map the {@link Element} to a {@link Map} of the properties key'd according to their {@link Property#key}.
//     * If no property keys are provided, then all properties are retrieved.
//     *
//     * @param propertyKeys the properties to retrieve
//     * @param <E2>         the value type of the returned properties
//     * @return the traversal with an appended {@link PropertyMapStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#propertymap-step" target="_blank">Reference Documentation - PropertyMap Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, Map<String, E2>> propertyMap(final String... propertyKeys) {
//        this.getBytecode().addStep(Symbols.propertyMap, propertyKeys);
//        return this.addStep(new PropertyMapStep<>(this.asAdmin(), false, PropertyType.PROPERTY, propertyKeys));
//    }
//
//    /**
//     * Map the {@link Element} to a {@link Map} of the property values key'd according to their {@link Property#key}.
//     * If no property keys are provided, then all property values are retrieved.
//     *
//     * @param propertyKeys the properties to retrieve
//     * @param <E2>         the value type of the returned properties
//     * @return the traversal with an appended {@link PropertyMapStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#valuemap-step" target="_blank">Reference Documentation - ValueMap Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, Map<String, E2>> valueMap(final String... propertyKeys) {
//        this.getBytecode().addStep(Symbols.valueMap, propertyKeys);
//        return this.addStep(new PropertyMapStep<>(this.asAdmin(), false, PropertyType.VALUE, propertyKeys));
//    }
//
//    /**
//     * Map the {@link Element} to a {@link Map} of the property values key'd according to their {@link Property#key}.
//     * If no property keys are provided, then all property values are retrieved.
//     *
//     * @param includeTokens whether to include {@link T} tokens in the emitted map.
//     * @param propertyKeys  the properties to retrieve
//     * @param <E2>          the value type of the returned properties
//     * @return the traversal with an appended {@link PropertyMapStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#valuemap-step" target="_blank">Reference Documentation - ValueMap Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, Map<String, E2>> valueMap(final boolean includeTokens, final String... propertyKeys) {
//        this.getBytecode().addStep(Symbols.valueMap, includeTokens, propertyKeys);
//        return this.addStep(new PropertyMapStep<>(this.asAdmin(), includeTokens, PropertyType.VALUE, propertyKeys));
//    }
//
//    /**
//     * @since 3.0.0-incubating
//     * @deprecated As of release 3.1.0, replaced by {@link GraphTraversal#select(Column)}
//     */
//    @Deprecated
//    public default <E2> GraphTraversal<S, E2> mapValues() {
//        return this.select(Column.values).unfold();
//    }
//
//    /**
//     * @since 3.0.0-incubating
//     * @deprecated As of release 3.1.0, replaced by {@link GraphTraversal#select(Column)}
//     */
//    @Deprecated
//    public default <E2> GraphTraversal<S, E2> mapKeys() {
//        return this.select(Column.keys).unfold();
//    }
//
//    /**
//     * Map the {@link Property} to its {@link Property#key}.
//     *
//     * @return the traversal with an appended {@link PropertyKeyStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#key-step" target="_blank">Reference Documentation - Key Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, String> key() {
//        this.getBytecode().addStep(Symbols.key);
//        return this.addStep(new PropertyKeyStep(this.asAdmin()));
//    }
//
//    /**
//     * Map the {@link Property} to its {@link Property#value}.
//     *
//     * @return the traversal with an appended {@link PropertyValueStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#value-step" target="_blank">Reference Documentation - Value Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> value() {
//        this.getBytecode().addStep(Symbols.value);
//        return this.addStep(new PropertyValueStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Map the {@link Traverser} to its {@link Path} history via {@link Traverser#path}.
//     *
//     * @return the traversal with an appended {@link PathStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#path-step" target="_blank">Reference Documentation - Path Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, Path> path() {
//        this.getBytecode().addStep(Symbols.path);
//        return this.addStep(new PathStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Map the {@link Traverser} to a {@link Map} of bindings as specified by the provided match traversals.
//     *
//     * @param matchTraversals the traversal that maintain variables which must hold for the life of the traverser
//     * @param <E2>            the type of the obejcts bound in the variables
//     * @return the traversal with an appended {@link MatchStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#match-step" target="_blank">Reference Documentation - Match Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, Map<String, E2>> match(final Traversal<?, ?>... matchTraversals) {
//        this.getBytecode().addStep(Symbols.match, matchTraversals);
//        return this.addStep(new MatchStep<>(this.asAdmin(), ConnectiveStep.Connective.AND, matchTraversals));
//    }
//
//    /**
//     * Map the {@link Traverser} to its {@link Traverser#sack} value.
//     *
//     * @param <E2> the sack value type
//     * @return the traversal with an appended {@link SackStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#sack-step" target="_blank">Reference Documentation - Sack Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> sack() {
//        this.getBytecode().addStep(Symbols.sack);
//        return this.addStep(new SackStep<>(this.asAdmin()));
//    }
//
//    /**
//     * If the {@link Traverser} supports looping then calling this method will extract the number of loops for that
//     * traverser.
//     *
//     * @return the traversal with an appended {@link LoopsStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#loops-step" target="_blank">Reference Documentation - Loops Step</a>
//     * @since 3.1.0-incubating
//     */
//    public default GraphTraversal<S, Integer> loops() {
//        this.getBytecode().addStep(Symbols.loops);
//        return this.addStep(new LoopsStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Projects the current object in the stream into a {@code Map} that is keyed by the provided labels.
//     *
//     * @return the traversal with an appended {@link ProjectStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#project-step" target="_blank">Reference Documentation - Project Step</a>
//     * @since 3.2.0-incubating
//     */
//    public default <E2> GraphTraversal<S, Map<String, E2>> project(final String projectKey, final String... otherProjectKeys) {
//        final String[] projectKeys = new String[otherProjectKeys.length + 1];
//        projectKeys[0] = projectKey;
//        System.arraycopy(otherProjectKeys, 0, projectKeys, 1, otherProjectKeys.length);
//        this.getBytecode().addStep(Symbols.project, projectKey, otherProjectKeys);
//        return this.addStep(new ProjectStep<>(this.asAdmin(), projectKeys));
//    }
//
//    /**
//     * Map the {@link Traverser} to a {@link Map} projection of sideEffect values, map values, and/or path values.
//     *
//     * @param pop             if there are multiple objects referenced in the path, the {@link Pop} to use.
//     * @param selectKey1      the first key to project
//     * @param selectKey2      the second key to project
//     * @param otherSelectKeys the third+ keys to project
//     * @param <E2>            the type of the objects projected
//     * @return the traversal with an appended {@link SelectStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#select-step" target="_blank">Reference Documentation - Select Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, Map<String, E2>> select(final Pop pop, final String selectKey1, final String selectKey2, String... otherSelectKeys) {
//        final String[] selectKeys = new String[otherSelectKeys.length + 2];
//        selectKeys[0] = selectKey1;
//        selectKeys[1] = selectKey2;
//        System.arraycopy(otherSelectKeys, 0, selectKeys, 2, otherSelectKeys.length);
//        this.getBytecode().addStep(Symbols.select, pop, selectKey1, selectKey2, otherSelectKeys);
//        return this.addStep(new SelectStep<>(this.asAdmin(), pop, selectKeys));
//    }
//
//    /**
//     * Map the {@link Traverser} to a {@link Map} projection of sideEffect values, map values, and/or path values.
//     *
//     * @param selectKey1      the first key to project
//     * @param selectKey2      the second key to project
//     * @param otherSelectKeys the third+ keys to project
//     * @param <E2>            the type of the objects projected
//     * @return the traversal with an appended {@link SelectStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#select-step" target="_blank">Reference Documentation - Select Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, Map<String, E2>> select(final String selectKey1, final String selectKey2, String... otherSelectKeys) {
//        final String[] selectKeys = new String[otherSelectKeys.length + 2];
//        selectKeys[0] = selectKey1;
//        selectKeys[1] = selectKey2;
//        System.arraycopy(otherSelectKeys, 0, selectKeys, 2, otherSelectKeys.length);
//        this.getBytecode().addStep(Symbols.select, selectKey1, selectKey2, otherSelectKeys);
//        return this.addStep(new SelectStep<>(this.asAdmin(), null, selectKeys));
//    }
//
//    /**
//     * Map the {@link Traverser} to the object specified by the {@code selectKey} and apply the {@link Pop} operation
//     * to it.
//     *
//     * @param selectKey the key to project
//     * @return the traversal with an appended {@link SelectStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#select-step" target="_blank">Reference Documentation - Select Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> select(final Pop pop, final String selectKey) {
//        this.getBytecode().addStep(Symbols.select, pop, selectKey);
//        return this.addStep(new SelectOneStep<>(this.asAdmin(), pop, selectKey));
//    }
//
//    /**
//     * Map the {@link Traverser} to the object specified by the {@code selectKey}. Note that unlike other uses of
//     * {@code select} where there are multiple keys, this use of {@code select} with a single key does not produce a
//     * {@code Map}.
//     *
//     * @param selectKey the key to project
//     * @return the traversal with an appended {@link SelectStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#select-step" target="_blank">Reference Documentation - Select Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> select(final String selectKey) {
//        this.getBytecode().addStep(Symbols.select, selectKey);
//        return this.addStep(new SelectOneStep<>(this.asAdmin(), null, selectKey));
//    }
//
//    /**
//     * A version of {@code select} that allows for the extraction of a {@link Column} from objects in the traversal.
//     *
//     * @param column the column to extract
//     * @return the traversal with an appended {@link TraversalMapStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#select-step" target="_blank">Reference Documentation - Select Step</a>
//     * @since 3.1.0-incubating
//     */
//    public default <E2> GraphTraversal<S, Collection<E2>> select(final Column column) {
//        this.getBytecode().addStep(Symbols.select, column);
//        return this.addStep(new TraversalMapStep<>(this.asAdmin(), new ColumnTraversal(column)));
//    }
//
//    /**
//     * Unrolls a {@code Iterator}, {@code Iterable} or {@code Map} into a linear form or simply emits the object if it
//     * is not one of those types.
//     *
//     * @return the traversal with an appended {@link UnfoldStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#unfold-step" target="_blank">Reference Documentation - Unfold Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> unfold() {
//        this.getBytecode().addStep(Symbols.unfold);
//        return this.addStep(new UnfoldStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Rolls up objects in the stream into an aggregate list.
//     *
//     * @return the traversal with an appended {@link FoldStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#fold-step" target="_blank">Reference Documentation - Fold Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, List<E>> fold() {
//        this.getBytecode().addStep(Symbols.fold);
//        return this.addStep(new FoldStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Rolls up objects in the stream into an aggregate value as defined by a {@code seed} and {@code BiFunction}.
//     *
//     * @param seed         the value to provide as the first argument to the {@code foldFunction}
//     * @param foldFunction the function to fold by where the first argument is the {@code seed} or the value returned from subsequent calss and
//     *                     the second argument is the value from the stream
//     * @return the traversal with an appended {@link FoldStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#fold-step" target="_blank">Reference Documentation - Fold Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> fold(final E2 seed, final BiFunction<E2, E, E2> foldFunction) {
//        this.getBytecode().addStep(Symbols.fold, seed, foldFunction);
//        return this.addStep(new FoldStep<>(this.asAdmin(), new ConstantSupplier<>(seed), foldFunction)); 
//    }
//
//    /**
//     * Map the traversal stream to its reduction as a sum of the {@link Traverser#bulk} values (i.e. count the number
//     * of traversers up to this point).
//     *
//     * @return the traversal with an appended {@link CountGlobalStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#count-step" target="_blank">Reference Documentation - Count Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, Long> count() {
//        this.getBytecode().addStep(Symbols.count);
//        return this.addStep(new CountGlobalStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Map the traversal stream to its reduction as a sum of the {@link Traverser#bulk} values given the specified
//     * {@link Scope} (i.e. count the number of traversers up to this point).
//     *
//     * @return the traversal with an appended {@link CountGlobalStep} or {@link CountLocalStep} depending on the {@link Scope}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#count-step" target="_blank">Reference Documentation - Count Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, Long> count(final Scope scope) {
//        this.getBytecode().addStep(Symbols.count, scope);
//        return this.addStep(scope.equals(Scope.global) ? new CountGlobalStep<>(this.asAdmin()) : new CountLocalStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Map the traversal stream to its reduction as a sum of the {@link Traverser#get} values multiplied by their
//     * {@link Traverser#bulk} (i.e. sum the traverser values up to this point).
//     *
//     * @return the traversal with an appended {@link SumGlobalStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#sum-step" target="_blank">Reference Documentation - Sum Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2 extends Number> GraphTraversal<S, E2> sum() {
//        this.getBytecode().addStep(Symbols.sum);
//        return this.addStep(new SumGlobalStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Map the traversal stream to its reduction as a sum of the {@link Traverser#get} values multiplied by their
//     * {@link Traverser#bulk} given the specified {@link Scope} (i.e. sum the traverser values up to this point).
//     *
//     * @return the traversal with an appended {@link SumGlobalStep} or {@link SumLocalStep} depending on the {@link Scope}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#sum-step" target="_blank">Reference Documentation - Sum Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2 extends Number> GraphTraversal<S, E2> sum(final Scope scope) {
//        this.getBytecode().addStep(Symbols.sum, scope);
//        return this.addStep(scope.equals(Scope.global) ? new SumGlobalStep<>(this.asAdmin()) : new SumLocalStep(this.asAdmin()));
//    }
//
//    /**
//     * Determines the largest value in the stream.
//     *
//     * @return the traversal with an appended {@link MaxGlobalStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#max-step" target="_blank">Reference Documentation - Max Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2 extends Number> GraphTraversal<S, E2> max() {
//        this.getBytecode().addStep(Symbols.max);
//        return this.addStep(new MaxGlobalStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Determines the largest value in the stream given the {@link Scope}.
//     *
//     * @return the traversal with an appended {@link MaxGlobalStep} or {@link MaxLocalStep} depending on the {@link Scope}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#max-step" target="_blank">Reference Documentation - Max Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2 extends Number> GraphTraversal<S, E2> max(final Scope scope) {
//        this.getBytecode().addStep(Symbols.max, scope);
//        return this.addStep(scope.equals(Scope.global) ? new MaxGlobalStep<>(this.asAdmin()) : new MaxLocalStep(this.asAdmin()));
//    }
//
//    /**
//     * Determines the smallest value in the stream.
//     *
//     * @return the traversal with an appended {@link MinGlobalStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#min-step" target="_blank">Reference Documentation - Min Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2 extends Number> GraphTraversal<S, E2> min() {
//        this.getBytecode().addStep(Symbols.min);
//        return this.addStep(new MinGlobalStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Determines the smallest value in the stream given the {@link Scope}.
//     *
//     * @return the traversal with an appended {@link MinGlobalStep} or {@link MinLocalStep} depending on the {@link Scope}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#min-step" target="_blank">Reference Documentation - Min Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2 extends Number> GraphTraversal<S, E2> min(final Scope scope) {
//        this.getBytecode().addStep(Symbols.min, scope);
//        return this.addStep(scope.equals(Scope.global) ? new MinGlobalStep<E2>(this.asAdmin()) : new MinLocalStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Determines the mean value in the stream.
//     *
//     * @return the traversal with an appended {@link MeanGlobalStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#mean-step" target="_blank">Reference Documentation - Mean Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2 extends Number> GraphTraversal<S, E2> mean() {
//        this.getBytecode().addStep(Symbols.mean);
//        return this.addStep(new MeanGlobalStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Determines the mean value in the stream given the {@link Scope}.
//     *
//     * @return the traversal with an appended {@link MeanGlobalStep} or {@link MeanLocalStep} depending on the {@link Scope}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#mean-step" target="_blank">Reference Documentation - Mean Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2 extends Number> GraphTraversal<S, E2> mean(final Scope scope) {
//        this.getBytecode().addStep(Symbols.mean, scope);
//        return this.addStep(scope.equals(Scope.global) ? new MeanGlobalStep<>(this.asAdmin()) : new MeanLocalStep(this.asAdmin()));
//    }
//
//    /**
//     * Organize objects in the stream into a {@code Map}. Calls to {@code group()} are typically accompanied with
//     * {@link #by()} modulators which help specify how the grouping should occur.
//     *
//     * @return the traversal with an appended {@link GroupStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#group-step" target="_blank">Reference Documentation - Group Step</a>
//     * @since 3.1.0-incubating
//     */
//    public default <K, V> GraphTraversal<S, Map<K, V>> group() {
//        this.getBytecode().addStep(Symbols.group);
//        return this.addStep(new GroupStep<>(this.asAdmin()));
//    }
//
//    /**
//     * @since 3.0.0-incubating
//     * @deprecated As of release 3.1.0, replaced by {@link #group()}
//     */
//    @Deprecated
//    public default <K, V> GraphTraversal<S, Map<K, V>> groupV3d0() {
//        this.getBytecode().addStep(Symbols.groupV3d0);
//        return this.addStep(new GroupStepV3d0<>(this.asAdmin()));
//    }
//
//    /**
//     * Counts the number of times a particular objects has been part of a traversal, returning a {@code Map} where the
//     * object is the key and the value is the count.
//     *
//     * @return the traversal with an appended {@link GroupCountStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#groupcount-step" target="_blank">Reference Documentation - GroupCount Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <K> GraphTraversal<S, Map<K, Long>> groupCount() {
//        this.getBytecode().addStep(Symbols.groupCount);
//        return this.addStep(new GroupCountStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Aggregates the emanating paths into a {@link Tree} data structure.
//     *
//     * @return the traversal with an appended {@link TreeStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#tree-step" target="_blank">Reference Documentation - Tree Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, Tree> tree() {
//        this.getBytecode().addStep(Symbols.tree);
//        return this.addStep(new TreeStep<>(this.asAdmin()));
//    }
//
//    /**
//     * Adds a {@link Vertex}.
//     *
//     * @param vertexLabel the label of the {@link Vertex} to add
//     * @return the traversal with the {@link AddVertexStep} added
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#addvertex-step" target="_blank">Reference Documentation - AddVertex Step</a>
//     * @since 3.1.0-incubating
//     */
//    public default GraphTraversal<S, Vertex> addV(final String vertexLabel) {
//        this.getBytecode().addStep(Symbols.addV, vertexLabel);
//        return this.addStep(new AddVertexStep<>(this.asAdmin(), vertexLabel));
//    }
//
//    /**
//     * Adds a {@link Vertex} with a  vertex label.
//     *
//     * @return the traversal with the {@link AddVertexStep} added
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#addvertex-step" target="_blank">Reference Documentation - AddVertex Step</a>
//     * @since 3.1.0-incubating
//     */
//    public default GraphTraversal<S, Vertex> addV() {
//        this.getBytecode().addStep(Symbols.addV);
//        return this.addStep(new AddVertexStep<>(this.asAdmin(), null));
//    }
//
//    /**
//     * @since 3.0.0-incubating
//     * @deprecated As of release 3.1.0, replaced by {@link #addV()}
//     */
//    @Deprecated
//    public default GraphTraversal<S, Vertex> addV(final Object... propertyKeyValues) {
//        this.addV();
//        for (int i = 0; i < propertyKeyValues.length; i = i + 2) {
//            this.property(propertyKeyValues[i], propertyKeyValues[i + 1]);
//        }
//        return (GraphTraversal<S, Vertex>) this;
//    }
//
//    /**
//     * Adds an {@link Edge} with the specified edge label.
//     *
//     * @param edgeLabel the label of the newly added edge
//     * @return the traversal with the {@link AddEdgeStep} added
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#addedge-step" target="_blank">Reference Documentation - AddEdge Step</a>
//     * @since 3.1.0-incubating
//     */
//    public default GraphTraversal<S, Edge> addE(final String edgeLabel) {
//        this.getBytecode().addStep(Symbols.addE, edgeLabel);
//        return this.addStep(new AddEdgeStep<>(this.asAdmin(), edgeLabel));
//    }
//
//    /**
//     * Provide {@code to()}-modulation to respective steps.
//     *
//     * @param toStepLabel the step label to modulate to.
//     * @return the traversal with the modified {@link FromToModulating} step.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#to-step" target="_blank">Reference Documentation - To Step</a>
//     * @since 3.1.0-incubating
//     */
//    public default GraphTraversal<S, E> to(final String toStepLabel) {
//        this.getBytecode().addStep(Symbols.to, toStepLabel);
//        ((FromToModulating) this.getEndStep()).addTo(toStepLabel);
//        return this;
//    }
//
//    /**
//     * Provide {@code from()}-modulation to respective steps.
//     *
//     * @param fromStepLabel the step label to modulate to.
//     * @return the traversal with the modified {@link FromToModulating} step.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#from-step" target="_blank">Reference Documentation - From Step</a>
//     * @since 3.1.0-incubating
//     */
//    public default GraphTraversal<S, E> from(final String fromStepLabel) {
//        this.getBytecode().addStep(Symbols.from, fromStepLabel);
//        ((FromToModulating) this.getEndStep()).addFrom(fromStepLabel);
//        return this;
//    }
//
//    /**
//     * When used as a modifier to {@link #addE(String)} this method specifies the traversal to use for selecting the
//     * incoming vertex of the newly added {@link Edge}.
//     *
//     * @param toVertex the traversal for selecting the incoming vertex
//     * @return the traversal with the modified {@link AddEdgeStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#addedge-step" target="_blank">Reference Documentation - From Step</a>
//     * @since 3.1.0-incubating
//     */
//    public default GraphTraversal<S, E> to(final Traversal<E, Vertex> toVertex) {
//        this.getBytecode().addStep(Symbols.to, toVertex);
//        ((FromToModulating) this.getEndStep()).addTo(toVertex.asAdmin());
//        return this;
//    }
//
//    /**
//     * When used as a modifier to {@link #addE(String)} this method specifies the traversal to use for selecting the
//     * outgoing vertex of the newly added {@link Edge}.
//     *
//     * @param fromVertex the traversal for selecting the outgoing vertex
//     * @return the traversal with the modified {@link AddEdgeStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#addedge-step" target="_blank">Reference Documentation - From Step</a>
//     * @since 3.1.0-incubating
//     */
//    public default GraphTraversal<S, E> from(final Traversal<E, Vertex> fromVertex) {
//        this.getBytecode().addStep(Symbols.from, fromVertex);
//        ((FromToModulating) this.getEndStep()).addFrom(fromVertex.asAdmin());
//        return this;
//    }
//
//    /**
//     * @since 3.0.0-incubating
//     * @deprecated As of release 3.1.0, replaced by {@link #addE(String)}
//     */
//    @Deprecated
//    public default GraphTraversal<S, Edge> addE(final Direction direction, final String firstVertexKeyOrEdgeLabel, final String edgeLabelOrSecondVertexKey, final Object... propertyKeyValues) {
//        if (propertyKeyValues.length % 2 == 0) {
//            // addOutE("createdBy", "a")
//            this.addE(firstVertexKeyOrEdgeLabel);
//            if (direction.equals(Direction.OUT))
//                this.to(edgeLabelOrSecondVertexKey);
//            else
//                this.from(edgeLabelOrSecondVertexKey);
//
//            for (int i = 0; i < propertyKeyValues.length; i = i + 2) {
//                this.property(propertyKeyValues[i], propertyKeyValues[i + 1]);
//            }
//            //((Mutating) this.getEndStep()).addPropertyMutations(propertyKeyValues);
//            return (GraphTraversal<S, Edge>) this;
//        } else {
//            // addInE("a", "codeveloper", "b", "year", 2009)
//            this.addE(edgeLabelOrSecondVertexKey);
//            if (direction.equals(Direction.OUT))
//                this.from(firstVertexKeyOrEdgeLabel).to((String) propertyKeyValues[0]);
//            else
//                this.to(firstVertexKeyOrEdgeLabel).from((String) propertyKeyValues[0]);
//
//            for (int i = 1; i < propertyKeyValues.length; i = i + 2) {
//                this.property(propertyKeyValues[i], propertyKeyValues[i + 1]);
//            }
//            //((Mutating) this.getEndStep()).addPropertyMutations(Arrays.copyOfRange(propertyKeyValues, 1, propertyKeyValues.length));
//            return (GraphTraversal<S, Edge>) this;
//        }
//    }
//
//
//    ///////////////////// FILTER STEPS /////////////////////
//
//    /**
//     * Map the {@link Traverser} to either {@code true} or {@code false}, where {@code false} will not pass the
//     * traverser to the next step.
//     *
//     * @param predicate the filter function to apply
//     * @return the traversal with the {@link LambdaFilterStep} added
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#general-steps" target="_blank">Reference Documentation - General Steps</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> filter(final Predicate<Traverser<E>> predicate) {
//        this.getBytecode().addStep(Symbols.filter, predicate);
//        return this.addStep(new LambdaFilterStep<>(this.asAdmin(), predicate));
//    }
//
    /**
     * Map the {@link Traverser} to either {@code true} or {@code false}, where {@code false} will not pass the
     * traverser to the next step.
     *
     * @param filterTraversal the filter traversal to apply
     * @return the traversal with the {@link TraversalFilterStep} added
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#general-steps" target="_blank">Reference Documentation - General Steps</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, E> filter(final Traversal<?, ?> filterTraversal) {
    	Admin<S, E> clone = (Admin<S, E>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.filter, filterTraversal);
        return clone;
    }
//
//    /**
//     * Ensures that at least one of the provided traversals yield a result.
//     *
//     * @param orTraversals filter traversals where at least one must be satisfied
//     * @return the traversal with an appended {@link OrStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#or-step" target="_blank">Reference Documentation - Or Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> or(final Traversal<?, ?>... orTraversals) {
//        this.getBytecode().addStep(Symbols.or, orTraversals);
//        return this.addStep(new OrStep(this.asAdmin(), orTraversals));
//    }
//
//    /**
//     * Ensures that all of the provided traversals yield a result.
//     *
//     * @param andTraversals filter traversals that must be satisfied
//     * @return the traversal with an appended {@link AndStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#and-step" target="_blank">Reference Documentation - And Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> and(final Traversal<?, ?>... andTraversals) {
//        this.getBytecode().addStep(Symbols.and, andTraversals);
//        return this.addStep(new AndStep(this.asAdmin(), andTraversals));
//    }
//
//    /**
//     * Provides a way to add arbitrary objects to a traversal stream.
//     *
//     * @param injections the objects to add to the stream
//     * @return the traversal with an appended {@link InjectStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#inject-step" target="_blank">Reference Documentation - Inject Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> inject(final E... injections) {
//        this.getBytecode().addStep(Symbols.inject, injections);
//        return this.addStep(new InjectStep<>(this.asAdmin(), injections));
//    }
//
//    /**
//     * Remove all duplicates in the traversal stream up to this point.
//     *
//     * @param scope       whether the deduplication is on the stream (global) or the current object (local).
//     * @param dedupLabels if labels are provided, then the scope labels determine de-duplication. No labels implies current object.
//     * @return the traversal with an appended {@link DedupGlobalStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#dedup-step" target="_blank">Reference Documentation - Dedup Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> dedup(final Scope scope, final String... dedupLabels) {
//        this.getBytecode().addStep(Symbols.dedup, scope, dedupLabels);
//        return this.addStep(scope.equals(Scope.global) ? new DedupGlobalStep<>(this.asAdmin(), dedupLabels) : new DedupLocalStep(this.asAdmin()));
//    }
//
    /**
     * Remove all duplicates in the traversal stream up to this point.
     *
     * @param dedupLabels if labels are provided, then the scoped object's labels determine de-duplication. No labels implies current object.
     * @return the traversal with an appended {@link DedupGlobalStep}.
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#dedup-step" target="_blank">Reference Documentation - Dedup Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, E> dedup(final String... dedupLabels) {
    	Admin<S, E> clone = (Admin<S, E>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.dedup, dedupLabels);
        return clone;
    }
//
//    /**
//     * Filters the current object based on the object itself or the path history.
//     *
//     * @param startKey  the key containing the object to filter
//     * @param predicate the filter to apply
//     * @return the traversal with an appended {@link WherePredicateStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#where-step" target="_blank">Reference Documentation - Where Step</a>
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#using-where-with-match" target="_blank">Reference Documentation - Where with Match</a>
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#using-where-with-select" target="_blank">Reference Documentation - Where with Select</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> where(final String startKey, final P<String> predicate) {
//        this.getBytecode().addStep(Symbols.where, startKey, predicate);
//        return this.addStep(new WherePredicateStep<>(this.asAdmin(), Optional.ofNullable(startKey), predicate));
//    }
//
//    /**
//     * Filters the current object based on the object itself or the path history.
//     *
//     * @param predicate the filter to apply
//     * @return the traversal with an appended {@link WherePredicateStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#where-step" target="_blank">Reference Documentation - Where Step</a>
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#using-where-with-match" target="_blank">Reference Documentation - Where with Match</a>
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#using-where-with-select" target="_blank">Reference Documentation - Where with Select</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> where(final P<String> predicate) {
//        this.getBytecode().addStep(Symbols.where, predicate);
//        return this.addStep(new WherePredicateStep<>(this.asAdmin(), Optional.empty(), predicate));
//    }
//
//    /**
//     * Filters the current object based on the object itself or the path history.
//     *
//     * @param whereTraversal the filter to apply
//     * @return the traversal with an appended {@link WherePredicateStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#where-step" target="_blank">Reference Documentation - Where Step</a>
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#using-where-with-match" target="_blank">Reference Documentation - Where with Match</a>
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#using-where-with-select" target="_blank">Reference Documentation - Where with Select</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> where(final Traversal<?, ?> whereTraversal) {
//        this.getBytecode().addStep(Symbols.where, whereTraversal);
//        return TraversalHelper.getVariableLocations(whereTraversal.asAdmin()).isEmpty() ?
//                this.addStep(new TraversalFilterStep<>(this.asAdmin(), (Traversal) whereTraversal)) :
//                this.addStep(new WhereTraversalStep<>(this.asAdmin(), whereTraversal));
//    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on their properties.
//     *
//     * @param propertyKey the key of the property to filter on
//     * @param predicate   the filter to apply to the key's value
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> has(final String propertyKey, final P<?> predicate) {
//        this.getBytecode().addStep(Symbols.has, propertyKey, predicate);
//        return TraversalHelper.addHasContainer(this.asAdmin(), new HasContainer(propertyKey, predicate));
//    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on their properties.
//     *
//     * @param accessor  the {@link T} accessor of the property to filter on
//     * @param predicate the filter to apply to the key's value
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> has(final T accessor, final P<?> predicate) {
//        this.getBytecode().addStep(Symbols.has, accessor, predicate);
//        return TraversalHelper.addHasContainer(this.asAdmin(), new HasContainer(accessor.getAccessor(), predicate));
//    }
//
    /**
     * Filters vertices, edges and vertex properties based on their properties.
     *
     * @param propertyKey the key of the property to filter on
     * @param value       the value to compare the property value to for equality
     * @return the traversal with an appended {@link HasStep}
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, E> has(final String propertyKey, final Object value) {
        if (value instanceof Traversal)
            return this.has(propertyKey, (Traversal) value);
        else {
        	Admin<S, E> clone = this.asAdmin().clone();
        	clone.getBytecode().addStep(Symbols.has, propertyKey, value);
            return clone;
        }
    }

  
//
//    /**
//     * Filters vertices, edges and vertex properties based on their properties.
//     *
//     * @param label       the label of the {@link Element}
//     * @param propertyKey the key of the property to filter on
//     * @param predicate   the filter to apply to the key's value
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> has(final String label, final String propertyKey, final P<?> predicate) {
//        this.getBytecode().addStep(Symbols.has, label, propertyKey, predicate);
//        TraversalHelper.addHasContainer(this.asAdmin(), new HasContainer(T.label.getAccessor(), P.eq(label)));
//        return TraversalHelper.addHasContainer(this.asAdmin(), new HasContainer(propertyKey, predicate));
//    }
//
    /**
     * Filters vertices, edges and vertex properties based on their properties.
     *
     * @param label       the label of the {@link Element}
     * @param propertyKey the key of the property to filter on
     * @param value       the value to compare the accessor value to for equality
     * @return the traversal with an appended {@link HasStep}
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, E> has(final String label, final String propertyKey, final Object value) {
    	Admin<S, E> clone = (Admin<S, E>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.has, label, propertyKey, value);
        return clone;
    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on their properties.
//     *
//     * @param accessor          the {@link T} accessor of the property to filter on
//     * @param propertyTraversal the traversal to filter the accessor value by
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.1.0-incubating
//     */
//    public default GraphTraversal<S, E> has(final T accessor, final Traversal<?, ?> propertyTraversal) {
//        this.getBytecode().addStep(Symbols.has, accessor, propertyTraversal);
//        return this.addStep(
//                new TraversalFilterStep<>(this.asAdmin(), propertyTraversal.addStep(0,
//                        new PropertiesStep(propertyTraversal.asAdmin(), PropertyType.VALUE, accessor.getAccessor()))));
//    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on their properties.
//     *
//     * @param propertyKey       the key of the property to filter on
//     * @param propertyTraversal the traversal to filter the property value by
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> has(final String propertyKey, final Traversal<?, ?> propertyTraversal) {
//        this.getBytecode().addStep(Symbols.has, propertyKey, propertyTraversal);
//        return this.addStep(
//                new TraversalFilterStep<>(this.asAdmin(), propertyTraversal.addStep(0,
//                        new PropertiesStep(propertyTraversal.asAdmin(), PropertyType.VALUE, propertyKey))));
//    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on the existence of properties.
//     *
//     * @param propertyKey the key of the property to filter on for existence
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> has(final String propertyKey) {
//        this.getBytecode().addStep(Symbols.has, propertyKey);
//        return this.addStep(new TraversalFilterStep<>(this.asAdmin(), __.values(propertyKey)));
//    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on the non-existence of properties.
//     *
//     * @param propertyKey the key of the property to filter on for existence
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> hasNot(final String propertyKey) {
//        this.getBytecode().addStep(Symbols.hasNot, propertyKey);
//        return this.addStep(new NotStep<>(this.asAdmin(), __.values(propertyKey)));
//    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on their label.
//     *
//     * @param label       the label of the {@link Element}
//     * @param otherLabels additional labels of the {@link Element}
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.2.2
//     */
    public default GraphTraversal<S, E> hasLabel(final String label, final String... otherLabels) {
        final String[] labels = new String[otherLabels.length + 1];
        labels[0] = label;
        System.arraycopy(otherLabels, 0, labels, 1, otherLabels.length);
        Admin<S, E> clone = (Admin<S, E>) this.asAdmin().clone();
        clone.getBytecode().addStep(Symbols.hasLabel, labels);
        
        return clone;
    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on their label.
//     *
//     * @param predicate the filter to apply to the label of the {@link Element}
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.2.4
//     */
//    public default GraphTraversal<S, E> hasLabel(final P<String> predicate) {
//        this.getBytecode().addStep(Symbols.hasLabel, predicate);
//        return TraversalHelper.addHasContainer(this.asAdmin(), new HasContainer(T.label.getAccessor(), predicate));
//    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on their identifier.
//     *
//     * @param id       the identifier of the {@link Element}
//     * @param otherIds additional identifiers of the {@link Element}
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.2.2
//     */
//    public default GraphTraversal<S, E> hasId(final Object id, final Object... otherIds) {
//        if (id instanceof P)
//            return this.hasId((P) id);
//        else {
//            final List<Object> ids = new ArrayList<>();
//            if (id instanceof Object[]) {
//                for (final Object i : (Object[]) id) {
//                    ids.add(i);
//                }
//            } else
//                ids.add(id);
//            for (final Object i : otherIds) {
//                if (i.getClass().isArray()) {
//                    for (final Object ii : (Object[]) i) {
//                        ids.add(ii);
//                    }
//                } else
//                    ids.add(i);
//            }
//            this.getBytecode().addStep(Symbols.hasId, ids.toArray());
//            return TraversalHelper.addHasContainer(this.asAdmin(), new HasContainer(T.id.getAccessor(), ids.size() == 1 ? P.eq(ids.get(0)) : P.within(ids)));
//        }
//    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on their identifier.
//     *
//     * @param predicate the filter to apply to the identifier of the {@link Element}
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.2.4
//     */
//    public default GraphTraversal<S, E> hasId(final P<Object> predicate) {
//        this.getBytecode().addStep(Symbols.hasId, predicate);
//        return TraversalHelper.addHasContainer(this.asAdmin(), new HasContainer(T.id.getAccessor(), predicate));
//    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on their key.
//     *
//     * @param label       the key of the {@link Element}
//     * @param otherLabels additional key of the {@link Element}
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.2.2
//     */
//    public default GraphTraversal<S, E> hasKey(final String label, final String... otherLabels) {
//        final String[] labels = new String[otherLabels.length + 1];
//        labels[0] = label;
//        System.arraycopy(otherLabels, 0, labels, 1, otherLabels.length);
//        this.getBytecode().addStep(Symbols.hasKey, labels);
//        return TraversalHelper.addHasContainer(this.asAdmin(), new HasContainer(T.key.getAccessor(), labels.length == 1 ? P.eq(labels[0]) : P.within(labels)));
//    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on their key.
//     *
//     * @param predicate the filter to apply to the key of the {@link Element}
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.2.4
//     */
//    public default GraphTraversal<S, E> hasKey(final P<String> predicate) {
//        this.getBytecode().addStep(Symbols.hasKey, predicate);
//        return TraversalHelper.addHasContainer(this.asAdmin(), new HasContainer(T.key.getAccessor(), predicate));
//    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on their value.
//     *
//     * @param value       the value of the {@link Element}
//     * @param otherValues additional values of the {@link Element}
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     */
//    public default GraphTraversal<S, E> hasValue(final Object value, final Object... otherValues) {
//        if (value instanceof P)
//            return this.hasValue((P) value);
//        else {
//            final List<Object> values = new ArrayList<>();
//            if (value instanceof Object[]) {
//                for (final Object v : (Object[]) value) {
//                    values.add(v);
//                }
//            } else
//                values.add(value);
//            for (final Object v : otherValues) {
//                if (v instanceof Object[]) {
//                    for (final Object vv : (Object[]) v) {
//                        values.add(vv);
//                    }
//                } else
//                    values.add(v);
//            }
//            this.getBytecode().addStep(Symbols.hasValue, values.toArray());
//            return TraversalHelper.addHasContainer(this.asAdmin(), new HasContainer(T.value.getAccessor(), values.size() == 1 ? P.eq(values.get(0)) : P.within(values)));
//        }
//    }
//
//    /**
//     * Filters vertices, edges and vertex properties based on their value.
//     *
//     * @param predicate the filter to apply to the value of the {@link Element}
//     * @return the traversal with an appended {@link HasStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#has-step" target="_blank">Reference Documentation - Has Step</a>
//     * @since 3.2.4
//     */
//    public default GraphTraversal<S, E> hasValue(final P<Object> predicate) {
//        this.getBytecode().addStep(Symbols.hasValue, predicate);
//        return TraversalHelper.addHasContainer(this.asAdmin(), new HasContainer(T.value.getAccessor(), predicate));
//    }
//
//    /**
//     * Filters <code>E</code> object values given the provided {@code predicate}.
//     *
//     * @param predicate the filter to apply
//     * @return the traversal with an appended {@link IsStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#is-step" target="_blank">Reference Documentation - Is Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> is(final P<E> predicate) {
//        this.getBytecode().addStep(Symbols.is, predicate);
//        return this.addStep(new IsStep<>(this.asAdmin(), predicate));
//    }
//
//    /**
//     * Filter the <code>E</code> object if it is not {@link P#eq} to the provided value.
//     *
//     * @param value the value that the object must equal.
//     * @return the traversal with an appended {@link IsStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#is-step" target="_blank">Reference Documentation - Is Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> is(final Object value) {
//        this.getBytecode().addStep(Symbols.is, value);
//        return this.addStep(new IsStep<>(this.asAdmin(), value instanceof P ? (P<E>) value : P.eq((E) value)));
//    }
//
//    /**
//     * Removes objects from the traversal stream when the traversal provided as an argument does not return any objects.
//     *
//     * @param notTraversal the traversal to filter by.
//     * @return the traversal with an appended {@link NotStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#not-step" target="_blank">Reference Documentation - Not Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> not(final Traversal<?, ?> notTraversal) {
//        this.getBytecode().addStep(Symbols.not, notTraversal);
//        return this.addStep(new NotStep<>(this.asAdmin(), (Traversal<E, ?>) notTraversal));
//    }
//
//    /**
//     * Filter the <code>E</code> object given a biased coin toss.
//     *
//     * @param probability the probability that the object will pass through
//     * @return the traversal with an appended {@link CoinStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#coin-step" target="_blank">Reference Documentation - Coin Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> coin(final double probability) {
//        this.getBytecode().addStep(Symbols.coin, probability);
//        return this.addStep(new CoinStep<>(this.asAdmin(), probability));
//    }
//
    /**
     * Filter the objects in the traversal by the number of them to pass through the stream. Those before the value
     * of {@code low} do not pass through and those that exceed the value of {@code high} will end the iteration.
     *
     * @param low  the number at which to start allowing objects through the stream
     * @param high the number at which to end the stream
     * @return the traversal with an appended {@link RangeGlobalStep}
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#range-step" target="_blank">Reference Documentation - Range Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, E> range(final long low, final long high) {
    	Admin<S, E> clone = (Admin<S, E>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.range, low, high);
        return clone;
    }
//
//    /**
//     * Filter the objects in the traversal by the number of them to pass through the stream as constrained by the
//     * {@link Scope}. Those before the value of {@code low} do not pass through and those that exceed the value of
//     * {@code high} will end the iteration.
//     *
//     * @param scope the scope of how to apply the {@code range}
//     * @param low   the number at which to start allowing objects through the stream
//     * @param high  the number at which to end the stream
//     * @return the traversal with an appended {@link RangeGlobalStep} or {@link RangeLocalStep} depending on {@code scope}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#range-step" target="_blank">Reference Documentation - Range Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> range(final Scope scope, final long low, final long high) {
//        this.getBytecode().addStep(Symbols.range, scope, low, high);
//        return this.addStep(scope.equals(Scope.global)
//                ? new RangeGlobalStep<>(this.asAdmin(), low, high)
//                : new RangeLocalStep<>(this.asAdmin(), low, high));
//    }
//
    /**
     * Filter the objects in the traversal by the number of them to pass through the stream, where only the first
     * {@code n} objects are allowed as defined by the {@code limit} argument.
     *
     * @param limit the number at which to end the stream
     * @return the traversal with an appended {@link RangeGlobalStep}
     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#limit-step" target="_blank">Reference Documentation - Limit Step</a>
     * @since 3.0.0-incubating
     */
    public default GraphTraversal<S, E> limit(final long limit) {
    	Admin<S, E> clone = (Admin<S, E>) this.asAdmin().clone();
    	clone.getBytecode().addStep(Symbols.limit, limit);
        return clone;
    }
//
//    /**
//     * Filter the objects in the traversal by the number of them to pass through the stream given the {@link Scope},
//     * where only the first {@code n} objects are allowed as defined by the {@code limit} argument.
//     *
//     * @param scope the scope of how to apply the {@code limit}
//     * @param limit the number at which to end the stream
//     * @return the traversal with an appended {@link RangeGlobalStep} or {@link RangeLocalStep} depending on {@code scope}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#limit-step" target="_blank">Reference Documentation - Limit Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> limit(final Scope scope, final long limit) {
//        this.getBytecode().addStep(Symbols.limit, scope, limit);
//        return this.addStep(scope.equals(Scope.global)
//                ? new RangeGlobalStep<>(this.asAdmin(), 0, limit)
//                : new RangeLocalStep<>(this.asAdmin(), 0, limit));
//    }
//
//    /**
//     * Filters the objects in the traversal emitted as being last objects in the stream. In this case, only the last
//     * object will be returned.
//     *
//     * @return the traversal with an appended {@link TailGlobalStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#tail-step" target="_blank">Reference Documentation - Tail Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> tail() {
//        this.getBytecode().addStep(Symbols.tail);
//        return this.addStep(new TailGlobalStep<>(this.asAdmin(), 1));
//    }
//
//    /**
//     * Filters the objects in the traversal emitted as being last objects in the stream. In this case, only the last
//     * {@code n} objects will be returned as defined by the {@code limit}.
//     *
//     * @param limit the number at which to end the stream
//     * @return the traversal with an appended {@link TailGlobalStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#tail-step" target="_blank">Reference Documentation - Tail Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> tail(final long limit) {
//        this.getBytecode().addStep(Symbols.tail, limit);
//        return this.addStep(new TailGlobalStep<>(this.asAdmin(), limit));
//    }
//
//    /**
//     * Filters the objects in the traversal emitted as being last objects in the stream given the {@link Scope}. In
//     * this case, only the last object in the stream will be returned.
//     *
//     * @param scope the scope of how to apply the {@code tail}
//     * @return the traversal with an appended {@link TailGlobalStep} or {@link TailLocalStep} depending on {@code scope}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#tail-step" target="_blank">Reference Documentation - Tail Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> tail(final Scope scope) {
//        this.getBytecode().addStep(Symbols.tail, scope);
//        return this.addStep(scope.equals(Scope.global)
//                ? new TailGlobalStep<>(this.asAdmin(), 1)
//                : new TailLocalStep<>(this.asAdmin(), 1));
//    }
//
//    /**
//     * Filters the objects in the traversal emitted as being last objects in the stream given the {@link Scope}. In
//     * this case, only the last {@code n} objects will be returned as defined by the {@code limit}.
//     *
//     * @param scope the scope of how to apply the {@code tail}
//     * @param limit the number at which to end the stream
//     * @return the traversal with an appended {@link TailGlobalStep} or {@link TailLocalStep} depending on {@code scope}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#tail-step" target="_blank">Reference Documentation - Tail Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> tail(final Scope scope, final long limit) {
//        this.getBytecode().addStep(Symbols.tail, scope, limit);
//        return this.addStep(scope.equals(Scope.global)
//                ? new TailGlobalStep<>(this.asAdmin(), limit)
//                : new TailLocalStep<>(this.asAdmin(), limit));
//    }
//
//    /**
//     * Once the first {@link Traverser} hits this step, a count down is started. Once the time limit is up, all remaining traversers are filtered out.
//     *
//     * @param timeLimit the count down time
//     * @return the traversal with an appended {@link TimeLimitStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#timelimit-step" target="_blank">Reference Documentation - TimeLimit Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> timeLimit(final long timeLimit) {
//        this.getBytecode().addStep(Symbols.timeLimit, timeLimit);
//        return this.addStep(new TimeLimitStep<E>(this.asAdmin(), timeLimit));
//    }
//
//    /**
//     * Filter the <code>E</code> object if its {@link Traverser#path} is not {@link Path#isSimple}.
//     *
//     * @return the traversal with an appended {@link PathFilterStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#simplepath-step" target="_blank">Reference Documentation - SimplePath Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> simplePath() {
//        this.getBytecode().addStep(Symbols.simplePath);
//        return this.addStep(new PathFilterStep<E>(this.asAdmin(), true));
//    }
//
//    /**
//     * Filter the <code>E</code> object if its {@link Traverser#path} is {@link Path#isSimple}.
//     *
//     * @return the traversal with an appended {@link PathFilterStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#cyclicpath-step" target="_blank">Reference Documentation - CyclicPath Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> cyclicPath() {
//        this.getBytecode().addStep(Symbols.cyclicPath);
//        return this.addStep(new PathFilterStep<E>(this.asAdmin(), false));
//    }
//
//    /**
//     * Allow some specified number of objects to pass through the stream.
//     *
//     * @param amountToSample the number of objects to allow
//     * @return the traversal with an appended {@link SampleGlobalStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#sample-step" target="_blank">Reference Documentation - Sample Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> sample(final int amountToSample) {
//        this.getBytecode().addStep(Symbols.sample, amountToSample);
//        return this.addStep(new SampleGlobalStep<>(this.asAdmin(), amountToSample));
//    }
//
//    /**
//     * Allow some specified number of objects to pass through the stream.
//     *
//     * @param scope          the scope of how to apply the {@code sample}
//     * @param amountToSample the number of objects to allow
//     * @return the traversal with an appended {@link SampleGlobalStep} or {@link SampleLocalStep} depending on the {@code scope}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#sample-step" target="_blank">Reference Documentation - Sample Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> sample(final Scope scope, final int amountToSample) {
//        this.getBytecode().addStep(Symbols.sample, scope, amountToSample);
//        return this.addStep(scope.equals(Scope.global)
//                ? new SampleGlobalStep<>(this.asAdmin(), amountToSample)
//                : new SampleLocalStep<>(this.asAdmin(), amountToSample));
//    }
//
//    /**
//     * Removes elements and properties from the graph. This step is not a terminating, in the sense that it does not
//     * automatically iterate the traversal. It is therefore necessary to do some form of iteration for the removal
//     * to actually take place. In most cases, iteration is best accomplished with {@code g.V().drop().iterate()}.
//     *
//     * @return the traversal with the {@link DropStep} added
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#drop-step" target="_blank">Reference Documentation - Drop Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> drop() {
//        this.getBytecode().addStep(Symbols.drop);
//        return this.addStep(new DropStep<>(this.asAdmin()));
//    }
//
//    ///////////////////// SIDE-EFFECT STEPS /////////////////////
//
//    /**
//     * Perform some operation on the {@link Traverser} and pass it to the next step unmodified.
//     *
//     * @param consumer the operation to perform at this step in relation to the {@link Traverser}
//     * @return the traversal with an appended {@link LambdaSideEffectStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#general-steps" target="_blank">Reference Documentation - General Steps</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> sideEffect(final Consumer<Traverser<E>> consumer) {
//        this.getBytecode().addStep(Symbols.sideEffect, consumer);
//        return this.addStep(new LambdaSideEffectStep<>(this.asAdmin(), consumer));
//    }
//
//    /**
//     * Perform some operation on the {@link Traverser} and pass it to the next step unmodified.
//     *
//     * @param sideEffectTraversal the operation to perform at this step in relation to the {@link Traverser}
//     * @return the traversal with an appended {@link TraversalSideEffectStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#general-steps" target="_blank">Reference Documentation - General Steps</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> sideEffect(final Traversal<?, ?> sideEffectTraversal) {
//        this.getBytecode().addStep(Symbols.sideEffect, sideEffectTraversal);
//        return this.addStep(new TraversalSideEffectStep<>(this.asAdmin(), (Traversal) sideEffectTraversal));
//    }
//
//    /**
//     * Iterates the traversal up to the itself and emits the side-effect referenced by the key. If multiple keys are
//     * supplied then the side-effects are emitted as a {@code Map}.
//     *
//     * @param sideEffectKey  the side-effect to emit
//     * @param sideEffectKeys other side-effects to emit
//     * @return the traversal with an appended {@link SideEffectCapStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#cap-step" target="_blank">Reference Documentation - Cap Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> cap(final String sideEffectKey, final String... sideEffectKeys) {
//        this.getBytecode().addStep(Symbols.cap, sideEffectKey, sideEffectKeys);
//        return this.addStep(new SideEffectCapStep<>(this.asAdmin(), sideEffectKey, sideEffectKeys));
//    }
//
//    /**
//     * Extracts a portion of the graph being traversed into a {@link Graph} object held in the specified side-effect
//     * key.
//     *
//     * @param sideEffectKey the name of the side-effect key that will hold the subgraph
//     * @return the traversal with an appended {@link SubgraphStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#subgraph-step" target="_blank">Reference Documentation - Subgraph Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, Edge> subgraph(final String sideEffectKey) {
//        this.getBytecode().addStep(Symbols.subgraph, sideEffectKey);
//        return this.addStep(new SubgraphStep(this.asAdmin(), sideEffectKey));
//    }
//
//    /**
//     * Eagerly collects objects up to this step into a side-effect.
//     *
//     * @param sideEffectKey the name of the side-effect key that will hold the aggregated objects
//     * @return the traversal with an appended {@link AggregateStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#aggregate-step" target="_blank">Reference Documentation - Aggregate Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> aggregate(final String sideEffectKey) {
//        this.getBytecode().addStep(Symbols.aggregate, sideEffectKey);
//        return this.addStep(new AggregateStep<>(this.asAdmin(), sideEffectKey));
//    }
//
//    /**
//     * Organize objects in the stream into a {@code Map}. Calls to {@code group()} are typically accompanied with
//     * {@link #by()} modulators which help specify how the grouping should occur.
//     *
//     * @param sideEffectKey the name of the side-effect key that will hold the aggregated grouping
//     * @return the traversal with an appended {@link GroupStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#group-step" target="_blank">Reference Documentation - Group Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> group(final String sideEffectKey) {
//        this.getBytecode().addStep(Symbols.group, sideEffectKey);
//        return this.addStep(new GroupSideEffectStep<>(this.asAdmin(), sideEffectKey));
//    }
//
//    /**
//     * @since 3.0.0-incubating
//     * @deprecated As of release 3.1.0, replaced by {@link #group(String)}.
//     */
//    public default GraphTraversal<S, E> groupV3d0(final String sideEffectKey) {
//        this.getBytecode().addStep(Symbols.groupV3d0, sideEffectKey);
//        return this.addStep(new GroupSideEffectStepV3d0<>(this.asAdmin(), sideEffectKey));
//    }
//
//    /**
//     * Counts the number of times a particular objects has been part of a traversal, returning a {@code Map} where the
//     * object is the key and the value is the count.
//     *
//     * @param sideEffectKey the name of the side-effect key that will hold the aggregated grouping
//     * @return the traversal with an appended {@link GroupCountStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#groupcount-step" target="_blank">Reference Documentation - GroupCount Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> groupCount(final String sideEffectKey) {
//        this.getBytecode().addStep(Symbols.groupCount, sideEffectKey);
//        return this.addStep(new GroupCountSideEffectStep<>(this.asAdmin(), sideEffectKey));
//    }
//
//    /**
//     * Aggregates the emanating paths into a {@link Tree} data structure.
//     *
//     * @param sideEffectKey the name of the side-effect key that will hold the tree
//     * @return the traversal with an appended {@link TreeStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#tree-step" target="_blank">Reference Documentation - Tree Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> tree(final String sideEffectKey) {
//        this.getBytecode().addStep(Symbols.tree, sideEffectKey);
//        return this.addStep(new TreeSideEffectStep<>(this.asAdmin(), sideEffectKey));
//    }
//
//    /**
//     * Map the {@link Traverser} to its {@link Traverser#sack} value.
//     *
//     * @param sackOperator the operator to apply to the sack value
//     * @return the traversal with an appended {@link SackStep}.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#sack-step" target="_blank">Reference Documentation - Sack Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <V, U> GraphTraversal<S, E> sack(final BiFunction<V, U, V> sackOperator) {
//        this.getBytecode().addStep(Symbols.sack, sackOperator);
//        return this.addStep(new SackValueStep<>(this.asAdmin(), sackOperator));
//    }
//
//    /**
//     * @since 3.0.0-incubating
//     * @deprecated As of release 3.1.0, replaced by {@link #sack(BiFunction)} with {@link #by(String)}.
//     */
//    @Deprecated
//    public default <V, U> GraphTraversal<S, E> sack(final BiFunction<V, U, V> sackOperator, final String elementPropertyKey) {
//        return this.sack(sackOperator).by(elementPropertyKey);
//    }
//
//    /**
//     * Lazily aggregates objects in the stream into a side-effect collection.
//     *
//     * @param sideEffectKey the name of the side-effect key that will hold the aggregate
//     * @return the traversal with an appended {@link StoreStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#store-step" target="_blank">Reference Documentation - Store Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> store(final String sideEffectKey) {
//        this.getBytecode().addStep(Symbols.store, sideEffectKey);
//        return this.addStep(new StoreStep<>(this.asAdmin(), sideEffectKey));
//    }
//
//    /**
//     * Allows developers to examine statistical information about a traversal providing data like execution times,
//     * counts, etc.
//     *
//     * @param sideEffectKey the name of the side-effect key within which to hold the profile object
//     * @return the traversal with an appended {@link ProfileSideEffectStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#profile-step" target="_blank">Reference Documentation - Profile Step</a>
//     * @since 3.2.0-incubating
//     */
//    public default GraphTraversal<S, E> profile(final String sideEffectKey) {
//        this.getBytecode().addStep(Traversal.Symbols.profile, sideEffectKey);
//        return this.addStep(new ProfileSideEffectStep<>(this.asAdmin(), sideEffectKey));
//    }
//
//    /**
//     * Allows developers to examine statistical information about a traversal providing data like execution times,
//     * counts, etc.
//     *
//     * @return the traversal with an appended {@link ProfileSideEffectStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#profile-step" target="_blank">Reference Documentation - Profile Step</a>
//     * @since 3.0.0-incubating
//     */
//    @Override
//    public default GraphTraversal<S, TraversalMetrics> profile() {
//        return (GraphTraversal<S, TraversalMetrics>) Traversal.super.profile();
//    }
//
//    /**
//     * Sets a {@link Property} value and related meta properties if supplied, if supported by the {@link Graph}
//     * and if the {@link Element} is a {@link VertexProperty}.  This method is the long-hand version of
//     * {@link #property(Object, Object, Object...)} with the difference that the
//     * {@link org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality} can be supplied.
//     * <p/>
//     * Generally speaking, this method will append an {@link AddPropertyStep} to the {@link Traversal} but when
//     * possible, this method will attempt to fold key/value pairs into an {@link AddVertexStep}, {@link AddEdgeStep} or
//     * {@link AddVertexStartStep}.  This potential optimization can only happen if cardinality is not supplied
//     * and when meta-properties are not included.
//     *
//     * @param cardinality the specified cardinality of the property where {@code null} will allow the {@link Graph}
//     *                    to use its  settings
//     * @param key         the key for the property
//     * @param value       the value for the property
//     * @param keyValues   any meta properties to be assigned to this property
//     * @return the traversal with the last step modified to add a property
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#addproperty-step" target="_blank">AddProperty Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> property(final VertexProperty.Cardinality cardinality, final Object key, final Object value, final Object... keyValues) {
//        if (null == cardinality)
//            this.getBytecode().addStep(Symbols.property, key, value, keyValues);
//        else
//            this.getBytecode().addStep(Symbols.property, cardinality, key, value, keyValues);
//
//        // if it can be detected that this call to property() is related to an addV/E() then we can attempt to fold
//        // the properties into that step to gain an optimization for those graphs that support such capabilities.
//        final Step endStep = this.getEndStep();
//        if ((endStep instanceof AddVertexStep || endStep instanceof AddEdgeStep || endStep instanceof AddVertexStartStep) &&
//                keyValues.length == 0 && null == cardinality) {
//            ((Mutating) endStep).addPropertyMutations(key, value);
//        } else {
//            this.addStep(new AddPropertyStep(this.asAdmin(), cardinality, key, value));
//            ((AddPropertyStep) this.getEndStep()).addPropertyMutations(keyValues);
//        }
//        return this;
//    }
//
//    /**
//     * Sets the key and value of a {@link Property}. If the {@link Element} is a {@link VertexProperty} and the
//     * {@link Graph} supports it, meta properties can be set.  Use of this method assumes that the
//     * {@link org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality} is ed to {@code null} which
//     * means that the  cardinality for the {@link Graph} will be used.
//     * <p/>
//     * This method is effectively calls
//     * {@link #property(org.apache.tinkerpop.gremlin.structure.VertexProperty.Cardinality, Object, Object, Object...)}
//     * as {@code property(null, key, value, keyValues}.
//     *
//     * @param key       the key for the property
//     * @param value     the value for the property
//     * @param keyValues any meta properties to be assigned to this property
//     * @return the traversal with the last step modified to add a property
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#addproperty-step" target="_blank">AddProperty Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> property(final Object key, final Object value, final Object... keyValues) {
//        return key instanceof VertexProperty.Cardinality ?
//                this.property((VertexProperty.Cardinality) key, value, keyValues[0],
//                        keyValues.length > 1 ?
//                                Arrays.copyOfRange(keyValues, 1, keyValues.length) :
//                                new Object[]{}) :
//                this.property(null, key, value, keyValues);
//    }
//
//    ///////////////////// BRANCH STEPS /////////////////////
//
//    /**
//     * Split the {@link Traverser} to all the specified traversals.
//     *
//     * @param branchTraversal the traversal to branch the {@link Traverser} to
//     * @return the {@link Traversal} with the {@link BranchStep} added
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#general-steps" target="_blank">Reference Documentation - General Steps</a>
//     * @since 3.0.0-incubating
//     */
//    public default <M, E2> GraphTraversal<S, E2> branch(final Traversal<?, M> branchTraversal) {
//        this.getBytecode().addStep(Symbols.branch, branchTraversal);
//        final BranchStep<E, E2, M> branchStep = new BranchStep<>(this.asAdmin());
//        branchStep.setBranchTraversal((Traversal.Admin<E, M>) branchTraversal);
//        return this.addStep(branchStep);
//    }
//
//    /**
//     * Split the {@link Traverser} to all the specified functions.
//     *
//     * @param function the traversal to branch the {@link Traverser} to
//     * @return the {@link Traversal} with the {@link BranchStep} added
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#general-steps" target="_blank">Reference Documentation - General Steps</a>
//     * @since 3.0.0-incubating
//     */
//    public default <M, E2> GraphTraversal<S, E2> branch(final Function<Traverser<E>, M> function) {
//        this.getBytecode().addStep(Symbols.branch, function);
//        final BranchStep<E, E2, M> branchStep = new BranchStep<>(this.asAdmin());
//        branchStep.setBranchTraversal((Traversal.Admin<E, M>) __.map(function));
//        return this.addStep(branchStep);
//    }
//
//    /**
//     * Routes the current traverser to a particular traversal branch option which allows the creation of if-then-else
//     * like semantics within a traversal. A {@code choose} is modified by {@link #option} which provides the various
//     * branch choices.
//     *
//     * @param choiceTraversal the traversal used to determine the value for the branch
//     * @return the traversal with the appended {@link ChooseStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#choose-step" target="_blank">Reference Documentation - Choose Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <M, E2> GraphTraversal<S, E2> choose(final Traversal<?, M> choiceTraversal) {
//        this.getBytecode().addStep(Symbols.choose, choiceTraversal);
//        return this.addStep(new ChooseStep<>(this.asAdmin(), (Traversal.Admin<E, M>) choiceTraversal));
//    }
//
//    /**
//     * Routes the current traverser to a particular traversal branch option which allows the creation of if-then-else
//     * like semantics within a traversal.
//     *
//     * @param traversalPredicate the traversal used to determine the "if" portion of the if-then-else
//     * @param trueChoice         the traversal to execute in the event the {@code traversalPredicate} returns true
//     * @param falseChoice        the traversal to execute in the event the {@code traversalPredicate} returns false
//     * @return the traversal with the appended {@link ChooseStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#choose-step" target="_blank">Reference Documentation - Choose Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> choose(final Traversal<?, ?> traversalPredicate,
//                                                     final Traversal<?, E2> trueChoice, final Traversal<?, E2> falseChoice) {
//        this.getBytecode().addStep(Symbols.choose, traversalPredicate, trueChoice, falseChoice);
//        return this.addStep(new ChooseStep<E, E2, Boolean>(this.asAdmin(), (Traversal.Admin<E, ?>) traversalPredicate, (Traversal.Admin<E, E2>) trueChoice, (Traversal.Admin<E, E2>) falseChoice));
//    }
//
//    /**
//     * Routes the current traverser to a particular traversal branch option which allows the creation of if-then
//     * like semantics within a traversal.
//     *
//     * @param traversalPredicate the traversal used to determine the "if" portion of the if-then-else
//     * @param trueChoice         the traversal to execute in the event the {@code traversalPredicate} returns true
//     * @return the traversal with the appended {@link ChooseStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#choose-step" target="_blank">Reference Documentation - Choose Step</a>
//     * @since 3.2.4
//     */
//    public default <E2> GraphTraversal<S, E2> choose(final Traversal<?, ?> traversalPredicate,
//                                                     final Traversal<?, E2> trueChoice) {
//        this.getBytecode().addStep(Symbols.choose, traversalPredicate, trueChoice);
//        return this.addStep(new ChooseStep<E, E2, Boolean>(this.asAdmin(), (Traversal.Admin<E, ?>) traversalPredicate, (Traversal.Admin<E, E2>) trueChoice, (Traversal.Admin<E, E2>) __.identity()));
//    }
//
//    /**
//     * Routes the current traverser to a particular traversal branch option which allows the creation of if-then-else
//     * like semantics within a traversal. A {@code choose} is modified by {@link #option} which provides the various
//     * branch choices.
//     *
//     * @param choiceFunction the function used to determine the value for the branch
//     * @return the traversal with the appended {@link ChooseStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#choose-step" target="_blank">Reference Documentation - Choose Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <M, E2> GraphTraversal<S, E2> choose(final Function<E, M> choiceFunction) {
//        this.getBytecode().addStep(Symbols.choose, choiceFunction);
//        return this.addStep(new ChooseStep<>(this.asAdmin(), (Traversal.Admin<E, M>) __.map(new FunctionTraverser<>(choiceFunction))));
//    }
//
//    /**
//     * Routes the current traverser to a particular traversal branch option which allows the creation of if-then-else
//     * like semantics within a traversal.
//     *
//     * @param choosePredicate the function used to determine the "if" portion of the if-then-else
//     * @param trueChoice      the traversal to execute in the event the {@code traversalPredicate} returns true
//     * @param falseChoice     the traversal to execute in the event the {@code traversalPredicate} returns false
//     * @return the traversal with the appended {@link ChooseStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#choose-step" target="_blank">Reference Documentation - Choose Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> choose(final Predicate<E> choosePredicate,
//                                                     final Traversal<?, E2> trueChoice, final Traversal<?, E2> falseChoice) {
//        this.getBytecode().addStep(Symbols.choose, choosePredicate, trueChoice, falseChoice);
//        return this.addStep(new ChooseStep<E, E2, Boolean>(this.asAdmin(), (Traversal.Admin<E, ?>) __.filter(new PredicateTraverser<>(choosePredicate)), (Traversal.Admin<E, E2>) trueChoice, (Traversal.Admin<E, E2>) falseChoice));
//    }
//
//    /**
//     * Routes the current traverser to a particular traversal branch option which allows the creation of if-then
//     * like semantics within a traversal.
//     *
//     * @param choosePredicate the function used to determine the "if" portion of the if-then-else
//     * @param trueChoice      the traversal to execute in the event the {@code traversalPredicate} returns true
//     * @return the traversal with the appended {@link ChooseStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#choose-step" target="_blank">Reference Documentation - Choose Step</a>
//     * @since 3.2.4
//     */
//    public default <E2> GraphTraversal<S, E2> choose(final Predicate<E> choosePredicate,
//                                                     final Traversal<?, E2> trueChoice) {
//        this.getBytecode().addStep(Symbols.choose, choosePredicate, trueChoice);
//        return this.addStep(new ChooseStep<E, E2, Boolean>(this.asAdmin(), (Traversal.Admin<E, ?>) __.filter(new PredicateTraverser<>(choosePredicate)), (Traversal.Admin<E, E2>) trueChoice, (Traversal.Admin<E, E2>) __.identity()));
//    }
//
//    /**
//     * Returns the result of the specified traversal if it yields a result, otherwise it returns the calling element.
//     *
//     * @param optionalTraversal the traversal to execute for a potential result
//     * @return the traversal with the appended {@link ChooseStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#optional-step" target="_blank">Reference Documentation - Optional Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> optional(final Traversal<?, E2> optionalTraversal) {
//        this.getBytecode().addStep(Symbols.optional, optionalTraversal);
//        return this.addStep(new ChooseStep<>(this.asAdmin(), (Traversal.Admin<E, ?>) optionalTraversal, (Traversal.Admin<E, E2>) optionalTraversal.clone(), (Traversal.Admin<E, E2>) __.<E2>identity()));
//    }
//
//    /**
//     * Merges the results of an arbitrary number of traversals.
//     *
//     * @param unionTraversals the traversals to merge
//     * @return the traversal with the appended {@link UnionStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#union-step" target="_blank">Reference Documentation - Union Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> union(final Traversal<?, E2>... unionTraversals) {
//        this.getBytecode().addStep(Symbols.union, unionTraversals);
//        return this.addStep(new UnionStep(this.asAdmin(), Arrays.copyOf(unionTraversals, unionTraversals.length, Traversal.Admin[].class)));
//    }
//
//    /**
//     * Evaluates the provided traversals and returns the result of the first traversal to emit at least one object.
//     *
//     * @param coalesceTraversals the traversals to coalesce
//     * @return the traversal with the appended {@link CoalesceStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#coalesce-step" target="_blank">Reference Documentation - Coalesce Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> coalesce(final Traversal<?, E2>... coalesceTraversals) {
//        this.getBytecode().addStep(Symbols.coalesce, coalesceTraversals);
//        return this.addStep(new CoalesceStep(this.asAdmin(), Arrays.copyOf(coalesceTraversals, coalesceTraversals.length, Traversal.Admin[].class)));
//    }
//
//    /**
//     * This step is used for looping over a some traversal given some break predicate.
//     *
//     * @param repeatTraversal the traversal to repeat over
//     * @return the traversal with the appended {@link RepeatStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#repeat-step" target="_blank">Reference Documentation - Repeat Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> repeat(final Traversal<?, E> repeatTraversal) {
//        this.getBytecode().addStep(Symbols.repeat, repeatTraversal);
//        return RepeatStep.addRepeatToTraversal(this, (Traversal.Admin<E, E>) repeatTraversal);
//    }
//
//    /**
//     * Emit is used in conjunction with {@link #repeat(Traversal)} to determine what objects get emit from the loop.
//     *
//     * @param emitTraversal the emit predicate defined as a traversal
//     * @return the traversal with the appended {@link RepeatStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#repeat-step" target="_blank">Reference Documentation - Repeat Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> emit(final Traversal<?, ?> emitTraversal) {
//        this.getBytecode().addStep(Symbols.emit, emitTraversal);
//        return RepeatStep.addEmitToTraversal(this, (Traversal.Admin<E, ?>) emitTraversal);
//    }
//
//    /**
//     * Emit is used in conjunction with {@link #repeat(Traversal)} to determine what objects get emit from the loop.
//     *
//     * @param emitPredicate the emit predicate
//     * @return the traversal with the appended {@link RepeatStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#repeat-step" target="_blank">Reference Documentation - Repeat Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> emit(final Predicate<Traverser<E>> emitPredicate) {
//        this.getBytecode().addStep(Symbols.emit, emitPredicate);
//        return RepeatStep.addEmitToTraversal(this, (Traversal.Admin<E, ?>) __.filter(emitPredicate));
//    }
//
//    /**
//     * Emit is used in conjunction with {@link #repeat(Traversal)} to emit all objects from the loop.
//     *
//     * @return the traversal with the appended {@link RepeatStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#repeat-step" target="_blank">Reference Documentation - Repeat Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> emit() {
//        this.getBytecode().addStep(Symbols.emit);
//        return RepeatStep.addEmitToTraversal(this, TrueTraversal.instance());
//    }
//
//    /**
//     * Modifies a {@link #repeat(Traversal)} to determine when the loop should exit.
//     *
//     * @param untilTraversal the traversal that determines when the loop exits
//     * @return the traversal with the appended {@link RepeatStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#repeat-step" target="_blank">Reference Documentation - Repeat Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> until(final Traversal<?, ?> untilTraversal) {
//        this.getBytecode().addStep(Symbols.until, untilTraversal);
//        return RepeatStep.addUntilToTraversal(this, (Traversal.Admin<E, ?>) untilTraversal);
//    }
//
//    /**
//     * Modifies a {@link #repeat(Traversal)} to determine when the loop should exit.
//     *
//     * @param untilPredicate the predicate that determines when the loop exits
//     * @return the traversal with the appended {@link RepeatStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#repeat-step" target="_blank">Reference Documentation - Repeat Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> until(final Predicate<Traverser<E>> untilPredicate) {
//        this.getBytecode().addStep(Symbols.until, untilPredicate);
//        return RepeatStep.addUntilToTraversal(this, (Traversal.Admin<E, ?>) __.filter(untilPredicate));
//    }
//
//    /**
//     * Modifies a {@link #repeat(Traversal)} to specify how many loops should occur before exiting.
//     *
//     * @param maxLoops the number of loops to execute prior to exiting
//     * @return the traversal with the appended {@link RepeatStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#repeat-step" target="_blank">Reference Documentation - Repeat Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> times(final int maxLoops) {
//        this.getBytecode().addStep(Symbols.times, maxLoops);
//        if (this.getEndStep() instanceof TimesModulating) {
//            ((TimesModulating) this.getEndStep()).modulateTimes(maxLoops);
//            return this;
//        } else
//            return RepeatStep.addUntilToTraversal(this, new LoopTraversal<>(maxLoops));
//    }
//
//    /**
//     * Provides a execute a specified traversal on a single element within a stream.
//     *
//     * @param localTraversal the traversal to execute locally
//     * @return the traversal with the appended {@link LocalStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#local-step" target="_blank">Reference Documentation - Local Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E2> local(final Traversal<?, E2> localTraversal) {
//        this.getBytecode().addStep(Symbols.local, localTraversal);
//        return this.addStep(new LocalStep<>(this.asAdmin(), localTraversal.asAdmin()));
//    }
//
//    /////////////////// VERTEX PROGRAM STEPS ////////////////
//
//    /**
//     * Calculates a PageRank over the graph using a 0.85 for the {@code alpha} value.
//     *
//     * @return the traversal with the appended {@link PageRankVertexProgramStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#pagerank-step" target="_blank">Reference Documentation - PageRank Step</a>
//     * @since 3.2.0-incubating
//     */
//    public default GraphTraversal<S, E> pageRank() {
//        return this.pageRank(0.85d);
//    }
//
//    /**
//     * Calculates a PageRank over the graph.
//     *
//     * @return the traversal with the appended {@link PageRankVertexProgramStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#pagerank-step" target="_blank">Reference Documentation - PageRank Step</a>
//     * @since 3.2.0-incubating
//     */
//    public default GraphTraversal<S, E> pageRank(final double alpha) {
//        this.getBytecode().addStep(Symbols.pageRank, alpha);
//        return this.addStep((Step<E, E>) new PageRankVertexProgramStep(this.asAdmin(), alpha));
//    }
//
//    /**
//     * Executes a Peer Pressure community detection algorithm over the graph.
//     *
//     * @return the traversal with the appended {@link PeerPressureVertexProgramStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#peerpressure-step" target="_blank">Reference Documentation - PeerPressure Step</a>
//     * @since 3.2.0-incubating
//     */
//    public default GraphTraversal<S, E> peerPressure() {
//        this.getBytecode().addStep(Symbols.peerPressure);
//        return this.addStep((Step<E, E>) new PeerPressureVertexProgramStep(this.asAdmin()));
//    }
//
//    /**
//     * Executes a Peer Pressure community detection algorithm over the graph.
//     *
//     * @return the traversal with the appended {@link PeerPressureVertexProgramStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#peerpressure-step" target="_blank">Reference Documentation - PeerPressure Step</a>
//     * @since 3.2.0-incubating
//     */
//    public default GraphTraversal<S, E> program(final VertexProgram<?> vertexProgram) {
//        return this.addStep((Step<E, E>) new ProgramVertexProgramStep(this.asAdmin(), vertexProgram));
//    }
//
//    ///////////////////// UTILITY STEPS /////////////////////
//
//    /**
//     * A step modulator that provides a lable to the step that can be accessed later in the traversal by other steps.
//     *
//     * @param stepLabel  the name of the step
//     * @param stepLabels additional names for the label
//     * @return the traversal with the modified end step
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#as-step" target="_blank">Reference Documentation - As Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> as(final String stepLabel, final String... stepLabels) {
//        this.getBytecode().addStep(Symbols.as, stepLabel, stepLabels);
//        if (this.getSteps().size() == 0) this.addStep(new StartStep<>(this.asAdmin()));
//        final Step<?, E> endStep = this.getEndStep();
//        endStep.addLabel(stepLabel);
//        for (final String label : stepLabels) {
//            endStep.addLabel(label);
//        }
//        return this;
//    }
//
//    /**
//     * Turns the lazy traversal pipeline into a bulk-synchronous pipeline which basically iterates that traversal to
//     * the size of the barrier. In this case, it iterates the entire thing as the  barrier size is set to
//     * {@code Integer.MAX_VALUE}.
//     *
//     * @return the traversal with an appended {@link NoOpBarrierStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#barrier-step" target="_blank">Reference Documentation - Barrier Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> barrier() {
//        return this.barrier(Integer.MAX_VALUE);
//    }
//
//    /**
//     * Turns the lazy traversal pipeline into a bulk-synchronous pipeline which basically iterates that traversal to
//     * the size of the barrier.
//     *
//     * @param maxBarrierSize the size of the barrier
//     * @return the traversal with an appended {@link NoOpBarrierStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#barrier-step" target="_blank">Reference Documentation - Barrier Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> barrier(final int maxBarrierSize) {
//        this.getBytecode().addStep(Symbols.barrier, maxBarrierSize);
//        return this.addStep(new NoOpBarrierStep<>(this.asAdmin(), maxBarrierSize));
//    }
//
//    /**
//     * Turns the lazy traversal pipeline into a bulk-synchronous pipeline which basically iterates that traversal to
//     * the size of the barrier. In this case, it iterates the entire thing as the  barrier size is set to
//     * {@code Integer.MAX_VALUE}.
//     *
//     * @param barrierConsumer a consumer function that is applied to the objects aggregated to the barrier
//     * @return the traversal with an appended {@link NoOpBarrierStep}
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#barrier-step" target="_blank">Reference Documentation - Barrier Step</a>
//     * @since 3.2.0-incubating
//     */
//    public default GraphTraversal<S, E> barrier(final Consumer<TraverserSet<Object>> barrierConsumer) {
//        this.getBytecode().addStep(Symbols.barrier, barrierConsumer);
//        return this.addStep(new LambdaCollectingBarrierStep<>(this.asAdmin(), (Consumer) barrierConsumer, Integer.MAX_VALUE));
//    }
//
//
//    //// BY-MODULATORS
//
//    /**
//     * The {@code by()} can be applied to a number of different step to alter their behaviors. This form is essentially
//     * an {@link #identity()} modulation.
//     *
//     * @return the traversal with a modulated step.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#by-step" target="_blank">Reference Documentation - By Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> by() {
//        this.getBytecode().addStep(Symbols.by);
//        ((ByModulating) this.getEndStep()).modulateBy();
//        return this;
//    }
//
//    /**
//     * The {@code by()} can be applied to a number of different step to alter their behaviors. Modifies the previous
//     * step with the specified traversal.
//     *
//     * @param traversal the traversal to apply
//     * @return the traversal with a modulated step.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#by-step" target="_blank">Reference Documentation - By Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> by(final Traversal<?, ?> traversal) {
//        this.getBytecode().addStep(Symbols.by, traversal);
//        ((ByModulating) this.getEndStep()).modulateBy(traversal.asAdmin());
//        return this;
//    }
//
//    /**
//     * The {@code by()} can be applied to a number of different step to alter their behaviors. Modifies the previous
//     * step with the specified token of {@link T}.
//     *
//     * @param token the token to apply
//     * @return the traversal with a modulated step.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#by-step" target="_blank">Reference Documentation - By Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> by(final T token) {
//        this.getBytecode().addStep(Symbols.by, token);
//        ((ByModulating) this.getEndStep()).modulateBy(token);
//        return this;
//    }
//
//    /**
//     * The {@code by()} can be applied to a number of different step to alter their behaviors. Modifies the previous
//     * step with the specified key.
//     *
//     * @param key the key to apply
//     * @return the traversal with a modulated step.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#by-step" target="_blank">Reference Documentation - By Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> by(final String key) {
//        this.getBytecode().addStep(Symbols.by, key);
//        ((ByModulating) this.getEndStep()).modulateBy(key);
//        return this;
//    }
//
//    /**
//     * The {@code by()} can be applied to a number of different step to alter their behaviors. Modifies the previous
//     * step with the specified function.
//     *
//     * @param function the function to apply
//     * @return the traversal with a modulated step.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#by-step" target="_blank">Reference Documentation - By Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <V> GraphTraversal<S, E> by(final Function<V, Object> function) {
//        this.getBytecode().addStep(Symbols.by, function);
//        ((ByModulating) this.getEndStep()).modulateBy(function);
//        return this;
//    }
//
//    //// COMPARATOR BY-MODULATORS
//
//    /**
//     * The {@code by()} can be applied to a number of different step to alter their behaviors. Modifies the previous
//     * step with the specified function.
//     *
//     * @param traversal  the traversal to apply
//     * @param comparator the comparator to apply typically for some {@link #order()}
//     * @return the traversal with a modulated step.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#by-step" target="_blank">Reference Documentation - By Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <V> GraphTraversal<S, E> by(final Traversal<?, ?> traversal, final Comparator<V> comparator) {
//        this.getBytecode().addStep(Symbols.by, traversal, comparator);
//        ((ByModulating) this.getEndStep()).modulateBy(traversal.asAdmin(), comparator);
//        return this;
//    }
//
//    /**
//     * The {@code by()} can be applied to a number of different step to alter their behaviors. Modifies the previous
//     * step with the specified function.
//     *
//     * @param comparator the comparator to apply typically for some {@link #order()}
//     * @return the traversal with a modulated step.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#by-step" target="_blank">Reference Documentation - By Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> by(final Comparator<E> comparator) {
//        this.getBytecode().addStep(Symbols.by, comparator);
//        ((ByModulating) this.getEndStep()).modulateBy(comparator);
//        return this;
//    }
//
//    /**
//     * The {@code by()} can be applied to a number of different step to alter their behaviors. Modifies the previous
//     * step with the specified function.
//     *
//     * @param order the comparator to apply typically for some {@link #order()}
//     * @return the traversal with a modulated step.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#by-step" target="_blank">Reference Documentation - By Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default GraphTraversal<S, E> by(final Order order) {
//        this.getBytecode().addStep(Symbols.by, order);
//        ((ByModulating) this.getEndStep()).modulateBy(order);
//        return this;
//    }
//
//    /**
//     * The {@code by()} can be applied to a number of different step to alter their behaviors. Modifies the previous
//     * step with the specified function.
//     *
//     * @param key        the key to apply                                                                                                     traversal
//     * @param comparator the comparator to apply typically for some {@link #order()}
//     * @return the traversal with a modulated step.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#by-step" target="_blank">Reference Documentation - By Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <V> GraphTraversal<S, E> by(final String key, final Comparator<V> comparator) {
//        this.getBytecode().addStep(Symbols.by, key, comparator);
//        ((ByModulating) this.getEndStep()).modulateBy(key, comparator);
//        return this;
//    }
//
//    /**
//     * The {@code by()} can be applied to a number of different step to alter their behaviors. Modifies the previous
//     * step with the specified function.
//     *
//     * @param function   the function to apply
//     * @param comparator the comparator to apply typically for some {@link #order()}
//     * @return the traversal with a modulated step.
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#by-step" target="_blank">Reference Documentation - By Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <U> GraphTraversal<S, E> by(final Function<U, Object> function, final Comparator comparator) {
//        this.getBytecode().addStep(Symbols.by, function, comparator);
//        ((ByModulating) this.getEndStep()).modulateBy(function, comparator);
//        return this;
//    }
//
//    ////
//
//    /**
//     * This step modifies {@link #choose(Function)} to specifies the available choices that might be executed.
//     *
//     * @param pickToken       the token that would trigger this option
//     * @param traversalOption the option as a traversal
//     * @return the traversal with the modulated step
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#choose-step" target="_blank">Reference Documentation - Choose Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <M, E2> GraphTraversal<S, E> option(final M pickToken, final Traversal<E, E2> traversalOption) {
//        this.getBytecode().addStep(Symbols.option, pickToken, traversalOption);
//        ((TraversalOptionParent<M, E, E2>) this.getEndStep()).addGlobalChildOption(pickToken, traversalOption.asAdmin());
//        return this;
//    }
//
//    /**
//     * This step modifies {@link #choose(Function)} to specifies the available choices that might be executed.
//     *
//     * @param traversalOption the option as a traversal
//     * @return the traversal with the modulated step
//     * @see <a href="http://tinkerpop.apache.org/docs/${project.version}/reference/#choose-step" target="_blank">Reference Documentation - Choose Step</a>
//     * @since 3.0.0-incubating
//     */
//    public default <E2> GraphTraversal<S, E> option(final Traversal<E, E2> traversalOption) {
//        this.getBytecode().addStep(Symbols.option, traversalOption);
//        return this.option(TraversalOptionParent.Pick.any, traversalOption.asAdmin());
//    }

    ////


    ////
}