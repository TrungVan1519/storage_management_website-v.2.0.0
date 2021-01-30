package com.trungvan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.trungvan.dao.ProductInStockDAO;
import com.trungvan.dto.Paging;
import com.trungvan.entity.Invoice;
import com.trungvan.entity.ProductInStock;
import com.trungvan.entity.ProductInfo;

@Service
public class ProductInStockService {
	
	private final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ProductInStockDAO<ProductInStock> productInStockDAO;
	
	public List<ProductInStock> findAll() {

		log.info("<<==>> productInStockDAO find all");
		return productInStockDAO.findAll();
	}
	
	public List<ProductInStock> paging(ProductInStock productInStock, Paging paging){
		
		StringBuilder searchingQueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		// Vi productInStock co 1 field tham chieu productInfo de search nen muon search phai check ca 2 
		if(productInStock != null && productInStock.getProductInfo() != null) {
			
			if(productInStock.getProductInfo().getCategory().getName() != null 
					&& !StringUtils.isEmpty(productInStock.getProductInfo().getCategory().getName())) {
				
				searchingQueryString.append(" and model.productInfo.category.name like :categoryName");
				mapParams.put("categoryName", "%" + productInStock.getProductInfo().getCategory().getName() + "%");
			}
			if(productInStock.getProductInfo().getCode() != null 
					&& !StringUtils.isEmpty(productInStock.getProductInfo().getCode())) {
		
				searchingQueryString.append(" and model.productInfo.code = :code");
				mapParams.put("code", productInStock.getProductInfo().getCode());
			}
			if(productInStock.getProductInfo().getName() != null 
					&& !StringUtils.isEmpty(productInStock.getProductInfo().getName()) ) {
			
				searchingQueryString.append(" and model.productInfo.name like :name");
				mapParams.put("name", "%" + productInStock.getProductInfo().getName() + "%");
			}
		}

		log.info("<<==>> productInStockDAO paging all");
		log.info(">> paging productInStock:" + productInStock);
		return productInStockDAO.paging(searchingQueryString, mapParams, paging);
	}
	
	public List<ProductInStock> searchAll(ProductInStock productInStock){
		
		StringBuilder searchingQueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		// Vi productInStock co 1 field tham chieu productInfo de search nen muon search phai check ca 2 
		if(productInStock != null && productInStock.getProductInfo() != null) {
			
			if(productInStock.getProductInfo().getCategory().getName() != null 
					&& !StringUtils.isEmpty(productInStock.getProductInfo().getCategory().getName())) {
				
				searchingQueryString.append(" and model.productInfo.category.name like :categoryName");
				mapParams.put("categoryName", "%" + productInStock.getProductInfo().getCategory().getName() + "%");
			}
			if(productInStock.getProductInfo().getCode() != null 
					&& !StringUtils.isEmpty(productInStock.getProductInfo().getCode())) {
		
				searchingQueryString.append(" and model.productInfo.code = :code");
				mapParams.put("code", productInStock.getProductInfo().getCode());
			}
			if(productInStock.getProductInfo().getName() != null 
					&& !StringUtils.isEmpty(productInStock.getProductInfo().getName()) ) {
			
				searchingQueryString.append(" and model.productInfo.name like :name");
				mapParams.put("name", "%" + productInStock.getProductInfo().getName() + "%");
			}
		}

		log.info("<<==>> productInStockDAO search all");
		return productInStockDAO.searchAll(searchingQueryString, mapParams);
	}
	
	/**
	 * > Dung khi nhap, xuat productInStock thi dong thoi cung thay doi luon invoice de tu do 
	 * 		cap nhap so luong productInStock
	 * > invoice chi xu ly 2 van de nhap productInStock va xuat productInStock thong qua prop type: 
	 * 		+ type = 1: nhap productInStock va invoice.getQuantity() > 0
	 * 		+ type = 2: xuat productInStock va invoice.getQuantity() < 0
	 * @param invoice
	 * @throws Exception
	 */
	public void saveOrUpdate(Invoice invoice) throws Exception{
		
		if(invoice.getProductInfo() != null) {
			
			int productInfoId = invoice.getProductInfo().getId();
			List<ProductInStock> productInStocks = productInStockDAO.findByProperty("productInfo.id", productInfoId);
			ProductInStock productInStock = null;
			
			// TH hang da ton tai trong ca ProductInfo List va trong stock roi
			if(productInStocks != null && !productInStocks.isEmpty()) {
				
				productInStock = productInStocks.get(0);
				productInStock.setUpdatedDate(new Date());
				
				// > Cap nhat so luong hang:
				//		+ product.getQuantity(): La so hang co san
				//		+ invoice.getQuantity(): La so hang se nhap/xuat tuong ung type = 1/2
				//			- TH nhap hang: invoice.getQuantity() > 0
				//			- TH xuat hang: invoice.getQuantity() < 0
				// > TH nhap hang: thi phai lam them 1 viec, cap nhat gia hang vi khi nhap hang vao stock
				//		thi gia tien cua hang ngoai thi truong co the thay doi theo gia tieu dung
				if(invoice.getType() == 1) {

					// Cap nhat gia
					productInStock.setPrice(invoice.getPrice());
					productInStock.setQuantity(productInStock.getQuantity() + invoice.getQuantity());

				} else if(invoice.getType() == 2) {

					productInStock.setQuantity(productInStock.getQuantity() - invoice.getQuantity());
				}

				log.info("<<==>> productInStockDAO update from stock quantity: " + invoice.getQuantity() + " and price: " + invoice.getPrice());
				productInStockDAO.update(productInStock);
			}
			
			// TH hang moi chi co trong ProductInfo List nhung chua co trong stock
			else {
				// Khi hang chua co trong stock thi khi nhap hang ta se them moi hang nay vao
				if (invoice.getType() == 1) {
					
					ProductInfo productInfo = new ProductInfo();
					productInfo.setId(invoice.getProductInfo().getId());

					productInStock = new ProductInStock();
					// Khi nhap hang vao stock ma hang nay chua co trong stock nhung da co trong ProductInfo List roi
					//		thi phai tham chieu den
					productInStock.setProductInfo(productInfo);
					// Cap nhat time va activeFlag
					productInStock.setActiveFlag(1);
					productInStock.setCreatedDate(new Date());
					productInStock.setUpdatedDate(new Date());
					// Cap nhat so luong va gia tien: Vi hang chua co trong stock nen khi nhap chac chan 100%
					//		productInStock.getQuantity() == 0
					productInStock.setQuantity(invoice.getQuantity());
					productInStock.setPrice(invoice.getPrice());

					log.info("<<==>> productInStockDAO insert to stock quantity: " + invoice.getQuantity() 
								+ " and price: " + invoice.getPrice());
					productInStockDAO.save(productInStock);
				}
			}
		}
	}
}
