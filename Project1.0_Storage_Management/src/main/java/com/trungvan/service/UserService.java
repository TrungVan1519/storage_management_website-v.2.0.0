package com.trungvan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trungvan.dao.UserDAO;
import com.trungvan.dao.UserRoleDAO;
import com.trungvan.dto.Paging;
import com.trungvan.entity.Role;
import com.trungvan.entity.User;
import com.trungvan.entity.UserRole;
import com.trungvan.utils.HashingPassword;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
	
	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private UserDAO<User> userDAO;
	
	@Autowired
	private UserRoleDAO<UserRole> userRoleDAO;
	
	public List<User> findAll() {
		
		log.info("<<==>> userDAO find all");
		return userDAO.findAll();
	}
	
	public List<User> paging(User user, Paging paging) {
		
		StringBuilder searchingQueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		if(user != null) {

			if(user.getUsername() != null && !user.getUsername().isEmpty()) {

				searchingQueryString.append(" and model.username like :username");
				mapParams.put("username", "%" + user.getUsername() + "%");
			}
			if(user.getName() != null && !user.getName().isEmpty()) {
				
				searchingQueryString.append(" and model.name like :name");
				mapParams.put("name", "%" + user.getName() + "%");
			}
		}

		log.info("<<==>> userDAO paging all");
		log.info(">> paging user:" + user);
		return userDAO.paging(searchingQueryString, mapParams, paging);
	}
	
	public List<User> searchAll(User user){
		
		StringBuilder searchingQueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		if(user != null) {
			
			if(user.getUsername() != null && !user.getUsername().isEmpty()) {

				searchingQueryString.append(" and model.username like :username");
				mapParams.put("username", "%" + user.getUsername() + "%");
			}
			if(user.getName() != null && !user.getName().isEmpty()) {
				
				searchingQueryString.append(" and model.name like :name");
				mapParams.put("name", "%" + user.getName() + "%");
			}
		}

		log.info("<<==>> userDAO search all");
		return userDAO.searchAll(searchingQueryString, mapParams);
	}
	
	public List<User> findByProperty(String property, Object value) {
		
		log.info("<<==>> userDao find by property: " + property + "\tvalue: " + value.toString());
		return userDAO.findByProperty(property, value);
	}
	
	public User findById(int id) {
		
		log.info("<<==>> userDAO find by id: " + id);
		return userDAO.findById(User.class, id);
	}
	
	public void save(User user) throws Exception {

		// Save User
		user.setPassword(HashingPassword.encrypt(user.getPassword()));
		user.setActiveFlag(1);
		user.setCreatedDate(new Date());
		user.setUpdatedDate(new Date());
		
		log.info("<<==>> userDAO save user: " + user);
		userDAO.save(user);

		// Save Role va UserRole
		Role role = new Role();
		role.setId(user.getUserRoles().iterator().next().getRole().getId());
		
		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(role);
		userRole.setActiveFlag(1);
		userRole.setCreatedDate(new Date());
		userRole.setUpdatedDate(new Date());
		
		log.info("<<==>> userRoleDAO save userRole: " + userRole);
		userRoleDAO.save(userRole);
	}
	
	public void update(User user) throws Exception {
		
		// Update User
		user.setUpdatedDate(new Date());
		
		log.info("<<==>> userDAO update user: " + user);
		userDAO.update(user);
		
		// Update Role va UserRole
		UserRole userRole =(UserRole) user.getUserRoles().iterator().next();
		userRole.setUpdatedDate(new Date());
		
		Role role = userRole.getRole();
		role.setId(user.getUserRoles().iterator().next().getRole().getId());

		userRole.setRole(role);

		log.info("<<==>> userRoleDAO update userRole: " + userRole);
		userRoleDAO.update(userRole);
	}
	
	public void delete(User user) throws Exception {
		
		// Do khi xoa ta khong xoa han object ma chi set activeFlag = 0
		user.setActiveFlag(0);
		user.setUpdatedDate(new Date());
		
		log.info("<<==>> userDAO delete user (activeFlag = 0):" + user);
		userDAO.update(user);
	}
}
