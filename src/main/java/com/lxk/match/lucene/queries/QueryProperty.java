package com.lxk.match.lucene.queries;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 */
public class QueryProperty {

	public static final String NOKEY = "_NOKEY_";
	
	private String key;
	private String[] keyValue;
	private int keyValueLength;

	public String getKey() {
		if (NOKEY.equals(key)) {
			return "";
		}
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
		this.keyValue = getKey(this.key);
		if (this.keyValue == null) {
			this.keyValueLength = 0;
		} else {
			this.keyValueLength = this.keyValue.length;
		}
	}

	public Object getValue(Object obj) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return this.getValue(obj, 0);
	}
	
	@SuppressWarnings("unchecked")
	private Object getValue(Object obj, int index) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (keyValue == null) {
			return obj;
		}
		Object object;
		if (obj instanceof Map) {
			object = ((Map<String, Object>)obj).get(keyValue[index]);
		} else if (isBasicType(obj)) {
			return obj;
		}
		else {
			object = PropertyUtils.getProperty(obj, keyValue[index]);
		}
		if (++index == keyValueLength) {
			return object;
		} else {
			return getValue(object, index);
		}
	}

	/**
	 * 判断对象是否是基本类型
	 *
	 * @param obj 输入参数
	 * @return 是否是基本类型
	 */
	private boolean isBasicType(Object obj) {
		return obj instanceof Number || obj instanceof String || obj instanceof Date;
	}
	
	public boolean existValue(Object obj) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return this.existValue(obj, 0);
	}
	
	@SuppressWarnings("unchecked")
	private boolean existValue(Object obj, int index) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (keyValue == null) {
			return obj != null;
		}
		Object object;
		if (obj instanceof Map) {
			if (!((Map<String, Object>) obj).containsKey(keyValue[index])) {
				return false;
			}
			object = ((Map<String, Object>) obj).get(keyValue[index]);
		} else {
			try {
				object = PropertyUtils.getProperty(obj, keyValue[index]);
			} catch (NoSuchMethodException ex) {
				return false;
			}
		}
		return ++index == keyValue.length || existValue(object, index);
	}
	
	private String[] getKey(String key) {
		return NOKEY.equals(key) ? null : key.split("\\.");
	}
	
	public boolean isNoKey() {
		return this.keyValue == null;
	}
}
