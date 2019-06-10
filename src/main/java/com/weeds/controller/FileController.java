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
@Api(value = "�ļ��ϴ�����")
public class FileController {
	
	private final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	FileService fileService;
	
	/*
	 * ҵ���������ǲ���Ӧ�ö������������زŶԣ�
	 */
	//@GetMapping("/downs")
	@ApiOperation(value="��������")
	public void multiFilesDownload(HttpServletRequest request,HttpServletResponse response,Integer...uids) {
		
	}

	@GetMapping("/down")
	@ApiOperation(value="���ļ�����")
	public void filesDownload(HttpServletRequest request,
			HttpServletResponse response, String filePath) {
		fileService.filesDownload(request, response, filePath);
	}
}
