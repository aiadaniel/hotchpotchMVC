package com.weeds.dao;

import org.springframework.stereotype.Repository;

import com.weeds.domain.Board;

@Repository
public class BoardDao<T extends Board> extends BaseDao<T>{

	
}
