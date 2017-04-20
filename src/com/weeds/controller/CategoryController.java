package com.weeds.controller;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.weeds.domain.Category;
import com.weeds.service.IService;

@RestController
@RequestMapping("/forum")
@Api(value="����api")
public class CategoryController {
	
	//����ɾ�����ࡢ��ȡ�����б��
	@Autowired
	IService<Category> categoryService;
	
	@GetMapping("/category/list")
	@ResponseBody
	@ApiOperation(value="���з���",notes="�б�չʾ����")
	public List<Category> listCategory() {
		List<Category> categories = categoryService.list("from Category");
		return categories;
	}

	/*
	 * 
	 */
	@GetMapping("/category/create/{cname}")
	@ApiOperation(value="��������",notes="�����������")
	public ResponseEntity<?> createCategory(@ApiParam(required=true,name="cname",value="���������") @PathVariable String cname) {
		Category category = new Category();
		category.setName(cname);
		categoryService.create(category);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
