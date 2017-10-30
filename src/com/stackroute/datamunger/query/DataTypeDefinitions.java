package com.stackroute.datamunger.query;

//this class contains the data type definitions
public class DataTypeDefinitions {
	public DataTypeDefinitions() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * this class should contain a member variable which is a String array, to hold
	 * the data type for all columns for all data types
	 */
	private String[] dataTypes;

	/**
	 * @param dataTypes
	 *            the dataTypes to set
	 */
	public void setDataTypes(String... dataTypes) {
		this.dataTypes = dataTypes;
	}

	/**
	 * @return dataTypes the dataTypes to get
	 */
	public String[] getDataTypes() {
		return this.dataTypes;
	}
}
