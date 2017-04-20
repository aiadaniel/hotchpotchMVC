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
@Api(value="分类api")
public class CategoryController {
	
	//创建删除分类、获取分类列表等
	@Autowired
	IService<Category> categoryService;
	
	@GetMapping("/category/list")
	@ResponseBody
	@ApiOperation(value="所有分类",notes="列表展示分类")
	public List<Category> listCategory() {
		List<Category> categories = categoryService.list("from Category");
		return categories;
	}

	/*
	 * 
	 */
	@GetMapping("/category/create/{cname}")
	@ApiOperation(value="创建分类",notes="创建分类入口")
	public ResponseEntity<?> createCategory(@ApiParam(required=true,name="cname",value="输入分类名") @PathVariable String cname) {
		Category category = new Category();
		category.setName(cname);
		categoryService.create(category);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
