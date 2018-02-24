package com.weige.pojo;

import java.util.List;

public class SolrResult<T> {
	private Long total;
	private List<T> rows;
	
	public SolrResult() {
	
	}
	public SolrResult(Long total, List<T> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
