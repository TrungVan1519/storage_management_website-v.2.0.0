package com.trungvan.validator;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.trungvan.entity.Invoice;
import com.trungvan.service.InvoiceService;

@Component
public class InvoiceValidator implements Validator {
	@Autowired
	private InvoiceService invoiceService;

	@Override
	public boolean supports(Class<?> clazz) {
		
		return clazz == Invoice.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Invoice invoice = (Invoice) target;
		
		ValidationUtils.rejectIfEmpty(errors, "code", "msg.required");
		ValidationUtils.rejectIfEmpty(errors, "quantity", "msg.required");
		ValidationUtils.rejectIfEmpty(errors, "price", "msg.required");
		
		// Khong duoc de code trung nhau
		if (invoice.getCode() != null && !invoice.getCode().isEmpty()) {
			
			List<Invoice> invoices = invoiceService.findByProperty("code", invoice.getCode());
			if (invoices != null && !invoices.isEmpty()) {
				
				if (invoice.getId() != null && invoice.getId() != 0) {
					
					if (invoices.get(0).getId() != invoice.getId()) {
						
						errors.rejectValue("code", "msg.code.existing");
					}
				} else {
					
					errors.rejectValue("code", "msg.code.existing");
				}
			}
		}
		
		// Khong duoc nhap so luong <= 0
		if (invoice.getQuantity() <= 0) {
			
			errors.rejectValue("quantity", "msg.wrong.format");
		}
		
		// Khong duoc nhap price <= 0
		if (invoice.getPrice() != null && invoice.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			
			errors.rejectValue("price", "msg.wrong.format");
		}
		
		// Neu nhap ca 2 field search fromDate va toDate (TH muon tim kiem field trong 1 khoang)
		//		thi khong duoc nhap fromDate > toDate
		// Vi fromDate va toDate la kieu Date nen khong su dung "<" duoc ma su dung after()
		if (invoice.getFromDate() != null && invoice.getToDate() != null) {
			
			if (invoice.getFromDate().after(invoice.getToDate())) {
				
				errors.rejectValue("fromDate", "msg.wrong.date");
			}
		}

	}

}
