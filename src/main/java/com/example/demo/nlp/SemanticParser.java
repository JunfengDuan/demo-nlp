package com.example.demo.nlp;

import com.example.demo.service.elasticsearch.ElasticsearchFullSearch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.demo.nlp.AlgorithmLibrary.chineseNumbers;
import static com.example.demo.nlp.AlgorithmLibrary.connect2Num;
import static com.example.demo.nlp.AlgorithmLibrary.convert2Num;

/**
 * Created by jfd on 11/11/17.
 */
@Component
public class SemanticParser {

    @Autowired
    private SegmentJob segmentJob;
    @Autowired
    private Api api;
    @Autowired
    private QueryGraph queryGraph;
    @Autowired
    private ElasticsearchFullSearch fullSearch;

    public static final String TODO = "todo";
    public static final String DONE = "done";

    /**
     * 对查询词典的返回结果进行解析
     *
     * @param customDict
     * @param labels label的集合
     * @param props 属性的集合
     *@param queryStr 搜索的自然语言  @return
     */
    public void parse(Map<String, Object> customDict, List labels,  Map<String, Object> props, String queryStr){

        String query = specialStringHandle(queryStr);

        //实体对齐
        String kbQuery = entityAlignment(query);

        //自然语言 分词、NER、POS
        Map<String, String> nlp = segmentJob.doNlp(kbQuery);

        //年龄处理
        List<String> segments = new ArrayList<>(Collections.unmodifiableCollection(nlp.keySet()));
        Map<String, Object> range = ageRangeParse(segments);

        segments = (List<String>) range.get("removeAgeWords");
        List<String> age = (List<String>) range.get("age");

        if(age != null && !age.isEmpty()){
            Map<String,String> birthMap = ageToBirth(age);
            props.put(DONE, birthMap);
        }

        //去非后的分词
        List<String> words = addToCustomDict(customDict, segments);

        //实体链接
        List<String> entityWords = extractEntityWords(nlp, words);
        String topicWordsString = StringUtils.join(entityWords, " ");

        List<String> topicWords = new ArrayList<>();
        Map<String,String> entityNames = new HashMap<>();
        entityMatch(topicWordsString, topicWords, entityNames);

        //属性链接
        List<String> entityRemovedWords = words.stream().filter(w -> !topicWords.contains(w)).collect(Collectors.toList());
        List<Map<String, Object>> propsList = propMatch(entityRemovedWords);


        String remain = StringUtils.join(words, "").trim();

        List<Map<String, Object>> containedProps = propsList.stream().filter(fields -> {
            String value = fields.get("value") == null ? "" : (String) fields.get("value");
            value = stringFilter(value);
            return remain.contains(value.trim());
        }).collect(Collectors.toList());


        labels.add(entityNames);
        props.put(TODO, containedProps);
    }

    private List<Map<String, Object>> propMatch(List<String> entityRemovedWords) {
        Map<String, Object> map  = new HashMap<>();
        map.put("type", "propDict");
        map.put("size", 20);

        List<String> conjunctions = new ArrayList();
        conjunctions.add("AS");
        conjunctions.add("DEC");
        conjunctions.add("DEG");
        conjunctions.add("DER");
        conjunctions.add("DEV");
        conjunctions.add("CC");
        conjunctions.add("P");
        conjunctions.add("SP");
        conjunctions.add("VC");
        conjunctions.add("PN");
        conjunctions.add("PU");

        List<Map<String, Object>> propsList = new ArrayList<>();
        entityRemovedWords.stream().filter(e -> !conjunctions.contains(e))
                .map(word -> fullSearch.StringMatch(word, map)).forEach(propsList :: addAll);
        return propsList;
    }

    private String entityAlignment(String query) {
        Map<String, Object> map  = new HashMap<>();
        map.put("type", "customDict");
        map.put("size", 20);
        List<Map<String, Object>> alignmentList = fullSearch.StringMatch(query, map);
        if(!alignmentList.isEmpty()){
            for(Map e: alignmentList){
                String cx_value = (String) e.get("cx_value");
                String kb_value = (String) e.get("kb_value");
                query = query.replace(cx_value, kb_value);
            }
        }
        return query;
    }

    private void entityMatch(String topicWordsString, List<String> topicWords, Map<String, String> entityNames) {
        Map<String, Object> map  = new HashMap<>();
        map.put("type", "entityDict");
        map.put("size", 10);
        List<Map<String, Object>> entityList = fullSearch.StringMatch(topicWordsString, map);

        if(!entityList.isEmpty()){
            entityList.stream().filter(e -> topicWordsString.contains((String) e.get("cn_name"))).forEach(e ->{
                String cn_name = (String) e.get("cn_name");
                topicWords.add(cn_name);
                String label = (String) e.get("en_name");
                String type = (String) e.get("type");
                entityNames.put("label", label);
                entityNames.put("type", type);
            } );
        }
    }

    /**
     * 根据词性提取名词、动词
     * @param nlp
     * @param words
     * @return
     */
    private List<String> extractEntityWords(Map<String, String> nlp, List<String> words) {
        List<String> nounsAndVerbs = new ArrayList();
        nounsAndVerbs.add("NN");
        nounsAndVerbs.add("NR");
        nounsAndVerbs.add("VV");
        return nlp.entrySet().stream().filter(entry -> words.contains(entry.getKey())
                && nounsAndVerbs.contains(entry.getValue())).map(e -> e.getKey()).collect(Collectors.toList());
    }

