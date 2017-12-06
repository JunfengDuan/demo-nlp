package com.example.demo.nlp;

import com.example.demo.service.elasticsearch.ElasticsearchFullSearch;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.demo.nlp.AlgorithmLibrary.chineseNumbers;
import static com.example.demo.nlp.AlgorithmLibrary.connect2Num;
import static com.example.demo.nlp.AlgorithmLibrary.convert2Num;
import static com.example.demo.nlp.StringConst.*;
import static java.util.stream.Collectors.toList;

/**
 * Created by jfd on 11/11/17.
 */
@Component
public class SemanticParser {

    private static final Logger logger = LoggerFactory.getLogger(SemanticParser.class);

    @Autowired
    private SegmentJob segmentJob;
    @Autowired
    private Api api;
    @Autowired
    private ElasticsearchFullSearch fullSearch;

    /**
     * 对查询词典的返回结果进行解析
     *
     * @param customDict
     * @param labels label的集合
     * @param props 属性的集合
     *@param queryStr 搜索的自然语言  @return
     */
    public void parse(Map<String, Object> customDict, List<Map<String, Object>> labels,  Map<String, Object> props, String queryStr){

        String query = specialStringHandle(queryStr);

        //实体对齐
        String kbQuery = entityAlignment(query);
        logger.debug("Text:{}, alignmentText:{}", query, kbQuery);

        //自然语言 分词、NER、POS
        Map<String, String> nlp = segmentJob.doNlp(kbQuery);
        logger.debug("NLP:{}",nlp);

        //年龄处理
        List<String> segments = new ArrayList<>(Collections.unmodifiableCollection(nlp.keySet()));

        Map<String, Object> range = ageRangeParse(segments);

        segments = (List<String>) range.get("removeAgeWords");
        String ageWord = (String) range.get("ageWord");
        List<String> age = (List<String>) range.get(ageWord);

        if(age != null && !age.isEmpty()){
            List<Map<String,Object>> birthMap = ageToBirth(age);
            props.put(ageWord, birthMap);
            labels.add(new HashMap(){{put("label","Cadre");put("type", "e");}});
        }

        //去非后的分词
        List<String> words = addToCustomDict(customDict, segments);

        //实体链接
        List<String> entityWords = extractEntityWords(nlp, words);
        String topicWordsString = StringUtils.join(entityWords, " ");

        List<String> topicWords = new ArrayList<>();
        List<Map<String,Object>> entityNames = new ArrayList<>();
        entityMatch(topicWordsString, topicWords, entityNames);
        logger.debug("Dict entity link:{}",entityNames);

        //属性链接
        List<String> entityRemovedWords = words.stream().filter(w -> !topicWords.contains(w)).collect(toList());
        Map<String, Object> propsList = propMatch(entityRemovedWords);

        //候选属性排序
        Map<String, Object> rankedProps = candidatePropsRank(words, propsList);
        logger.debug("Candidate properties rank:{}", rankedProps);

        //选择分值最高的属性集合
        Map<String, Object> topProps = selectTopProps(rankedProps);
        logger.debug("Select top n properties:{}", topProps);

        labels.addAll(entityNames);
        props.putAll(topProps);
    }

    private Map selectTopProps(Map<String, Object> containedProps) {
        Map<String, Object> topWordProps = new HashMap<>();
        for(Map.Entry entry : containedProps.entrySet()){
            float maxScore = 0;
            String word = (String) entry.getKey();
            List<Map<String, Object>> props = (List<Map<String, Object>>) entry.getValue();
            for(Map map : props){
                float score = (float) map.get(SCORE);
                if(score > maxScore){
                    maxScore = score;
                }
            }
            float top = maxScore;
            List<Map<String, Object>> topProps = props.stream().distinct().filter(p -> top == (float) p.get(SCORE)).collect(toList());
            topWordProps.put(word, topProps);
        }
        return topWordProps;
    }

    private Map<String, Object> candidatePropsRank(List<String> words, Map<String, Object> propsList) {
        String remain = StringUtils.join(words, "").trim();
        for(Map.Entry entry : propsList.entrySet()){
            String word = (String) entry.getKey();
            List<Map<String, Object>> linkedProps = (List<Map<String, Object>>) entry.getValue();
            linkedProps.forEach(map -> {
                float score = computeScore(word, map);
                map.put("score", score);
            });
        }

        return propsList;
    }

