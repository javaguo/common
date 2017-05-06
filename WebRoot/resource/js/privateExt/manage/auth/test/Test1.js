/**
 * @框架唯一的升级和技术支持地址：http://shop111863449.taobao.com
 */
Ext.define('Common.auth.test.Test1', { // 起始页
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		Ext.apply(this, {
			autoScroll : true,
			defaults : {
				defaults : {
					ui : 'light',
					closable : false
				}
			},
			items : [ {
				id : 'c1',
				items : [ {
					id : 'p1',
					// title : '欢迎语',
					style : 'padding:10px; line-height:22px;',
					html : '<center><img src = "/static/leaflet/images/lksbig.jpg" width = "901" height = "350"/></center>'
				} ]
			} ],
			isReLayout : false
		});
		this.callParent(arguments);
	}
});
