package com.trungvan.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.trungvan.entity.Auth;
import com.trungvan.entity.Menu;
import com.trungvan.entity.Role;
import com.trungvan.entity.User;
import com.trungvan.entity.UserRole;
import com.trungvan.service.UserService;
import com.trungvan.utils.Constant;
import com.trungvan.validator.LoginValidator;

@Controller
public class AuthenticationController {
	
	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private UserService userService;
	
	@Autowired
	private LoginValidator loginValidator;
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		
		if(binder.getTarget() == null) return;
		
		if(binder.getTarget().getClass() == User.class) {
			
			binder.setValidator(loginValidator);
		}
	}
	
	private void sortMenuList(List<Menu> menus) {
		
		Collections.sort(menus, new Comparator<Menu>() {

			@Override
			public int compare(Menu o1, Menu o2) {
				
				// Sort theo thu tu tang dan
				return o1.getOrderIndex() - o2.getOrderIndex();
			}
		});
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		
		model.addAttribute("userAccount", new User());
		return "login";
	}
	
	@PostMapping("/login")
	public String processLogin(Model model, 
			@Validated @ModelAttribute("userAccount") User userAccount,
			BindingResult bindingResult,
			HttpSession session) {
		
		if(bindingResult.hasErrors()) {
			
			return "login";
		}
		
		// Them user vao session lam viec vi user da ton tai trong DB va duoc dang nhap thanh cong
		// > Day la gia thuyet de giai thich, khong chac chan: Co le vi o day khi truy van ta truy van ra 
		//		toan bo Set User nen trong class Entity User ta phai su dung fetch = FetchType.EAGER () hoac
		//		su dung "hibernate.enable_lazy_load_no_trans = true"
		User user = userService.findByProperty("username", userAccount.getUsername()).get(0);
		session.setAttribute(Constant.USER_IN_SESSION, user);
		
		// Sinh menu dong dua tren Role cua user da dang nhap o tren
		// > Vi trong DB thu tu User_Role nhu sau: admin, staff
		// > Nen chi can lay ra User_Role dau tien la du de biet user dang nhap o tren co nhung quyen gi r
		UserRole userRole = user.getUserRoles().iterator().next(); 
		Role role = userRole.getRole();
		log.info(">> Role name: " + role.getRoleName());
		
		// > Tach cac menu trong DB thanh 2 phan:
		//	- Phan 1: menus chi chua cac menu cha (cac menu co parent_id_menu == 0)
		// 	- Phan 2: childTempList chua toan bo cac menu con (cac menu co parent_id_menu > 0)
		//	- Cac menu co endpoint == -1 thi khong them vi no chi dung cho viec CRUD, khong phai hien thi ra web
		List<Menu> menus = new ArrayList<Menu>();
		List<Menu> childTempList = new ArrayList<Menu>();
		// > Day la gia thuyet de giai thich, khong chac chan: Co le vi o day khi truy van ta truy van ra 
		//		toan bo Set Auth nen trong class Entity Role ta phai su dung fetch = FetchType.EAGER () hoac
		//		su dung "hibernate.enable_lazy_load_no_trans = true"
		for(Auth auth: role.getAuths()) {
			
			Menu menu = auth.getMenu();
			
			// - Phan 1: menus chi chua cac menu cha (cac menu co parent_id_menu == 0)
			if(menu.getParentMenuId() == 0 && menu.getOrderIndex() != -1 && menu.getActiveFlag() == 1
					&& auth.getPermission() == 1 && auth.getActiveFlag() == 1) {
				
				menu.setIdMenu(menu.getUrl().replace("/", "") + "Id"); // > Set id cho menu dang thuc thi de su dung jQuery, neu chua ro jQuery co the bo
				menus.add(menu);
			} 
			// - Phan 2: childTempList chua toan bo cac menu con (cac menu co parent_id_menu > 0)
			else if(menu.getParentMenuId() != 0 && menu.getOrderIndex() != -1 && menu.getActiveFlag() == 1
					&& auth.getPermission() == 1 && auth.getActiveFlag() == 1) {

				menu.setIdMenu(menu.getUrl().replace("/", "") + "Id"); // > Set id cho menu dang thuc thi de su dung jQuery, neu chua ro jQuery co the bo
				childTempList.add(menu);
			}
		}
		
		// > Phan chia lai cac menu con trong childTempList vao dung voi cac menu cha tuong ung trong menus
		for(Menu menu: menus) {
			
			List<Menu> childMenus = new ArrayList<Menu>();
			for(Menu childMenu: childTempList) {
				
				if(childMenu.getParentMenuId() == menu.getId()) {
					
					childMenus.add(childMenu);
				}
			}
			menu.setChildMenus(childMenus);
		}
		
		// > Sap xep thu tu menu cha va cac menu con trong no sau khi da phan chia lai xong
		sortMenuList(menus);
		for(Menu menu: menus) {
			
			sortMenuList(menu.getChildMenus());
		}
		
		// > Luu cac menu sau khi da hoan thanh phan chia va sap xep vao session lam viec
		session.setAttribute(Constant.MENU_IN_SESSION, menus);
		
		return "redirect:/home";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		// Xoa tai khoan user da dang nhap thanh cong truoc do khoi session lam viec
		session.removeAttribute(Constant.USER_IN_SESSION);
		session.removeAttribute(Constant.MENU_IN_SESSION);
		
		return "redirect:/login";
	}
}
