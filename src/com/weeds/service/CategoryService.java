package com.weeds.service;

import org.springframework.stereotype.Service;

import com.weeds.domain.Category;

@Service
public class CategoryService<T extends Category> extends BaseService<T> {

}
