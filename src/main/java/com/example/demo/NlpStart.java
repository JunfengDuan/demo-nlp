package com.example.demo;

import com.example.demo.nlp.Api;
import com.example.demo.nlp.SegmentJob;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by jfd on 8/26/17.
 */
@Component
public class NlpStart implements CommandLineRunner{

    private static final Logger logger = LoggerFactory.getLogger(NlpStart.class);
    @Autowired
    private SegmentJob segmentJob;

    @Autowired
    private Api api;

    @Override
    public void run(String... strings) throws Exception {
        logger.info("Start segment");
        String query = "少数民族女市管干部";
        String query1 = "干部";
        String query2 = "少数民族女";
        String query3 = "市管干部";
        String query4 = "非中共党员干部";
        String query5 = "籍贯是河北的干部";

        api.constructDict();

        Map<String, Object> result = search(query);
        logger.info("\nAfter segment :{}",result);

     /* List<String> list = new ArrayList(){{add("aaa");add("bbbb");add("cc");add("dd");add("r");}};
        String s = compare1(list);
        logger.info("s-----> :"+s);*/

    }



    /**
     * 自然语言搜索
     * @param query
     * @return
     */
    public Map<String, Object> search(String query){
        List<String> labels = new ArrayList<>();
        Map<String, Object> args = new HashMap<>();

        List<Map<String, Object>> queryResults = parse(labels, query);

        Map<String,Object> customDict = new HashMap<>();
        queryResults.stream().filter(m -> m.containsKey("antonym") && m.get("antonym")!= null).forEach(map ->{
            customDict.put((String) map.get("value"), map.get("antonym"));});

        if(queryResults.isEmpty()){
            if(labels.isEmpty())
                return new HashMap<>();
        }

        queryResults.forEach(e -> {
            String key = e.get("label").toString().toLowerCase() +"."+ e.get("field");
            String value = (String) e.get("value");

            if(args.containsKey(key)){
                List<String> values = (List<String>) args.get(key);
                values.add(value);
            }else {
                args.put(key, new ArrayList<String>(){{add(value);}});
            }

            if(!labels.contains(e.get("label")))
                labels.add((String) e.get("label"));
        });

        Map<String, Object> params = matcher(args);

        //对自定义的属性条件做进一步处理
        Map<String, Object> properties = customPropsHandle(customDict, params);

        return new HashMap(){{put("labels",labels);put("properties",properties);}};
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
     * 对查询词典的返回结果进行解析
     * @param labels label的集合
     * @param query 搜索的自然语言
     * @return
     */
    private List parse(List<String> labels, String query){

        List<String> words = new ArrayList<>(segmentJob.doSegment(query));
        String segmentString = StringUtils.join(words, " ");

        List<String> entityNames = new ArrayList<>();

        List<Map<String, Object>> entityList = api.StringMatch(segmentString);
        if(!entityList.isEmpty()){
            entityList.forEach(e ->{
                String name = (String) e.get("name");
                if(words.contains(name)) {
                    labels.add((String) e.get("label"));
                    entityNames.add(name);
                }
            } );
        }

        words.removeAll(entityNames);

        List<Map<String, Object>> fieldsList = new ArrayList<>();
        words.stream().map(word -> api.StringMatch(word)).forEach(fieldsList :: addAll);

        String remain = StringUtils.join(words, "").trim();

        List<Map<String, Object>> collect = fieldsList.stream().filter(fields -> {
            String value = fields.get("value") == null ? "" : (String) fields.get("value");
            return remain.equalsIgnoreCase(value.trim());
        }).collect(Collectors.toList());

        if (collect.size()>0){
            return collect;
        }

        List<Map<String, Object>> containedFields = fieldsList.stream().filter(fields -> {
            String value = fields.get("value") == null ? "" : (String) fields.get("value");
            return query.contains(value.trim());
        }).collect(Collectors.toList());

        Set<Object> fields = new HashSet<>();
        containedFields.stream().map(map -> map.get("field")).forEach(fields :: add);
        boolean fullMatch = (words.size() <= fields.size());

        //如果用户输入的查询不完整,就要作反向匹配。比如 query="北京市局级单位"， fieldsList=[value=(正)局级,value=(副局)级]
        if(!fullMatch){
            words.stream().map(w -> filter(fieldsList, w)).forEach(containedFields :: addAll);
        }

        return containedFields;
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
            map.keySet().stream().filter(key -> map.get(key)==n).forEach(k -> {if(!keys.contains(k)) keys.add(k);});
        });
        return keys.get(0);
    }
    /**
     * 当输入的查询语句不包含完整属性的时候，就用词典中的属性去匹配查询
     * @param fieldsList
     * @param s
     * @return
     */
    private List<Map<String,Object>> filter(List<Map<String, Object>> fieldsList, String s){
        List<Map<String, Object>> list = fieldsList.stream().filter(fields -> {
            String value = fields.get("value") == null ? "" : (String) fields.get("value");
            value = stringFilter(value);
            return value.contains(s.trim());
        }).collect(Collectors.toList());

        return list;
    }

    // 过滤特殊字符
    private static String stringFilter(String str){
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }

}
