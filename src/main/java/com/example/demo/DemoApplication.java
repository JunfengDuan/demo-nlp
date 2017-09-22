package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);

        String s = "aaaa(sdf证据）几倍322&&是地方";
        String query8 = "小于30岁的处级干部";
        String query9 = "30-35岁的正处级干部";
        String query10 = "30到35岁之间的正处级干部";
        String query12 = "1990年出生的干部";
        System.out.print(isNumeric(query12) ? 1:0);

        System.out.print(findCommonString("局级单位","北京市教育矫治局（副局级"));

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
