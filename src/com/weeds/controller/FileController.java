package com.weeds.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weeds.service.FileService;

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
