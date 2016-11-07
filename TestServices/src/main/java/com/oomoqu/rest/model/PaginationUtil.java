package com.oomoqu.rest.model;

import java.util.List;

public class PaginationUtil {

	private List<Object> Data;
	private int count;

	public List<Object> getData() {
		return Data;
	}
	public void setData(List<Object> data) {
		Data = data;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
