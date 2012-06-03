package com.sillycat.easynosql.dao.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Rolemongo {

	@Id
	private String id;

	private Integer role;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}
	
}
