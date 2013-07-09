package com.rong.realestateqq.json;


public interface JSONArrayBean<T extends JSONBean> extends JSONBean {
	public boolean add(T t);

	public T get(int index);
	
	public Class<T> getGenericClass();
}
