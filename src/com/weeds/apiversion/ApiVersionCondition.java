package com.weeds.apiversion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.util.UrlPathHelper;

public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    // ·���а汾��ǰ׺�� ������ /v[1-9]/����ʽ
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");
     
    private int apiVersion;
     
    public ApiVersionCondition(int apiVersion){
        this.apiVersion = apiVersion;
    }
     
    public ApiVersionCondition combine(ApiVersionCondition other) {
        // �������������ԭ���򷽷��ϵĶ��帲��������Ķ���
        return new ApiVersionCondition(other.getApiVersion());
    }
 
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
    	UrlPathHelper helper = new UrlPathHelper();
    	String url = helper.getLookupPathForRequest(request);
    	//System.out.println("== get look up path " + url);
        Matcher m = VERSION_PREFIX_PATTERN.matcher(url);//request.getPathInfo() TODO: CHECK why this api return null
        if(m.find()){
            Integer version = Integer.valueOf(m.group(1));
            if(version >= this.apiVersion) // �������İ汾�Ŵ������ð汾�ţ� ������
                return this;
        }
        return null;
    }
 
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // ����ƥ�����µİ汾��
        return other.getApiVersion() - this.apiVersion;
    }
 
    public int getApiVersion() {
        return apiVersion;
    }

}
