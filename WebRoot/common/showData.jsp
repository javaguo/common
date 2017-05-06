<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>表示展示a</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
<!-- 	<link rel="stylesheet" type="text/css" 
			href="resource/js/extjs/extjs5/packages/ext-theme-classic/build/resources/ext-theme-classic-all.css">
	<script type="text/javascript"
		 	src="resource/js/extjs/extjs5/ext-all.js"></script>	
	<script type="text/javascript"
		 	src="resource/js/extjs/extjs5/packages/ext-theme-neptune/build/ext-theme-neptune.js"></script>
	
 -->	<link rel="stylesheet" type="text/css" 
			href="resource/css/platform/manage/app/index.css">
	<link rel="stylesheet" type="text/css" 
			href="resource/js/extjs/extjs5/packages/ext-theme-classic/build/resources/ext-theme-classic-all.css">
	<script type="text/javascript"
		 	src="resource/js/extjs/extjs5/ext-all.js"></script>	
	
	<script type="text/javascript">
	Ext.onReady(function() {
	/**onReady开始*/	
	
	var itemsPerPage = 2; // set the number of items you want per page

Ext.create('Ext.data.Store', {
    id: 'simpsonsStore',
    autoLoad: true,
    fields: ['id', 'name', 'descn'],
    pageSize: itemsPerPage, // items per page
    proxy: {
        type: 'ajax',
        url: 'sysmenu/loadData.do', // url that will load data with respect to start and limit params
        reader: {
            type: 'json',
            rootProperty: 'items',
            totalProperty: 'total'
        }
    }
});

// specify segment of data you want to load using params
/* simpsonsStore.load({
    params: {
        start: 0,
        limit: itemsPerPage
    }
}); */

Ext.create('Ext.grid.Panel', {
    title: 'Simpsons',
    store: 'simpsonsStore',
    columns: [{
        text: 'id',
        dataIndex: 'id'
    }, {
        text: 'name',
        dataIndex: 'name',
    }, {
        text: 'descn',
        dataIndex: 'descn'
    }],
    width: 400,
    height: 125,
    dockedItems: [{
        xtype: 'pagingtoolbar',
        store: 'simpsonsStore', // same store GridPanel is using
        dock: 'bottom',
        displayInfo: true
    }],
    renderTo: Ext.getBody()
});
	
/* simpsonsStore.load({params:{start:0,limit:10}}); */
	/**onReady结束*/	
	}); 	
	
	
	</script>
  </head>
  	
  <body>
  </body>
</html>
