package com.lxk.match.lucene.queries;


/**
 */
public class TermQueryFilter extends KeyQueryFilter {
	private TermValue term;
	
	@Override
	protected boolean matchQuery(Object obj) {
		try {
			if (obj == null) {
				return false;
			}
			Object object = this.getValue(obj);
			return term.match(object);
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

	public TermValue getTerm() {
		return term;
	}

	public void setTerm(TermValue term) {
		this.term = term;
	}
	
	@Override
	public String toString() {
		return getKey() + ":" + this.term;
	}

	@Override
	protected boolean findQuery(Object obj) {
		try {
			Object object = this.getValue(obj);
			return term.find(object);
		} catch (Exception e) {
			return false;
		}
	}
}
