package com.lxk.match.lucene;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class QueryFilter {


	protected abstract boolean matchQuery(Object obj);

	protected abstract boolean findQuery(Object obj);
	
	public boolean match(Object obj) {
		return obj != null && matchQuery(obj);
	}


	public boolean find(Object obj) {
		return obj != null && findQuery(obj);
	}
	
	protected Object getValue(Object obj, String[] key) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return this.getValue(obj, key, 0);
	}
	
	@SuppressWarnings("unchecked")
	private Object getValue(Object obj, String[] key, int index) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object object;
		if (obj instanceof Map) {
			object = ((Map<String, Object>)obj).get(key[index]);
		}
		else {
			object = PropertyUtils.getProperty(obj, key[index]);
		}
		if (++index == key.length) {
            return object;
        } else {
            return getValue(object, key, index);
        }
	}
	
	protected boolean existValue(Object obj, String[] key) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return this.existValue(obj, key, 0);
	}
	
	@SuppressWarnings("unchecked")
	private boolean existValue(Object obj, String[] key, int index) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object object;
		if (obj instanceof Map) {
			if (!((Map<String, Object>)obj).containsKey(key[index])) {
                return false;
            }
			object = ((Map<String, Object>)obj).get(key[index]);
		}
		else {
			try {
				object = PropertyUtils.getProperty(obj, key[index]);
			}
			catch(NoSuchMethodException ex) {
				return false;
			}
		}
		if (++index == key.length) {
            return true;
        } else {
            return existValue(object, key, index);
        }
	}
}
