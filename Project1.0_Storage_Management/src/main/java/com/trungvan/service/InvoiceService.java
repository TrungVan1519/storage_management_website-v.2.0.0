package com.trungvan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.trungvan.dao.InvoiceDAO;
import com.trungvan.dto.Paging;
import com.trungvan.entity.Invoice;
import com.trungvan.entity.ProductInfo;
import com.trungvan.utils.Constant;

@Service
public class InvoiceService {

	private final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ProductInStockService productInStockService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private InvoiceDAO<Invoice> invoiceDAO;
	
	public List<Invoice> findAll() {
		
		log.info("<<==>> invoiceDAO find all");
		return invoiceDAO.findAll();
	}

	public List<Invoice> paging(Invoice invoice, Paging paging) {
		
		StringBuilder searchingQueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		if (invoice != null) {
			
			if (invoice.getType() == 1 || invoice.getType() == 2) {
				
				searchingQueryString.append(" and model.type = :type");
				mapParams.put("type", invoice.getType());
			}
			if (!StringUtils.isEmpty(invoice.getCode())) {
				
				searchingQueryString.append(" and model.code = :code ");
				mapParams.put("code", invoice.getCode());
			}
			if(invoice.getFromDate() != null) {
				
				searchingQueryString.append(" and model.updatedDate >= :fromDate");
				mapParams.put("fromDate", invoice.getFromDate());
			}
			if(invoice.getToDate() != null) {
				
				searchingQueryString.append(" and model.updatedDate <= :toDate");
				mapParams.put("toDate", invoice.getToDate());
			}
		}

		log.info("<<==>> invoiceDAO paging all");
		log.info(">> paging invoice:" + invoice);
		return invoiceDAO.paging(searchingQueryString, mapParams, paging);
	}

	public List<Invoice> searchAll(Invoice invoice) {
		
		StringBuilder searchingQueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		if (invoice != null) {
			
			// Dung chung cho ca nhap va xuat 
			if (invoice.getType() == 1 || invoice.getType() == 2) {
				
				searchingQueryString.append(" and model.type=:type");
				mapParams.put("type", invoice.getType());
			}
			if (!StringUtils.isEmpty(invoice.getCode())) {
				
				searchingQueryString.append(" and model.code =:code ");
				mapParams.put("code", invoice.getCode());
			}
			
			// Tim kiem theo ngay, thang update
			if(invoice.getFromDate() != null) {
				
				searchingQueryString.append(" and model.updatedDate >= :fromDate");
				mapParams.put("fromDate", invoice.getFromDate());
			}
			if(invoice.getToDate() != null) {
				
				searchingQueryString.append(" and model.updatedDate <= :toDate");
				mapParams.put("toDate", invoice.getToDate());
			}
		}

		log.info("<<==>> invoiceDAO search all");
		return invoiceDAO.searchAll(searchingQueryString, mapParams);
	}

	public List<Invoice> findByProperty(String property, Object value) {
		
		log.info("<<==>> invoiceDAO find by property: " + property + "\tvalue: " + value.toString());
		return invoiceDAO.findByProperty(property, value);
	}

	public void save(Invoice invoice) throws Exception {
		
		invoice.setActiveFlag(1);
		invoice.setCreatedDate(new Date());
		invoice.setUpdatedDate(new Date());
		
		invoiceDAO.save(invoice);
		
		// Luu vao lich su
		historyService.save(invoice, Constant.ACTION_ADD);
		
		// Cap nhat ve so luong, loai hang hoa, price ... trong stock
		productInStockService.saveOrUpdate(invoice);
	}

	public void update(Invoice invoice) throws Exception {
		
		ProductInfo productInfo = new ProductInfo();
		productInfo.setId(invoice.getProductInfo().getId());

		invoice.setProductInfo(productInfo);
		invoice.setUpdatedDate(new Date());
		
		// Lay ra so luong hang hoa hien tai co trong stock
		int oldQuantity = invoiceDAO.findById(Invoice.class, invoice.getId()).getQuantity();
		
		// Lay ra invoice nhap tu View vao va cap nhat lai so luong va luu lai invoice cu~
		Invoice newInvoice = new Invoice();
		newInvoice.setProductInfo(invoice.getProductInfo());
		newInvoice.setQuantity(invoice.getQuantity() - oldQuantity);
		newInvoice.setPrice(invoice.getPrice());

		// Luu vao invoice
		invoiceDAO.update(invoice);
		
		// Luu vao lich su
		historyService.save(invoice, Constant.ACTION_EDIT);
		
		// Cap nhat ve so luong, loai hang hoa, ... trong stock
		productInStockService.saveOrUpdate(newInvoice);
	}
	
	public void delete(Invoice invoice) throws Exception {
		
		// Do khi xoa ta khong xoa han object ma chi set activeFlag = 0
		invoice.setActiveFlag(0);
		invoice.setUpdatedDate(new Date());
		
		log.info("<<==>> invoiceDAO delete invoice (activeFlag = 0):" + invoice);
		invoiceDAO.update(invoice);
	}
}
