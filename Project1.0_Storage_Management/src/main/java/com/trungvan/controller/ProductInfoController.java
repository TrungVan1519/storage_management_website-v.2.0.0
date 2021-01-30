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
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trungvan.entity.Category;
import com.trungvan.entity.ProductInfo;
import com.trungvan.service.CategoryService;
import com.trungvan.service.ProductInfoService;
import com.trungvan.utils.Constant;
import com.trungvan.validator.ProductInfoValidator;

@Controller
@RequestMapping("/product-info")
public class ProductInfoController {
	
	private final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductInfoService productInfoService;
	
	@Autowired
	private ProductInfoValidator productInfoValidator;
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		
		// > Chu y: Do trong DB ta de createdDate va updatedDate la kieu TimeStamp tuong duong voi kieu Date() trong Java,
		//			nhung <form:hidden/> mac dinh luon tra ve kieu String nen trong @InitBinder ta phai convert tu kieu String
		//			tra ve tu cac the <form:hidden/> nay sang kieu Date() thi moi co the update trong DB duoc
		// > Dung de format string tu <form:hidden path="createdDate"/> va <form:hidden path="updatedDate"/> sang kieu TimeStamp
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

		if(binder.getTarget() == null) return;
		
		if(binder.getTarget().getClass() == ProductInfo.class) {
			
			binder.setValidator(productInfoValidator);
		}
	}
	
	// Retrieve
	/**
	 * > Do trong endpoint /product-info/list ta tao san Form tim kiem nen khong can phai tao 1 endpoint khac
	 * 		de show Form
	 * > Do khi truy van binh thuong den /product-info/list su dung GET, nhung neu tim kiem productInfo thi truy
	 * 		van den /product-info/list su dung POST nen o day khong chi dinh cu the @GetMapping hay @PostMapping duoc
	 * 		ma phai su dung @RequestMapping va ke ca khi su dung @RequestMapping ta cung khong duoc phep
	 * 		chi ro la su dung method=GET hay method=POST
	 * 
	 * @param model
	 * @param session
	 * @param productInfo
	 * @return
	 */
	@RequestMapping("/list")
	public String showProductInfoList(Model model, HttpSession session,
			@ModelAttribute("productInfoSearchForm") ProductInfo productInfo) {
		
		log.info("<<==>> productInfoService search productInfo list - productInfoSearchForm:" + productInfo);
		
		if(session.getAttribute(Constant.MSG_SUCCESS) != null) {
			
			model.addAttribute(Constant.MSG_SUCCESS, session.getAttribute(Constant.MSG_SUCCESS));
			session.removeAttribute(Constant.MSG_SUCCESS);
		}
		
		if(session.getAttribute(Constant.MSG_FAILURE) != null) {
			
			model.addAttribute(Constant.MSG_FAILURE, session.getAttribute(Constant.MSG_FAILURE));
			session.removeAttribute(Constant.MSG_FAILURE);
		}
		
		// Do su dung kieu nay nen @ModelAttribute("productInfoSearchForm") ProductInfo productInfo luon != null, vi the chi can 
		//		kiem tra cac truong can search co empty hay khong thoi la duoc
		if((productInfo.getId() != null && productInfo.getId() != 0) 
			|| (productInfo.getCode() != null && !productInfo.getCode().isEmpty()) 
			|| (productInfo.getName() != null && !productInfo.getName().isEmpty())) {
			
			model.addAttribute("productInfos", productInfoService.searchAll(productInfo));
		} else {

			model.addAttribute("productInfos", productInfoService.findAll());
		}
		
		return "productInfoListView.definition";
		
	}
	
	@GetMapping("/view/{productInfoId}")
	public String showSingleProductInfo(Model model, 
			@PathVariable("productInfoId") int productInfoId) {
		
		log.info("<<==>> productInfoService show single productInfo with id: " + productInfoId);
		
		ProductInfo productInfo = productInfoService.findById(productInfoId);
		
		// Van phai check vi co the productInfo se nhap tu URL thay vi chon link trong ProductInfo List
		if(productInfo != null) {

			model.addAttribute("formTitle", "View selected productInfo");
			model.addAttribute("viewMode", true); // > Dung de cho phep user chi duoc xem hay khong
			model.addAttribute("productInfoForm", productInfo);
			
			return "productInfoAction.definition";
		}
		return "redirect:/product-info/list";
	}
	
	// Create and Update
	@GetMapping("/add")
	public String showForm(Model model) {
		
		log.info("<<==>> productInfoService show adding form");
		
		model.addAttribute("formTitle", "Add new productInfo");
		model.addAttribute("viewMode", false); // > Dung de cho phep user chi duoc xem hay khong
		model.addAttribute("productInfoForm", new ProductInfo());
		
		// Truyen Category List sang cho <form:options items="${mapCategories }"/>
		List<Category> categories = categoryService.findAll();
		Map<String, String> mapCategories = new HashMap<>();
		for(Category category : categories) {
			
			mapCategories.put(String.valueOf(category.getId()), category.getName());
		}
		model.addAttribute("mapCategories", mapCategories);
		
		return "productInfoAction.definition";
	}
	
	@GetMapping("/edit/{productInfoId}")
	public String showForm(Model model, @PathVariable("productInfoId") int productInfoId) {
		
		log.info("<<==>> productInfoService show updating form with id: " + productInfoId);
		
		ProductInfo productInfo = productInfoService.findById(productInfoId);
		
		// Van phai check vi co the user se nhap tu URL thay vi chon link trong Category List
		if(productInfo != null) {

			// Thiet lap category da co san trong productInfo de hien thi trong Form Edit
			productInfo.getCategory().setId(productInfo.getCategory().getId());
			System.out.println(">> /edit - category tuong ung:" + productInfo.getCategory());
			
			model.addAttribute("formTitle", "Update selected productInfo");
			model.addAttribute("viewMode", false); // > Dung de cho phep user chi duoc xem hay khong
			model.addAttribute("productInfoForm", productInfo);

			// Truyen Category List sang cho <form:options items="${mapCategories }"/>
			List<Category> categories = categoryService.findAll();
			Map<String, String> mapCategories = new HashMap<>();
			for(Category category : categories) {
				
				mapCategories.put(String.valueOf(category.getId()), category.getName());
			}
			model.addAttribute("mapCategories", mapCategories);
			
			return "productInfoAction.definition";
		}
		return "redirect:/product-info/list";
	}
	
	@PostMapping("/save")
	public String saveProductInfo(Model model, HttpSession session,
			@Validated @ModelAttribute("productInfoForm") ProductInfo productInfo,
			BindingResult bindingResult) {
		
		log.info("<<==>> productInfoService save productInfo - productInfoForm: " + productInfo);
		System.out.println(">> /save - category tuong ung:" + productInfo.getCategory());
		
		if(bindingResult.hasErrors()) {
			
			log.info("<<==>> BindingResult List:");
			for(ObjectError e: bindingResult.getAllErrors()) {

				log.info(">> bindingResult.e: " + e);
			}
			
			if(productInfo.getId() != null && productInfo.getId() != 0) {

				model.addAttribute("formTitle", "Update selected productInfo");
			} else {
				
				model.addAttribute("formTitle", "Add new productInfo");
			}
			model.addAttribute("viewMode", false); // > Dung de cho phep user chi duoc xem hay khong

			// Truyen Category List sang cho <form:options items="${mapCategories }"/>
			List<Category> categories = categoryService.findAll();
			Map<String, String> mapCategories = new HashMap<>();
			for(Category category : categories) {
				
				mapCategories.put(String.valueOf(category.getId()), category.getName());
			}
			model.addAttribute("mapCategories", mapCategories);
			
			return "productInfoAction.definition";
		}
		
		// TH su dung Form de update vi id != 0, null co nghia productInfo da co trong DB
		if(productInfo.getId() != null && productInfo.getId() != 0) {
			
			try {
				productInfoService.update(productInfo);
				session.setAttribute(Constant.MSG_SUCCESS, "Update productInfo successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Update productInfo failed!");
				e.printStackTrace();
			}
		}
		// TH su dung Form de add
		else {

			try {
				productInfoService.save(productInfo);
				session.setAttribute(Constant.MSG_SUCCESS, "Add productInfo successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Add productInfo failed!");
				e.printStackTrace();
			}
		}
		return "redirect:/product-info/list"; 
	}
	
	// Delete
	@GetMapping("/delete/{productInfoId}")
	public String deleteProductInfo(Model model, HttpSession session,
			@PathVariable("productInfoId") int productInfoId) {

		log.info(">> productInfoService delete productInfo with id: " + productInfoId);
		
		ProductInfo productInfo = productInfoService.findById(productInfoId);
		if(productInfo != null) {

			try {
				productInfoService.delete(productInfo);
				session.setAttribute(Constant.MSG_SUCCESS, "Delete productInfo successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Delete productInfo failed!");
				e.printStackTrace();
			}
		}
		return "redirect:/product-info/list";
	}
}
