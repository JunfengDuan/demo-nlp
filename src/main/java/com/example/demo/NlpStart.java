package com.example.demo;

import com.example.demo.nlp.*;
import com.example.demo.service.tinkerpop.Neo4jGraph;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static com.example.demo.nlp.AlgorithmLibrary.*;
import static java.util.stream.Collectors.toList;
import static com.example.demo.nlp.StringConst.*;

@Component
public class NlpStart{

    private static final Logger logger = LoggerFactory.getLogger(NlpStart.class);

    @Autowired
    private SemanticParser semanticParser;
    @Autowired
    private QueryGraph queryGraph;



    @PostConstruct
    public void run(){
        logger.info("Start segment");
        String query = "少数民族女市管干部";
        String query1 = "干部";
        String query2 = "少数民族女";
        String query3 = "市管干部";
        String query4 = "女非中共党员干部";
        String query5 = "籍贯是河北的干部";
        String query6 = "全部局级非领导职务干部";
        String query7 = "全部市管非局级领导职务干部";
        String query8 = "小于30岁的处级干部";
        String query9 = "30-35岁的正处级干部";
        String query10 = "30到35岁之间的正处级干部";
        String query11 = "小于三十岁的正处级干部";
        String query12 = "1990年出生的干部";
        String query13 = "30岁的干部";
        String query14 = "北京市公安局80后干部";
        String query15 = "质监局1990年出生的干部";
        String query16 = "八零后干部";
        String query17 = "三十岁的干部";
        String query18 = "毕业于北京大学的少数民族女干部";

        Map<String, Object> result = search(query18);
        logger.info("\nAfter segment :{}",result);

    }


    /**
     * 自然语言搜索
     * @param query
     * @return
     */
    public Map<String, Object> search(String query){

        List<Map<String, Object>> labels = new ArrayList<>();
        List<Map<String, Object>> props = new ArrayList<>();
        Map<String,Object> customDict = new HashMap<>();

        //存放属性及其操作 key:Cadre.age; value:>30
        Map<String, Object> args = new HashMap<>();

        semanticParser.parse(customDict, labels, props, query);

        //利用知识库作实体、关系的筛选
        entityLinkedWithKB(labels, props);


        props.forEach(e -> {
            String key = e.get(LABEL) +"."+ e.get(FIELD);
            String value = (String) e.get(VALUE);

            if(args.containsKey(key)){
                List<String> values = (List<String>) args.get(key);
                values.add(value);
            }else {
                args.put(key, new ArrayList<String>(){{add(value);}});
            }

            String label = (String) e.get(LABEL);
            String type = (String) e.get(TYPE);
            Map<String,Object> labelMap = new HashMap(){{put(LABEL, label);put(TYPE, type);}};
            if(!labels.contains(label))
                labels.add(labelMap);
        });

        Map<String, Object> params = matcher(args);

        //对自定义的属性条件做进一步处理
        Map<String, Object> properties = customPropsHandle(customDict, params);


        return new HashMap(){{put(LABEL,labels);put("properties",properties);}};
    }

    private void entityLinkedWithKB(List<Map<String, Object>> labels, List<Map<String, Object>> todoProps) {
        List<String> labelList = new ArrayList<>();
        labels.stream().map(l -> (String)l.get(LABEL)).forEach(labelList :: add);
        todoProps.stream().map(p -> (String) p.get(LABEL)).forEach(labelList :: add);
        List<List> oneStep = labelList.stream().map(label -> queryGraph.oneStep(label)).flatMap(list -> list.stream()).collect(toList());

    }

    private Map<String, Object> customPropsHandle(Map<String, Object> customDict, Map<String, Object> params) {
        Map<String,Object> properties = new HashMap<>();
        params.entrySet().forEach(entry -> {
            String value = (String) entry.getValue();
            if(customDict.containsKey(value)){
                value = " <> '" +customDict.get(value)+"'";
            }else {
                value = " contains '"+value+"'";
            }
            properties.put(entry.getKey(), value);
        });
        return properties;
    }

    /**
     * 对模糊匹配到的属性集合做最佳匹配度筛选
     * @param map
     * @return
     */
    private Map matcher(Map<String, Object> map){
        Map<String, Object> params = new HashMap<>();
        map.entrySet().forEach(entry ->{
            List<String> list =  (List<String>) entry.getValue();
            if(list.isEmpty()){
                params.put(entry.getKey(),"");
            }else if(list.size()==1){
                params.put(entry.getKey(),list.get(0));
            }else {
                params.put(entry.getKey(),compare(list));
            }

        });
        return params;
    }

    /**
     * 计算word的匹配度
     * @param list
     * @return
     */
    private String compare(List<String> list) {
        Map<String,Integer> map  = new HashMap<>();
        List<String> keys = new ArrayList<>();
        list.forEach(s -> map.put(s, s.length()));
        map.values().stream().sorted(Comparator.reverseOrder()).forEach(n -> {
            map.keySet().stream().filter(key -> map.get(key)==n).forEach(k -> {
                if(!keys.contains(k)) keys.add(k);});
        });
        return keys.get(0);
    }



}
