package com.trungvan.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.trungvan.entity.Invoice;
import com.trungvan.entity.ProductInfo;
import com.trungvan.service.InvoiceReport;
import com.trungvan.service.InvoiceService;
import com.trungvan.service.ProductInfoService;
import com.trungvan.utils.Constant;
import com.trungvan.validator.InvoiceValidator;

@Controller
@RequestMapping("/goods-receipt")
public class GoodsReceiptController {

	private final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ProductInfoService productInfoService;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private InvoiceValidator invoiceValidator;
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		
		// > Chu y: Do trong DB ta de createdDate va updatedDate la kieu TimeStamp tuong duong voi kieu Date() trong Java,
		//			nhung <form:hidden/> mac dinh luon tra ve kieu String nen trong @InitBinder ta phai convert tu kieu String
		//			tra ve tu cac the <form:hidden/> nay sang kieu Date() thi moi co the update trong DB duoc
		// > Dung de format string tu <form:hidden path="createdDate"/> va <form:hidden path="updatedDate"/> sang kieu TimeStamp
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

		if(binder.getTarget() == null) return;
		
		if(binder.getTarget().getClass()== Invoice.class) {
			
			binder.setValidator(invoiceValidator);
		}
	}


	
	// Retrieve
	/**
	 * > Do trong endpoint /goods-receipt/list ta tao san Form tim kiem nen khong can phai tao 1 endpoint khac
	 * 		de show Form
	 * > Do khi truy van binh thuong den /goods-receipt/list su dung GET, nhung neu tim kiem productInfo thi truy
	 * 		van den /goods-receipt/list su dung POST nen o day khong chi dinh cu the @GetMapping hay @PostMapping duoc
	 * 		ma phai su dung @RequestMapping va ke ca khi su dung @RequestMapping ta cung khong duoc phep
	 * 		chi ro la su dung method=GET hay method=POST
	 * 
	 * @param model
	 * @param session
	 * @param invoice
	 * @return
	 */
	@RequestMapping("/list")
	public String showInvoiceList(Model model, HttpSession session,
			@ModelAttribute("invoiceSearchForm") Invoice invoice) {
		
		log.info("<<==>> invoiceService search invoice list - invoiceSearchForm: " + invoice);
		
		if(session.getAttribute(Constant.MSG_SUCCESS) != null) {
			
			model.addAttribute(Constant.MSG_SUCCESS, session.getAttribute(Constant.MSG_SUCCESS));
			session.removeAttribute(Constant.MSG_SUCCESS);
		}
		
		if(session.getAttribute(Constant.MSG_FAILURE) != null) {
			
			model.addAttribute(Constant.MSG_FAILURE, session.getAttribute(Constant.MSG_FAILURE));
			session.removeAttribute(Constant.MSG_FAILURE);
		}

		if(invoice==null) {
			
			invoice = new Invoice();
		}
		invoice.setType(Constant.TYPE_GOODS_RECEIPT);
		model.addAttribute("invoices", invoiceService.searchAll(invoice));
		
		return "invoiceListView.definition";
	}
	
	@GetMapping("/view/{invoiceId}")
	public String showSingleInvoice(Model model,
			@PathVariable("invoiceId") int invoiceId) {
		
		log.info("<<==>> invoiceService show single invoice with id: " + invoiceId);
		
		Invoice invoice = invoiceService.findByProperty("id", invoiceId).get(0);
		
		// Van phai check vi co the user se nhap tu URL thay vi chon link trong Category List
		if(invoice != null) {
			
			model.addAttribute("formTitle", "View selected invoice");
			model.addAttribute("viewMode", true);
			model.addAttribute("invoiceForm", invoice);
			
			return "invoiceAction.definition";
		}
		return "redirect:/goods-receipt/list";
	}
	
	// Create and Update
	@GetMapping("/add")
	public String showForm(Model model) {
		
		log.info("<<==>> invoiceService show adding form");
		
		model.addAttribute("formTitle", "Add new invoice");
		model.addAttribute("viewMode", false);
		model.addAttribute("invoiceForm", new Invoice());
		
		model.addAttribute("mapProduct", initMapProduct());
		
		return "invoiceAction.definition";
	}
	
	@GetMapping("/edit/{invoiceId}")
	public String showForm(Model model,
			@PathVariable("invoiceId") int invoiceId) {
		
		log.info("<<==>> invoiceService show updating form with id: " + invoiceId);
		
		Invoice invoice = invoiceService.findByProperty("id", invoiceId).get(0);
		
		if(invoice != null) {
			
			model.addAttribute("formTitle", "Update selected invoice");
			model.addAttribute("viewMode", false);
			model.addAttribute("invoiceForm", invoice);
			
			model.addAttribute("mapProduct", initMapProduct());
			
			return "invoiceAction.definition";
		}
		return "redirect:/goods-receipt/list";
	}
	
	@PostMapping("/save")
	public String save(Model model, HttpSession session,
			@Validated @ModelAttribute("invoiceForm") Invoice invoice,
			BindingResult bindingResult) {
		
		log.info("<<==>> invoiceService save invoice - invoiceForm: " + invoice);
		
		if(bindingResult.hasErrors()) {
			
			if(invoice.getId() != null && invoice.getId() != 0) {
				
				model.addAttribute("formTitle", "Update selected invoice");
			}else {
				
				model.addAttribute("formTitle", "Add new invoice");
			}
			model.addAttribute("viewMode", false);
			
			model.addAttribute("mapProduct", initMapProduct());
			
			return "invoiceAction.definition";
		}
		
		// Day la Controller nhap hang nen bat buoc phai them cai nay
		invoice.setType(Constant.TYPE_GOODS_RECEIPT);
		
		// TH update hang da co trong stock
		if(invoice.getId() != null && invoice.getId() != 0) {
			
			try {
				invoiceService.update(invoice);
				session.setAttribute(Constant.MSG_SUCCESS, "Update invoice successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Update invoice failed!");
				e.printStackTrace();
			}
		} 
		// TH them moi hang chua co trong stock
		else {
			
			try {
				invoiceService.save(invoice);
				session.setAttribute(Constant.MSG_SUCCESS, "Update invoice successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Update invoice failed!");
				e.printStackTrace();
			}
		}
		return "redirect:/goods-receipt/list";
	}
	
	// Delete
	@GetMapping("/delete/{invoiceId}")
	public String deleteInvoice(Model model, HttpSession session,
			@PathVariable("invoiceId") int invoiceId) {

		log.info("<<==>> invoiceService delete invoice with id: " + invoiceId);
		
		Invoice invoice = invoiceService.findByProperty("id", invoiceId).get(0);
		
		if(invoice != null) {

			try {
				invoiceService.delete(invoice);
				session.setAttribute(Constant.MSG_SUCCESS, "Delete category successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Delete category failed!");
				e.printStackTrace();
			}
		}
		return "redirect:/goods-receipt/list";
	}
	
	/**
	 * > Phai su dung ModelAndView, no se tra ve luon du lieu
	 * 
	 * @return
	 */
	@GetMapping("/export")
	public ModelAndView exportReport() {
		
		// Object invoice nay tao ra de dua vao searchAll lay ra cac invoice nhap hang thoi, vi vay ma ta 
		//		chi can invoice.setType(Constant.TYPE_GOODS_RECEIPT), con cac field thi khong quan trong
		Invoice invoice = new Invoice();
		invoice.setType(Constant.TYPE_GOODS_RECEIPT);
		
		// Truyen du lieu vao va xuat file excel
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(Constant.KEY_GOODS_RECEIPT_REPORT, invoiceService.searchAll(invoice));
		modelAndView.setView(new InvoiceReport());
		
		return modelAndView;
	}
	
	private Map<String,String> initMapProduct(){
		
		List<ProductInfo> productInfos = productInfoService.findAll();
		Map<String, String> mapProduct = new HashMap<>();
		for(ProductInfo productInfo : productInfos) {
			
			mapProduct.put(productInfo.getId().toString(), productInfo.getName());
		}
		return mapProduct;
	}
}
