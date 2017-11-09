package com.example.demo.service.neo4j;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.neo4j.executor.BoltCypherExecutor;
import com.example.demo.service.neo4j.executor.CypherExecutor;
import com.example.demo.service.neo4j.util.Neo4jCypher;
import com.example.demo.service.neo4j.util.Neo4jCypherConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.service.neo4j.util.Neo4jCypher.QUERY_ENTITY;
import static com.example.demo.service.neo4j.util.Neo4jCypher.QUERY_GRAPH;


public class Neo4jService {
    private CypherExecutor cypherExecutor;
    private Neo4jCypher neo4jCypher;

    public Neo4jService(){
        cypherExecutor = new BoltCypherExecutor();
        neo4jCypher = new Neo4jCypherConstructor();
    }

    public List<Map<String,Object>> search(String key, String label, String att) {

        if (key==null || key.trim().isEmpty()) return Collections.emptyList();

        String formatStr = String.format(QUERY_ENTITY, label.toLowerCase(), label, label.toLowerCase(), att, label.toLowerCase());

        return cypherExecutor.query(formatStr, "part", key.toLowerCase());
    }

    public List<Map<String,Object>> deepSearch(List<Map<String, Object>> terms, int skip, int limit) {
        neo4jCypher = new Neo4jCypherConstructor(skip, limit);

        if (terms == null || terms.isEmpty()) return Collections.emptyList();
        List<String> labels = new ArrayList<>();
        Map<String, Object> args = new HashMap<>();

        List<Map<String,Object>> collection = new ArrayList<>();
        int total = collection.size();
        List<Map<String, Object>> conditions = terms.stream().filter(term -> StringUtils.isNotBlank((String) term.get(VALUE))).collect(Collectors.toList());
        collection.add(new HashMap<String, Object>(){{put("total",total);}{put("filter",conditions);}});
        return collection;
    }


    public Map<String, Object> graph(String label, String att, String key, int limit) {
        List<Map<String, Object>> result = cypherExecutor.query(QUERY_GRAPH,
        		"label",label, "att",att, "limit", limit, "part", key.toLowerCase());
        List nodes = new ArrayList();
        List rels= new ArrayList();
        int i=0;
        while (result.iterator().hasNext()) {
            Map<String, Object> row = result.iterator().next();
            nodes.add(map("text",row.get("movie"),"label","movie"));
            int target=i;
            i++;
            for (Object name : (Collection) row.get("person")) {
                Map<String, Object> actor = map("text", name,"label","actor");
                int source = nodes.indexOf(actor);
                if (source == -1) {
                    nodes.add(actor);
                    source = i++;
                }
                rels.add(map("source",source,"target",target));
            }
        }
        return map("nodes", nodes, "links", rels);
    }
    
    private Map map(Object...objects) {
    	Map<String, Object> map = new HashMap<String, Object>();
		
		int i = objects.length / 2;
		for(int j = 0; j < i; j ++) {
			String key = objects[j].toString();
			Object value = objects[j + 1];
			map.put(key, value);
		}
		return map;
    }

    private static final String LABEL = "label";
    private static final String FIELD = "field";
    private static final String VALUE = "value";
    private static final String OPERATION = "op";
}