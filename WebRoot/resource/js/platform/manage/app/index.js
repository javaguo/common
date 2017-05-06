
Ext.Ajax.timeout = 60000;
Ext.Loader.setConfig({
	enabled : true,
	paths:{
		'Ext':'resource/js/extjs/extjs5/examples',
		'Common.app':'resource/js/privateExt/manage/app',//与具体业务相关的定义
		'Common.auth':'resource/js/privateExt/manage/auth'//权限相关的定义
	}
});
Ext.require([ 'Ext.ux.IFrame']);

Ext.onReady(function() {
	/**onReady开始*/		
	
	Ext.QuickTips.init();
	
	//顶部区域
	var topPanel = Ext.create('Ext.panel.Panel', {
	    region: 'north',
	    margin: '0 2 0 2',
	    width:'100%',
	    collapsible:true,
	    layout: {
	        type: 'table',
	        columns: 8
	    },
	    items:[
	            {
	            	contentEl: 'head-region-container',
	            	colspan: 7
	            	/*width:'70%'*/
	            },
				{  		
					  xtype: 'combo',  
					  width: 100,  
					  /*labelWidth: '30',  */
					  /*fieldLabel: '主题',  */
					  displayField: 'name',  
					  valueField: 'value',  
					  //labelStyle: 'cursor:move;',  
					  margin: '5 5 5 5',  
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
					}				  	
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
				url: 'sysmenu/queryMenuByUser.do'
			},
			root: {
				text: '我的功能菜单',
				expanded: true
			}
		})
    });
	
	
	
	var rightTabs = Ext.create('Ext.tab.Panel', {
	    region: 'center',
	    xtype: 'panel',
	    autoScroll : true,
	    resizable:true,
	    margin: '2 2 2 2',
	    items: [],
        listeners:{
        	resize:function(){
        		//alert("rightTabs大小改变");
        		//this.updateLayout();
        		//alert("rightTabs大小改变aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        	}
        }
	});
	
	
	leftFunctionTree.on("itemclick", function( myself, record, item, index, e, eOpts ){
		
		if( !record.raw.leaf ){
			return;
		}
		
		var openedTab;
		openedTab = rightTabs.getComponent( 'right-tab-'+record.raw.id );
        if( openedTab ) {
        	rightTabs.setActiveTab(openedTab);
            return;
        }
		
		/*var openedTab =  rightTabs.add({
			id:'right-tab-'+record.raw.id,
            title: record.raw.text,
            closable: true,
            layout: 'fit',
            html : '<iframe style="border:0" width=100% height=100% src="'+record.raw.link+'" />'
        });*/
		/*var openedTab = Ext.create('Ext.panel.Panel', {
			closable : true,
			id : 'right-tab-'+record.raw.id,
			title : record.raw.text,
			layout : 'fit',
			loadMask: '页面加载中...',
			autoScroll : true,
			resizable:true, 
			border : false,
			bodyStyle: {
			    background: '#ffc'
			},
	        loader:{
	        	url:record.raw.link,
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
	        		//alert('right-tab-'+record.raw.id+"   大小改变");
	        	}
	        }
		});*/
		
        //下面的方法已经测试成功
		openedTab = Ext.create('Common.auth.test.Test4', {
			id : 'right-tab-default4'
		});
		
		rightTabs.add(openedTab);
		rightTabs.setActiveTab( openedTab );
		
    });
	
	/**
	 * 开发工具的一个bug，也是醉了，不能使用搜狗五笔输入法输入汉字，一输入汉字，eclipse就崩溃。
	 */
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
	
	
	/*
	 //右侧默认显示的tab页面，需要放到最后，否则可能会导致页面布局混乱
	 //右侧tab使用原生iframe，此种方法无法实现换肤
	 var defaultTab =  rightTabs.add({
		id:'right-tab-default',
		title: '通知公告',
		closable: true,
		layout: 'fit',
		html : '<iframe style="border:0" width=100% height=100% src="http://www.baidu.com" />'
	});*/
	
	/*
	 //右侧tab使用ext的iframe，此种方法无法实现换肤
	 var defaultTab =  rightTabs.add(new Ext.ux.IFrame({  
		xtype: 'iframepanel',  
		id: "right-tab-default",  
		title: '通知公告',  
		closable: true,  
		layout: 'fit',  
		loadMask: '页面加载中...',  
		border: false,
        loader:{
        	url:'ssouser/test.do',
        	autoLoad: true
        }
	}) 
	);
	
	//需要主动调用加载数据的方法，在配置中配置加载路径无法加载数据
	defaultTab.load('ssouser/test.do');
	*/
	
	
	
	
	var searPanel=new Ext.FormPanel({    
        id:'searPanel888888',  
        collapsible:true,
        region: 'north',
        height:130,
        scrollable:true,
        resizable:true, 
        defaultType: 'textfield',
        fieldDefaults: {
            labelWidth: 60,
            labelAlign: "right",
            width:200,
            flex: 0,//每项item的宽度权重。值为0或未设置此属性时，item的width值才起作用。
            margin: 8
        },
        /* defaults: {
            anchor: '90%',
        }, */
        items: [
                {
                    xtype: "container",
                    layout: "hbox",
                    width: 1500,
                    items: [
                        { xtype: "textfield", name: "name", fieldLabel: "姓名a", allowBlank: false },
                        { xtype: "textfield", name: "name1", fieldLabel: "姓名b", allowBlank: false },
                        { xtype: "textfield", name: "name1", fieldLabel: "姓名b", allowBlank: false },
                        { xtype: "textfield", name: "name2", fieldLabel: "姓名c", allowBlank: false },
                        { xtype: "numberfield", name: "age", fieldLabel: "年龄", decimalPrecision: 0, vtype: "age" }
                    ]
                },
                {
                    xtype: "container",
                    layout: "hbox",
                    width: 1000,
                    items: [
						{ xtype: "datefield", name: "date", fieldLabel: "日期", allowBlank: false },
                        { xtype: "textfield", name: "phone", fieldLabel: "电话", allowBlank: false, emptyText: "电话或手机号码" },
                        { xtype: "textfield", name: "phone", fieldLabel: "邮箱", allowBlank: false, emptyText: "Email地址", vtype: "email" },
                        { xtype: "button",  text: "搜索" }
                    ]
                }
            ]
    });
	
	
	
	var sm = new Ext.selection.CheckboxModel({checkOnly: false});
	
	Ext.define("Platform.model.Entity888888", {
	    extend: "Ext.data.Model",
	    fields: [
	        { name: 'id', type: 'string' },
	        { name: 'name', type: 'string' },
	        { name: 'descn', type: 'string' }
	    ]
	});
	
	var dataStore888888 = new Ext.data.Store({
		model:"Platform.model.Entity888888",
		proxy: {
			type: 'ajax',
			//actionMethods:{create: 'POST'},
			url: 'sysmenu/loadData.do',
			getMethod: function(){ return 'POST'; },//设置为post提交，避免乱码
			reader: {
				type: 'json',
				totalProperty: 'total',
				rootProperty: 'items'
			}
		},
        remoteSort: true
    });
	dataStore888888.on("beforeload",function(){
	    Ext.apply(dataStore888888.proxy.extraParams, {param1:'abcd123',param2:'汉字abcd123'});
	});
	
	var columnsHead888888 = [
	       			new Ext.grid.RowNumberer(),
	               {text:'编号a',dataIndex:'id',menuDisabled:false},
	               {text:'名称',dataIndex:'name',sortable:true,menuDisabled:false,hideable: false},
	               {text:'描述',dataIndex:'descn',menuDisabled:true}
	           ];
	
	
	var operateMenu = [{
    	text:'增加',
    },{
    	text:'删除',
    }];
	
	var pagingBarMenu = [{
    	text:'导出',
    },{
    	text:'每页条数',
    }];
	
	var platformPageBar = new Ext.PagingToolbar({
        pageSize: 10,
        store: dataStore888888,
        displayInfo: true,
        padding: "0 20 0 20",
        items: pagingBarMenu
    });
	
	var gridPanel888888 = new Ext.grid.GridPanel({
		region: 'center',
		store: dataStore888888,
        columns: columnsHead888888,
        selModel: {
            injectCheckbox: 1,
            mode: "SIMPLE",     //"SINGLE"/"SIMPLE"/"MULTI"
            checkOnly: true     //只能通过checkbox选择
        },
        selType: "checkboxmodel",
        autoHeight: true,
        resizable:true,
        //layout:'fit', 
        viewConfig:{
        	forceFit:true
        },
        tbar: operateMenu ,
        bbar: platformPageBar
    });
    
	/*var gridPanel888888 = new Ext.grid.GridPanel({
		region: 'center',
		store: dataStore888888,
        columns: columnsHead888888
    });*/
	
   /* var pagePanel=new Ext.container.Container({    
        id:'pagePanel888888',  
        renderTo:defaultTab,  
        layout:'border',
		resizable:true, 
        border:false,  
		
        bodyStyle: {
		    background: '#aa312f2'
		},
       // contentEl: 'pageContainer888888',
        items:[searPanel,gridPanel888888]  
    });
	
    
    pagePanel.on('resize', function abcd(){
    	//alert("我大小发生变化 了，看一看");
    }) ;*/
	dataStore888888.load({params:{start:0,limit:25}});
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*var defaultTab = Ext.create('Ext.panel.Panel', {
		closable : true,
		id : 'right-tab-default',
		title : '通知公告',
		layout : 'border',
		loadMask: '页面加载中...',
		autoScroll : true,
		resizable:true,
		border : false,
		html:'<div  style="background-color:red">abdDdddd</div>',
		items:[searPanel,gridPanel888888], 
        loader:{
        	url:'ssouser/test.do',
        	autoLoad: true,
        	scripts:true,
        	renderer: function (loader, response, active) {//重写此方法实现执行scripts脚本
                var text = response.responseText;
                loader.getTarget().update(text, true);
                return true;
            },
        }
	});*/
	
	/*
	  //下面的方法可行
	  var defaultTab = Ext.create('Ext.panel.Panel', {
		closable : true,
		id : 'right-tab-default',
		title : '通知公告测试a',
		layout : 'border',
		loadMask: '页面加载中...',
		autoScroll : true,
		resizable:true,
		border : false,
		items:Ext.create('Common.auth.test.Test3', {id:"test-test"}  )
		//items:[searPanel,gridPanel888888]
	});*/
	
	//默认显示显示，测试成功了已经
	var defaultTab = Ext.create('Common.auth.test.Test3', {
		id : 'right-tab-default3'
	});
	
	
    
/**onReady结束*/	
    
    
    
    
    
    
    
    
    
    rightTabs.add( defaultTab );
	rightTabs.setActiveTab( defaultTab );
}); 

	