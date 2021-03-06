package com.example.demo.nlp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jfd on 8/26/17.
 */
@Component
public class Api {

    public List<Dict> dictList;
    public List<String> ageList;
    public List<String> compareList;
    public Map<String,String> compareMap;

    Api(){constructCustomDict();}

    public List StringMatch(String query){


        if(StringUtils.isBlank(query)){
            return new ArrayList();
        }else{
            query = query.trim();
        }

        List<Map> list = new ArrayList<>();

        for(Dict dict : dictList){
            String name = dict.name;
            if(StringUtils.isNotBlank(name) && query.contains(name)){
                list.add(new HashMap(){{ put("label",dict.label);put("name",dict.name);}});
            }

            String value = dict.value;
            if(StringUtils.isBlank(value)) continue;
            if(query.contains(value) || value.contains(query) ){
                list.add(new HashMap(){{ put("label",dict.label); put("field",dict.field);
                put("value",dict.value);put("antonym",dict.antonym);put("name",dict.name);}});
            }
        }

        return list;

    }

   public void constructDict(){
       Dict dict1 = new Dict("Cadre","nation","民族");
       Dict dict2 = new Dict("Cadre","nation","汉族");
       Dict dict3 = new Dict("Cadre","nation","少数民族","汉族");
       Dict dict4 = new Dict("Cadre","gender","男");
       Dict dict5 = new Dict("Cadre","gender","女");
       Dict dict6 = new Dict("Cadre","level","市管");
       Dict dict7 = new Dict("Organization","name","局级");
       Dict dict8 = new Dict("Cadre","干部");
       Dict dict9 = new Dict("Organization","单位");
       Dict dict10 = new Dict("Cadre","political_status;","中共党员");
       Dict dict11 = new Dict("Organization","level","领导职务");
       Dict dict12 = new Dict("Organization","name","正处级");

       dictList = new ArrayList<>();
       dictList.add(dict1);
       dictList.add(dict2);
       dictList.add(dict3);
       dictList.add(dict4);
       dictList.add(dict5);
       dictList.add(dict6);
       dictList.add(dict7);
       dictList.add(dict8);
       dictList.add(dict9);
       dictList.add(dict10);
       dictList.add(dict11);
       dictList.add(dict12);
   }


    public static class Dict{
        private String label;
        private String field;
        private String value;
        private String name;
        private String antonym;

        Dict(){}

        Dict(String label,String field,String value){
           this.label = label;
           this.field = field;
           this.value = value;
        }

        Dict(String label,String field,String value,String antonym){
            this.label = label;
            this.field = field;
            this.value = value;
            this.antonym = antonym;
        }
        
        Dict(String label,String name){
            this.label = label;
            this.name = name;
        }
    }

    public void constructCustomDict(){
        ageList = new ArrayList<>();
        ageList.add("岁");
        ageList.add("年龄");
        ageList.add("出生");

        compareList = new ArrayList<>();
        compareList.add("大于");
        compareList.add("大于等于");
        compareList.add("小于");
        compareList.add("小于等于");
        compareList.add("等于");
        compareList.add("不等于");
        compareList.add("-");
        compareList.add("——");
        compareList.add("到");

        compareMap = new HashMap<>();
        compareMap.put("大于","<");
        compareMap.put("大于等于","<=");
        compareMap.put("小于",">");
        compareMap.put("小于等于",">=");
        compareMap.put("等于","=");
        compareMap.put("不等于","<>");
        compareMap.put("-","-");
        compareMap.put("——","-");
        compareMap.put("到","-");
        compareMap.put("和","-");
        compareMap.put("后",">=");
    }
}
