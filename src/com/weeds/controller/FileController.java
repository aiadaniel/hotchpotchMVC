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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
@Api(value = "�ļ��ϴ�����")
public class FileController {
	
	private final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Value("${image.file.upload.dir}")//����ʹ�����ַ�ʽ��������ȡ�����ã�ȱ�����Ҫ����һ���ֲ��������Ҳ���Ϊstatic��final
	private String uploadDir;
	
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
		// get server path (real path)
		String picPath = uploadDir + File.separator + filePath;
		String realPath = request.getSession().getServletContext().getRealPath(picPath);
		logger.info("== realpath: {}",realPath);
		File file = new File(realPath);
//		String filenames = file.getName();
		InputStream inputStream;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
			inputStream.close();
			response.reset();
			// ��ȥ���ļ������еĿո�,Ȼ��ת�������ʽΪutf-8,��֤����������,����ļ�������������������ؿ����Զ���ʾ���ļ���
//			response.addHeader("Content-Disposition", "attachment;filename="
//					+ new String(filenames.replaceAll(" ", "").getBytes("utf-8"), "iso8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream os = new BufferedOutputStream(
					response.getOutputStream());
			response.setContentType("application/octet-stream");
			os.write(buffer);// ����ļ�
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
