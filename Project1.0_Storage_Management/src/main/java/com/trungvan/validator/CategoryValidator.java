package com.trungvan.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.trungvan.entity.Category;
import com.trungvan.service.CategoryService;

@Component
public class CategoryValidator implements Validator {

	@Autowired
	private CategoryService categoryService;

	@Override
	public boolean supports(Class<?> clazz) {

		return clazz == Category.class;
	}

	@Override
	public void validate(Object target, Errors errors) {

		Category category = (Category) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "msg.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "msg.required");

		// Khong duoc de code duoc trung nhau
		if (category.getCode() != null) {

			List<Category> categories = categoryService.findByProperty("code", category.getCode());
			if (categories != null && !categories.isEmpty()) {
				
				// Validate khi update
				if (category.getId() != null && category.getId() != 0) {

					if (categories.get(0).getId() != category.getId()) {

						errors.rejectValue("code", "msg.code.existing");
					}
				} 
				// Validate khi them moi
				else {

					errors.rejectValue("code", "msg.code.existing");
				}
			}
		}
	}

}
