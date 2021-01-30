package com.trungvan.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.trungvan.entity.Category;
import com.trungvan.service.CategoryService;
import com.trungvan.utils.Constant;
import com.trungvan.validator.CategoryValidator;

@Controller
@RequestMapping("/category")
public class CategoryController {
	
	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CategoryValidator categoryValidator;
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		
		// > Chu y: Do trong DB ta de createdDate va updatedDate la kieu TimeStamp tuong duong voi kieu Date() trong Java,
		//			nhung <form:hidden/> mac dinh luon tra ve kieu String nen trong @InitBinder ta phai convert tu kieu String
		//			tra ve tu cac the <form:hidden/> nay sang kieu Date() thi moi co the update trong DB duoc
		// > Dung de format string tu <form:hidden path="createdDate"/> va <form:hidden path="updatedDate"/> sang kieu TimeStamp
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

		if(binder.getTarget() == null) return;
		
		if(binder.getTarget().getClass() == Category.class) {
			
			binder.setValidator(categoryValidator);
		}
	}
	
	// Retrieve
	/**
	 * > Do trong endpoint /category/list ta tao san Form tim kiem nen khong can phai tao 1 endpoint khac
	 * 		de show Form
	 * > Do khi truy van binh thuong den /category/list su dung GET, nhung neu tim kiem Category thi truy
	 * 		van den /category/list su dung POST nen o day khong chi dinh cu the @GetMapping hay @PostMapping duoc
	 * 		ma phai su dung @RequestMapping va ke ca khi su dung @RequestMapping ta cung khong duoc phep
	 * 		chi ro la su dung method=GET hay method=POST
	 * 
	 * @param model
	 * @param session
	 * @param category
	 * @return
	 */
	@RequestMapping("/list")
	public String showCategoryList(Model model, HttpSession session,
			@ModelAttribute("categorySearchForm") Category category) {

		log.info("<<==>> categoryService search category list - categorySearchForm: " + category);
		
		if(session.getAttribute(Constant.MSG_SUCCESS) != null) {
			
			model.addAttribute(Constant.MSG_SUCCESS, session.getAttribute(Constant.MSG_SUCCESS));
			session.removeAttribute(Constant.MSG_SUCCESS);
		}
		
		if(session.getAttribute(Constant.MSG_FAILURE) != null) {
			
			model.addAttribute(Constant.MSG_FAILURE, session.getAttribute(Constant.MSG_FAILURE));
			session.removeAttribute(Constant.MSG_FAILURE);
		}
		
		// Do su dung kieu nay nen @ModelAttribute("categorySearchForm") Category category luon != null, vi the chi can 
		//		kiem tra cac truong can search co empty hay khong thoi la duoc
		if((category.getId() != null && category.getId() != 0)
			|| (category.getCode() != null && !category.getCode().isEmpty()) 
			|| (category.getName() != null && !category.getName().isEmpty())) {
			
			model.addAttribute("categories", categoryService.searchAll(category));
		} else {

			model.addAttribute("categories", categoryService.findAll());
		}
		
		return "categoryListView.definition";
	}
	
	@GetMapping("/view/{categoryId}")
	public String showSingleCategory(Model model, 
			@PathVariable("categoryId") int categoryId) {
		
		log.info("<<==>> categoryService show single category with id: " + categoryId);
		
		Category category = categoryService.findById(categoryId);
		
		// Van phai check vi co the user se nhap tu URL thay vi chon link trong Category List
		if(category != null) {

			model.addAttribute("formTitle", "View selected category");
			model.addAttribute("viewMode", true); // > Dung de cho phep user chi duoc xem hay khong
			model.addAttribute("categoryForm", category);
			
			return "categoryAction.definition";
		}
		return "redirect:/category/list";
	}
	
	// Create and Update
	@GetMapping("/add")
	public String showForm(Model model) {
		
		log.info("<<==>> categoryService show adding form");
		
		model.addAttribute("formTitle", "Add new category");
		model.addAttribute("viewMode", false); // > Dung de cho phep user chi duoc xem hay khong
		model.addAttribute("categoryForm", new Category());
		
		return "categoryAction.definition";
	}
	
	@GetMapping("/edit/{categoryId}")
	public String showForm(Model model, @PathVariable("categoryId") int categoryId) {
		
		log.info("<<==>> categoryService show updating form with id: " + categoryId);
		
		Category category = categoryService.findById(categoryId);
		
		// Van phai check vi co the user se nhap tu URL thay vi chon link trong Category List
		if(category != null) {

			model.addAttribute("formTitle", "Update selected category");
			model.addAttribute("viewMode", false); // > Dung de cho phep user chi duoc xem hay khong
			model.addAttribute("categoryForm", category);
			
			return "categoryAction.definition";
		}
		return "redirect:/category/list";
	}
	
	@PostMapping("/save")
	public String saveCategory(Model model, HttpSession session,
			@Validated @ModelAttribute("categoryForm") Category category,
			BindingResult bindingResult) {
		
		log.info("<<==>> categoryService save category - categoryForm: " + category);
		
		if(bindingResult.hasErrors()) {
			
			if(category.getId() != null && category.getId() != 0) {

				model.addAttribute("formTitle", "Update selected category");
			} else {
				
				model.addAttribute("formTitle", "Add new category");
			}
			model.addAttribute("viewMode", false); // > Dung de cho phep user chi duoc xem hay khong
			
			return "categoryAction.definition";
		}
		
		// TH su dung Form de update vi id != 0, null co nghia category da co trong DB
		if(category.getId() != null && category.getId() != 0) {
			
			try {
				categoryService.update(category);
				session.setAttribute(Constant.MSG_SUCCESS, "Update category successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Update category failed!");
				e.printStackTrace();
			}
		}
		// TH su dung Form de add
		else {

			try {
				categoryService.save(category);
				session.setAttribute(Constant.MSG_SUCCESS, "Add category successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Add category failed!");
				e.printStackTrace();
			}
		}
		return "redirect:/category/list"; 
	}
	
	// Delete
	@GetMapping("/delete/{categoryId}")
	public String deleteCategory(Model model, HttpSession session,
			@PathVariable("categoryId") int categoryId) {

		log.info("<<==>> categoryService delete category with id: " + categoryId);
		
		Category category = categoryService.findById(categoryId);
		if(category != null) {

			try {
				categoryService.delete(category);
				session.setAttribute(Constant.MSG_SUCCESS, "Delete category successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Delete category failed!");
				e.printStackTrace();
			}
		}
		return "redirect:/category/list";
	}
}
