package com.example.demo.nlp;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Created by jfd on 5/3/17.
 */
@Component
public class SegmentJob{

    private static final Logger logger = LoggerFactory.getLogger(SegmentJob.class);
    private final CRFClassifier<CoreLabel> segment = CoreNLPUtil.getSegmentProps();

    public List<String> doSegment(String sent) {

        List<String> strings = segment.segmentString(sent);
//        String segmentString = StringUtils.join(strings.iterator(), " ");

        return strings;
    }


}
