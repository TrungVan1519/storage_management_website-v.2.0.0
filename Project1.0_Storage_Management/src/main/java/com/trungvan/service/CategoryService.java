package com.trungvan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trungvan.dao.CategoryDAO;
import com.trungvan.dto.Paging;
import com.trungvan.entity.Category;

@Service
public class CategoryService {
	
	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private CategoryDAO<Category> categoryDAO;
	
	public List<Category> findAll() {
		
		log.info("<<==>> categoryDAO find all");
		return categoryDAO.findAll();
	}
	
	public List<Category> paging(Category category, Paging paging) {
		
		StringBuilder searchingQueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		if(category != null) {

			if(category.getId() != null && category.getId() != 0) {
				
				searchingQueryString.append(" and model.id = :id");
				mapParams.put("id", category.getId());
			}
			if(category.getCode() != null && !category.getCode().isEmpty()) {
				
				searchingQueryString.append(" and model.code = :code");
				mapParams.put("code", category.getCode());
			}
			if(category.getName() != null && !category.getName().isEmpty()) {
				
				searchingQueryString.append(" and model.name like :name");
				mapParams.put("name", "%" + category.getName() + "%");
			}
		}

		log.info("<<==>> categoryDAO paging all");
		log.info(">> paging category:" + category);
		return categoryDAO.paging(searchingQueryString, mapParams, paging);
	}
	
	public List<Category> searchAll(Category category){
		
		StringBuilder searchingQueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		if(category != null) {
			
			if(category.getId() != null && category.getId() != 0) {
				
				searchingQueryString.append(" and model.id = :id");
				mapParams.put("id", category.getId());
			}
			if(category.getCode() != null && !category.getCode().isEmpty()) {
				
				searchingQueryString.append(" and model.code = :code");
				mapParams.put("code", category.getCode());
			}
			if(category.getName() != null && !category.getName().isEmpty()) {
				
				searchingQueryString.append(" and model.name like :name");
				mapParams.put("name", "%" + category.getName() + "%");
			}
		}

		log.info("<<==>> categoryDAO search all");
		return categoryDAO.searchAll(searchingQueryString, mapParams);
	}
	
	public List<Category> findByProperty(String property, Object value){
		
		log.info("<<==>> categoryDAO find by property: " + property + "\tvalue: " + value.toString());
		return categoryDAO.findByProperty(property, value);
	}
	
	public Category findById(int id) {
		
		log.info("<<==>> categoryDAO find by id: " + id);
		return categoryDAO.findById(Category.class, id);
	}
	
	public void save(Category category) throws Exception {
		
		category.setActiveFlag(1);
		category.setCreatedDate(new Date());
		category.setUpdatedDate(new Date());
		
		log.info("<<==>> categoryDAO save category: " + category);
		categoryDAO.save(category);
	}
	
	public void update(Category category) throws Exception {
		
		category.setUpdatedDate(new Date());
		
		log.info("<<==>> categoryDAO update category: " + category);
		categoryDAO.update(category);
	}
	
	public void delete(Category category) throws Exception {
		
		// Do khi xoa ta khong xoa han object ma chi set activeFlag = 0
		category.setActiveFlag(0);
		category.setUpdatedDate(new Date());
		
		log.info("<<==>> categoryDAO delete category (activeFlag = 0):" + category);
		categoryDAO.update(category);
	}
}
