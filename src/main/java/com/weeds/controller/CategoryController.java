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
@Api(value="����api")
public class CategoryController {
	
	//����ɾ�����ࡢ��ȡ�����б��
	@Autowired
	CategoryService<Category> categoryService;
	
	//@Authorization
	@GetMapping("/list")
	@ResponseBody
	@ApiOperation(value="���з���",notes="�б�չʾ����")
	public List<Category> listCategory() {
		List<Category> categories = categoryService.list("from Category");
		return categories;
	}

	/*
	 * 
	 */
	@GetMapping("/create/{cname}")
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
