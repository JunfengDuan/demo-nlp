package com.example.demo.nlp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toMap;


/**
 * Created by jfd on 5/3/17.
 */
@Component
public class SegmentJob{

    private static final Logger logger = LoggerFactory.getLogger(SegmentJob.class);
    private final CRFClassifier<CoreLabel> segment = CoreNLPUtil.getSegment();
    private final MaxentTagger tagger = CoreNLPUtil.getTagger();
    private final AbstractSequenceClassifier<CoreLabel> ner = CoreNLPUtil.getNer();

    private static final String REGEX = "<.*?>([^a-zA-Z]+)</.*?>";

    public Map<String, String> doNlp(String text) {

        String segString = doSegment(text);

        String nerString = doNer(segString);

        String tagText = tagText(nerString);

        Map<String, String> tags = parseTag(tagText);

        return tags;

    }

    public String doSegment(String sent) {

        List<String> strings = segment.segmentString(sent);
        String segmentString = StringUtils.join(strings, " ");

        return segmentString;
    }

    public String tagText(String toTag){
        String tagString = tagger.tagString(toTag);
        return tagString;
    }

    private String doNer(String tokenString){

        Map<String,Object> tags = new HashMap<>();
        String xml = ner.classifyWithInlineXML(tokenString);

        logger.debug("\nxml:{}", xml);

        List<String> regex = regex(xml);
        regex.forEach(r -> parseXml(tags, r));

        tags.entrySet().forEach(e -> logger.debug("text:{}; ner:{} tag:{} ",
                tokenString.replaceAll(" ",""), e.getKey().replace(" ",""), e.getValue()));

        String msg = tokenString;

        for(String key: tags.keySet()){
            msg = msg.replace(key, key.replace(" ",""));
        }
        return msg;
    }

    private List<String> regex(String xml){
        List<String> list = new ArrayList<>();
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(xml);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }

    private void parseXml(Map<String,Object> tags, String xml){
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();

            tags.put(root.getText(), root.getName());

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> parseTag(String tag){
        String[] split = tag.split(" ");
        Map<String, String> pos = Arrays.stream(split).map(t -> t.split("#")).collect(toMap(e -> e[0], e -> e[1]));
        return pos;
    }


}
