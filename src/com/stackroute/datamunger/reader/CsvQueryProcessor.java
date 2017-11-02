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

/**
 * class CsvQueryProcessor
 */
public class CsvQueryProcessor extends QueryProcessingEngine {

	/**
	 * Holds File name.
	 */
	private transient final String fileName;

	/**
	 * Regular Expression for determining Integer value.
	 */
	private static final String INT_REGEX = "^[0-9]+$";

	/**
	 * Regular Expression for determining Float value.
	 */
	private static final String FLOAT_REGEX = "[+-]?([0-9]*[.])[0-9]+";

	/**
	 * checking for date format dd/mm/yyyy
	 */
	private static final String DDMMYYYY_REGEX = "(([12][0-9]|3[01]|0?[1-9])/(0?[1-9]|1[012])/(?:19|20)[0-9]{1}[0-9]{1})";

	/**
	 * checking for date format mm/dd/yyyy
	 */
	private static final String MMDDYYYY_REGEX = "((0?[1-9]|1[012])/([12][0-9]|3[01]|0?[1-9])/(?:19|20)[0-9]{1}[0-9]{1})";

	/**
	 * date format dd-mon-yy
	 */
	private static final String DD_MON_YY_REGEX = "(([12][0-9]|3[01]|0?[1-9])-([a-z]{3})-(?)[0-9]{1}[0-9]{1})";

	/**
	 * date format dd-mon-yyyy
	 */
	private static final String DD_MON_YYYY_REGEX = "(([12][0-9]|3[01]|0?[1-9])-([a-z]{3})-(?:19|20)[0-9]{1}[0-9]{1})";

	/**
	 * date format dd-month-yy
	 */
	private static final String DD_MONTH_YY_REGEX = "(([12][0-9]|3[01]|0?[1-9])-([a-z])-(?)[0-9]{1}[0-9]{1})";

	/**
	 * date format dd-month-yyyy
	 */
	private static final String DD_MONTH_YYYY_REG = "(([12][0-9]|3[01]|0?[1-9])-([a-z])-(?:19|20)[0-9]{1}[0-9]{1})";

	/**
	 * date format yyyy-mm-dd
	 */
	private static final String YYYYMMDD_REGEX = "((?:19|20)[0-9]{1}[0-9]{1})-(0?[1-9]|1[012])-([12][0-9]|3[01]|0?[1-9])";

	/**
	 * parameterized constructor to initialize filename. As you are trying to
	 * perform file reading, hence you need to be ready to handle the IO Exceptions.
	 */
	public CsvQueryProcessor(final String fileName) throws FileNotFoundException {
		super();
		this.fileName = fileName;
		final File file = new File(this.fileName);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}

	}

	/**
	 * implementation of getHeader() method. We will have to extract the headers
	 * from the first line of the file.
	 */
	@Override
	public Header getHeader() throws IOException {
		// read the first line
		// populate the header object with the String array containing the header names
		Header header = null;
		if (null == this.fileName || this.fileName.isEmpty()) {
			return header;
		}

		final Path filePath = FileSystems.getDefault().getPath(this.fileName);
		try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

			String line = null;
			int count = 0;
			while ((line = reader.readLine()) != null) {
				count++;
				if (count == 1) {
					header = new Header(line.split(","));
				}
			}
		} catch (IOException ex) {
			System.err.format("IOException occured: {}", ex);
		}
		return header;
	}

	/**
	 * This method will be used in the upcoming assignments
	 */
	@Override
	public void getDataRow() {

	}

	/**
	 * implementation of getColumnType() method. 
	 */
	@Override
	public DataTypeDefinitions getColumnType() throws IOException {
		DataTypeDefinitions dataTypeDef = null;
		if (null != this.fileName && !this.fileName.isEmpty()) {

			// Getting data from the file.
			final String[] data = getData(this.fileName);

			if (null != data) {
				dataTypeDef = new DataTypeDefinitions();
				final List<String> dataType = new ArrayList<String>();
				String dataTypeString = null;
				for (final String string : data) {
					dataTypeString = this.getDataType(string).toString();
					dataTypeString = dataTypeString.split("class ")[1];
					dataType.add(dataTypeString);
				}
				dataTypeDef.setDataTypes(dataType.toArray(new String[dataType.size()]));
			}
		}
		return dataTypeDef;
	}

	/**
	 * Method to get Data from the file.
	 * 
	 * @param filePath
	 * @return
	 */
	private String[] getData(String fileName) throws IOException {
		final Path filePath = FileSystems.getDefault().getPath(fileName);
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
	 * 
	 * @param value
	 * @return
	 */
	public Object getDataType(final String input) {
		if (null == input || input.isEmpty()) {
			// System.out.println("matched Object");
			return Object.class;
		}
		// checking for Integer
		if (Pattern.matches(INT_REGEX, input)) {

			// System.out.println("matched Integer");
			return Integer.class;
		}
		// checking for floating point numbers
		if (Pattern.matches(FLOAT_REGEX, input)) {
			// System.out.println("matched float");
			return Float.class;
		}
		// checking for date format dd/mm/yyyy
		if (Pattern.matches(DDMMYYYY_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// checking for date format mm/dd/yyyy
		if (Pattern.matches(MMDDYYYY_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// checking for date format dd-mon-yy
		if (Pattern.matches(DD_MON_YY_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// checking for date format dd-mon-yyyy
		if (Pattern.matches(DD_MON_YYYY_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// checking for date format dd-month-yy
		if (Pattern.matches(DD_MONTH_YY_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// checking for date format dd-month-yyyy
		if (Pattern.matches(DD_MONTH_YYYY_REG, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// checking for date format yyyy-mm-dd
		if (Pattern.matches(YYYYMMDD_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// System.out.println("matched String");
		return String.class;
	}
}
