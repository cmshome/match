package com.lxk.match.lucene.queries;

import java.util.Map;

/**
 */
public class TermValue {
	private String value;
	private Long longValue;
	private Double doubleValue;
	private Integer intValue;
	private boolean needLowerCase = false;
	
	public TermValue(String value, Map<String, String> replaceValues) {
		if (replaceValues != null && replaceValues.containsKey(value)) {
			value = replaceValues.remove(value);
		}
		this.value = value;
		//needLowerCase = !this.value.toUpperCase().equals(this.value.toLowerCase());
		try {
			this.longValue = Long.parseLong(value);
		}
		catch(Exception ignored) { }
		try {
			this.doubleValue = Double.parseDouble(value);
		}
		catch(Exception ignored) { }
		try {
			this.intValue = Integer.parseInt(value);
		}
		catch(Exception ignored) { }
	}

	public boolean match(Object object) {
		if (object == null) {
			return false;
		}
		if (object instanceof Long && this.longValue != null) {
			return this.longValue.equals(object);
		} else if (object instanceof Double && this.doubleValue != null) {
			return this.doubleValue.equals(object);
		} else if (object instanceof Integer && this.intValue != null) {
			return this.intValue.equals(object);
		} else if (needLowerCase) {
			return this.value.equals(object.toString().toLowerCase());
		} else {
			return this.value.equals(object.toString());
		}
	}
	
	public int compareTo(Object object) {
		if (object instanceof Long && this.longValue != null) {
			return this.longValue.compareTo((Long) object);
		} else if (object instanceof Double && this.doubleValue != null) {
			return this.doubleValue.compareTo((Double) object);
		} else if (object instanceof Integer && this.intValue != null) {
			return this.intValue.compareTo((Integer) object);
		} else {
			return this.value.compareTo(object.toString());
		}
	}

	@Override
	public String toString() {
		return this.value;
	}
	
	public String getValue() {
		return value;
	}

	public boolean find(Object object) {
		if (object instanceof Long && this.longValue != null) {
			return this.longValue.equals(object);
		} else if (object instanceof Double && this.doubleValue != null) {
			return this.doubleValue.equals(object);
		} else if (object instanceof Integer && this.intValue != null) {
			return this.intValue.equals(object);
		} else if (needLowerCase) {
			return this.value.indexOf(object.toString().toLowerCase()) != -1;
		} else {
			return this.value.indexOf(object.toString()) != -1;
		}
	}
}
