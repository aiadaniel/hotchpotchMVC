package com.weeds.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

//@Controller
public class TestController implements org.springframework.web.servlet.mvc.Controller{

	public TestController() {

	}

	//@RequestMapping(value="/test",method=RequestMethod.GET)
	public ModelAndView req() {
		ModelAndView modelAndView = new ModelAndView("/WEB-INF/test.jsp");
		modelAndView.addObject("message", "hi gaga");
		return modelAndView;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		ModelAndView modelAndView = new ModelAndView(/*"/WEB-INF/test.jsp"*/);
		modelAndView.addObject("message", "hi gaga");
		modelAndView.setViewName("test");
		return modelAndView;
	}
}
