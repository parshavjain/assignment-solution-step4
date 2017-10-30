package com.stackroute.datamunger;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Header;
import com.stackroute.datamunger.reader.CsvQueryProcessor;

public class DataMunger {
	private DataMunger() {
		
	}

	public static void main(String... args) throws Exception {

		// read the file name from the user
		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		final String fileName = reader.readLine();

		/*
		 * create object of CsvQueryProcessor. We are trying to read from a file inside
		 * the constructor of this class. Hence, we will have to handle exceptions.
		 */
		final CsvQueryProcessor csvQueryProcessor = new CsvQueryProcessor(fileName);

		// call getHeader() method to get the array of headers
		final Header header = csvQueryProcessor.getHeader();

		/*
		 * call getColumnType() method of CsvQueryProcessor class to retrieve the array
		 * of column data types which is actually the object of DataTypeDefinitions
		 * class
		 */
		final DataTypeDefinitions dataTypeDef = csvQueryProcessor.getColumnType();

		/*
		 * display the columnName from the header object along with its data type from
		 * DataTypeDefinitions object
		 */
		if(null != header && null != header.getHeaders()
				 && null != dataTypeDef && null != dataTypeDef.getDataTypes()) {
			StringBuffer temp = null;
			 for (int i = 0; i < header.getHeaders().length && i < dataTypeDef.getDataTypes().length; i++) {
			    temp = new StringBuffer(header.getHeaders()[i] + ":");
				temp.append(dataTypeDef.getDataTypes()[i]);
				System.out.println(temp.toString());
			}
		 }

	}

}