    /**
     * 计算两个字符串的相似性
     * score=(源字符串和目标字符串相同字符个数)/目标字符串个数
     * @param word
     * @param map
     * @return
     */
    private float computeScore(String word, Map<String, Object> map) {
        float score = 0;
        String propValue = (String) map.get(VALUE);
        for(char c: word.toCharArray()){
            if(propValue.contains(String.valueOf(c)))
                score++;
        }
        return score/propValue.length();
    }

    private Map<String, Object> propMatch(List<String> entityRemovedWords) {
        Map<String, Object> rule2  = new HashMap<>();
        rule2.put("type", "propDict");
        rule2.put("size", 100);

        Map<String, Object> rule3  = new HashMap<>();
        rule3.put("type", "customDict");
        rule3.put("size", 30);

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

        Map<String,Object> entityLinkedPair  = new HashMap<>();
        entityRemovedWords.stream().filter(e -> !conjunctions.contains(e))
                .forEach(word -> {
//                    String completedWord = entityAlignment(word);
                    List list = fullSearch.StringMatch(word, rule2);
                    entityLinkedPair.put(word, list);
                });
        entityRemovedWords.stream().filter(e -> !conjunctions.contains(e))
                .forEach(word -> {
                    List<Map<String, Object>> list = fullSearch.StringMatch(word, rule3);

                    if(!list.isEmpty()){
                        list.forEach(e -> {
                            String kb_value = (String) e.get("kb_value");
                            e.remove("cx_value");
                            e.remove("kb_value");
                            e.put(VALUE, kb_value);
                        });
                        entityLinkedPair.put(word, list);
                    }

                });
        return entityLinkedPair;
    }

    private String entityAlignment(String query) {
        Map<String, Object> rule3  = new HashMap<>();
        rule3.put("type", "customDict");
        rule3.put("size", 30);
        List<Map<String, Object>> alignmentList = fullSearch.StringMatch(query, rule3);
        if(!alignmentList.isEmpty()){
            for(Map e: alignmentList){
                String cx_value = (String) e.get("cx_value");
                String kb_value = (String) e.get("kb_value");
                query = query.replace(cx_value, kb_value);
            }
        }
        return query;
    }

    private void entityMatch(String topicWordsString, List<String> topicWords, List<Map<String,Object>> entityNames) {
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
                entityNames.add(new HashMap(){{put("label", label);put("type", type);}});
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
                && nounsAndVerbs.contains(entry.getValue())).map(e -> e.getKey()).collect(toList());
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
        return segments.stream().filter(s -> !"非".equals(s)).collect(toList());
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
            List<String> collect = chineseNumbers.keySet().stream().filter(c -> w.contains(c)).collect(toList());
            String str = w;
            if(!collect.isEmpty()){
                str = String.valueOf(convert2Num(str));
                if(str.length()<=w.length()/2)
                    str = connect2Num(w);
            }
            words.add(str);
        }
        String query = StringUtils.join(words.iterator(),"");
        List<String> age = words.stream().filter(w -> api.ageList.contains(w)).collect(toList());
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

        List<String> compare = words.stream().filter(w -> api.compareMap.keySet().contains(w) || containNumeric(w)).collect(toList());

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
        String ageWord = age.get(0) == null ? "noneAge" : age.get(0);
        List<String> removeAgeWords = words.stream().filter(w -> !api.ageList.contains(w) && !compare.contains(w)).collect(toList());
        return new HashMap(){{put("removeAgeWords",removeAgeWords);put(ageWord,ageList);put("ageWord",ageWord);}};
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

    private List<Map<String, Object>> ageToBirth(List<String> ages) {
        List<Map<String,Object>> birthdayMap = new ArrayList<>();
        ages.forEach(age -> {
            String[] operatorAndAge = StringUtils.split(age, " ");
            String birthday = getBirthday(operatorAndAge[1]);
            birthdayMap.add(new HashMap(){{put(LABEL,"Cadre");put(FIELD,"birthday");put(OP,operatorAndAge[0]);put(VALUE,birthday);}});

        });
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
