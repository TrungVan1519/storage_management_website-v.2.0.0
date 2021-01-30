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

import com.trungvan.entity.Role;
import com.trungvan.entity.User;
import com.trungvan.entity.UserRole;
import com.trungvan.service.RoleService;
import com.trungvan.service.UserService;
import com.trungvan.utils.Constant;
import com.trungvan.validator.UserValidator;


@Controller
@RequestMapping("/user")
public class UserController {

	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserValidator userValidator;
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		
		// > Chu y: Do trong DB ta de createdDate va updatedDate la kieu TimeStamp tuong duong voi kieu Date() trong Java,
		//			nhung <form:hidden/> mac dinh luon tra ve kieu String nen trong @InitBinder ta phai convert tu kieu String
		//			tra ve tu cac the <form:hidden/> nay sang kieu Date() thi moi co the update trong DB duoc
		// > Dung de format string tu <form:hidden path="createdDate"/> va <form:hidden path="updatedDate"/> sang kieu TimeStamp
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		if(binder.getTarget() == null) return;
		
		if(binder.getTarget().getClass() == User.class) {
			
			binder.setValidator(userValidator);
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
	@RequestMapping(value="/list")
	public String showUserList(Model model, HttpSession session,
			@ModelAttribute("userSearchForm") User user) {

		log.info("<<==>> userService search user list - userSearchForm: " + user);
		
		if(session.getAttribute(Constant.MSG_SUCCESS) != null) {
			
			model.addAttribute(Constant.MSG_SUCCESS, session.getAttribute(Constant.MSG_SUCCESS));
			session.removeAttribute(Constant.MSG_SUCCESS);
		}
		if(session.getAttribute(Constant.MSG_FAILURE) != null) {
			
			model.addAttribute(Constant.MSG_FAILURE, session.getAttribute(Constant.MSG_FAILURE));
			session.removeAttribute(Constant.MSG_FAILURE);
		}
		
		if((user.getUsername() != null && !user.getUsername().isEmpty())
			|| (user.getName() != null && !user.getName().isEmpty())) {
			
			model.addAttribute("users", userService.searchAll(user));
		} else {
			
			model.addAttribute("users", userService.findAll());
		}
		
		return "userListView.definition";
	}
	
	@GetMapping("/view/{userId}")
	public String view(Model model,
			@PathVariable("userId") int userId) {
		
		log.info("<<==>> userService show single user with id: " + userId);
		
		User user = userService.findByProperty("id", userId).get(0);
		
		// Van phai check vi co the user se nhap tu URL thay vi chon link trong User List
		if(user != null) {
			
			model.addAttribute("formTitle", "View selected user");
			model.addAttribute("viewMode", true);
			model.addAttribute("editMode", true);
			model.addAttribute("userForm", user);

			model.addAttribute("mapRole", initRoleMap());
			
			return "userAction.definition";
		}
		return "redirect:/user/list";
	}
	
	// Create and Update
	@GetMapping("/add")
	public String showForm(Model model) {
		
		log.info("<<==>> userService show adding form");
		
		model.addAttribute("formTitle", "Add new user");
		model.addAttribute("viewMode", false);
		model.addAttribute("userForm", new User());
		
		model.addAttribute("mapRole", initRoleMap());
		
		return "userAction.definition";
	}
	
	@GetMapping("/edit/{userId}")
	public String showForm(Model model,
			@PathVariable("userId") int userId) {
		
		log.info("<<==>> userService show updating form with id: " + userId);
		
		User user = userService.findByProperty("id", userId).get(0);
		
		// Van phai check vi co the user se nhap tu URL thay vi chon link trong User List
		if(user != null) {

			model.addAttribute("formTitle", "Edit selected user");
			model.addAttribute("viewMode", false);
			model.addAttribute("editMode", true); // > 
			model.addAttribute("userForm", user);
			
			UserRole userRole = user.getUserRoles().iterator().next();
			userRole.getRole().setId(userRole.getRole().getId());
			
			model.addAttribute("mapRole", initRoleMap());
			
			return "userAction.definition";
		}
		return "redirect:/user/list";
	}
	
	@PostMapping("/save")
	public String save(Model model, HttpSession session ,
			@Validated @ModelAttribute("userForm") User user,
			BindingResult bindingResult) {
		
		log.info("<<==>> userService save user - userForm: " + user);
		
		if(bindingResult.hasErrors()) {
			
			if(user.getId() != null && user.getId() != 0) {
				
				model.addAttribute("formTitle", "Edit selected user");
				model.addAttribute("editMode", true);
				
			} else {
				
				model.addAttribute("formTitle", "Add new user");
			}
			model.addAttribute("viewMode", false);
			
			model.addAttribute("mapRole", initRoleMap());
			
			return "userAction.definition";
		}
		
		// Th su dung Form de upate
		if(user.getId() != null && user.getId()!=0) {
			
			try {
			
				userService.update(user);
				session.setAttribute(Constant.MSG_SUCCESS, "Update user successfully!");
			
			} catch (Exception e) { 
				
				session.setAttribute(Constant.MSG_FAILURE, "Update user failed!");
				e.printStackTrace();
			}
		}
		// TH su dung Form de add
		else {
			
			try {
				
				userService.save(user);
				session.setAttribute(Constant.MSG_SUCCESS, "Insert success!!!");
				
			} catch (Exception e) {
				
				e.printStackTrace();
				session.setAttribute(Constant.MSG_FAILURE, "Insert has error!!!");
			}
		}
		return "redirect:/user/list";
	}
	
	// Delete
	@GetMapping("/delete/{userId}")
	public String deleteUser(Model model, HttpSession session,
			@PathVariable("userId") int userId) {

		log.info("<<==>> userService delete user with id: " + userId);
		
		User user = userService.findByProperty("id", userId).get(0);
		if(user != null) {
			
			try {
				
				userService.delete(user);
				session.setAttribute(Constant.MSG_SUCCESS, "Delete success!!!");
				
			} catch (Exception e) {
				
				e.printStackTrace();
				session.setAttribute(Constant.MSG_FAILURE, "Delete has error!!!");
			}
		}
		return "redirect:/user/list";
	}
	
	public Map<String, String> initRoleMap() {
		
		List<Role> roles = roleService.findAll();
		
		Map<String, String> mapRole = new HashMap<>();
		for(Role role : roles) {
			
			mapRole.put(String.valueOf(role.getId()), role.getRoleName());
		}
		return mapRole;
	}
}
