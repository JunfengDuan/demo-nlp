package com.example.demo.service.elasticsearch;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class ESRest {
	private static Logger logger = LoggerFactory.getLogger(ESRest.class);
	@Value("{esServerHost:localhost}")
	private String esServerHost;
    @Value("{esServerPort:9300}")
	private int esServerPort;
    @Value("{esClusterName:escluster}")
    private String esClusterName;
    @Value("{esIndexName:esindex}")
	private String[] esIndexName;

    private InetAddress _ESServerIp;
    private TransportClient _Client;
    private SearchResponse _Response;

	/**
	 * 创建ES客户端接口对象
	 */
	@PostConstruct
	private void OpenClient() {
		if (_Client == null) {
		    esClusterName = "beidasoft-es";
		    esServerHost = "192.168.1.151";
		    esServerPort = 9300;
		    esIndexName = new String[]{"lexicon"};
			try {
				_ESServerIp = InetAddress.getByName(esServerHost);
				Settings esSettings = Settings.builder().put("cluster.name", esClusterName) // 设置ES实例的名称
						.put("client.transport.sniff", true) // 自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
						.build();

				_Client = new PreBuiltTransportClient(esSettings)
						.addTransportAddress(new InetSocketTransportAddress(_ESServerIp, esServerPort));

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	/**
	 * 关闭ES客户端接口对象
	 */
	@PreDestroy
	private void CloseClient() {
		try {
			if (_Client != null) {
				_Client.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * ES客户端对索引全文检索查询接口
	 * @param request
	 * @return
	 */
	public SearchResponse FullText(RequestQuery request){
		if (request == null) {
			return null;
		}
		_Response = null;

		try {
			if (_Client == null) {
				OpenClient();
			}
			//获取Type
			String _type = request.getType();		
			
			QueryBuilder qb = request.getQueryBuilder();

			@SuppressWarnings("deprecation")
            SearchRequestBuilder srb = _Client.prepareSearch(esIndexName)
					.setSearchType(SearchType.DEFAULT)
					.setSize(request.getSize());
			// 检索条件不为空
			if (qb != null) {
				srb.setQuery(qb);
			}
			// 设置检索type
			if (_type != null) {
				srb.setTypes(_type);
			}
			
			// 起始记录数
			if (request.getFrom() > 0) {
				srb.setFrom(request.getFrom());
			}

			/*------------为检索添加高亮标记   Start---------------*/
			HighlightBuilder highLightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
			highLightBuilder.preTags("<span style=\"color:red\">");
			highLightBuilder.postTags("</span>");
			srb.highlighter(highLightBuilder);
			/*------------为检索添加高亮标记   End---------------*/

			_Response = srb.get();				
			logger.debug("Response :{}", _Response.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			logger.debug("Query Finally");
			// this.CloseClient();
		}
		return _Response;
	}

}
