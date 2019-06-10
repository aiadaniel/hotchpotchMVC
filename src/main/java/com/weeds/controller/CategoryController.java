package com.weeds.controller;

import com.weeds.domain.Category;
import com.weeds.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/forum/category")
@Api(value="分类api")
public class CategoryController {
	
	//创建删除分类、获取分类列表等
	@Autowired
	CategoryService<Category> categoryService;
	
	//@Authorization
	@GetMapping("/list")
	@ResponseBody
	@ApiOperation(value="所有分类",notes="列表展示分类")
	public List<Category> listCategory() {
		List<Category> categories = categoryService.list("from Category");
		return categories;
	}

	/*
	 * 
	 */
	@GetMapping("/create/{cname}")
	@ApiOperation(value="创建分类",notes="创建分类入口")
	public ResponseEntity<?> createCategory(@ApiParam(required=true,name="cname",value="输入分类名") @PathVariable String cname) {
		Map<String, Integer> res = new HashMap<String, Integer>();
		if (categoryService.find(cname) != null) {
			res.put("reason", -1);
			return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
		}
		Category category = new Category();
		category.setName(cname);
		categoryService.create(category);
		res.put("categoryid", category.getId());
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
}
