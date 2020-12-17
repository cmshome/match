package com.lxk.match.lucene;


import com.lxk.match.exceptions.QueryFilterNotSupportedException;

public interface FilterParser {
	QueryFilter parse(String queryString) throws QueryFilterNotSupportedException;
}