    /**
     * 添加反义词到自定义词典
     * @param customDict
     * @param segments
     * @return
     */
    private List<String> addToCustomDict(Map<String, Object> customDict, List<String> segments) {
        if(!segments.contains("非")) return segments;
        segments.forEach(s -> {
            if(s.equalsIgnoreCase("非")){
                int pos = segments.indexOf(s);
                String w = segments.get(pos+1);
                customDict.put(w, w+" ");
            }

        });
        return segments.stream().filter(s -> !"非".equals(s)).collect(Collectors.toList());
    }

    private String specialStringHandle(String search) {
        search = search.contains("-") ? search.replace("-", " - ") : search;
        search = search.contains("——") ? search.replace("——", " —— ") : search;
        return search;
    }


    // 过滤特殊字符
    private String stringFilter(String str){
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    // 年龄范围解析
    private Map<String, Object> ageRangeParse(List<String> wordsList){
        List<String> ageList = new ArrayList<>();
        List<String> words = new ArrayList<>();

        for(String w: wordsList){
            List<String> collect = chineseNumbers.keySet().stream().filter(c -> w.contains(c)).collect(Collectors.toList());
            String str = w;
            if(!collect.isEmpty()){
                str = String.valueOf(convert2Num(str));
                if(str.length()<=w.length()/2)
                    str = connect2Num(w);
            }
            words.add(str);
        }
        String query = StringUtils.join(words.iterator(),"");
        List<String> age = words.stream().filter(w -> api.ageList.contains(w)).collect(Collectors.toList());
        if(age.isEmpty()){
            boolean isAge = false;
            if(query.contains("后")){
                int n = query.indexOf("后")-1;
                if(n>=0){
                    char c = query.charAt(n);
                    isAge = isNumeric(String.valueOf(c));
                }
            }
            if(!isAge)  return new HashMap(){{put("removeAgeWords",words);}};
        }

        List<String> compare = words.stream().filter(w -> api.compareMap.keySet().contains(w) || containNumeric(w)).collect(Collectors.toList());

        compare.forEach(w -> {

            if(isNumeric(w)){
                int i = compare.indexOf(w);
                if(i-1>=0){
                    String op = compare.get(i-1);
                    if( api.compareMap.keySet().contains(op)){
                        switch (op){
                            case "-" :
                            case "——" :
                            case "到" :
                            case "和" : {
                                ageList.add(">= "+ w);
                                if(i-2>=0)
                                    ageList.add("<= "+compare.get(i-2));break;}
                            default: {
                                String op1 = api.compareMap.get(op);
                                ageList.add(op1+" "+w);
                            }
                        }
                    }else if(i+1<compare.size()){
                        if(age.contains("出生")){
                            ageList.add("contains "+ w);
                        }else if(compare.get(i+1).contains("后") ){
                            ageList.add(">= "+ "19"+w);
                        }
                    }

                }else if(i+1<compare.size()){
                    if(age.contains("出生")){
                        ageList.add("contains "+ w);
                    }else if(compare.get(i+1).contains("后") ){
                        ageList.add(">= "+ "19"+w);
                    }
                }else{
                    if(age.contains("岁")){
                        ageList.add("contains "+w);
                    }
                }
            }else if(containNumeric(w)){
                ageList.add("contains "+w);
            }
        });

        List<String> removeAgeWords = words.stream().filter(w -> !api.ageList.contains(w) && !compare.contains(w)).collect(Collectors.toList());
        return new HashMap(){{put("removeAgeWords",removeAgeWords);put("age",ageList);}};
    }


    private boolean containNumeric(String str){
        return str.matches(".*\\d+.*");
    }

    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    private String findNumeric(String str){
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher isNum = pattern.matcher(str);
        if( isNum.find() ){
            sb.append(isNum.group());
        }
        return sb.toString();
    }

    private Map<String,String> ageToBirth(List<String> ages) {
        Map<String,String> birthdayMap = new HashMap<>();
        List<String> collect = new ArrayList<>();
        ages.forEach(age -> {
            String[] operatorAndAge = StringUtils.split(age, " ");
            String birthday = getBirthday(operatorAndAge[1]);
            collect.add(operatorAndAge[0]+"'"+birthday+"'");

        });
        birthdayMap.put("cadre.birthday", StringUtils.join(collect, " and cadre.birthday "));
        return birthdayMap;
    }

    private String getBirthday(String s) {
        int ageYear = 0;
        int ageMonth = 0;
        int ageDay = 0;
        if(!isNumeric(s)){
            if(s.contains("年")){
                s = s.replace("年","");
                ageYear = Integer.valueOf(s);
            }else if (s.contains("月")){
                s = s.replace("月","");
                ageMonth = Integer.valueOf(s);
            }else {
                s = s.replace("日","");
                ageDay = Integer.valueOf(s);
            }
        }else {
            ageYear = Integer.valueOf(s);
        }

        if(ageYear > 200) return s;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -ageYear);
        c.add(Calendar.MONTH, -ageMonth);
        c.add(Calendar.DAY_OF_MONTH, -ageDay);
        return String.format("%tF", c.getTime());
    }



}
