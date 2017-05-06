/**
 * @框架唯一的升级和技术支持地址：http://shop111863449.taobao.com
 */
var searPanel4=new Ext.FormPanel({    
        id:'searPanel4888888',  
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
                        { xtype: "button",  text: "搜索4" }
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
	
	var dataStore4 = new Ext.data.Store({
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
	dataStore4.on("beforeload",function(){
	    Ext.apply(dataStore4.proxy.extraParams, {param1:'abcd123',param2:'汉字abcd123'});
	});
	
	var columnsHead888888 = [
	       			new Ext.grid.RowNumberer(),
	               {text:'编号4',dataIndex:'id',menuDisabled:false},
	               {text:'名称4',dataIndex:'name',sortable:true,menuDisabled:false,hideable: false},
	               {text:'描述4',dataIndex:'descn',menuDisabled:true}
	           ];
	
	
	var operateMenu = [{
    	text:'增加4',
    },{
    	text:'删除4',
    }];
	
	var pagingBarMenu = [{
    	text:'导出4',
    },{
    	text:'每页条数',
    }];
	
	var platformPageBar = new Ext.PagingToolbar({
        pageSize: 10,
        store: dataStore4,
        displayInfo: true,
        padding: "0 20 0 20",
        items: pagingBarMenu
    });
	
	var gridPanel4 = new Ext.grid.GridPanel({
		region: 'center',
		store: dataStore4,
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
    
	dataStore4.load({params:{start:0,limit:25}});
	
	
Ext.define('Common.auth.test.Test4', { // 起始页
	extend : 'Ext.panel.Panel',
	closable : true,
	id : 'right-tab-default4',
	title : '通知公告测试4a好',
	layout : 'border',
	loadMask: '页面加载中...',
	autoScroll : true,
	resizable:true,
	border : false,
	items:[searPanel4,gridPanel4]
	}
);
