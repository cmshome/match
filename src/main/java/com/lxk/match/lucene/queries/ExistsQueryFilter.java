package com.lxk.match.lucene.queries;


public class ExistsQueryFilter extends KeyQueryFilter {
	
	public String getKey() {
		return this.queryProperty.getKey();
	}

	public void setKey(String key) {
		this.queryProperty.setKey(key);
	}

	@Override
	protected boolean matchQuery(Object obj) {
		try {
			return existValue(obj);
		} catch (Exception e) {
			return false;
		} 
	}
	
	@Override
	public String toString() {
		return "exists:" + this.getKey();
	}

	@Override
	protected boolean findQuery(Object obj) {
		return matchQuery(obj);
	}

}
