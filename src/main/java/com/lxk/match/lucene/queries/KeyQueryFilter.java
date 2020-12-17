package com.lxk.match.lucene.queries;


import com.lxk.match.lucene.QueryFilter;

import java.lang.reflect.InvocationTargetException;

public abstract class KeyQueryFilter extends QueryFilter {
	
	protected QueryProperty queryProperty = new QueryProperty();
	
	protected Object getValue(Object obj) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return queryProperty.getValue(obj);
	}
	
	protected boolean existValue(Object obj) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return queryProperty.existValue(obj);
	}
}
