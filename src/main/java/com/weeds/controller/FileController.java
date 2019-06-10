package com.weeds.controller;

import com.weeds.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
@Api(value = "文件上传下载")
public class FileController {
	
	private final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	FileService fileService;
	
	/*
	 * 业务上这种是不是应该多条连接来下载才对？
	 */
	//@GetMapping("/downs")
	@ApiOperation(value="批量下载")
	public void multiFilesDownload(HttpServletRequest request,HttpServletResponse response,Integer...uids) {
		
	}

	@GetMapping("/down")
	@ApiOperation(value="单文件下载")
	public void filesDownload(HttpServletRequest request,
			HttpServletResponse response, String filePath) {
		fileService.filesDownload(request, response, filePath);
	}
}
