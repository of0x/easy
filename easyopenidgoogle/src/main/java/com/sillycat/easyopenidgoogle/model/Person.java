package com.sillycat.easyopenidgoogle.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonBackReference;

public class Person implements Serializable {

	private static final long serialVersionUID = -6580255478885072794L;

	private Integer id;

	private String personName;

	private Company company;

	public Person() {
	}

	public Person(Integer id, String personName) {
		this.id = id;
		this.personName = personName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	@JsonBackReference("Company-Person")
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
