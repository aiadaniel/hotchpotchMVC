package com.weeds.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.weeds.domain.Category;
import com.weeds.service.CategoryService;

@RestController
@RequestMapping("/forum")
@Api(value="����api")
public class CategoryController {
	
	//����ɾ�����ࡢ��ȡ�����б��
	@Autowired
	CategoryService<Category> categoryService;
	
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
