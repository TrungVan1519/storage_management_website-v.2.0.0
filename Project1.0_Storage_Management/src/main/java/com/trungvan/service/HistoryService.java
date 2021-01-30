package com.trungvan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.trungvan.dao.HistoryDAO;
import com.trungvan.dto.Paging;
import com.trungvan.entity.History;
import com.trungvan.entity.Invoice;

@Service
public class HistoryService {
	
	private final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private HistoryDAO<History> historyDAO;
	
	public List<History> findAll() {

		log.info("<<==>> historyDAO find all");
		return historyDAO.findAll();
	}
	
	public List<History> paging(History history, Paging paging){
		
		StringBuilder searchingqueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		if(history != null) {
			
			if(history.getProductInfo() != null) {
				
				if(!StringUtils.isEmpty(history.getProductInfo().getCategory().getName()) ) {
					
					searchingqueryString.append(" and model.productInfo.category.name like :categoryName");
					mapParams.put("categoryName","%" + history.getProductInfo().getCategory().getName() + "%");
				}
				if(!StringUtils.isEmpty(history.getProductInfo().getCode())) {
					
					searchingqueryString.append(" and model.productInfo.code = :code");
					mapParams.put("code", history.getProductInfo().getCode());
				}
				if( !StringUtils.isEmpty(history.getProductInfo().getName()) ) {
					
					searchingqueryString.append(" and model.productInfo.name like :name");
					mapParams.put("name", "%" + history.getProductInfo().getName() + "%");
				}
			}
			if(!StringUtils.isEmpty(history.getActionName()) ) {
				
				searchingqueryString.append(" and model.actionName like :actionName");
				mapParams.put("actionName", "%" + history.getActionName() + "%");
			}
			if(history.getType() != null && history.getType() != 0) {
				
				searchingqueryString.append(" and model.type = :type");
				mapParams.put("type", history.getType());
			}
		}
		
		log.info("<<==>> historyDAO paging all");
		return historyDAO.paging(searchingqueryString, mapParams, paging);
	}
	
	public List<History> searchAll(History history){
		
		StringBuilder searchingqueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		if(history != null) {
			
			if(history.getProductInfo() != null) {
				
				if(!StringUtils.isEmpty(history.getProductInfo().getCategory().getName()) ) {
					
					searchingqueryString.append(" and model.productInfo.category.name like :categoryName");
					mapParams.put("categoryName","%" + history.getProductInfo().getCategory().getName() + "%");
				}
				if(!StringUtils.isEmpty(history.getProductInfo().getCode())) {
					
					searchingqueryString.append(" and model.productInfo.code = :code");
					mapParams.put("code", history.getProductInfo().getCode());
				}
				if( !StringUtils.isEmpty(history.getProductInfo().getName()) ) {
					
					searchingqueryString.append(" and model.productInfo.name like :name");
					mapParams.put("name", "%" + history.getProductInfo().getName() + "%");
				}
			}
			if(!StringUtils.isEmpty(history.getActionName()) ) {
				
				searchingqueryString.append(" and model.actionName like :actionName");
				mapParams.put("actionName", "%" + history.getActionName() + "%");
			}
			// Dung chung cho ca nhap va xuat 
			if(history.getType() == 1 || history.getType() == 2) {
				
				searchingqueryString.append(" and model.type = :type");
				mapParams.put("type", history.getType());
			}
		}

		log.info("<<==>> historyDAO search all");
		return historyDAO.searchAll(searchingqueryString, mapParams);
	}
	
	/**
	 * > Cu khi nao co thay doi ben Invoice (nhap/xuat) thi se them 1 History moi
	 * 		khong quan trong ProductInfo, ProductInStock, ...
	 * 
	 * @param invoice
	 * @param actionName
	 */
	public void save(Invoice invoice, String actionName) {
		
		History history = new History();
		history.setActionName(actionName);
		history.setType(invoice.getType());
		history.setProductInfo(invoice.getProductInfo());
		history.setQuantity(invoice.getQuantity());
		history.setPrice(invoice.getPrice());
		history.setActiveFlag(1);
		history.setCreatedDate(new Date());
		history.setUpdatedDate(new Date());

		log.info("<<==>> historyDAO save new history");
		historyDAO.save(history);
	}

}
