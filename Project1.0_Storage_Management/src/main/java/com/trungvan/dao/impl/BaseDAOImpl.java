package com.trungvan.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trungvan.dao.BaseDAO;
import com.trungvan.dto.Paging;

@Repository
@Transactional(rollbackFor = Exception.class)
public class BaseDAOImpl<E> implements BaseDAO<E> {
	
	final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	SessionFactory sessionFactory;
	
	// Dung de xac dinh kieu du lieu truyen vao Generic
	public String getGenericName() {

		String generic = "null";
		
		String classNameType = getClass().getGenericSuperclass().toString();
		log.info(">> classNameType: " + classNameType);
		
		// Cach 1: Tra ve "com.trungvan.entity.User"
		Pattern pattern = Pattern.compile("\\<(.*?)\\>");
		
		Matcher matcher = pattern.matcher(classNameType);
		log.info(">> matcher: " + matcher);
		
		if(matcher.find()) {
			
			generic = matcher.group(1);
		}
		
//		// Cach 2: Lay han? ra "User" 
//		Pattern pattern = Pattern.compile("<(.*?)>");
//		Matcher matcher = pattern.matcher(classNameType);
//		if(matcher.find()) {
//			
//			String subClassNameType = matcher.group(1);
//			String[] generics = Pattern.compile("\\.").split(subClassNameType);
//			generic = generics[generics.length - 1];
//			
//			log.info("duma:" + generic);
//		}
		
		return generic;
	}

	// > Su dung:
	//	+ findAll()			: Tra ve toan bo Record trong DB
	//	+ paging()			: Tra ve toan bo Record dua theo nhieu field search va tu dong paging
	//	+ searchAll()		: Tra ve toan bo Record dua theo nhieu field search
	//	+ findByProperty()	: Tra ve toan bo Record dua theo duy nhat 1 field search
	//	+ findById()		: Tra ve 1 Record dua theo field search la id
	// > Pham vi su dung: 	findAll()	>>	paging()	>>	searchAll()	>>	findByProperty()	>>	findById()
	
	@Override
	public List<E> findAll() {
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(" from ").append(getGenericName())
									.append(" as model where model.activeFlag = 1");

		Query<E> query = sessionFactory.getCurrentSession().createQuery(queryString.toString());
		log.info(">> Query find all:" + query.getQueryString());

		log.info("<<==>> sessionFactory find all");
		return query.getResultList();
	}

	@Override
	public List<E> paging(StringBuilder searchingQueryString, Map<String, Object> mapParams, Paging paging) {
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(" from ").append(getGenericName())
									.append(" as model where model.activeFlag = 1");


		StringBuilder countingQueryString = new StringBuilder();
		countingQueryString.append(" select count(*) from ").append(getGenericName())
						   									.append(" as model where model.activeFlag = 1");
		
		if(searchingQueryString != null && !searchingQueryString.toString().isEmpty()) {

			// Them dieu kien search vao queryString ban dau
			queryString.append(searchingQueryString);
			
			// Them dieu kien search vao countingQueryString ban dau
			countingQueryString.append(searchingQueryString);
		}

		// Tao cau lenh tu queryString da duoc them dieu kien search
		Query<E> query = sessionFactory.getCurrentSession().createQuery(queryString.toString());
		log.info(">> Query paging all:" + query.getQueryString());
		
		Query<E> countingQuery = sessionFactory.getCurrentSession().createQuery(countingQueryString.toString());
		
		// set param cho query thong qua Map<>
		if(mapParams != null && !mapParams.isEmpty()) {
			
			for (String key : mapParams.keySet()) {
				query.setParameter(key, mapParams.get(key));
				countingQuery.setParameter(key, mapParams.get(key));
			}
		}
		
		if(paging != null) {
			
			// from ... as model where model.activeFlag = 1 limit paging.getOffset(), paging.getRecordPerPage() 
			query.setFirstResult(paging.getOffset());	
			query.setMaxResults(paging.getRecordPerPage());
			
			long totalRecords = (long) countingQuery.uniqueResult();
			paging.setTotalRows(totalRecords);
		}

		log.info("<<==>> sessionFactory paging all");
		log.info(">> searchingQueryString in paging:" + searchingQueryString);
		return query.getResultList();
	}
	
	@Override
	public List<E> searchAll(StringBuilder searchingQueryString, Map<String, Object> mapParams) {
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(" from ").append(getGenericName())
									.append(" as model where model.activeFlag = 1");

		if(searchingQueryString != null && !searchingQueryString.toString().isEmpty()) {

			// Them dieu kien search vao queryString ban dau
			queryString.append(searchingQueryString);
			
			// Tao cau lenh tu queryString da duoc them dieu kien search
			Query<E> query = sessionFactory.getCurrentSession().createQuery(queryString.toString());
			log.info(">> Query search all:" + query.getQueryString());
			
			// set param cho query thong qua Map<>
			if(mapParams != null && !mapParams.isEmpty()) {
				
				for (String key : mapParams.keySet()) {
					query.setParameter(key, mapParams.get(key));
				}
			}

			log.info("<<==>> sessionFactory search all");
			return query.getResultList();
		}
		return null;
	}

	/**
	 * > Ham searchAll() la truong hop tong quat cua ham findByProperty():
	 * 		+ Ham searchAll() co the tim theo bat cu field nao truyen vao va so luong khong gioi han boi su dung Map<>
	 * 		+ Ham findByProperty() cung co the tim theo bat cu field nao truyen vao nhung chi tim duoc theo 1 field 
	 */
	@Override
	public List<E> findByProperty(String property, Object value) {
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(" from ").append(getGenericName())
									.append(" as model where model.activeFlag = 1 and model.")
									.append(property)
									.append(" = ?");
		
		Query<E> query = sessionFactory.getCurrentSession().createQuery(queryString.toString());
		query.setParameter(0, value);
		log.info(">> Query find by property:" + query.getQueryString());

		log.info("<<==>> sessionFactory find by property");
		return query.getResultList();
	}

	@Override
	public E findById(Class<E> e, Serializable id) {
		
		log.info("<<==>> sessionFactory find by ID");
		return sessionFactory.getCurrentSession().get(e, id);
	}

	@Override
	public void save(E instance) {
		
		log.info("<<==>> sessionFactory save instance");
		sessionFactory.getCurrentSession().save(instance);
	}

	@Override
	public void update(E instance) {
		
		log.info("<<==>> sessionFactory update instance");
		sessionFactory.getCurrentSession().merge(instance);
	}
}
