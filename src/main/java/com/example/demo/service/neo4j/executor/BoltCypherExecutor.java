package com.example.demo.service.neo4j.executor;

import com.example.demo.service.neo4j.util.Neo4jUtil;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


public class BoltCypherExecutor implements CypherExecutor {

    private final org.neo4j.driver.v1.Driver driver;

    public BoltCypherExecutor() {
        driver = Neo4jUtil.getNeo4jDriver();
    }

    @Override
    public List<Map<String, Object>> query(String statement, Object... params) {

        Map<String, Object> map = new HashMap<>();

        IntStream.range(0,params.length/2).forEach(i -> map.put((String)params[2*i],params[2*i+1]));

        return query(statement, map);
    }

    @Override
    public List<Map<String, Object>> query(String query, Map<String, Object> params) {
        try (Session session = driver.session()) {
            List<Map<String, Object>> list = session.run(query, params)
                    .list( r -> r.asMap(BoltCypherExecutor::convert));
            return list;
        }
    }

    static Object convert(Value value) {
        switch (value.type().name()) {
            case "PATH":
                return value.asList(BoltCypherExecutor::convert);
            case "NODE":
            case "RELATIONSHIP":
                return value.asMap();
        }
        return value.asObject();
    }

}
