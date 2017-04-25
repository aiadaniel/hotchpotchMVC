package com.weeds.apiversion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.util.UrlPathHelper;

public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    // 路径中版本的前缀， 这里用 /v[1-9]/的形式
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");
     
    private int apiVersion;
     
    public ApiVersionCondition(int apiVersion){
        this.apiVersion = apiVersion;
    }
     
    public ApiVersionCondition combine(ApiVersionCondition other) {
        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVersionCondition(other.getApiVersion());
    }
 
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
    	UrlPathHelper helper = new UrlPathHelper();
    	String url = helper.getLookupPathForRequest(request);
    	//System.out.println("== get look up path " + url);
        Matcher m = VERSION_PREFIX_PATTERN.matcher(url);//request.getPathInfo() TODO: CHECK why this api return null
        if(m.find()){
            Integer version = Integer.valueOf(m.group(1));
            if(version >= this.apiVersion) // 如果请求的版本号大于配置版本号， 则满足
                return this;
        }
        return null;
    }
 
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // 优先匹配最新的版本号
        return other.getApiVersion() - this.apiVersion;
    }
 
    public int getApiVersion() {
        return apiVersion;
    }

}
