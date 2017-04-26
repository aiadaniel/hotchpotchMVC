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
@Api(value = "文件上传下载")
public class FileController {
	
	private final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Value("${image.file.upload.dir}")//测试使用这种方式能正常获取到配置，缺点就是要定义一个局部变量，且不能为static或final
	private String uploadDir;
	
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
			// 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
//			response.addHeader("Content-Disposition", "attachment;filename="
//					+ new String(filenames.replaceAll(" ", "").getBytes("utf-8"), "iso8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream os = new BufferedOutputStream(
					response.getOutputStream());
			response.setContentType("application/octet-stream");
			os.write(buffer);// 输出文件
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
