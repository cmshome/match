package com.lxk.match.lucene.queries;


public class MissingQueryFilter extends KeyQueryFilter {
	
	public String getKey() {
		return this.queryProperty.getKey();
	}

	public void setKey(String key) {
		this.queryProperty.setKey(key);
	}
	
	@Override
	public boolean match(Object obj) {
		if (!this.queryProperty.isNoKey() && obj == null) {
            return false;
        }
		return matchQuery(obj);
	}

	@Override
	protected boolean matchQuery(Object obj) {
		try {
			return !existValue(obj);
		} catch (Exception e) {
			return false;
		} 
	}
	
	@Override
	public String toString() {
		return "missing:" + this.getKey();
	}

	@Override
	protected boolean findQuery(Object obj) {
		return matchQuery(obj);
	}

}
