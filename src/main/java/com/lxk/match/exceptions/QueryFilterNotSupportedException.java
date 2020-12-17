package com.lxk.match.exceptions;

public class QueryFilterNotSupportedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3741586975031545048L;
	
	public QueryFilterNotSupportedException() {
		super();
	}
	
	public QueryFilterNotSupportedException(String msg) {
		super(msg);
	}
	
	public QueryFilterNotSupportedException(Exception ex) {
		super(ex);
	}
	
	public QueryFilterNotSupportedException(String msg, Exception ex) {
		super(msg, ex);
	}

}
