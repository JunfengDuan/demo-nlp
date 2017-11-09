package com.example.demo.service.neo4j.executor;

import java.util.List;
import java.util.Map;

public interface CypherExecutor {
	List<Map<String, Object>> query(String statement, Map<String, Object> params);
	List<Map<String, Object>> query(String statement, Object... params);
}
