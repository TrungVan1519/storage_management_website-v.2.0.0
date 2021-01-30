package com.trungvan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trungvan.dao.RoleDAO;
import com.trungvan.dto.Paging;
import com.trungvan.entity.Role;
import com.trungvan.entity.User;

@Service
public class RoleService {

	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private RoleDAO<Role> roleDAO;
	
	public List<Role> findAll() {
		
		log.info("<<==>> roleDAO find all");
		return roleDAO.findAll();
	}
	
	public List<Role> paging(Role role , Paging paging){
		
		StringBuilder searchingQueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();

		log.info("<<==>> roleDAO paging all");
		return roleDAO.paging(searchingQueryString, mapParams, paging);
	}
	
	public List<Role> searchAll(User user){
		
		StringBuilder searchingQueryString = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		log.info("<<==>> roleDAO search all");
		return roleDAO.searchAll(searchingQueryString, mapParams);
	}
}
