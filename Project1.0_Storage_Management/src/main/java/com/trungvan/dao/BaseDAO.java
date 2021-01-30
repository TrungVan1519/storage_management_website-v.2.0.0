package com.trungvan.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.trungvan.dto.Paging;

public interface BaseDAO<E> {
	
	// > Su dung:
	//	+ findAll()			: Tra ve toan bo Record trong DB
	//	+ paging()			: Tra ve toan bo Record dua theo nhieu field search va tu dong paging
	//	+ searchAll()		: Tra ve toan bo Record dua theo nhieu field search
	//	+ findByProperty()	: Tra ve toan bo Record dua theo duy nhat 1 field search
	//	+ findById()		: Tra ve 1 Record dua theo field search la id
	// > Pham vi su dung: 	findAll()	>>	paging()	>>	searchAll()	>>	findByProperty()	>>	findById()
	
	List<E> findAll(); 
	
	List<E> paging(StringBuilder searchingQueryString, Map<String, Object> mapParams, Paging paging);
	
	List<E> searchAll(StringBuilder searchingQueryString, Map<String, Object> mapParams);
	
	List<E> findByProperty(String property , Object value);
	
	E findById(Class<E> e, Serializable id);
	
	void save(E instance);
	
	void update(E instance);
}
