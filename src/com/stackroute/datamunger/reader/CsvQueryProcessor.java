package com.stackroute.datamunger.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Header;

public class CsvQueryProcessor extends QueryProcessingEngine {

	private String fileName;
	// Regular Expression for determining Integer value.
	private static final String INT_REGEX = "^[0-9]+$";

	// Regular Expression for determining Float value.
	private static final String FLOAT_REGEX = "[+-]?([0-9]*[.])[0-9]+";

	// Regular Expression for determining vaious Date Formats.
	// checking for date format dd/mm/yyyy
	private static final String DDMMYYYY_REGEX = "(([12][0-9]|3[01]|0?[1-9])/(0?[1-9]|1[012])/(?:19|20)[0-9]{1}[0-9]{1})";
	// checking for date format mm/dd/yyyy
	private static final String MMDDYYYY_REGEX = "((0?[1-9]|1[012])/([12][0-9]|3[01]|0?[1-9])/(?:19|20)[0-9]{1}[0-9]{1})";
	// date format dd-mon-yy
	private static final String DD_MON_YY_REGEX = "(([12][0-9]|3[01]|0?[1-9])-([a-z]{3})-(?)[0-9]{1}[0-9]{1})";
	// date format dd-mon-yyyy
	private static final String DD_MON_YYYY_REGEX = "(([12][0-9]|3[01]|0?[1-9])-([a-z]{3})-(?:19|20)[0-9]{1}[0-9]{1})";
	// date format dd-month-yy
	private static final String DD_MONTH_YY_REGEX = "(([12][0-9]|3[01]|0?[1-9])-([a-z])-(?)[0-9]{1}[0-9]{1})";
	// date format dd-month-yyyy
	private static final String DD_MONTH_YYYY_REGEX = "(([12][0-9]|3[01]|0?[1-9])-([a-z])-(?:19|20)[0-9]{1}[0-9]{1})";
	// date format yyyy-mm-dd
	private static final String YYYYMMDD_REGEX = "((?:19|20)[0-9]{1}[0-9]{1})-(0?[1-9]|1[012])-([12][0-9]|3[01]|0?[1-9])";

	/*
	 * parameterized constructor to initialize filename. As you are trying to
	 * perform file reading, hence you need to be ready to handle the IO Exceptions.
	 */
	public CsvQueryProcessor(String fileName) throws FileNotFoundException {
	    this.fileName = fileName;
		File file = new File(fileName);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
	}

	/*
	 * implementation of getHeader() method. We will have to extract the headers
	 * from the first line of the file.
	 */
	@Override
	public Header getHeader() throws IOException {
		// read the first line
		// populate the header object with the String array containing the header names
		if (null == fileName || fileName.isEmpty()) {
			return null;
		}

		Header header = new Header();
		Path filePath = FileSystems.getDefault().getPath(fileName);
		try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				header.setHeaders(line.split(","));
				return header;
			}
		} catch (IOException ex) {
			System.err.format("IOException occured: {}", ex);
		}
		return null;
	}

	/**
	 * This method will be used in the upcoming assignments
	 */
	@Override
	public void getDataRow() {

	}

	/*
	 * implementation of getColumnType() method. To find out the data types, we will
	 * read the first line from the file and extract the field values from it. In
	 * the previous assignment, we have tried to convert a specific field value to
	 * Integer or Double. However, in this assignment, we are going to use Regular
	 * Expression to find the appropriate data type of a field. Integers: should
	 * contain only digits without decimal point Double: should contain digits as
	 * well as decimal point Date: Dates can be written in many formats in the CSV
	 * file. However, in this assignment,we will test for the following date
	 * formats('dd/mm/yyyy',
	 * 'mm/dd/yyyy','dd-mon-yy','dd-mon-yyyy','dd-month-yy','dd-month-yyyy','yyyy-mm
	 * -dd')
	 */
	@Override
	public DataTypeDefinitions getColumnType() throws IOException {
		if (null == fileName || fileName.isEmpty()) {
			return null;
		}

		// Getting data from the file.
		String[] data = getData(fileName);

		DataTypeDefinitions dataTypeDefinitions = null;
		if (null != data) {
			dataTypeDefinitions = new DataTypeDefinitions();
			List<String> dataType = new ArrayList<String>();
			for (String string : data) {
				if (null == string || string.isEmpty()) {
					dataType.add(Object.class.toString().split("class ")[1]);
					continue;
				}
				// checking for date format dd/mm/yyyy
				if (Pattern.matches(DDMMYYYY_REGEX, string) || Pattern.matches(MMDDYYYY_REGEX, string)
						|| Pattern.matches(DD_MON_YY_REGEX, string) || Pattern.matches(DD_MON_YYYY_REGEX, string)
						|| Pattern.matches(DD_MONTH_YY_REGEX, string) || Pattern.matches(DD_MONTH_YYYY_REGEX, string)
						|| Pattern.matches(YYYYMMDD_REGEX, string)) {
					dataType.add(Date.class.toString().split("class ")[1]);
					continue;
				}
				dataType.add(this.determineIntegerFloat(string));
			}
			dataTypeDefinitions.setDataTypes(dataType.toArray(new String[dataType.size()]));
		}
		return dataTypeDefinitions;
	}

	/**
	 * Method to get Data from the file.
	 * 
	 * @param filePath
	 * @return
	 */
	private String[] getData(String fileName) throws IOException {
		Path filePath = FileSystems.getDefault().getPath(fileName);
		String[] data = null;
		try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
			String line = null;
			int count = 0;
			while ((line = reader.readLine()) != null) {
				if (count > 0) {
					data = line.split(",", -1);
					break;
				}
				count++;
			}
		} catch (IOException ex) {
			System.err.format("IOException occured: {}", ex);
		}
		return data;
	}

	/**
	 * Method to check for Integer, Float, String.
	 * @param value
	 * @return
	 */
	private String determineIntegerFloat(String value) {
		try {
			// checking for Integer
			Integer.parseInt(value);
			return Integer.class.toString().split("class ")[1];
		} catch (NumberFormatException ex1) {
			try {
				// checking for floating point numbers
				Float.parseFloat(value);
				return Float.class.toString().split("class ")[1];
			} catch (Exception e) {
				//Returning String.
				return String.class.toString().split("class ")[1];
			}
		}
	}

}


