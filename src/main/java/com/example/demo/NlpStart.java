package com.example.demo;

import com.example.demo.nlp.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static com.example.demo.nlp.StringConst.*;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

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
        String query0 ="北京大学";
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

        Map<String, Object> result = search(query0);
        logger.info("\nAfter segment :{}",result);

    }


    /**
     * 自然语言搜索
     * @param query 自然语言文本
     * @return 查询结果集
     */
    public Map<String, Object> search(String query){

        //实体或者关系的类型,用label表示;节点或边,用e/r表示.map={label="",type='e'/'r'}
        List<Map<String, Object>> labels = new ArrayList<>();
        //实体或关系类型-label,名称字段-field，名称-value,实体名称的约束操作-op，map={label=,field=,value=,type=,op=}
        List<Map<String, Object>> props = new ArrayList<>();
        //记录含有“非”的词的词典
        Map<String,Object> customDict = new HashMap<>();

        //临时存放具体的某一个实体及其操作 key:Cadre.age; value:>30
        Map<String, Object> args = new HashMap<>();

        //自然语言处理，解析text中的实体和类型
        semanticParser.parse(customDict, labels, props, query);

        //实体类型的集合
        List<String> labelResult = new ArrayList<>();
        labels.stream().map(l -> (String) l.get(LABEL)).forEach(labelResult :: add);
        props.stream().map(p -> (String) p.get(LABEL)).forEach(labelResult :: add);
        removeDuplicate(labelResult);

        //利用知识库对候选实体、关系的筛选
        List<String> linkedWithKB = entityLinkedWithKB(labelResult);
        logger.debug("LinkedWithKB:{}",linkedWithKB);

        //对候选属性筛选
        List<Map<String, Object>> properties = propertiesFilter(props, customDict, args);
        Set<Map<String, Object>> propertyResult = properties.stream().filter(map -> linkedWithKB.contains(map.get(LABEL))).collect(toSet());

        logger.debug("Filtered properties:{}",propertyResult);
        return new HashMap(){{put(LABEL,linkedWithKB);put(PROPERTIES,propertyResult);}};
    }

    /**
     *
     * @param hangLabels 待处理的实体类型
     * @return 结合知识库知识后返回最相关的实体及关系集合
     */
    private List<String> entityLinkedWithKB(List<String> hangLabels) {

        if(hangLabels.size()<=1) return hangLabels;

        List<List> oneStep = hangLabels.stream().map(label -> queryGraph.oneStep(label)).flatMap(list -> list.stream()).collect(toList());
        List<List> twoStep = hangLabels.stream().map(label -> queryGraph.twoStep(label)).flatMap(list -> list.stream()).collect(toList());
        List<List> kbLabels = new ArrayList<>(oneStep);
        kbLabels.addAll(twoStep);
        logger.debug("kbLabels:{}",kbLabels);

        List<Map> rdfs = kbLabels.stream().distinct().map(rdf -> commonCounter(rdf, hangLabels)).collect(toList());
        Integer maxScore = rdfs.stream().map(m -> (Integer) m.get(SCORE)).sorted(Comparator.reverseOrder()).findFirst().get();
        List<Map> maxScoreLabels = rdfs.stream().filter(m -> maxScore == m.get(SCORE)).collect(toList());
        return (List<String>) maxScoreLabels.get(0).get(RDF);
    }

    /**
     *
     * @param rdf 知识库中的三元组：实体-关系-实体
     * @param hangLabels 待处理的实体类型集合
     * @return
     */
    private Map commonCounter(List rdf, List<String> hangLabels) {
        Map<String,Object> scoredLabel = new HashMap<>();
        int score = 0;
        for(String str : hangLabels){
            if(rdf.contains(str)){
                score++;
            }
        }
        scoredLabel.put(RDF, rdf);
        scoredLabel.put(SCORE, score);
        return scoredLabel;
    }

    /**
     * 对候选实体筛选
     * @param props 候选实体集合
     * @param customDict 含有“非”的词的临时映射表
     * @param args
     * @return
     */
    private List<Map<String, Object>> propertiesFilter(List<Map<String, Object>> props, Map<String, Object> customDict, Map<String, Object> args) {
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
        });

        Map<String, Object> params = matcher(args);
        List<Map<String, Object>> rcp = props.stream().map(m -> recombineProps(params, m)).collect(toList());

        //对自定义的属性条件做进一步处理
        return customPropsHandle(customDict, rcp);
    }

    /**
     * 将原实体集合中的实体替换为最佳匹配实体
     * @param params
     * @param m
     * @return
     */
    private Map<String, Object> recombineProps(Map<String, Object> params, Map<String, Object> m) {
        if(params ==null || params.isEmpty()) return m;
        String label = (String) m.get(LABEL);
        String field = (String) m.get(FIELD);
        params.entrySet().forEach(entry -> {
            String[] key = entry.getKey().split(".");
            if(label.equals(key[0]) && field.equals(key[1])){
                m.put(VALUE,entry.getValue());
            }
        });
        return m;
    }

    /**
     * 添加实体的属性的操作符
     * @param customDict
     * @param params
     * @return
     */
    private List<Map<String, Object>> customPropsHandle(Map<String, Object> customDict, List<Map<String, Object>> params) {
        List<Map<String, Object>> properties = new ArrayList<>();
        for(Map m : params){
            String value = (String) m.get(VALUE);
            String op = (String) m.get(OP);
            if(StringUtils.isNotEmpty(op)) continue;
            if(customDict.containsKey(value)){
                String v = ((String) customDict.get(value)).trim();
                properties.add(new HashMap(){{put(VALUE,v);put(OP,"<>");}});
            }else {
                properties.add(new HashMap(){{put(VALUE,value);put(OP,"=");}});
            }
        }
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
        map.values().stream().sorted(Comparator.reverseOrder()).forEach(n ->
                map.keySet().stream().filter(key -> map.get(key)==n).forEach(k -> {if(!keys.contains(k)) keys.add(k);}));
        return keys.get(0);
    }

    /**
     * list 去重
     * @param list
     */
    public void removeDuplicate(List list){
        List listTemp = new ArrayList();
        for(int i=0;i<list.size();i++){
            if(!listTemp.contains(list.get(i))){
                listTemp.add(list.get(i));
            }
        }
        list.clear();
        list.addAll(listTemp);
    }

}
