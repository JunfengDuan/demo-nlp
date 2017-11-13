package com.example.demo.service.elasticsearch;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Created by jfd on 11/9/17.
 */
@Component
public class ElasticsearchFullSearch {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchFullSearch.class);
    private SearchResponse response;
    @Autowired
    private RequestQuery requestQuery;
    @Autowired
    private ESRest esRest;
    /**
     * 使用ES中的词典,对搜索内容作字符串匹配
     */
    public List StringMatch(String sQuery, Map<String, Object> map){
        if(requestQuery == null) requestQuery = new RequestQuery();
        if(esRest == null)  esRest = new ESRest();

        List<Map<String, Object>> sourceList = new ArrayList<>();

        try {

            String _type = map.get("type").toString();
            if(StringUtils.isBlank(_type))
                throw new Exception("Please set ES type value");
            requestQuery.setType(_type);

            int _size = map.get("size") == null ? 20 : (int) map.get("size");
            requestQuery.size = _size;

            QueryBuilder qb = queryStringQuery(sQuery);

            if (qb != null){
                requestQuery.setQueryBuilder(qb);
            }

            // 请求ES
            logger.debug("QueryAll开始es请求");
            if (requestQuery != null) {
                response = esRest.FullText(this.requestQuery);

                logger.debug("QueryAll完成es请求");
                SearchHit[] hits = response.getHits().getHits();

                IntStream.range(0,hits.length).forEach(i ->{
                    sourceList.add(hits[i].getSource());
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceList;
    }
}
