package com.example.demo.service.neo4j.util;

import java.util.List;
import java.util.Map;

/**
 * Neo4j cypherSql 接口类
 * @author djf 2017/7/14
 * @version 1.0
 */
public interface Neo4jCypher {

    String PROPERTIES = "match (n:label) return n";

    String QUERY_ENTITY = "MATCH (%s:%s)" +
            " WHERE lower(%s.%s) CONTAINS {part}" +
            " RETURN %s";

    String QUERY_REL = "MATCH (%s:%s)" +
            " OPTIONAL MATCH p=(n)-[r]-(m)" +
            "WHERE " +
            " RETURN  p LIMIT 1";

    String QUERY_GRAPH = "MATCH (n:{label})<-[r]-() " +
            " WHERE lower(n.{att}) CONTAINS {part}" +
            " RETURN r " +
            " LIMIT {limit}";
    /**
     * 拼接cypherSql开始部分
     * @param label 开始节点
     * @return
     */
    String doMatch(String label);
    /**
     * 拼接cypherSql关联部分
     * @param startLabel 开始节点
     * @param midLabel 关系
     * @param endLabel 结束节点
     * @return
     */
    String doOptionalMatch(String startLabel, String midLabel, String endLabel);
    /**
     * 拼接cypherSql关联条件
     * @param args 参数集合
     * @return
     */
    String doWhere(Map<String, Object> args);
    /**
     * cypherSql返回
     * @param startLabel 开始节点
     * @param list cypherSql中包含的节点，关系集合
     * @return
     */
    String doReturn(String startLabel, List<String> list);
    /**
     * 创建完整的cypherSql语句
     * @param startLabel 开始节点
     * @param midLabel 关系
     * @param endLabel 结束节点
     * @param args 参数集合
     * @param skip 分页开始位置
     * @param limit 分页大小
     * @return
     */
    String makeCypher(String startLabel, String midLabel, String endLabel, Map<String, Object> args, int skip, int limit);
    /**
     * 创建完整的cypherSql语句
     * @param label 开始节点
     * @param args 参数集合
     * @param formula 参数的计算表达式
     * @param skip 分页开始位置
     * @param limit 分页大小
     * @return
     */
    String makeCypher(String label, List<Map<String, Object>> args, String formula, int skip, int limit);
    /**
     * 统计cypherSql
     * @param startLabel 开始节点
     * @param midLabel 关系
     * @param endLabel 结束节点
     * @param args 参数集合
     * @return
     */
    String doCount(String startLabel, String midLabel, String endLabel, Map<String, Object> args);
    /**
     * 统计cypherSql
     * @param label 开始节点
     * @param args 参数集合
     * @param formula 参数的计算表达式
     * @param skip 分页开始位置
     * @param limit 分页大小
     * @return
     */
    String doCount(String label, List<Map<String, Object>> args, String formula, int skip, int limit);

    String cypherTemplate(int templateNum, Map<String, Object> args, String... labels);



}
