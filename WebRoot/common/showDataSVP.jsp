<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String browserLang=request.getLocale().toString();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>信息列表</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="">
	<meta http-equiv="description" content="">
	
	<%-- <link rel="stylesheet" type="text/css" 
			href="resource/js/extjs/extjs5/packages/ext-theme-classic/build/resources/ext-theme-classic-all.css">
	<script type="text/javascript"
		 	src="resource/js/extjs/extjs5/ext-all.js"></script>
	<!-- 语言包要在ext-all.js之后引入才能生效 -->	
	<script type="text/javascript"
		 	src="resource/js/extjs/extjs5/locale/ext-lang-<%=browserLang%>.js"></script>	 --%>
	
	<script type="text/javascript">
Ext.onReady(function() {
	/**onReady开始*/	
	Ext.tip.QuickTipManager.init();

	var  addPanel${menuIdentify}=new Ext.FormPanel({
		id:'addPanel${menuIdentify}',
		frame : true,
		bodyBorder:false,
		bodyStyle : 'padding:0px 0px 0px 0px',
		/*border:true,*/
		// The form will submit an AJAX request to this URL when submitted
		url: '<%=basePath%>${controllerBaseUrl}/save.do',
		defaultType: 'textfield',
		fieldDefaults: {
			labelWidth: 60,
			labelAlign: "right",
			width:200,
			flex: 0,//每项item的宽度权重。值为0或未设置此属性时，item的width值才起作用。
			margin: 8
		},
		items: [
			{
				xtype: 'radiogroup',
				fieldLabel: '单选按钮',
				// Arrange radio buttons into two columns, distributed vertically
				columns: 2,
				vertical: true,
				items: [
					{ boxLabel: 'Item 1', name: 'rb', inputValue: '1' },
					{ boxLabel: 'Item 2', name: 'rb', inputValue: '2', checked: true},
					{ boxLabel: 'Item 3', name: 'rb', inputValue: '3' },
					{ boxLabel: 'Item 4', name: 'rb', inputValue: '4' },
					{ boxLabel: 'Item 5', name: 'rb', inputValue: '5' },
					{ boxLabel: 'Item 6', name: 'rb', inputValue: '6' }
				]
			},
				<c:forEach items="${addFieldList}" var="fieldInfo" varStatus="fieldStatus">

					{ id:'${menuIdentify}${fieldInfo.name}Add',fieldLabel: '${fieldInfo.fieldLabel}',name: '${fieldInfo.name}',
					  type: '${fieldInfo.type}'
					  <c:if test='${fieldInfo.xtype!=null}'>,xtype: '${fieldInfo.xtype}'</c:if>
					}
					<c:choose>
						<c:when test="${fieldStatus.last}"></c:when>
						<c:otherwise>,</c:otherwise>
					</c:choose>
				</c:forEach>
				],

		// Reset and Submit buttons
		buttons: [{
			text: '重置',
			handler: function() {
				this.up('form').getForm().reset();
			}
		}, {
			text: '提交',
			formBind: true, //only enabled once the form is valid
			disabled: true,
			handler: function() {
				var form = this.up('form').getForm();
				if (form.isValid()) {
					form.submit({
						success: function(form, action) {
							Ext.Msg.alert('提示信息', action.result.msg);
							Ext.Msg.show({
								title:"提示信息",
								message:action.result.msg,
								buttons:Ext.Msg.OK,
								icon: Ext.Msg.INFO,
								fn: function(btn) {
									//重置表单不起作用
									//var searForm = 	searPanel${menuIdentify}.getForm( );
									//searForm.reset( false );

									//关闭添加窗口
									addWindow${menuIdentify}.close();

									//重新加载列表页面数据
									//dataStore${menuIdentify}.load({params:{page:1,start:0,limit:${pageSize}}});
									dataStore${menuIdentify}.load();//刷新当前页面
								}

							});
						},
						failure: function(form, action) {
							Ext.Msg.alert('错误提示', "抱歉，出错了！"+action.result.msg);
						}
					});
				}

			}
		}]
	});

	var addWindow${menuIdentify} = new Ext.Window({
		id:'addWindow${menuIdentify}',
		title: '添加窗口',
		width:400,
		autoHeight:true,
		resizable:false,
		bodyStyle : 'padding:0px 0px 0px 0px',
		closeAction : 'hide',
		modal : true,
		plain:false,
		listeners   : {'hide':{fn: closeAddWindow${menuIdentify}}},
		items: [
			addPanel${menuIdentify}
		]
	});

	function closeAddWindow${menuIdentify}(){
		/**
		  添加编辑都用到此方法
		 * 关闭窗口事件，空方法即可
		 * window窗口需要通过实现closeAction来关闭，
		 * 否则有其他问题，具体原理暂不清楚
		 * */
	}


    var  editPanel${menuIdentify}=new Ext.FormPanel({
        id:'editPanel${menuIdentify}',
        frame : true,
        bodyBorder:false,
        bodyStyle : 'padding:0px 0px 0px 0px',
        /*border:true,*/
        // The form will submit an AJAX request to this URL when submitted
        url: '<%=basePath%>${controllerBaseUrl}/update.do',
        defaultType: 'textfield',
        fieldDefaults: {
            labelWidth: 60,
            labelAlign: "right",
            width:200,
            flex: 0,//每项item的宽度权重。值为0或未设置此属性时，item的width值才起作用。
            margin: 8
        },
        items: [
            <c:forEach items="${updateFieldList}" var="fieldInfo" varStatus="fieldStatus">
            { id:'${menuIdentify}${fieldInfo.name}Edit',fieldLabel: '${fieldInfo.fieldLabel}',
			  name: '${fieldInfo.name}', type: '${fieldInfo.type}'
			  <c:if test='${fieldInfo.name=="id"}'>
				,hidden:true
			  </c:if>
            }
            <c:choose>
            <c:when test="${fieldStatus.last}"></c:when>
            <c:otherwise>,</c:otherwise>
            </c:choose>
            </c:forEach>
        ],

        // Reset and Submit buttons
        buttons: [{
            text: '重置',
            handler: function() {
                this.up('form').getForm().reset();
            }
        }, {
            text: '提交',
            formBind: true, //only enabled once the form is valid
            disabled: true,
            handler: function() {
                var form = this.up('form').getForm();
                if (form.isValid()) {
                    form.submit({
                        success: function(form, action) {
                            Ext.Msg.alert('提示信息', action.result.msg);
                            Ext.Msg.show({
                                title:"提示信息",
                                message:action.result.msg,
                                buttons:Ext.Msg.OK,
                                icon: Ext.Msg.INFO,
                                fn: function(btn) {
                                    //重置表单不起作用
                                    //var searForm = 	searPanel${menuIdentify}.getForm( );
                                    //searForm.reset( false );

                                    //关闭编辑窗口
                                    editWindow${menuIdentify}.close();

                                    //重新加载列表页面数据
                                    //dataStore${menuIdentify}.load({params:{page:1,start:0,limit:${pageSize}}});
                                    dataStore${menuIdentify}.load();//刷新当前页面
                                }

                            });
                        },
                        failure: function(form, action) {
                            Ext.Msg.alert('错误提示', "抱歉，出错了！"+action.result.msg);
                        }
                    });
                }

            }
        }]
    });

    var editWindow${menuIdentify} = new Ext.Window({
        id:'editWindow${menuIdentify}',
        title: '编辑窗口',
        width:400,
        autoHeight:true,
        resizable:false,
        bodyStyle : 'padding:0px 0px 0px 0px',
        closeAction : 'hide',
        modal : true,
        plain:false,
        listeners   : {'hide':{fn: closeAddWindow${menuIdentify}}},
        items: [
            editPanel${menuIdentify}
        ]
    });



	var searPanel${menuIdentify}=new Ext.FormPanel({
        id:'searPanel${menuIdentify}',
		title:'查询条件',
        collapsible:true,
        region: 'north',
        height:${searchConditionRowNum*40+30},
        scrollable:true,
        resizable:true,
		frame:true,
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

			<c:forEach items="${searFieldList}" var="fieldInfo" varStatus="fieldStatus">
				<c:choose>
					<c:when test="${fieldStatus.first || (fieldStatus.index+1)%layoutNumHoriSearchCondition==1}">
						<c:set var="searIsFirstColspan"  value="true"/>
					</c:when>
					<c:otherwise>
						<c:set var="searIsFirstColspan"  value="false"/>
					</c:otherwise>
				</c:choose>

				<c:choose>
					<c:when test="${fieldStatus.last || (fieldStatus.index+1)%layoutNumHoriSearchCondition==0}">
						<c:set var="searIsLastColspan"  value="true"/>
					</c:when>
					<c:otherwise>
						<c:set var="searIsLastColspan"  value="false"/>
					</c:otherwise>
				</c:choose>

				<c:if test="${searIsFirstColspan}">
					{
						xtype: "container",
						layout:{
							type:"hbox",
							//vertical:false,
							align: 'middle'
						},
						items: [
				</c:if>
								{id:"${fieldInfo.name}Sear${menuIdentify}", xtype: "textfield", name: "${fieldInfo.name}", fieldLabel: "${fieldInfo.fieldLabel}" }<c:if test="${!searIsLastColspan}">,</c:if>
								<c:if test="${fieldStatus.last}">,

								{ id:'searReset${menuIdentify}', xtype: 'button',text:'清空',width:60,height:25,
									margin:'0 10 0 10',
									listeners: {
										click: function() {
											searReset${menuIdentify}();
										}
									}
								},
								{ id:'searSubmit${menuIdentify}', xtype: 'button',text:'查询',width:60,height:25,
									margin:'0 10 0 10',
									listeners: {
										click: function() {
											searSubmit${menuIdentify}();
										}
									}
								}
								</c:if>
				<c:if test="${searIsLastColspan}">
								]
					}<c:if test="${!fieldStatus.last}">,</c:if>
				</c:if>

			</c:forEach>
        ]
});

	function searReset${menuIdentify}() {
		var searForm = 	searPanel${menuIdentify}.getForm( );
		searForm.reset( false );
	}

	function searSubmit${menuIdentify}() {
		//相当于点击分页栏的首页按钮
		platformPageBar${menuIdentify}.moveFirst( );
		//直接重新加载stroe无法刷新分页栏及Ext.grid.RowNumberer()的值
		//gridPanel${menuIdentify}.getStore().load({params:{page:1,start:0,limit:${pageSize}}});
	}

	var sm${menuIdentify} = new Ext.selection.CheckboxModel({checkOnly: false});

	Ext.define("Platform.model.Entity${menuIdentify}", {
    	extend: "Ext.data.Model",
    	fields: [
       		 <c:forEach items="${showFieldList}" var="fieldInfo" varStatus="fieldStatus">
           	 	{ name: '${fieldInfo.name}', type: '${fieldInfo.type}' }
           	 	<c:choose>
               	 	<c:when test="${fieldStatus.last}"></c:when>
               	 	<c:otherwise>,</c:otherwise>
            	</c:choose>
        	</c:forEach>
  		]
	});

	var dataStore${menuIdentify} = new Ext.data.Store({
    	model:"Platform.model.Entity${menuIdentify}",
		pageSize: ${pageSize},
    	proxy: {
        	type: 'ajax',
        	//actionMethods:{create: 'POST'},
        	url: '<%=basePath%>${loadDataUrl}',
			getMethod: function(){ return 'POST'; },//设置为post提交，避免乱码
		    //form:searForm, 此属性无效
			reader: {
				type: 'json',
				totalProperty: 'total',
				rootProperty: 'items'
			}
			},
    	remoteSort: true
    });

	dataStore${menuIdentify}.on("beforeload",function(){
		//示例
		<%--Ext.apply(dataStore${menuIdentify}.proxy.extraParams, {"id":"11","name":"name","namespace":"命名空间","note":"备注","code":"codevalue"});--%>

		var searForm = 	searPanel${menuIdentify}.getForm( );//获取表单
		var formFieldsValues = searForm.getFieldValues();//获取表单中的字段及对应的值
		/*var formJsonStr = Ext.JSON.encode(formFieldsValues);
		 var formJsonObj = Ext.JSON.decode( formJsonStr );*/

		//extraParams参数可以传formFieldsValues，也可以传formJsonObj
		Ext.apply(dataStore${menuIdentify}.proxy.extraParams, formFieldsValues );
	});

	var columnsHead${menuIdentify} = [
	       			new Ext.grid.RowNumberer(),
					<c:forEach items="${showFieldList}" var="fieldInfo" varStatus="fieldStatus">
						{ header:'${fieldInfo.fieldLabel}',
						  dataIndex:'${fieldInfo.name}',
						  sortable:true,
						  hidden:
							<c:choose>
								<c:when test='${fieldInfo.name=="id"}'>true</c:when>
								<c:otherwise>false</c:otherwise>
							</c:choose>
						}
						<c:choose>
							<c:when test="${fieldStatus.last}"></c:when>
							<c:otherwise>,</c:otherwise>
						</c:choose>
					</c:forEach>
	           ];
	
	
	var operateMenu${menuIdentify} = [
		<c:forEach items="${functionBarList}" var="functionInfo" varStatus="functionStatus">
			{
				id:'${functionInfo.identify}${menuIdentify}',
				text:'${functionInfo.name}',
				xtype: 'button',
				iconCls: '${functionInfo.iconCls}',
				listeners: {
					click: function(){
						<c:choose>
						<c:when test='${functionInfo.typeNum=="1"}'>
						addWindow${menuIdentify}.show();
						</c:when>
						<c:when test='${functionInfo.typeNum=="2"}'>
						var selection = gridPanel${menuIdentify}.getSelection( );
						//alert("单击事件：selection-->"+selection+"      selection size-->"+selection.length);

						if( selection.length==0 ){
							Ext.Msg.alert('提示', '请先勾选要操作的数据！');
							return;
						}

						<c:if test='${functionInfo.isSingle}'>
						if( selection.length!=1 ) {
							Ext.Msg.alert('提示', '只能勾选一条数据进行操作！');
							return;
						}
						</c:if>
						Ext.Msg.confirm("提示信息","确定要操作选中的数据吗？",function (btn) {
							alert("btn-->"+btn);
							if( btn=="yes" ){

								var ids = "";
								for( var i=0 ;i<selection.length;i++ ){
									var tempSelect = selection[i];
									var selecData = tempSelect.getData();
									//alert("selecData-->"+selecData+"     selecData.id-->"+selecData.id+"     selecData.name-->"+selecData.name );
									ids += selecData.id+",";
								}

								Ext.Ajax.request({
									<c:choose>
									<c:when test='${functionInfo.identify=="baseDelete"}'>
									url:'<%=basePath%>${controllerBaseUrl}${functionInfo.url}',
									</c:when>
									<c:otherwise>
									url: '<%=basePath%>${functionInfo.url}',
									</c:otherwise>
									</c:choose>

									params: {
										ids: ids
									},
									method:'POST',
									//form：     指定要提交的表单id或者表单数据对象
									//isUpload： 指定要提交的表单是否是文件上传表单，默认情况下会自动检查。
									//headers： 指定要请求的Header信息
									/**callback：指定Ajax请求的回调函数，该函数不管是调用成功还是失败，都会执行。传递给回调函数的参数有三个，
									 * 			第一个参数options表示执行request方法时的参数，
									 第二个参数表示success请求是否成功，
									 第三个参数表示response用来执行Ajax请求的XMLHttpRequest对象*/
									success: function(response){
										var responseStr = response.responseText;
										var responseJsonObj = Ext.JSON.decode( responseStr );

										if( responseJsonObj.success ){
											Ext.Msg.alert('提示', '操作成功！' );
											gridPanel${menuIdentify}.getStore().load();
										}else{
											Ext.Msg.alert('提示', '操作失败！'+responseJsonObj.msg );
										}

										//Ext.Msg.alert('提示', '响应结果：text--->'+responseStr+'           responseJson-->'+responseJsonObj.success+'           msg-->'+responseJsonObj.msg );
									},
									failure:function(response){
										Ext.Msg.alert('提示', '抱歉，操作失败，出错了！' );
									}
								});

							}
						});


						</c:when>
						<c:otherwise>
						Ext.Msg.alert('提示', '没有定义对应的响应事件！-->${functionInfo.typeNum}');
						</c:otherwise>
						</c:choose>
					}
				}
			}

			<c:choose>
				<c:when test="${functionStatus.last}"></c:when>
				<c:otherwise>,</c:otherwise>
			</c:choose>
		</c:forEach>
    ];
	
	var pagingBarMenu${menuIdentify} = [{
    	text:'每页显示${pageSize}条',
    },{
    	text:'导出',
    }];
	
	var platformPageBar${menuIdentify} = new Ext.PagingToolbar({
        store: dataStore${menuIdentify},
        displayInfo: true,
        padding: "0 20 0 20",
        items: pagingBarMenu${menuIdentify}
    });
	
	var gridPanel${menuIdentify} = new Ext.grid.GridPanel({
		title:'查询结果',
		region: 'center',
		store: dataStore${menuIdentify},
        columns: columnsHead${menuIdentify},
        selModel: {
            injectCheckbox: 1,
            mode: "SIMPLE",     //"SINGLE"/"SIMPLE"/"MULTI"
            checkOnly: true     //只能通过checkbox选择
        },
        selType: "checkboxmodel",
        autoHeight: true,
        resizable:true,
        layout:'fit', 
        viewConfig:{
        	forceFit:true
        },
        tbar: operateMenu${menuIdentify} ,
        bbar: platformPageBar${menuIdentify},
		listeners:{
			itemdblclick:function (dataview, record, item, index, e, eOpts) {
                var tempModel = record.getData();
                //查看model结构信息
                //var tempModelJSONStr = Ext.JSON.encode( tempModel );
                //Ext.Msg.alert('提示', '双击了一行！'+"    tempModel.name-->"+tempModel.name+"    tempModel.id-->"+tempModel.id );

                /**  编辑窗口开始 */
                Ext.Ajax.request({
                    url:'<%=basePath%>${controllerBaseUrl}/edit.do',
                    params: {
                        beanId: tempModel.id
                    },
                    method:'POST',
                    /**callback：指定Ajax请求的回调函数，该函数不管是调用成功还是失败，都会执行。传递给回调函数的参数有三个，
                     * 			第一个参数options表示执行request方法时的参数，
                     第二个参数表示success请求是否成功，
                     第三个参数表示response用来执行Ajax请求的XMLHttpRequest对象*/
                    success: function(response){
                        var responseStr = response.responseText;
                        var responseJsonObj = Ext.JSON.decode( responseStr );

                        if( responseJsonObj.success ){
                            //Ext.Msg.alert('提示', '操作成功！' );
                            var updateBean = responseJsonObj.bean;
                            <c:forEach items="${updateFieldList}" var="fieldInfo" varStatus="fieldStatus">
                                Ext.getCmp("${menuIdentify}${fieldInfo.name}Edit").setValue( updateBean.${fieldInfo.name} );
                            </c:forEach>

                            editWindow${menuIdentify}.show();

                        }else{
                            Ext.Msg.alert('提示', '操作失败！'+responseJsonObj.msg );
                        }


                    },
                    failure:function(response){
                        Ext.Msg.alert('提示', '抱歉，操作失败，出错了！' );
                    }
                });
                /**  编辑窗口结束 */
			}
		}

    });
    
    var pagePanel${menuIdentify} =new Ext.container.Container({
        id:'pagePanel${menuIdentify}',  
        //renderTo:'pageContainer${menuIdentify}',
		region: 'center',
        layout:'border',
		resizable:true, 
        border:false,  
		width:'100%',
        height:'100%', 
        bodyStyle: {
		    background: '#aa312f2'
		},
        //contentEl: 'containerId',
        items:[searPanel${menuIdentify},gridPanel${menuIdentify}]
    });
	
    dataStore${menuIdentify}.load({params:{page:1,start:0,limit:${pageSize}}});
    

    /*Ext.on('resize', function abc(){
    	//pagePanel.updateLayout();
    	//pagePanel.doLayout();
    }) ;*/

	/**
	 * 此代码意义非凡，绝对属于核心代码
	 * loader异步加载页面时，loader加载到的页面无法自动布局进行页面大小自适应
	 * 解决方法：
	 * 			pagePanel不指定renderTo属性，使用Ext.getCmp(parentId).add( pagePanel )方法加载页面
	 */
	Ext.getCmp('right_tab_${menuIdentify}').add( pagePanel${menuIdentify} );

	/**onReady结束*/
}); 	

	</script>
  </head>
  <body>

  </body>
</html>
