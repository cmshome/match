package com.lxk.match.lucene.queries;


/**
 */
public class PrefixQueryFilter extends KeyQueryFilter {
	
	private String prefix;

	@Override
	protected boolean matchQuery(Object obj) {
		try {
			Object object = this.getValue(obj);
			return object != null && object.toString().startsWith(prefix);
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

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public String toString() {
		return getKey() + ":" + prefix + "*";
	}

	@Override
	protected boolean findQuery(Object obj) {
		return matchQuery(obj);
	}
}
