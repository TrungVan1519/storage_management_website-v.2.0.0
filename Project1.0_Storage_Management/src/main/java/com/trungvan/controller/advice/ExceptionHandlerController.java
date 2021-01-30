package com.trungvan.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
@RequestMapping("/exception-hanlder")
public class ExceptionHandlerController {

	@GetMapping("/access-denied")
	public String accessDeniedHandler() {
		
		return "accessDeniedView.definition";
	}
}
