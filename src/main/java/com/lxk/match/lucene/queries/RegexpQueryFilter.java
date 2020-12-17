package com.lxk.match.lucene.queries;


import java.util.regex.Pattern;

/**
 */
public class RegexpQueryFilter extends KeyQueryFilter {
	private Pattern pattern;

	@Override
	protected boolean matchQuery(Object obj) {
		try {
			if (obj == null) {
				return false;
			}
			Object object = this.getValue(obj);
			return this.pattern.matcher(object.toString()).matches();
		} catch (Exception e) {
			return false;
		}
	}
	
	public String getKey() {
		return this.queryProperty.getKey();
	}

	public void setKey(String key) {
		this.queryProperty.setKey(key);
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public void setValue(String text) {
		this.pattern = Pattern.compile(text);
	}

	@Override
	public String toString() {
		return getKey() + ":/" + this.pattern + "/";
	}

	@Override
	protected boolean findQuery(Object obj) {
		try {
			Object object = this.getValue(obj);
			return this.pattern.matcher(object.toString()).find();
		} catch (Exception e) {
			return false;
		}
	}
}
