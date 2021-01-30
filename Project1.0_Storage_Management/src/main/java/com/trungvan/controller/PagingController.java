package com.trungvan.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trungvan.dto.Paging;
import com.trungvan.entity.Category;
import com.trungvan.entity.History;
import com.trungvan.entity.Invoice;
import com.trungvan.entity.ProductInStock;
import com.trungvan.entity.ProductInfo;
import com.trungvan.service.CategoryService;
import com.trungvan.service.HistoryService;
import com.trungvan.service.InvoiceService;
import com.trungvan.service.ProductInStockService;
import com.trungvan.service.ProductInfoService;
import com.trungvan.utils.Constant;

@Controller
@RequestMapping("/paging")
public class PagingController {
	
	private final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductInfoService productInfoService;
	
	@Autowired
	private ProductInStockService productInStockService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private InvoiceService invoiceService;
	
	
	// Retrieve paging category list
	@RequestMapping(value = { "/category/list", "/category/list/" })
	public String redirectToCategoryList() {
		
		return "redirect:/paging/category/list/1";
	}

	@RequestMapping("/category/list/{page}")
	public String pagingCategoryList(Model model, 
			@ModelAttribute("categorySearchForm") Category category, 
			@PathVariable("page") int page) {

		// Do su dung co che paging nen "categorySearchForm" o trong <form:form/> khong mapped voi "categorySearchForm" trong Controller duoc,
		//		vi vay "categorySearchForm" chac chan == mac dinh Category(id=null, name=null, code=null, description=null, activeFlag=0, createdDate=null, updatedDate=null, productInfos=[])
		//		nen ta se su dung cach servletRelativeAction="/category/list" thay vi servletRelativeAction="/paging/category/list"
		log.info("<<==>> categoryService paging category list - categorySearchForm: " + category);
		
		Paging paging = new Paging(5);
		paging.setIndexPage(page);	// > Lay ra page hien tai de tinh offset
		
		model.addAttribute("pageInfo", paging);
		model.addAttribute("categories", categoryService.paging(category, paging));
		
		return "pagingCategoryListView.definition";
	}
	
	// Retrieve paging productInfo list
	@RequestMapping(value = { "/product-info/list", "/product-info/list/" })
	public String redirectToProductInfoList() {
		
		return "redirect:/paging/product-info/list/1";
	}
	
	@RequestMapping("/product-info/list/{page}")
	public String pagingProductInfoList(Model model, 
			@ModelAttribute("productInfoSearchForm") ProductInfo productInfo,
			@PathVariable("page") int page) {
		
		// Do su dung co che paging nen "productInfoSearchForm" o trong <form:form/> khong mapped voi "productInfoSearchForm" trong Controller duoc,
		//		vi vay "productInfoSearchForm" chac chan == mac dinh ProductInfo(id=null, name=null, code=null, description=null, activeFlag=0, createdDate=null, updatedDate=null, ...)
		//		nen ta se su dung cach servletRelativeAction="/product-info/list" thay vi servletRelativeAction="/paging/product-info/list"
		log.info("<<==>> productInfoService paging productInfo list - productInfoSearchForm: " + productInfo);
		
		Paging paging = new Paging(5);
		paging.setIndexPage(page);	// > Lay ra page hien tai de tinh offset

		model.addAttribute("pageInfo", paging);
		model.addAttribute("productInfos", productInfoService.paging(productInfo, paging));
		
		return "pagingProductInfoListView.definition";
	}
	
	// Retieve paging productInStock list
	@GetMapping(value = {"/product-in-stock/list","/product-in-stock/list/"})
	public String redirectToProductInStockList() {
		
		return "redirect:/paging/product-in-stock/list/1";
	}
	
