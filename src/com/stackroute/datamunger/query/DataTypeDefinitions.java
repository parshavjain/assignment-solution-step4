package com.stackroute.datamunger.query;

/**
 * this class contains the data type definitions
 * @author PARSAV
 *
 */
public class DataTypeDefinitions {

	/**
	 * this class should contain a member variable which is a String array, to hold
	 * the data type for all columns for all data types
	 */
	private String[] dataTypes;

	/**
	 * Constructor
	 * 
	 * @param dataTypes
	 */
	public DataTypeDefinitions(final String... dataTypes) {
		this.dataTypes = dataTypes;
	}

	/**
	 * @param dataTypes
	 *            the dataTypes to set
	 */
	public void setDataTypes(final String... dataTypes) {
		this.dataTypes = dataTypes;
	}

	/**
	 * @return dataTypes the dataTypes to get
	 */
	public String[] getDataTypes() {
		return this.dataTypes.clone();
	}
}
