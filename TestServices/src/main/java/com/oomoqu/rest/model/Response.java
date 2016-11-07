package com.oomoqu.rest.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class Response {
	private int code;
	private String message;
	private String reason;
	private Object data;
	
	String extraFieldId;
	String extraFieldValue;
	
	public Response() {
		// TODO Auto-generated constructor stub
	}
	
	public Response(int code){
		this.code = code;
		if(code == ResponseStatusCode.saved){
			this.recordSaved();
		}else if(code == ResponseStatusCode.updated){
			this.recordUpdated();
		}else if(code == ResponseStatusCode.deleted){
			this.recordDeleted();
		}else if(code == ResponseStatusCode.failed){
			this.operationFailed();
		}
	}
	
	public Response(int code, Object o){
		this(code);
		this.data = o;
	}
	
	public Response(int code, String s){
		this(code);
		this.setReason(s);
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public void append(String key,String value){
		this.extraFieldId = key;
		this.extraFieldValue = value;
	}
	
	public void recordSaved(){
		this.message = "Record saved successfully";
	}
	
	public void recordUpdated(){
		this.message = "Record updated successfully";
	}
	
	public void recordDeleted(){
		this.message = "Record deleted successfully";
	}
	
	public void operationFailed(){
		this.message = "Failed to perform operation";
	}
	
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode tree = mapper.valueToTree(this);
		if(this.extraFieldId != null){
			((ObjectNode)tree).put(this.extraFieldId, this.extraFieldValue);
		}
		return tree.toString();
	}
}