	@RequestMapping("/product-in-stock/list/{page}")
	public String showProductInStockList(Model model, 
			@ModelAttribute("productInStockSearchForm") ProductInStock productInStock,
			@PathVariable("page") int page) {
		
		// Do su dung co che paging nen "productInStockSearchForm" o trong <form:form/> khong mapped voi "productInStockSearchForm" trong Controller duoc,
		//		vi vay "productInStockSearchForm" chac chan == mac dinh ProductInStock(id=null, name=null, code=null, description=null, activeFlag=0, createdDate=null, updatedDate=null, ...)
		//		nen ta se su dung cach servletRelativeAction="/product-in-stock/list" thay vi servletRelativeAction="/paging/product-in-stock/list"
		log.info("<<==>> productInStockService paging productInStock list - productInStockSearchForm: " + productInStock);

		Paging paging = new Paging(3);
		paging.setIndexPage(page);	// > Lay ra page hien tai de tinh offset

		model.addAttribute("pageInfo", paging);
		model.addAttribute("productInStocks", productInStockService.paging(productInStock, paging));
		
		return "pagingProductInStockView.definition";
	}
	
	// Retrieve paging history list
	@GetMapping(value = {"/history/list","/history/list/"})
	public String redirectToHistoryList() {
		return "redirect:/paging/history/list/1";
	}
	
	@RequestMapping("/history/list/{page}")
	public String showHistoryList(Model model,
			@ModelAttribute("historySearchForm") History history,
			@PathVariable("page") int page) {
		
		// Do su dung co che paging nen "historySearchForm" o trong <form:form/> khong mapped voi "historySearchForm" trong Controller duoc,
		//		vi vay "historySearchForm" chac chan == mac dinh History(id=null, name=null, code=null, description=null, activeFlag=0, createdDate=null, updatedDate=null, ...)
		//		nen ta se su dung cach servletRelativeAction="/history/list" thay vi servletRelativeAction="/paging/history/list"
		log.info("<<==>> historyService paging history list - historySearchForm: " + history);
		
		Map<String,String> mapType = new HashMap<>();
		mapType.put(String.valueOf(Constant.TYPE_ALL), "All");
		mapType.put(String.valueOf(Constant.TYPE_GOODS_RECEIPT), "Goods Receipt");
		mapType.put(String.valueOf(Constant.TYPE_GOODS_ISSUES), "Goods Issues");
		model.addAttribute("mapType", mapType);

		Paging paging = new Paging(5);
		paging.setIndexPage(page);	// > Lay ra page hien tai de tinh offset

		model.addAttribute("pageInfo", paging);
		model.addAttribute("histories", historyService.paging(history, paging));
		
		return "pagingHistoryListView.definition";
	}
	
	// Retrieve paging invoice list
	@RequestMapping(value= {"/goods-receipt/list","/goods-receipt/list/"})
	public String redirectToInvoiceList() {
		
		return "redirect:/paging/goods-receipt/list/1";
	}
	
	@RequestMapping(value="/goods-receipt/list/{page}")
	public String showInvoiceList(Model model, HttpSession session,
			@ModelAttribute("invoiceSearchForm") Invoice invoice,
			@PathVariable("page") int page) {
		
		// Do su dung co che paging nen "invoiceSearchForm" o trong <form:form/> khong mapped voi "invoiceSearchForm" trong Controller duoc,
		//		vi vay "invoiceSearchForm" chac chan == mac dinh History(id=null, name=null, code=null, description=null, activeFlag=0, createdDate=null, updatedDate=null, ...)
		//		nen ta se su dung cach servletRelativeAction="/history/list" thay vi servletRelativeAction="/paging/history/list"
		log.info("<<==>> invoiceService paging invoice list - invoiceSearchForm: " + invoice);
		
		if(invoice==null) {
			
			invoice = new Invoice();
		}
		invoice.setType(Constant.TYPE_GOODS_RECEIPT);
		
		Paging paging = new Paging(5);
		paging.setIndexPage(page);	// > Lay ra page hien tai de tinh offset
		
		model.addAttribute("pageInfo", paging);
		model.addAttribute("invoices", invoiceService.paging(invoice, paging));
		
		return "pagingGoodReceiptListView.definition";
	}
}
