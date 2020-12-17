package com.lxk.match.lucene.queries;

import com.lxk.match.lucene.QueryFilter;
import org.apache.lucene.search.BooleanClause.Occur;

public class BooleanClause {
	private QueryFilter query;
	private Occur occur;

	public QueryFilter getQuery() {
		return query;
	}

	public void setQuery(QueryFilter query) {
		this.query = query;
	}

	public Occur getOccur() {
		return occur;
	}

	public void setOccur(Occur occur) {
		this.occur = occur;
	}

	@Override
	public String toString() {
		return occur.toString() + query;
	}

	public boolean isProhibited() {
		return Occur.MUST_NOT == occur;
	}

	public boolean isRequired() {
		return Occur.MUST == occur;
	}
}
