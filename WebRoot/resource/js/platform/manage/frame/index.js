Ext.Ajax.timeout = 60000;
Ext.Loader.setConfig({
	enabled : true,
	paths:{
		'Ext':'resource/js/extjs/extjs5/examples',
		'app':'resource/js/extjs/extjs5/examples',
		'Common.app':'resource/js/privateExt/manage/app',//与具体业务相关的定义
		'Common.auth':'resource/js/privateExt/manage/auth'//权限相关的定义
	}
});
Ext.require([ 'Ext.ux.IFrame']);
Ext.require([ 'Ext.ux.TreePicker']);
Ext.require([ 'Ext.ux.ComboBoxTree']);//自定义扩展的多选下拉树控件
Ext.require([ 'app.ux.DateTimeField']);//自定义扩展的日期时间控件

//打开一个新的tab窗口,和index.js中的操作方法类似
function openNewTab(url,tabTitle,id){
	var objUrl;
	if( id!=null && id!="" && id!=undefined ){
		objUrl =  url+"?id="+id;
	}else{
		objUrl =  url;
	}

	globalOpenNewTab(objUrl,tabTitle,'');
}
function openNewTab(url,tabTitle,id,tabFlag){
	var objUrl;
	if( id!=null && id!="" && id!=undefined ){
		objUrl =  url+"?id="+id;
	}else{
		objUrl =  url;
	}

	globalOpenNewTab(objUrl,tabTitle,tabFlag);
}
function globalOpenNewTab(url,tabTitle,controllerIdentifier){
	var tabFlag = '';
	if( controllerIdentifier!=null || controllerIdentifier!="" || controllerIdentifier!=undefined ){
		tabFlag = controllerIdentifier;
	}else{
		tabFlag = Ext.Date.format( new Date(), 'YmdHisu');
	}

	var openedTab = rightTabs.getComponent( 'right_tab_'+tabFlag );
	if( openedTab ) {
		rightTabs.setActiveTab(openedTab);
		return;
	}

	openedTab = Ext.create('Ext.panel.Panel', {
		closable : true,
		id : 'right_tab_'+tabFlag,
		title : tabTitle,
		layout : 'border',
		loadMask: '页面加载中...',
		autoScroll : true,
		resizable:true,
		border : false,
		bodyStyle: {
			//background: '#ffc'
		},
		loader:{
			url:globalBasePath+url,
			autoLoad: true,
			scripts:true,
			renderer: function (loader, response, active) {//重写此方法实现执行scripts脚本
				var text = response.responseText;
				loader.getTarget().update(text, true);
				return true;
			},
		},
		listeners:{
			resize:function(){
			}
		}
	});

	rightTabs.add(openedTab);
	rightTabs.setActiveTab( openedTab );
}


function openNewBrowserWindow(url,id){
	var objUrl;
	if( id!=null && id!="" && id!=undefined ){
		objUrl =  url+"?id="+id;
	}else{
		objUrl =  url;
	}

	window.open(objUrl);
}


var rightTabs = Ext.create('Ext.tab.Panel', {
	region: 'center',
	xtype: 'panel',
	autoScroll : true,
	resizable:true,
	margin: '2 2 2 2',
	plugins : Ext.create('Ext.ux.TabCloseMenu', {
		closeTabText : '关闭面板',
		closeOthersTabsText : '关闭其他',
		closeAllTabsText : '关闭所有'
	}),
	items: [],
	listeners:{
		resize:function(){

		}
	}
});

var themeComboox = Ext.create({
	xtype: 'combobox',
	width: 150,
	labelWidth: 50,
	labelAlign: 'right',
	fieldLabel: '主题',
	displayField: 'name',
	valueField: 'value',
	//labelStyle: 'cursor:move;',
	margin: '5 5 5 5',
	//flex: 1,
	queryMode: 'local',
	store: Ext.create('Ext.data.Store', {
		fields: ['value', 'name'],
		data : [
			{ value: 'classic', name: 'Classic主题' },
			{ value: 'gray', name: 'Gray主题' },
			{ value: 'neptune', name: 'Neptune主题' },
			{ value: 'crisp', name: 'Crisp主题' },
			{ value: 'aria', name: 'aria主题' }
		]
	}),
	//value: theme,
	listeners: {
		select: function(combo) {
			var  theme = combo.getValue();
			var href = 'resource/js/extjs/extjs5/packages/ext-theme-'+theme+'/build/resources/ext-theme-'+theme+'-all.css';
			var link = Ext.fly('theme');

			if(!link) {
				link = Ext.getHead().appendChild({
					tag:'link',
					id:'theme',
					rel:'stylesheet',
					href:''
				});
			};
			link.set({href:Ext.String.format(href, theme)});
		}
	}
});

//顶部区域
var topPanel = Ext.create('Ext.panel.Panel', {
	region: 'north',
	margin: '0 2 0 2',
	width:'100%',
	collapsible:true,
	frame:true,//渲染时是否应用当前主题
	items:[
		{
			xtype:'container',
			layout: {
				type: 'hbox',
				align: 'middle'
			},
			defaults : {
				xtype : 'component'
			},
			items:[{
				contentEl: 'head-region-container',
				flex: 4
				//html:'通用管理系统 '
			},
				{
					html : '欢迎您，管理员！',
					style : 'text-align:center;font-size:14px;',
					width:300
				},themeComboox
			]
		},
	]
});


var leftFunctionTree = new Ext.tree.TreePanel({
	id: 'west-region-container',
	title: '功能列表',
	width: 200,
	rootVisible: false,
	region: 'west',
	xtype: 'panel',
	margin: '2 2 2 2',
	collapsible: true,
	store: new Ext.data.TreeStore({
		proxy: {
			type: 'ajax',
			url: 'sysMenu/queryMenuByUser.do'
		},
		root: {
			text: '我的功能菜单',
			expanded: true
		}
	})
});

leftFunctionTree.on("itemclick", function( myself, record, item, index, e, eOpts ){

	if( !record.raw.leaf ){
		return;
	}

	globalOpenNewTab(record.raw.link,record.raw.text,record.raw.menuIdentify);

	/*
	 //下面的方法已经测试成功
	 openedTab = Ext.create('Common.auth.test.Test4', {
	 id : 'right_tab_default4'
	 });*/
});

Ext.onReady(function() {
	/**onReady开始*/
	Ext.QuickTips.init();

	var viewport = new Ext.Viewport({
		layout: 'border',
		scrollable:true,
		items: [/*{
		 region: 'north',
		 contentEl: 'head-region-container',
		 collapsible:true
		 },*/
			topPanel,
			leftFunctionTree,
			rightTabs,
			{
				region: 'south',
				contentEl: 'foot-region-container',
				margin: '0 2 2 2'
			}
		]
	});

	//设置打开管理后台显示的默认tab窗口
	globalOpenNewTab('exampleBeanFormVal/search.do','默认窗口','ExampleBeanFormVal');
	/**onReady结束*/
});