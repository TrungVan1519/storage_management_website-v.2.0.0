package com.trungvan.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.trungvan.entity.User;
import com.trungvan.service.UserService;

@Component
public class UserValidator implements Validator {
	
	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {

		return clazz == User.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		User user = (User) target;
		
		ValidationUtils.rejectIfEmpty(errors, "username", "msg.required");
		ValidationUtils.rejectIfEmpty(errors, "password", "msg.required");
		ValidationUtils.rejectIfEmpty(errors, "name", "msg.required");
		
		if(user.getId()==null) {
			
			ValidationUtils.rejectIfEmpty(errors, "name", "msg.required");
		}
		
		List<User> users = userService.findByProperty("username", user.getUsername());
		if (users != null && !users.isEmpty()) {
			
			errors.rejectValue("username", "msg.username.existing");
		}
	}
}
