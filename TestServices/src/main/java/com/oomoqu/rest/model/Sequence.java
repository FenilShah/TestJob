package com.oomoqu.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sequence")
public class Sequence {
	private String sequenceId;
	private String item;
	private Long counter;
	
	@Id
	public String getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
	public Long getCounter() {
		return counter;
	}
	public void setCounter(Long counter) {
		this.counter = counter;
	}
	
	
}
