package com.example.demo;

import com.example.demo.nlp.AlgorithmLibrary;
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
import static com.example.demo.nlp.AlgorithmLibrary.*;

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

        Map<String, Object> result = search(query9);
        logger.info("\nAfter segment :{}",result);

     /* List<String> list = new ArrayList(){{add("aaa");add("bbbb");add("cc");add("dd");add("r");}};
        String s = compare1(list);
        logger.info("s-----> :"+s);*/

//        MATCH (n:Cadre) where n.birthday <'1958-04-05' RETURN n LIMIT 25

    }



    /**
     * 自然语言搜索
     * @param query
     * @return
     */
    public Map<String, Object> search(String query){
        List<String> labels = new ArrayList<>();
        Map<String, Object> args = new HashMap<>();
        Map<String, Object> ages = new HashMap<>();
        Map<String,Object> customDict = new HashMap<>();

        List<Map<String, Object>> queryResults = parse(customDict, labels, ages, query);

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
        properties.putAll(ages);

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
     *
     * @param customDict
     * @param labels label的集合
     * @param ages
     *@param search 搜索的自然语言  @return
     */
    private List parse(Map<String, Object> customDict, List<String> labels, Map<String, Object> ages, String search){

        String query = specialStringHandle(search);

        List<String> segments = new ArrayList<>(segmentJob.doSegment(query));

        Map<String, Object> range = rangeParse(segments);

        segments = (List<String>) range.get("removeAgeWords");
        List<String> age = (List<String>) range.get("age");

        if(age != null && !age.isEmpty()){
            Map<String,String> birthMap = ageToBirth(age);
            ages.putAll(birthMap);
        }

        List<String> words0 = addToCustomDict(customDict, segments);

        List<String> words = cutSingleWord(words0);

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
        search = search.contains("非") ? search.replace("非", " 非 ") : search;
        search = search.contains("-") ? search.replace("-", " - ") : search;
        search = search.contains("——") ? search.replace("——", " —— ") : search;
        return search;
    }

    /**
     * 排除查询语句中的介词、单个无意义词
     * @param words
     * @return
     */
    private List<String> cutSingleWord(List<String> words) {
        String[] s = {"是","的","了","在","于"};
        List<String> unavailableWords = Arrays.asList(s);
        List<String> strings = words.stream().filter(word -> !unavailableWords.contains(word)).collect(Collectors.toList());
        return strings;
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
    private String stringFilter(String str){
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    // 年龄范围解析
    private Map<String, Object> rangeParse(List<String> wordsList){
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
