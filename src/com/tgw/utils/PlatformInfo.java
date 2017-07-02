package com.tgw.utils;

import javax.servlet.http.HttpServletRequest;

public class PlatformInfo {
	
	/**
	 * 获取访问平台的url地址：例：http://localhost:8080/项目访问名称/
	 * @param request
	 * @return
	 */
	public static String baseUrl( HttpServletRequest request ){
		String basePath = request.getScheme()+
						  "://"+request.getServerName()+
						  ":"+request.getServerPort()+
						  request.getContextPath()+
						  "/";
		return basePath;
	}

	/**
	 * 返回绝对路径
	 * @param request
	 * @param savePath
     * @return
     */
	public static String getAbsolutePath(HttpServletRequest request,String savePath){
		return request.getServletContext().getRealPath( savePath );
	}
}
