package com.example.demo.nlp;



import org.springframework.stereotype.Component;
import java.util.HashMap;

@Component
public class AlgorithmLibrary {

    public static void main(String[] args) {
        long value = convert2Num("三千五百六十一");  // 3561
        long value1 = convert2Num("三万六千七百九十八亿三千两百七十五万八千六百九十九"); // 3679832758699
        long value2 = convert2Num("三千五");
        System.out.println(value);
        System.out.println(value1);
        System.out.println(value2);
    }

    /**
     * 汉字表示的数字转换成阿拉伯数字
     * 先把汉字数字模型作为二叉树(binary tree)的数据结构：每一个节点是一个汉字,每一层上的节点数都是最大节点数。
     * 第一步是把汉字里最大的数值作为父节点。每一个父节点的左节点是左边汉字的最大数值，右节点是右边汉字的最大数值。
     * 二叉树形成完了之后,算法很简单:每个父节点乘以它的左节点，加上它的右节点。最后可以用递归函数算出来。
     */
    public static long convert2Num(String s) {
        int sLen = s.length();
        if (sLen == 0)
            return 0;
        if (sLen > 1) {
            int pivot = 0; // index of the highest singular character value in the string
            for (int i = 0; i < sLen; i++)   // loop through the characters in the string to get the character with the highest value. That is your pivot
                if (convert2Num(String.valueOf(s.charAt(i))) > convert2Num(String.valueOf(s.charAt(pivot))))
                    pivot = i;
            long value = convert2Num(String.valueOf(s.charAt(pivot)));
            long LHS, RHS;
            LHS = convert2Num(s.substring(0, pivot));  // multiply value with LHS
            RHS = convert2Num(s.substring(pivot + 1));  // add value with RHS
            if (LHS > 0)
                value *= LHS;
            value += RHS;
            return value;
        } else {
            return chineseNumbers.get(s).longValue();
        }
    }

    public static String connect2Num(String s){
        String numStr = "";
        for (char c : s.toCharArray()){
            String ss = String.valueOf(c);
            String num = chineseNumbers.get(ss)+"";
            numStr += num;
        }
        return numStr;
    }

    public static HashMap<String, Long> chineseNumbers = new HashMap<String, Long>() {

        {
            put("零", 0L);
            put("一", 1L);
            put("壹", 1L);
            put("二", 2L);
            put("两", 2L);
            put("貳", 2L);
            put("贰", 2L);
            put("叁", 3L);
            put("參", 3L);
            put("三", 3L);
            put("肆", 4L);
            put("四", 4L);
            put("五", 5L);
            put("伍", 5L);
            put("陸", 6L);
            put("陆", 6L);
            put("六", 6L);
            put("柒", 7L);
            put("七", 7L);
            put("捌", 8L);
            put("八", 8L);
            put("九", 9L);
            put("玖", 9L);
            put("十", 10L);
            put("拾", 10L);
            put("佰", 100L);
            put("百", 100L);
            put("仟", 1000L);
            put("千", 1000L);
            put("万", 10000L);
            put("萬", 10000L);
            put("億", 100000000L);
            put("亿", 100000000L);
        }
    };
}