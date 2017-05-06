Ext.onReady(function() {
	//Ext.tip.QuickTipManager.init();
//alert("aaadddd");
	Ext.define('Common.auth.test.Test', {
		extend : 'Ext.grid.GridPanel',
		region : 'center',
		html : 'html  content.......................'
		initComponent : function() {
			var me = this;

			Ext.define('ModelList', {
				extend : 'Ext.data.Model',
				idProperty : 'id',
				fields : [ { name: 'id', type: 'string' },
					        { name: 'name', type: 'string' },
					        { name: 'descn', type: 'string' }
					      ]
			});

			var store = me.createStore({
				modelName : 'ModelList',
				proxyUrl : appBaseUri + '/sys/sysuser/getSysUser',
				proxyDeleteUrl : appBaseUri + '/sys/sysuser/deleteSysUser',
				extraParams : me.extraParams
			});

			var dataStore = new Ext.data.Store({
				model:"ModelList",
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
			
			
			var columns = [new Ext.grid.RowNumberer(),
			               {header:'编号',dataIndex:'id',sortable:true},
			               {header:'名称',dataIndex:'name',sortable:true},
			               {header:'描述',dataIndex:'descn',sortable:true}
			              ];

			Ext.apply(this, {
				id : 'usermanagementgrid',
				store : dataStore,
				columns : columns
			});

//			dataStore.loadPage(1);
			dataStore.load({params:{start:0,limit:25}});

			this.callParent( );
		}
	});
});