package com.trungvan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.trungvan.dao.ProductInfoDAO;
import com.trungvan.dto.Paging;
import com.trungvan.entity.ProductInfo;

@Service
public class ProductInfoService {
	
	private final Logger log = Logger.getLogger(this.getClass());
	
//	@Autowired
//	private CategoryService categoryService;

	@Autowired
	private ProductInfoDAO<ProductInfo> productInfoDAO;
	
	public List<ProductInfo> findAll() {

		log.info("<<==>> productInfoDAO find all");
		return productInfoDAO.findAll();
	}
	
	public List<ProductInfo> paging(ProductInfo productInfo, Paging paging) {
		
		StringBuilder searchingQueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		if(productInfo != null) {
			
			if(productInfo.getId() != null && productInfo.getId() != 0) {
				
				searchingQueryString.append(" and model.id = :id");
				mapParams.put("id", productInfo.getId());
			} 
			if(productInfo.getCode() != null && !StringUtils.isEmpty(productInfo.getCode())) {
				
				searchingQueryString.append(" and model.code = :code");
				mapParams.put("code", productInfo.getCode());
			}
			if(productInfo.getName() != null && !StringUtils.isEmpty(productInfo.getName())) {
				
				searchingQueryString.append(" and model.name = like :name");
				mapParams.put("name", "%" + productInfo.getName() + "%");
			}
		}

		log.info("<<==>> productInfoDAO paging all");
		log.info(">> paging productInfo:" + productInfo);
		return productInfoDAO.paging(searchingQueryString, mapParams, paging);
	}
	
	public List<ProductInfo> searchAll(ProductInfo productInfo){
		
		StringBuilder searchingQueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		if(productInfo != null) {
			
			if(productInfo.getId() != null && productInfo.getId() != 0) {
				
				searchingQueryString.append(" and model.id = :id");
				mapParams.put("id", productInfo.getId());
			}
			if(productInfo.getCode() != null && !productInfo.getCode().isEmpty()) {
				
				searchingQueryString.append(" and model.code = :code");
				mapParams.put("code", productInfo.getCode());
			}
			if(productInfo.getName() != null && !productInfo.getName().isEmpty()) {
				
				searchingQueryString.append(" and model.name like :name");
				mapParams.put("name", "%" + productInfo.getName() + "%");
			}
		}

		log.info("<<==>> productInfo search all");
		return productInfoDAO.searchAll(searchingQueryString, mapParams);
	}
	
	public List<ProductInfo> findByProperty(String property, Object value){
		
		log.info("<<==>> productInfoDAO find by property: " + property + "\tvalue: " + value.toString());
		return productInfoDAO.findByProperty(property, value);
	}
	
	public ProductInfo findById(int id) {
		
		log.info("<<==>> productInfoDAO find by id: " + id);
		return productInfoDAO.findById(ProductInfo.class, id);
	}
	
	public void save(ProductInfo productInfo) throws Exception {
		
		productInfo.setActiveFlag(1);
		productInfo.setCreatedDate(new Date());
		productInfo.setUpdatedDate(new Date());
		
		// TH them moi image thi dong thoi cung them imageUrl vao DB luon - /upload/ == /upload/**
		String fileName = productInfoDAO.uploadFile(productInfo);
		if(!fileName.isEmpty()) {

			log.info("<<==>> productInfoDAO upload file: " + fileName);
			productInfo.setImageUrl("/upload/" + fileName);
		}

		log.info("<<==>> productInfoDAO save productInfo: " + productInfo);
		productInfoDAO.save(productInfo);
	}
	
	public void update(ProductInfo productInfo) throws Exception {
		
		// Khong can thiet luc nay nhung trong tuong lai co the su dung
//		Category category = categoryService.findById(productInfo.getCategory().getId());
//		productInfo.setCategory(category);
//		log.info(">> category is chosen by options: " + category);
		// > Do ta chi nhan ve gia tri id cua object category trong productInfo nen ta chi lay ra va
		//		su dung duoc moi field nay 
		// > Sau do ta se phai tim kiem lai category trong danh sach category = field id nay de lay ra
		//		va luc nay moi truyen duoc vao propductInfo
		
		productInfo.setUpdatedDate(new Date());
		
		// TH update image khac thi thiet lap lai imageUrl trong DB - /upload/ == /upload/**
		String fileName = productInfoDAO.uploadFile(productInfo);
		if(!StringUtils.isEmpty(fileName)) {

			log.info("<<==>> productInfoDAO upload file: " + fileName);
			productInfo.setImageUrl("/upload/" + fileName);
		}

		log.info("<<==>> productInfoDAO update productInfo: " + productInfo);
		productInfoDAO.update(productInfo);
	}
	
	public void delete(ProductInfo productInfo) throws Exception {
		
		// Do khi xoa ta khong xoa han object ma chi set activeFlag = 0
		productInfo.setActiveFlag(0);
		productInfo.setUpdatedDate(new Date());

		log.info("<<==>> productInfoDAO delete productInfo (activeFlag = 0):" + productInfo);
		productInfoDAO.update(productInfo);
	}
}