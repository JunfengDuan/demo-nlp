package com.example.demo.service.elasticsearch;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.stereotype.Component;

@Component
public class RequestQuery {

	public String[] resFields;
	public int size = 20;
	public String orderby = null;
	public String order = "desc";
	public int from = 0;
	public String typeIndex = null;
	private QueryBuilder qb = null;
	private QueryBuilder qbWhere=null;

	public String getType() {
		return this.typeIndex;
	}

	public void setType(String type) {
		this.typeIndex = type;
	}

	public String[] getResFields() {
		return this.resFields;
	}

	public int getSize() {
		return size;
	}

	public int getFrom() {
		return from;
	}

	public String getOrderby() {
		return orderby;
	}

	public String getOrder() {
		return order;
	}

	public void setQueryBuilder(QueryBuilder qb) {
		this.qb = qb;
	}

	public QueryBuilder getQueryBuilder() {
		return this.qb;
	}

	public void setQBWhere(QueryBuilder qb){
		this.qbWhere=qb;
	}

	public QueryBuilder getQBWhere(){
		return this.qbWhere;
	}

	
}
