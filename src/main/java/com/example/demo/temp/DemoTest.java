package com.example.demo.temp;

import com.example.demo.service.elasticsearch.ElasticsearchFullSearch;
import com.example.demo.service.tinkerpop.Neo4jGraph;
import com.example.demo.service.tinkerpop.graph.Edge;
import com.example.demo.service.tinkerpop.graph.Vertex;
import com.example.demo.service.tinkerpop.internal.Neo4jGraphImpl;
import com.example.demo.service.tinkerpop.traversal.GraphTraversal;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jfd on 11/8/17.
 */
public class DemoTest {


    public static void main(String[] args){
        String s = "aaaa(sdf证据）几倍322&&是地方";
        String query8 = "小于30岁的处级干部";
        String query9 = "30-35岁的正处级干部";
        String query10 = "30到35岁之间的正处级干部";
        String query12 = "1990年出生的干部";
        String query5 = "籍贯是河北的干部";
        String query6 = "毕业于北京大学的少数民族女干部";

//        SegmentJob segmentJob = new SegmentJob();
//        Map<String, String> nlp = segmentJob.doNlp(query6);

      /*  ElasticsearchFullSearch fullSearch = new ElasticsearchFullSearch();
        Map<String, Object> map  = new HashMap<>();
        map.put("type", "propDict");
        map.put("size", 10);
        List list = fullSearch.StringMatch("党员", map);

        System.out.println("result: "+list);*/
        Neo4jGraph neo4jGraph = new Neo4jGraphImpl();
//        List<String> list = neo4jGraph.traversal().V().hasLabel("Cadre").outE().label().toList();
        List<Edge> edges = neo4jGraph.traversal().V().hasLabel("Cadre").has("name","解元新").outE().toList();
        edges.forEach(e -> System.out.println(e.label()));


    }



    // 过滤特殊字符
    private static String stringFilter(String str){
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }

    // 范围解析
    private static List rangeParse(String str){
        List<String> list = new ArrayList<>();
        String regex = "[0-9]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        System.out.println(m.regionStart());
        System.out.println(m.regionEnd());
        while (m.find()){
            list.add(m.group());
        }
        return  list;
    }

    public static boolean isNumeric(String str){
       /* Pattern pattern = Pattern.compile("[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;*/
        return str.matches(".*\\d+.*");
    }


    private static String maxLength(List<String> list) {
        if(list.isEmpty()) return "";
        Map<String,Integer> map  = new HashMap<>();
        List<String> keys = new ArrayList<>();
        list.forEach(s -> map.put(s, s.length()));
        map.values().stream().sorted(Comparator.reverseOrder()).forEach(n -> {
            map.keySet().stream().filter(key -> map.get(key)==n).forEach(k -> {if(!keys.contains(k)) keys.add(k);});
        });
        return keys.get(0);
    }

    private static String findCommonString(String query, String prop) {
        List strList= new ArrayList();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < query.length(); i++) {
            String s = String.valueOf(query.charAt(i));
            if(prop.contains(s)){
                sb.append(s);
            }else if(sb.length()>0){
                strList.add(sb.toString());
                sb.delete(0,sb.length());
            }

        }
        String commonString = maxLength(strList);

        return commonString;
    }
}
