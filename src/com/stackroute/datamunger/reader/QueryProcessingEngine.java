package com.stackroute.datamunger.reader;

import java.io.IOException;

import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Header;
/**
 * 
 * @author PARSAV
 *
 */
public abstract class QueryProcessingEngine {
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract Header getHeader() throws IOException;
	
	/**
	 * getDataRow() Signature
	 */
	public abstract void getDataRow();
	
	/**
	 * getColumnType() Signature
	 * 
	 * @return
	 * @throws IOException
	 */
	public abstract DataTypeDefinitions getColumnType() throws IOException;
	
	
}
