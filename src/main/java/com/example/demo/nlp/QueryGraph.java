package com.example.demo.nlp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.tinkerpop.Neo4jHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by jfd on 11/10/17.
 */
@Component
public class QueryGraph {

    @Autowired
    private Neo4jHttpClient neo4jHttpClient;

    private static final String R1 = "r1";
    private static final String E1 = "e1";
    private static final String R2 = "r2";
    private static final String E2 = "e2";

    /**
     * [[Cadre,Study,School],[Cadre,Position,Organization],[]]
     * @param label
     * @return
     */
    public List<List> oneStep(String label){
        if(neo4jHttpClient == null) neo4jHttpClient = new Neo4jHttpClient();
        String script = "g.V().hasLabel('"+label+"').outE().inV().path().by(label()).toSet()";
        Object response = neo4jHttpClient.execute(script);
        return responseParse(response);
    }

    public List<List> twoStep(String label){

        if(neo4jHttpClient == null) neo4jHttpClient = new Neo4jHttpClient();

        String script = "g.V().hasLabel('"+label+"').outE().inV().outE().inV().path().by(label()).toSet()";
        Object response = neo4jHttpClient.execute(script);
        return responseParse(response);
    }

    private List<List> responseParse(Object o){
        JSONArray jsonArray = JSONArray.parseArray(o.toString());

        List<List> list = jsonArray.stream().map(j -> (List)((JSONObject) j).get("objects")).collect(toList());
        return list;
    }

}
