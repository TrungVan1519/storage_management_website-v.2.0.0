package com.trungvan.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.trungvan.entity.Invoice;
import com.trungvan.utils.Constant;
import com.trungvan.utils.DateUtil;

public class InvoiceReport extends AbstractXlsxView{

	/**
	 * > Cac param truyen vao ham va tac dung:
	 * 		+ model: la object truyen vao ModelAndView ben Controller: invoiceService.searchAll(invoice) 
	 * 		+ workbook: la object giup tao file excel
	 */
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		List<Invoice> invoices =(List<Invoice>) model.get(Constant.KEY_GOODS_RECEIPT_REPORT);
		
		// Tao ten file tuy theo nhap/xuat hang trong invoice
		String fileName = "";
		if(invoices.get(0).getType() == Constant.TYPE_GOODS_RECEIPT) {
			
			fileName = "goods-receipt-" + System.currentTimeMillis() + ".xlsx";
			
		} else if(invoices.get(0).getType() == Constant.TYPE_GOODS_ISSUES) {

			fileName = "goods-issues-" + System.currentTimeMillis() + ".xlsx";
		}
		
		// Tao file tra ve
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		
		// Tao 1 sheet
		Sheet sheet = workbook.createSheet("data");
		
		// Tao 1 row header trong sheet da tao tuong ung vi tri 1
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("#");
		header.createCell(1).setCellValue("Code");
		header.createCell(2).setCellValue("Quantity");
		header.createCell(3).setCellValue("Price");
		header.createCell(4).setCellValue("Product");
		header.createCell(5).setCellValue("Update date");
		
		// Tao cac row content tuong ung voi invoiceService.searchAll(invoice) da truyen ben Controller
		//		tuong ung vi tri 2 tro di
		int rownum = 1;
		for(Invoice invoice : invoices) {
			
			Row row = sheet.createRow(rownum++);
			row.createCell(0).setCellValue(rownum - 1);
			row.createCell(1).setCellValue(invoice.getCode());
			row.createCell(2).setCellValue(invoice.getQuantity());
			row.createCell(3).setCellValue(invoice.getPrice().toString());
			row.createCell(4).setCellValue(invoice.getProductInfo().getName());
			row.createCell(5).setCellValue(DateUtil.dateToString(invoice.getUpdatedDate()));
		}
	}
}
