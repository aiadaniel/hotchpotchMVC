package com.weeds.service;

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
import org.springframework.stereotype.Service;

@Service
public class FileService {
	
	private final Logger logger = LoggerFactory.getLogger(FileService.class);
	
	@Value("${image.file.upload.dir}")//测试使用这种方式能正常获取到配置，缺点就是要定义一个局部变量，且不能为static或final
	private String uploadDir;
	
	@Autowired
	Environment env;
	
	public void filesDownload(HttpServletRequest request,
			HttpServletResponse response, String filePath) {
		// get server path (real path)
		String envUploadPath = env.getProperty("image.file.upload.dir");
		logger.info("file upload path {} and env {}",uploadDir,envUploadPath);
		String picPath = uploadDir + File.separator + filePath;
		String realPath = picPath;//request.getSession().getServletContext().getRealPath(picPath);
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
