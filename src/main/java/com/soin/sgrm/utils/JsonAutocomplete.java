package com.soin.sgrm.utils;

public class JsonAutocomplete {

	private String id;
	private String name;
	private String value;

	public JsonAutocomplete() {

	}

	public JsonAutocomplete(String id, String name, String value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
