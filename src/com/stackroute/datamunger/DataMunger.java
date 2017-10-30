package com.stackroute.datamunger;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Header;
import com.stackroute.datamunger.reader.CsvQueryProcessor;

public class DataMunger {

	public static void main(String[] args) throws Exception {

		// read the file name from the user
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String fileName = reader.readLine();

		/*
		 * create object of CsvQueryProcessor. We are trying to read from a file inside
		 * the constructor of this class. Hence, we will have to handle exceptions.
		 */
		CsvQueryProcessor csvQueryProcessor = new CsvQueryProcessor(fileName);

		// call getHeader() method to get the array of headers
		Header header = csvQueryProcessor.getHeader();

		/*
		 * call getColumnType() method of CsvQueryProcessor class to retrieve the array
		 * of column data types which is actually the object of DataTypeDefinitions
		 * class
		 */
		DataTypeDefinitions dataTypeDefinitions = csvQueryProcessor.getColumnType();

		/*
		 * display the columnName from the header object along with its data type from
		 * DataTypeDefinitions object
		 */
		if(null != header && null != header.getHeaders()
				 && null != dataTypeDefinitions && null != dataTypeDefinitions.getDataTypes()) {
			 for (int i = 0; i < header.getHeaders().length && i < dataTypeDefinitions.getDataTypes().length; i++) {
				String temp = "";
				temp += header.getHeaders()[i] + " : ";
				temp += dataTypeDefinitions.getDataTypes()[i];
				System.out.println(temp);
			}
		 }

	}

}

