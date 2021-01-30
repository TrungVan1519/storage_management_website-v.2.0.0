package com.trungvan.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trungvan.entity.ProductInStock;
import com.trungvan.service.ProductInStockService;

@Controller
@RequestMapping("/product-in-stock")
public class ProductInStockController {

	private final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ProductInStockService productInStockService;

	// Retrieve
	/**
	 * > Do trong endpoint /product-in-stock/list ta tao san Form tim kiem nen khong can phai tao 1 endpoint khac
	 * 		de show Form
	 * > Do khi truy van binh thuong den /product-in-stock/list su dung GET, nhung neu tim kiem ProductInStock thi truy
	 * 		van den /product-in-stock/list su dung POST nen o day khong chi dinh cu the @GetMapping hay @PostMapping duoc
	 * 		ma phai su dung @RequestMapping va ke ca khi su dung @RequestMapping ta cung khong duoc phep
	 * 		chi ro la su dung method=GET hay method=POST
	 * 
	 * @param model
	 * @param session
	 * @param productInStock
	 * @return
	 */
	@RequestMapping("/list")
	public String showProductInStockList(Model model,
			@ModelAttribute("productInStockSearchForm") ProductInStock productInStock) {

		log.info("<<==>> productInStockService search productInStock list - productInStockSearchForm: " + productInStock);
		boolean searchingMode = false;
		
		// Do su dung kieu nay nen @ModelAttribute("productInStockSearchForm") ProductInStock productInStock luon != null, vi the chi can 
		//		kiem tra cac truong can search co empty hay khong thoi la duoc
		if(productInStock != null && productInStock.getProductInfo() != null) {
			
			if(productInStock.getProductInfo().getCategory().getName() != null 
					&& !StringUtils.isEmpty(productInStock.getProductInfo().getCategory().getName())) {
				
				searchingMode = true;
			}
			if(productInStock.getProductInfo().getCode() != null 
					&& !StringUtils.isEmpty(productInStock.getProductInfo().getCode())) {

				searchingMode = true;
			}
			if(productInStock.getProductInfo().getName() != null 
					&& !StringUtils.isEmpty(productInStock.getProductInfo().getName()) ) {

				searchingMode = true;
			}
		}
		
		if(searchingMode) {
			
			model.addAttribute("productInStocks", productInStockService.searchAll(productInStock));
		} else {

			model.addAttribute("productInStocks", productInStockService.findAll());
		}
		
		return "productInStockView.definition";
	}
}
