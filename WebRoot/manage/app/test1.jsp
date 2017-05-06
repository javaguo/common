<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String browserLang=request.getLocale().toString();
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>表单提交汉字</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="管理系统">
	<meta http-equiv="description" content="通用管理系统">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" 
			href="resource/css/platform/manage/app/index.css">
	<link rel="stylesheet" type="text/css" 
			href="resource/js/extjs/extjs5/packages/ext-theme-classic/build/resources/ext-theme-classic-all.css">
	<script type="text/javascript"
		 	src="resource/js/extjs/extjs5/ext-all.js"></script>	
	<!-- 语言包要在ext-all.js之后引入才能生效 -->
	<script type="text/javascript"
		 	src="resource/js/extjs/extjs5/locale/ext-lang-<%=browserLang%>.js"></script>	
		 	
  </head>
  <body>
  	<form action="ssouser/test1.do" method="post">
  		<input type="text" name="param1" />
  		<input type="text" name="param2" />
  		<input type="submit" value="提交汉字a" />
  	</form>
  </body>
</html>
