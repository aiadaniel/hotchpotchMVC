package com.weeds.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController{

	public TestController() {

	}

	@RequestMapping(value="/test",method=RequestMethod.GET)
	public ModelAndView req() {
		ModelAndView modelAndView = new ModelAndView("test");
		modelAndView.addObject("message", "hi gaga");
		return modelAndView;
	}

	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		ModelAndView modelAndView = new ModelAndView(/*"/WEB-INF/test.jsp"*/);
		modelAndView.addObject("message", "hi gaga");
		modelAndView.setViewName("test");
		return modelAndView;
	}
}
