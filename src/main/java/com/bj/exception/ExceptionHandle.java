package com.bj.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandle {
	
	@ExceptionHandler(value = SessionTimeoutException.class)
	public ModelAndView handle(Exception e) {
		ModelAndView mav = new ModelAndView("login");
		//mav.addObject("hasError", true);
		//mav.addObject("message", "session is lost!");
		mav.setViewName("redirect:/admin/login");
        return mav;
	}
}
