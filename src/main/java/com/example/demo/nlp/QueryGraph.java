package com.example.demo.nlp;

import com.example.demo.service.tinkerpop.Neo4jGraph;
import com.example.demo.service.tinkerpop.graph.Edge;
import com.example.demo.service.tinkerpop.graph.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jfd on 11/10/17.
 */
@Component
public class QueryGraph {

    @Autowired
    private Neo4jGraph neo4jGraph;
    private static final String EDGES = "edges";
    private static final String VERTICES = "vertices";

    /**
     * http://192.168.1.151:7474/tp/gremlin/execute?script=g.V().hasLabel(%22Cadre%22).limit(1).outE().label().toSet()
     *
     * http://192.168.1.151:7474/tp/gremlin/execute?script=g.V().hasLabel(%22Cadre%22).limit(1).out().label().toSet()
     * @param label
     * @return
     */
    public Map<String,Object> getOneRelation(String label){

        List<Edge> edges = neo4jGraph.traversal().V().hasLabel(label).limit(1).outE().toList();
        Set<String> edgesSet = edges.stream().map(e -> e.label()).collect(Collectors.toSet());

        List<Vertex> vertices = neo4jGraph.traversal().V().hasLabel(label).limit(1).out().toList();
        Set<String> verticesSet = vertices.stream().map(e -> e.label()).collect(Collectors.toSet());

        return new HashMap(){{put(EDGES, edgesSet); put(VERTICES, verticesSet);}};
    }

    public Map<String,Object> getTwoRelation(String label){
        List<Edge> edges = neo4jGraph.traversal().V().hasLabel(label).limit(1).outE().toList();
        List<Vertex> vertices = neo4jGraph.traversal().V().hasLabel(label).limit(1).outV().toList();
        return new HashMap(){{put(EDGES, edges); put(VERTICES, vertices);}};
    }
}
