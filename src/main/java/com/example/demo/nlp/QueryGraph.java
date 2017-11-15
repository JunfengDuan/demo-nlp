package com.example.demo.nlp;

import com.example.demo.service.tinkerpop.Neo4jGraph;
import com.example.demo.service.tinkerpop.graph.Edge;
import com.example.demo.service.tinkerpop.graph.Vertex;
import com.example.demo.service.tinkerpop.traversal.Neo4jHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jfd on 11/10/17.
 */
@Component
public class QueryGraph {

    @Autowired
    private Neo4jGraph neo4jGraph;
    @Autowired
    private Neo4jHttpClient neo4jHttpClient;

    private static final String R1 = "r1";
    private static final String E1 = "e1";
    private static final String R2 = "r2";
    private static final String E2 = "e2";

    /**
     * http://192.168.1.151:7474/tp/gremlin/execute?script=g.V().hasLabel(%22Cadre%22).limit(1).outE().label().toSet()
     *
     * http://192.168.1.151:7474/tp/gremlin/execute?script=g.V().hasLabel(%22Cadre%22).limit(1).out().label().toSet()
     * @param label
     * @return
     */
    public Map<String,Object> getOneStep(String label){

        List<Edge> edges = neo4jGraph.traversal().V().hasLabel(label).outE().toList();

        Set<String> edgesSet = edges.stream().map(e -> e.label()).collect(Collectors.toSet());

        List<Vertex> vertices = neo4jGraph.traversal().V().hasLabel(label).out().toList();
        Set<String> verticesSet = vertices.stream().map(e -> e.label()).collect(Collectors.toSet());

        return new HashMap(){{put(R1, edgesSet); put(E1, verticesSet);}};
    }

    /**
     * [[Cadre,Study,School],[Cadre,Position,Organization],[]]
     * @param label
     * @return
     */
    public List<List> oneStep(String label){
        String script = "";
        neo4jHttpClient.execute(script);
        return new ArrayList<>();
    }

    public List<List> twoStep(String label){


        return new ArrayList<>();
    }

    /**
     * http://192.168.1.151:7474/tp/gremlin/execute?script=g.V().hasLabel(%22Cadre%22).out().outE().label().toSet()
     * @param label
     * @return
     */
    public Map<String,Object> getTwoStep(String label){

        Map<String, Object> oneRelation = getOneStep(label);
        Set<String> r1s = (Set) oneRelation.get(R1);
        Set<String> e1s = (Set) oneRelation.get(E1);


        Set<String> r2s = neo4jGraph.traversal().V().hasLabel(label).out().outE().toList()
                .stream().map(e -> e.label()).collect(Collectors.toSet());

        Set<String> e2s = neo4jGraph.traversal().V().hasLabel(label).out().out().toList()
                .stream().map(e -> e.label()).collect(Collectors.toSet());


        return new HashMap(){{put(R1, r1s); put(E1, e1s);put(R2, r2s); put(E2, e2s);}};
    }
}
