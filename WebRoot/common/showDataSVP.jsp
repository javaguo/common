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
	
	
	<%-- 定义下拉框数据模型 --%>
	Ext.define('comboBoxDataModel${identifier}', {
			extend: 'Ext.data.Model',
			fields: ['id', 'name']
	});
	
	<%-- 添加窗口方法开始 --%>
	function openAddWindow${identifier}(){
		<%-- 存放需要调整样式left值的级联comboBox。解决不对齐问题 --%>
		var resetLeftComboArray=new Array();
		
		<%-- 下拉框初始化数据开始 --%>
		<c:forEach items="${comboBoxAddList}" var="comboBoxInfo" varStatus="comboBoxStatus">
			<%-- 定义下拉框store开始 --%>
			<c:choose>
				<%-- 使用json串初始化下拉框数据 --%>
				<c:when test='${comboBoxInfo.loadDataImplModel=="json"}'>
					var comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier} = Ext.create('Ext.data.Store', {
						fields: ['id', 'name'],
						data : [
							<c:forEach items="${comboBoxInfo.comboBoxOptionList}" var="comboBoxOptionInfo" varStatus="comboBoxOptionStatus">
								{"id":"${comboBoxOptionInfo.value}", "name":"${comboBoxOptionInfo.name}"}
								<c:choose>
									<c:when test="${comboBoxOptionStatus.last}"></c:when>
									<c:otherwise>,</c:otherwise>
								</c:choose>
							</c:forEach>
						]
					});
				</c:when>
				<%-- 请求后台查询数据库初始化下拉框数据 --%>
				<c:when test='${comboBoxInfo.loadDataImplModel=="sql"}'>
					<c:choose>
						<c:when test='${comboBoxInfo.isCascade && !comboBoxInfo.isFirst }'>
							var cascChild_${comboBoxInfo.comboBoxName}_${identifier} = "true";
						</c:when>
						<c:otherwise>
							var cascChild_${comboBoxInfo.comboBoxName}_${identifier} = "false";
						</c:otherwise>
					</c:choose>
					var comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier} = new Ext.data.Store({
						model:comboBoxDataModel${identifier},
						proxy: new Ext.data.HttpProxy({
							url: '<%=basePath%>${controllerBaseUrl}/loadComboxData.do?cascChild='+cascChild_${comboBoxInfo.comboBoxName}_${identifier},
							noCache:false,
							reader:{
								type:'json',
								rootProperty: 'comboboxData'
							}
						}),
						remoteSort: true
					});
					<c:choose>
						<%-- 加载第一个下拉框的数据(级联有多个下拉框，非级联只有一个下拉框) --%>
						<c:when test='${ !comboBoxInfo.isCascade || ( comboBoxInfo.isCascade && comboBoxInfo.isFirst )}'>
							comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier}.on("beforeload",function(){
								Ext.apply(comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier}.proxy.extraParams, {"comboBoxFlag":"${comboBoxInfo.comboBoxFlag}","value":"${comboBoxInfo.firstComboBoxParamValue}"});
							});
							comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier}.load();
						</c:when>
					</c:choose>
				</c:when>
	
				<c:otherwise>
				</c:otherwise>
			</c:choose>
			<%-- 定义下拉框store结束 --%>
			
			<%-- 定义下拉框 --%>
			var  comboBox_${comboBoxInfo.comboBoxName}_${identifier} = Ext.create('Ext.form.ComboBox', {
							id:'comboBoxId${comboBoxInfo.comboBoxName}${identifier}',
							name:'${comboBoxInfo.comboBoxName}',
							xtype: 'combobox',
							store: comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier},
							triggerAction: 'all',
							//queryMode: 'local',
							displayField: 'name',
							valueField: 'id',
							width:100,
							loadingText: '正在加载...',
							emptyText: "请选择",
							mode: "local",
							typeAhead: true  //延时查询
							<c:if test='${comboBoxInfo.configs!=null}'>
								,${comboBoxInfo.configs}
							</c:if>
			});
			<%-- 最后再设置值，防止comboBoxInfo.configs中的配置覆盖 --%>
			if( '${comboBoxInfo.value}'.length>0 ){
				comboBox_${comboBoxInfo.comboBoxName}_${identifier}.setValue( '${comboBoxInfo.value}' );
			}
			<c:if test='${comboBoxInfo.isCascade}'>
				resetLeftComboArray.push( comboBox_${comboBoxInfo.comboBoxName}_${identifier} );	
			</c:if>
			
			<c:choose>
				<%-- 绑定下拉框级联事件 --%>
				<c:when test='${ comboBoxInfo.loadDataImplModel=="sql" && comboBoxInfo.isCascade && !comboBoxInfo.isLast }'>
					comboBox_${comboBoxInfo.comboBoxName}_${identifier}.on('select', function() {
						<c:forEach items="${comboBoxInfo.cascadeList}" var="cascadeComboBoxInfo" varStatus="cascadeComboBoxStatus">
							<%-- 给comboBox赋了初始值使用clearValue()方法不起作用，清除不了值。使用setValue("")清除。问题比较奇怪 --%>
							//comboBox_${cascadeComboBoxInfo.comboBoxName}_${identifier}.clearValue();
							comboBox_${cascadeComboBoxInfo.comboBoxName}_${identifier}.setValue( null );
							comboBox_${cascadeComboBoxInfo.comboBoxName}_${identifier}.reset();
							
							comboBoxStore_${cascadeComboBoxInfo.comboBoxName}_${identifier}.removeAll();
						</c:forEach>
						Ext.apply(comboBoxStore_${comboBoxInfo.childComboBox}_${identifier}.proxy.extraParams, 
									{"comboBoxFlag":"${comboBoxInfo.comboBoxFlag}",
									 "value":comboBox_${comboBoxInfo.comboBoxName}_${identifier}.getValue()});
						comboBoxStore_${comboBoxInfo.childComboBox}_${identifier}.load();
						
					}); 
				</c:when>
			</c:choose>
			
			<c:choose>
				<%-- 请求后台查询数据库初始化下拉框数据(所有的下拉框store都要初始化) --%>
				<c:when test='${comboBoxInfo.loadDataImplModel=="sql"}'>
					comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier}.on("beforeload",function(){
								Ext.apply(comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier}.proxy.extraParams, 
									{"comboBoxFlag":"${comboBoxInfo.comboBoxFlag}",
										<c:choose>
											<%-- 加载第一个下拉框的数据(级联有多个下拉框，非级联只有一个下拉框) --%>
											<c:when test='${ !comboBoxInfo.isCascade || ( comboBoxInfo.isCascade && comboBoxInfo.isFirst )}'>			
												"value":"${comboBoxInfo.firstComboBoxParamValue}"
											</c:when>
											<c:otherwise>
												"value":comboBox_${comboBoxInfo.parentComboBox}_${identifier}.getValue()
											</c:otherwise>
										</c:choose>
								});
					});
					<c:choose>
						<%-- 加载级联第一个下拉框或非级联下拉框(非级联下拉框只有一个) --%>
						<c:when test='${ !comboBoxInfo.isCascade || ( comboBoxInfo.isCascade && comboBoxInfo.isFirst )}'>			
							comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier}.load();
						</c:when>
						<c:otherwise>
							<%-- 加载级联下拉框数据(除第一个下拉框),父级联框有数据时才加载子级联框 --%>
						    if( comboBox_${comboBoxInfo.parentComboBox}_${identifier}.getValue() ){
								comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier}.load();
							}
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<%-- 下拉框初始化数据结束 --%>
		
		<%-- tag控件下拉框初始化数据开始 --%>
		<c:forEach items="${tagAddList}" var="tagInfo" varStatus="tagStatus">
			<%-- 定义下拉框store开始 --%>
			<c:choose>
				<%-- 使用json串初始化下拉框数据 --%>
				<c:when test='${tagInfo.loadDataImplModel=="json"}'>
					var tagStore_${tagInfo.comboBoxName}_${identifier} = Ext.create('Ext.data.Store', {
						fields: ['id', 'name'],
						data : [
							<c:forEach items="${tagInfo.comboBoxOptionList}" var="comboBoxOptionInfo" varStatus="comboBoxOptionStatus">
								{"id":"${comboBoxOptionInfo.value}", "name":"${comboBoxOptionInfo.name}"}
								<c:choose>
									<c:when test="${comboBoxOptionStatus.last}"></c:when>
									<c:otherwise>,</c:otherwise>
								</c:choose>
							</c:forEach>
						]
					});
				</c:when>
				<%-- 请求后台查询数据库初始化下拉框数据 --%>
				<c:when test='${tagInfo.loadDataImplModel=="sql"}'>
					var tagStore_${tagInfo.comboBoxName}_${identifier} = new Ext.data.Store({
						model:comboBoxDataModel${identifier},
						proxy: new Ext.data.HttpProxy({
							url: '<%=basePath%>${controllerBaseUrl}/loadComboxData.do',
							noCache:false,
							reader:{
								type:'json',
								rootProperty: 'comboboxData'
							}
						}),
						remoteSort: true
					});
					tagStore_${tagInfo.comboBoxName}_${identifier}.on("beforeload",function(){
						Ext.apply(tagStore_${tagInfo.comboBoxName}_${identifier}.proxy.extraParams, {"comboBoxFlag":"${tagInfo.comboBoxFlag}","value":"${tagInfo.firstComboBoxParamValue}"});
					});
					tagStore_${tagInfo.comboBoxName}_${identifier}.load();
				</c:when>
	
				<c:otherwise>
				</c:otherwise>
			</c:choose>
			<%-- 定义下拉框store结束 --%>
		</c:forEach>
		<%-- tag控件下拉框初始化数据结束 --%>		
		
		<%-- 生成所有字段表单元素开始 --%>
		<c:forEach items="${addFieldList}" var="fieldInfo" varStatus="validFieldStatus">
			var  field_${fieldInfo.name}_${identifier} =
			<c:choose>
				<%-- 单选按钮开始 --%>
				<c:when test='${fieldInfo.xtype=="radiogroup"}'>
				Ext.create({
					xtype: '${fieldInfo.xtype}',
					id: field_${fieldInfo.name}_${identifier},
					fieldLabel: '${fieldInfo.fieldLabel}',
					labelStyle:'vertical-align: middle;',
					columns: 10,
					vertical: true,
					items: [
						<c:forEach items="${fieldInfo.sysEnFieldAttr.radioList}" var="radioFieldInfo" varStatus="radioFieldStatus">
							{ boxLabel: '${radioFieldInfo.boxLabel}', name: '${fieldInfo.name}',
							inputValue: '${radioFieldInfo.inputValue}',
							checked:${radioFieldInfo.checked},width:60
							<c:if test='${radioFieldInfo.configs!=null}'>
									,${radioFieldInfo.configs}
							</c:if>
							}
							<c:choose>
								<c:when test="${radioFieldStatus.last}"></c:when>
								<c:otherwise>,</c:otherwise>
							</c:choose>
						</c:forEach>
					]
					<c:if test='${!fieldInfo.isAllowBlank}'>
						,beforeLabelTextTpl: ['<span class="required">*</span>']
					</c:if>
					<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
						,${fieldInfo.sysEnFieldAttr.configs}
					</c:if>
				});
				</c:when>
				<%-- 单选按钮结束 --%>
				<%-- 多选按钮开始 --%>
				<c:when test='${fieldInfo.xtype=="checkboxgroup"}'>
				Ext.create({
					xtype: '${fieldInfo.xtype}',
					id: field_${fieldInfo.name}_${identifier},
					fieldLabel: '${fieldInfo.fieldLabel}',
					labelStyle:'vertical-align: middle;',
					columns: 10,
					vertical: true,
					items: [
						<c:forEach items="${fieldInfo.sysEnFieldAttr.checkboxList}" var="checkboxFieldInfo" varStatus="checkboxFieldStatus">
								{ boxLabel: '${checkboxFieldInfo.boxLabel}', name: '${fieldInfo.name}',
									inputValue: '${checkboxFieldInfo.inputValue}',
									checked:${checkboxFieldInfo.checked},width:60
									<c:if test='${checkboxFieldInfo.configs!=null}'>
										,${checkboxFieldInfo.configs}
									</c:if>
								}
							<c:choose>
								<c:when test="${checkboxFieldStatus.last}"></c:when>
								<c:otherwise>,</c:otherwise>
							</c:choose>
						</c:forEach>
					]
					<c:if test='${!fieldInfo.isAllowBlank}'>
						,beforeLabelTextTpl: ['<span class="required">*</span>']
					</c:if>
					<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
						,${fieldInfo.sysEnFieldAttr.configs}
					</c:if>
				});
				</c:when>
				<%-- 多选按钮结束 --%>
				<%-- 下拉框开始 --%>
				<c:when test='${fieldInfo.xtype=="combobox"}'>
					<c:if test='${fieldInfo.sysEnFieldAttr.isCascade}'>
						Ext.create({
							xtype: 'fieldcontainer',
							id: field_${fieldInfo.name}_${identifier},
							fieldLabel: '${fieldInfo.fieldLabel}',
							labelStyle:'vertical-align: middle;',
							layout: 'hbox',
							items:[
								<c:forEach items="${fieldInfo.sysEnFieldAttr.comboBoxList}" var="comboBoxFieldInfo" varStatus="comboBoxFieldStatus">
									comboBox_${comboBoxFieldInfo.comboBoxName}_${identifier}
									<c:choose>
										<c:when test="${comboBoxFieldStatus.last}"></c:when>
										<c:otherwise>,</c:otherwise>
									</c:choose>
								</c:forEach>
							]
							<c:if test='${!fieldInfo.isAllowBlank}'>
								,beforeLabelTextTpl: ['<span class="required">*</span>']
							</c:if>
							<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
								,${fieldInfo.sysEnFieldAttr.configs}
							</c:if>
						});
					</c:if>
					<c:if test='${!fieldInfo.sysEnFieldAttr.isCascade}'>
						<%-- 非级联下拉框comboBoxList的size一定为1 --%>
						<c:forEach items="${fieldInfo.sysEnFieldAttr.comboBoxList}" var="comboBoxFieldInfo" varStatus="comboBoxFieldStatus">
							comboBox_${comboBoxFieldInfo.comboBoxName}_${identifier};
							comboBox_${comboBoxFieldInfo.comboBoxName}_${identifier}.fieldLabel='${fieldInfo.fieldLabel}';
							<c:if test='${!fieldInfo.isAllowBlank}'>
								comboBox_${comboBoxFieldInfo.comboBoxName}_${identifier}.beforeLabelTextTpl= ['<span class="required">*</span>'];
							</c:if>
						</c:forEach>
					</c:if>
				
				</c:when>
				<%-- 下拉框结束 --%>
				<%-- Tag控件开始 --%>
				<c:when test='${fieldInfo.xtype=="tagfield"}'>
				Ext.create({
							xtype: 'tagfield',
							id:'field_${fieldInfo.name}_${identifier}',
							name:'${fieldInfo.name}',
							fieldLabel: '${fieldInfo.fieldLabel}',
							store: tagStore_${fieldInfo.name}_${identifier},
							mode: "local",
							displayField: 'name',
							valueField: 'id',
							//filterPickList: true,
							triggerAction: 'all',
							//queryMode: 'local',
							loadingText: '正在加载...',
							emptyText: "请选择",
							typeAhead: true  //延时查询
							<c:if test='${!fieldInfo.isAllowBlank}'>
								,beforeLabelTextTpl: ['<span class="required">*</span>']
							</c:if>
							<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
								,${fieldInfo.sysEnFieldAttr.configs}
							</c:if>
				});
				</c:when>
				<%-- Tag控件结束 --%>
				<%-- 下拉树开始 --%>
				<c:when test='${fieldInfo.xtype=="comboboxtree"}'>
				Ext.create("Ext.ux.ComboBoxTree",{
					id:'field_${fieldInfo.name}_${identifier}',
					name: '${fieldInfo.name}',
					fieldLabel: '${fieldInfo.fieldLabel}',
					editable: false,
					loadTreeDataUrl:'<%=basePath%>${fieldInfo.sysEnFieldAttr.url}'
					<c:if test='${!fieldInfo.isAllowBlank}'>
						,beforeLabelTextTpl: ['<span class="required">*</span>']
					</c:if>
					
					<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
						,${fieldInfo.sysEnFieldAttr.configs}
					</c:if>
				});
				</c:when>
				<%-- 下拉树结束 --%>
				<%-- 附件开始 --%>
				<c:when test='${fieldInfo.xtype=="filefield"}'>
				Ext.create({
					xtype: '${fieldInfo.xtype}',
					id:'field_${fieldInfo.name}_${identifier}',
					name: '${fieldInfo.name}',
					fieldLabel: '${fieldInfo.fieldLabel}',
					buttonText:'选择文件',
					validator: function(value){
						<c:if test='${fieldInfo.sysEnFieldAttr!=null}'>
							return validateSuffix(value,'${fieldInfo.sysEnFieldAttr.allowFileType}');
						</c:if>
						
						<c:if test='${fieldInfo.sysEnFieldAttr==null}'>
							return "文件格式校验失败！缺少文件格式校验配置";
						</c:if>
					}
					//afterLabelTextTpl:['<font color=red>*</font>']
					<c:if test='${!fieldInfo.isAllowBlank}'>
						,beforeLabelTextTpl: ['<span class="required">*</span>']
					</c:if>
					
					<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
						,${fieldInfo.sysEnFieldAttr.configs}
					</c:if>
				});
				</c:when>
				<%-- 附件结束 --%>
				<%-- 富文本编辑器开始 --%>
				<c:when test='${fieldInfo.xtype=="htmleditor"}'>
				Ext.create({
					xtype: '${fieldInfo.xtype}',
					id:'field_${fieldInfo.name}_${identifier}',
					name: '${fieldInfo.name}',
					fieldLabel: '${fieldInfo.fieldLabel}',
					fontFamilies :['宋体','隶书','黑体','Arial', 'Courier New', 'Tahoma', 'Times New Roman', 'Verdana']
					<c:if test='${!fieldInfo.isAllowBlank}'>
						,beforeLabelTextTpl: ['<span class="required">*</span>']
					</c:if>
					
					<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
						,${fieldInfo.sysEnFieldAttr.configs}
					</c:if>
				});
				</c:when>
				<%-- 富文本编辑器结束 --%>
				<%-- 表单元素开始 --%>
				<c:otherwise>
				Ext.create({
					xtype: '${fieldInfo.xtype}',
					id:'field_${fieldInfo.name}_${identifier}',
					name: '${fieldInfo.name}',
					fieldLabel: '${fieldInfo.fieldLabel}'
					//afterLabelTextTpl:['<font color=red>*</font>']
					<c:if test='${!fieldInfo.isAllowBlank}'>
						,beforeLabelTextTpl: ['<span class="required">*</span>']
					</c:if>
					<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
						,${fieldInfo.sysEnFieldAttr.configs}
					</c:if>
				});
				</c:otherwise>
				<%-- 表单元素结束 --%>
			</c:choose>
		</c:forEach>
		<%-- 生成所有字段表单元素结束 --%>
			
		var  addPanel_${identifier}=new Ext.FormPanel({
			id:'addPanel_${identifier}',
			frame : true,
			bodyBorder:false,
			bodyStyle : 'padding:0px 0px 0px 0px',
			/*border:true,*/
			// The form will submit an AJAX request to this URL when submitted
			url: '<%=basePath%>${controllerBaseUrl}/save.do',
			defaultType: 'textfield',
			fieldDefaults: {
				labelWidth: 100,
				labelAlign: "right",
				width:400,
				flex: 0,//每项item的宽度权重。值为0或未设置此属性时，item的width值才起作用。
				margin: 8
			},
			items: [
					<c:forEach items="${addFieldList}" var="fieldInfo" varStatus="fieldStatus">
						field_${fieldInfo.name}_${identifier}
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
							submitEmptyText :false,
							waitMsg :'正在保存，请耐心等待......',
							success: function(form, action) {
								Ext.Msg.alert('提示信息', action.result.msg);
								Ext.Msg.show({
									title:"提示信息",
									message:action.result.msg,
									buttons:Ext.Msg.OK,
									icon: Ext.Msg.INFO,
									fn: function(btn) {
										//重置表单不起作用
										//var searForm = 	searPanel${identifier}.getForm( );
										//searForm.reset( false );
	
										//关闭添加窗口
										addWindow${identifier}.close();
	
										//重新加载列表页面数据
										//dataStore${identifier}.load({params:{page:1,start:0,limit:${pageSize}}});
										dataStore${identifier}.load();//刷新当前页面
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
	
		var addWindow${identifier} = new Ext.Window({
			id:'addWindow${identifier}',
			title: '添加窗口',
			width:550,
			maxHeight:500,
			scrollable:true,
			//autoHeight:true,
			resizable:false,
			bodyStyle : 'padding:0px 0px 0px 0px',
			<%-- closeAction应该使用destroy --%>
			//closeAction : 'hide',   默认为destroy
			modal : true,
			plain:false,
			//listeners   : {'hide':{fn: closeWindowPlatform}},
			listeners   : {afterlayout:function(){
					<%-- 纠正级联框左侧与其它控件对齐问题。须在afterlayout后执行，
						 ext计算布局完毕后，重新设置left值。
					--%>
					resetComLeft(resetLeftComboArray);
				}
			},
			items: [
				addPanel_${identifier}
			]
			<c:if test='${controller.addWindowConfigs!=null}'>
				,${controller.addWindowConfigs}
			</c:if>
		});
		
		addWindow${identifier}.show();
		
  }
  <%-- 添加窗口方法结束 --%>
	
	<%-- 编辑窗口方法开始 --%>
	function openEditWindow_${identifier}( beanValJson ){
		<%-- 存放需要调整样式left值的级联comboBox。解决不对齐问题 --%>
		var resetLeftComboArray=new Array();
		
		<%-- 下拉框初始化数据开始 --%>
		<c:forEach items="${comboBoxUpdateList}" var="comboBoxInfo" varStatus="comboBoxStatus">
			<%-- 定义下拉框store开始 --%>
			<c:choose>
				<%-- 使用json串初始化下拉框数据 --%>
				<c:when test='${comboBoxInfo.loadDataImplModel=="json"}'>
					var comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier} = Ext.create('Ext.data.Store', {
						fields: ['id', 'name'],
						data : [
							<c:forEach items="${comboBoxInfo.comboBoxOptionList}" var="comboBoxOptionInfo" varStatus="comboBoxOptionStatus">
								{"id":"${comboBoxOptionInfo.value}", "name":"${comboBoxOptionInfo.name}"}
								<c:choose>
									<c:when test="${comboBoxOptionStatus.last}"></c:when>
									<c:otherwise>,</c:otherwise>
								</c:choose>
							</c:forEach>
						]
					});
				</c:when>
				<%-- 请求后台查询数据库初始化下拉框数据 --%>
				<c:when test='${comboBoxInfo.loadDataImplModel=="sql"}'>
					<c:choose>
						<c:when test='${comboBoxInfo.isCascade && !comboBoxInfo.isFirst }'>
							var cascChild_${comboBoxInfo.comboBoxName}_${identifier} = "true";
						</c:when>
						<c:otherwise>
							var cascChild_${comboBoxInfo.comboBoxName}_${identifier} = "false";
						</c:otherwise>
					</c:choose>
					var comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier} = new Ext.data.Store({
						model:comboBoxDataModel${identifier},
						proxy: new Ext.data.HttpProxy({
							url: '<%=basePath%>${controllerBaseUrl}/loadComboxData.do?cascChild='+cascChild_${comboBoxInfo.comboBoxName}_${identifier},
							noCache:false,
							reader:{
								type:'json',
								rootProperty: 'comboboxData'
							}
						}),
						remoteSort: true
					});
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
			<%-- 定义下拉框store结束 --%>
			
			<%-- 定义下拉框 --%>
			var  comboBox_${comboBoxInfo.comboBoxName}_${identifier} = Ext.create('Ext.form.ComboBox', {
							id:'edit_${comboBoxInfo.comboBoxName}_${identifier}',
							name:'${comboBoxInfo.comboBoxName}',
							//value:beanValJson.${comboBoxInfo.comboBoxName},		
							xtype: 'combobox',
							store: comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier},
							triggerAction: 'all',
							//queryMode: 'local',
							displayField: 'name',
							valueField: 'id',
							width:80,
							loadingText: '正在加载...',
							emptyText: "请选择",
							mode: "local",
							typeAhead: true  //延时查询
							<c:if test='${comboBoxInfo.configs!=null}'>
								,${comboBoxInfo.configs}
							</c:if>
			});
			<%-- 最后再设置值，防止comboBoxInfo.configs中的配置覆盖 --%>
			if( '${comboBoxInfo.value}'.length>0 ){
				<%-- 默认值 --%>
				comboBox_${comboBoxInfo.comboBoxName}_${identifier}.setValue( '${comboBoxInfo.value}' );
			}
			if( beanValJson.${comboBoxInfo.comboBoxName} ){
				<%-- 数据库中的值 --%>
				comboBox_${comboBoxInfo.comboBoxName}_${identifier}.setValue( beanValJson.${comboBoxInfo.comboBoxName} );
			}
			<c:if test='${comboBoxInfo.isCascade}'>
				resetLeftComboArray.push( comboBox_${comboBoxInfo.comboBoxName}_${identifier} );	
			</c:if>
			
			
			<c:choose>
				<%-- 绑定下拉框级联事件 --%>
				<c:when test='${ comboBoxInfo.loadDataImplModel=="sql" && comboBoxInfo.isCascade && !comboBoxInfo.isLast }'>
					comboBox_${comboBoxInfo.comboBoxName}_${identifier}.on('select', function() {
						<c:forEach items="${comboBoxInfo.cascadeList}" var="cascadeComboBoxInfo" varStatus="cascadeComboBoxStatus">
							<%-- 编辑时，给comboBox赋了初始值使用clearValue()方法不气作用，清除不了值。使用setValue("")清除。问题比较奇怪 --%>
							//comboBox_${cascadeComboBoxInfo.comboBoxName}_${identifier}.clearValue();
							comboBox_${cascadeComboBoxInfo.comboBoxName}_${identifier}.setValue( null );
							comboBox_${cascadeComboBoxInfo.comboBoxName}_${identifier}.reset();
							
							comboBoxStore_${cascadeComboBoxInfo.comboBoxName}_${identifier}.removeAll();
						</c:forEach>
						
						Ext.apply(comboBoxStore_${comboBoxInfo.childComboBox}_${identifier}.proxy.extraParams, 
							{"comboBoxFlag":"${comboBoxInfo.comboBoxFlag}",
							 "value":comboBox_${comboBoxInfo.comboBoxName}_${identifier}.getValue()});
						comboBoxStore_${comboBoxInfo.childComboBox}_${identifier}.load();
					}); 
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
			
			<c:choose>
				<%-- 请求后台查询数据库初始化下拉框数据(所有的下拉框store都要初始化) --%>
				<c:when test='${comboBoxInfo.loadDataImplModel=="sql"}'>
					comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier}.on("beforeload",function(){
								Ext.apply(comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier}.proxy.extraParams, 
									{"comboBoxFlag":"${comboBoxInfo.comboBoxFlag}",
										<c:choose>
											<%-- 加载第一个下拉框的数据(级联有多个下拉框，非级联只有一个下拉框) --%>
											<c:when test='${ !comboBoxInfo.isCascade || ( comboBoxInfo.isCascade && comboBoxInfo.isFirst )}'>			
												"value":"${comboBoxInfo.firstComboBoxParamValue}"
											</c:when>
											<c:otherwise>
												"value":comboBox_${comboBoxInfo.parentComboBox}_${identifier}.getValue()
											</c:otherwise>
										</c:choose>
								});
					});
					<c:choose>
						<%-- 加载级联第一个下拉框或非级联下拉框(非级联下拉框只有一个) --%>
						<c:when test='${ !comboBoxInfo.isCascade || ( comboBoxInfo.isCascade && comboBoxInfo.isFirst )}'>			
							comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier}.load();
						</c:when>
						<c:otherwise>
							<%-- 加载级联下拉框数据(除第一个下拉框),父级联框有数据时才加载子级联框 --%>
						    if( comboBox_${comboBoxInfo.parentComboBox}_${identifier}.getValue() ){
								comboBoxStore_${comboBoxInfo.comboBoxName}_${identifier}.load();
							}
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<%-- 下拉框初始化数据结束 --%>
		
		<%-- tag控件下拉框初始化数据开始 --%>
		<c:forEach items="${tagUpdateList}" var="tagInfo" varStatus="tagStatus">
			<%-- 定义下拉框store开始 --%>
			<c:choose>
				<%-- 使用json串初始化下拉框数据 --%>
				<c:when test='${tagInfo.loadDataImplModel=="json"}'>
					var tagStore_${tagInfo.comboBoxName}_${identifier} = Ext.create('Ext.data.Store', {
						fields: ['id', 'name'],
						data : [
							<c:forEach items="${tagInfo.comboBoxOptionList}" var="comboBoxOptionInfo" varStatus="comboBoxOptionStatus">
								{"id":"${comboBoxOptionInfo.value}", "name":"${comboBoxOptionInfo.name}"}
								<c:choose>
									<c:when test="${comboBoxOptionStatus.last}"></c:when>
									<c:otherwise>,</c:otherwise>
								</c:choose>
							</c:forEach>
						]
					});
				</c:when>
				<%-- 请求后台查询数据库初始化下拉框数据 --%>
				<c:when test='${tagInfo.loadDataImplModel=="sql"}'>
					var tagStore_${tagInfo.comboBoxName}_${identifier} = new Ext.data.Store({
						model:comboBoxDataModel${identifier},
						proxy: new Ext.data.HttpProxy({
							url: '<%=basePath%>${controllerBaseUrl}/loadComboxData.do',
							noCache:false,
							reader:{
								type:'json',
								rootProperty: 'comboboxData'
							}
						}),
						remoteSort: true
					});
					tagStore_${tagInfo.comboBoxName}_${identifier}.on("beforeload",function(){
						Ext.apply(tagStore_${tagInfo.comboBoxName}_${identifier}.proxy.extraParams, {"comboBoxFlag":"${tagInfo.comboBoxFlag}","value":"${tagInfo.firstComboBoxParamValue}"});
					});
					tagStore_${tagInfo.comboBoxName}_${identifier}.load();
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
			<%-- 定义下拉框store结束 --%>
		</c:forEach>
		<%-- tag控件下拉框初始化数据结束 --%>
		
		<%-- 生成所有字段表单元素开始 --%>
		<c:forEach items="${updateFieldList}" var="fieldInfo" varStatus="validFieldStatus">
			
			<c:choose>
				<%-- 单选按钮开始 --%>
				<c:when test='${fieldInfo.xtype=="radiogroup"}'>
				var  edit_${fieldInfo.name}_${identifier} =
				Ext.create({
					xtype: '${fieldInfo.xtype}',
					id: 'edit_${fieldInfo.name}_${identifier}',
					fieldLabel: '${fieldInfo.fieldLabel}',
					labelStyle:'vertical-align: middle;',
					columns: 10,
					vertical: true,
					items: [
						<c:forEach items="${fieldInfo.sysEnFieldAttr.radioList}" var="radioFieldInfo" varStatus="radioFieldStatus">
							{id:'edit_${fieldInfo.name}_${radioFieldInfo.eleId}_${identifier}', 
							 boxLabel: '${radioFieldInfo.boxLabel}', name: '${fieldInfo.name}',
							 inputValue: '${radioFieldInfo.inputValue}',
							 checked:${radioFieldInfo.checked},width:60
							 <c:if test='${radioFieldInfo.configs!=null}'>
									,${radioFieldInfo.configs}
							 </c:if>
							 <%-- 最后再设置值，防止radioFieldInfo.configs中的配置覆盖 --%>
							 ,checked:( '${radioFieldInfo.inputValue}'==beanValJson.${fieldInfo.name}+""?true:false )
							}
							<c:choose>
								<c:when test="${radioFieldStatus.last}"></c:when>
								<c:otherwise>,</c:otherwise>
							</c:choose>
						</c:forEach>
					]
					<c:if test='${!fieldInfo.isAllowBlank}'>
						,beforeLabelTextTpl: ['<span class="required">*</span>']
					</c:if>
					<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
						,${fieldInfo.sysEnFieldAttr.configs}
					</c:if>
				});
				</c:when>
				<%-- 单选按钮结束 --%>
				<%-- 多选按钮开始 --%>
				<c:when test='${fieldInfo.xtype=="checkboxgroup"}'>
					<c:forEach items="${fieldInfo.sysEnFieldAttr.checkboxList}" var="checkboxFieldInfo" varStatus="checkboxFieldStatus">
						var val_${fieldInfo.name}_${checkboxFieldInfo.eleId}_${identifier}=isCheckedCheckbox( beanValJson.${fieldInfo.name},'${checkboxFieldInfo.inputValue}' );
					</c:forEach>
				
				var  edit_${fieldInfo.name}_${identifier} =
				Ext.create({
					xtype: '${fieldInfo.xtype}',
					id: 'edit_${fieldInfo.name}_${identifier}',
					fieldLabel: '${fieldInfo.fieldLabel}',
					labelStyle:'vertical-align: middle;',
					columns: 10,
					vertical: true,
					items: [
						<c:forEach items="${fieldInfo.sysEnFieldAttr.checkboxList}" var="checkboxFieldInfo" varStatus="checkboxFieldStatus">
								{   id:'edit_${fieldInfo.name}_${checkboxFieldInfo.eleId}_${identifier}',
									boxLabel: '${checkboxFieldInfo.boxLabel}', name: '${fieldInfo.name}',
									inputValue: '${checkboxFieldInfo.inputValue}',
									checked:${checkboxFieldInfo.checked},width:60
									<c:if test='${checkboxFieldInfo.configs!=null}'>
										,${checkboxFieldInfo.configs}
									</c:if>
									<%-- 最后再设置值，防止checkboxFieldInfo.configs中的配置覆盖 --%>
									,checked: val_${fieldInfo.name}_${checkboxFieldInfo.eleId}_${identifier}
								}
							<c:choose>
								<c:when test="${checkboxFieldStatus.last}"></c:when>
								<c:otherwise>,</c:otherwise>
							</c:choose>
						</c:forEach>
					]
					<c:if test='${!fieldInfo.isAllowBlank}'>
						,beforeLabelTextTpl: ['<span class="required">*</span>']
					</c:if>
					<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
						,${fieldInfo.sysEnFieldAttr.configs}
					</c:if>
				});
				</c:when>
				<%-- 多选按钮结束 --%>
				<%-- 下拉框开始 --%>
				<c:when test='${fieldInfo.xtype=="combobox"}'>
					var  edit_${fieldInfo.name}_${identifier} =
					<c:if test='${fieldInfo.sysEnFieldAttr.isCascade}'>	
						<%-- 生成级联下拉框 --%>
						Ext.create({
							xtype: 'fieldcontainer',
							id: 'fieldcontainer_${fieldInfo.name}_${identifier}',
							fieldLabel: '${fieldInfo.fieldLabel}',
							labelStyle:'vertical-align: middle;',
							layout: 'hbox',
							items:[
								<c:forEach items="${fieldInfo.sysEnFieldAttr.comboBoxList}" var="comboBoxFieldInfo" varStatus="comboBoxFieldStatus">
									comboBox_${comboBoxFieldInfo.comboBoxName}_${identifier}
									<c:choose>
										<c:when test="${comboBoxFieldStatus.last}"></c:when>
										<c:otherwise>,</c:otherwise>
									</c:choose>
								</c:forEach>
							]
							<c:if test='${!fieldInfo.isAllowBlank}'>
								,beforeLabelTextTpl: ['<span class="required">*</span>']
							</c:if>
							<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
								,${fieldInfo.sysEnFieldAttr.configs}
							</c:if>
						});
					</c:if>
					<c:if test='${!fieldInfo.sysEnFieldAttr.isCascade}'>
						<%-- 生成单个下拉框 --%>
						<%-- 非级联下拉框comboBoxList的size一定为1 --%>
						<c:forEach items="${fieldInfo.sysEnFieldAttr.comboBoxList}" var="comboBoxFieldInfo" varStatus="comboBoxFieldStatus">
							comboBox_${comboBoxFieldInfo.comboBoxName}_${identifier};
							comboBox_${comboBoxFieldInfo.comboBoxName}_${identifier}.fieldLabel='${fieldInfo.fieldLabel}';
							<c:if test='${!fieldInfo.isAllowBlank}'>
								comboBox_${comboBoxFieldInfo.comboBoxName}_${identifier}.beforeLabelTextTpl= ['<span class="required">*</span>'];
							</c:if>
						</c:forEach>
					</c:if>
				
				</c:when>
				<%-- 下拉框结束 --%>
				<%-- Tag控件开始 --%>
				<c:when test='${fieldInfo.xtype=="tagfield"}'>
				var  edit_${fieldInfo.name}_${identifier} =
				Ext.create({
							xtype: 'tagfield',
							id:'edit_${fieldInfo.name}_${identifier}',
							name:'${fieldInfo.name}',
							fieldLabel: '${fieldInfo.fieldLabel}',
							store: tagStore_${fieldInfo.name}_${identifier},
							mode: "local",
							displayField: 'name',
							valueField: 'id',
							//filterPickList: true,
							triggerAction: 'all',
							//queryMode: 'local',
							loadingText: '正在加载...',
							emptyText: "请选择",
							typeAhead: true  //延时查询
							<c:if test='${!fieldInfo.isAllowBlank}'>
								,beforeLabelTextTpl: ['<span class="required">*</span>']
							</c:if>
							<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
								,${fieldInfo.sysEnFieldAttr.configs}
							</c:if>
				});
				if( beanValJson.${fieldInfo.name} ){
					<%-- 最后设置值，防止fieldInfo.sysEnFieldAttr.configs中的配置覆盖 --%>
					edit_${fieldInfo.name}_${identifier}.setValue( beanValJson.${fieldInfo.name} );
				}
				</c:when>
				<%-- Tag控件结束 --%>
				<%-- 下拉树开始 --%>
				<c:when test='${fieldInfo.xtype=="comboboxtree"}'>
				var  edit_${fieldInfo.name}_${identifier} =
				Ext.create("Ext.ux.ComboBoxTree",{
					id: 'edit_${fieldInfo.name}_${identifier}',
					name: '${fieldInfo.name}',
					fieldLabel: '${fieldInfo.fieldLabel}',
					editable: false,
					loadTreeDataUrl:'<%=basePath%>${fieldInfo.sysEnFieldAttr.url}'
					<c:if test='${!fieldInfo.isAllowBlank}'>
						,beforeLabelTextTpl: ['<span class="required">*</span>']
					</c:if>
					<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
						,${fieldInfo.sysEnFieldAttr.configs}
					</c:if>
					<%-- 最后再设置值，防止fieldInfo.sysEnFieldAttr.configs中的配置覆盖 --%>
					,selectedIds:beanValJson.${fieldInfo.name}
				});
				</c:when>
				<%-- 下拉树结束 --%>
				<%-- 附件开始 --%>
				<c:when test='${fieldInfo.xtype=="filefield"}'>
				var  edit_${fieldInfo.name}_${identifier} =
				Ext.create({
					xtype: '${fieldInfo.xtype}',
					id:'edit_${fieldInfo.name}_${identifier}',
					name: '${fieldInfo.name}',
					//value: beanValJson.${fieldInfo.name},
					fieldLabel: '${fieldInfo.fieldLabel}',
					buttonText:'选择文件',
					validator: function(value){
						<c:if test='${fieldInfo.sysEnFieldAttr!=null}'>
							return validateSuffix(value,'${fieldInfo.sysEnFieldAttr.allowFileType}');
						</c:if>
						
						<c:if test='${fieldInfo.sysEnFieldAttr==null}'>
							return "文件格式校验失败！缺少文件格式校验配置";
						</c:if>
					}
					//afterLabelTextTpl:['<font color=red>*</font>']
					<c:if test='${!fieldInfo.isAllowBlank}'>
						,beforeLabelTextTpl: ['<span class="required">*</span>']
					</c:if>
					
					<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
						,${fieldInfo.sysEnFieldAttr.configs}
					</c:if>
				});
				if( beanValJson.${fieldInfo.name} ){
					<%-- 初始化配置value不起作用，使用如下方法在文本框中显示文件名称 --%>
					edit_${fieldInfo.name}_${identifier}.setRawValue( beanValJson.${fieldInfo.name} );
				}
				</c:when>
				<%-- 附件结束 --%>
				<%-- 富文本编辑器开始 --%>
				<c:when test='${fieldInfo.xtype=="htmleditor"}'>
				var  edit_${fieldInfo.name}_${identifier} =
				Ext.create({
					xtype: '${fieldInfo.xtype}',
					id: 'edit_${fieldInfo.name}_${identifier}',
					name: '${fieldInfo.name}',
					//value: beanValJson.${fieldInfo.name},
					fieldLabel: '${fieldInfo.fieldLabel}',
					fontFamilies :['宋体','隶书','黑体','Arial', 'Courier New', 'Tahoma', 'Times New Roman', 'Verdana']
					<c:if test='${!fieldInfo.isAllowBlank}'>
						,beforeLabelTextTpl: ['<span class="required">*</span>']
					</c:if>
					<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
						,${fieldInfo.sysEnFieldAttr.configs}
					</c:if>
				});
				if( beanValJson.${fieldInfo.name} ){
					<%-- 最后再设置值，防止fieldInfo.sysEnFieldAttr.configs中的配置覆盖 --%>
					edit_${fieldInfo.name}_${identifier}.setValue( beanValJson.${fieldInfo.name} );
				}
				</c:when>
				<%-- 富文本编辑器结束 --%>
				<%-- 表单元素开始 --%>
				<c:otherwise>
				var  edit_${fieldInfo.name}_${identifier} =
				Ext.create({
					xtype: '${fieldInfo.xtype}',
					id: 'edit_${fieldInfo.name}_${identifier}',
					name: '${fieldInfo.name}',
					//value: beanValJson.${fieldInfo.name},
					fieldLabel: '${fieldInfo.fieldLabel}'
					//afterLabelTextTpl:['<font color=red>*</font>']
					<c:if test='${!fieldInfo.isAllowBlank}'>
						,beforeLabelTextTpl: ['<span class="required">*</span>']
					</c:if>
					<c:if test='${fieldInfo.sysEnFieldAttr!=null && fieldInfo.sysEnFieldAttr.configs!=null}'>
						,${fieldInfo.sysEnFieldAttr.configs}
					</c:if>
				});
				if( beanValJson.${fieldInfo.name} ){
					<%-- 最后设置值，防止fieldInfo.sysEnFieldAttr.configs中的配置覆盖 --%>
					edit_${fieldInfo.name}_${identifier}.setValue( beanValJson.${fieldInfo.name} );
				}
				</c:otherwise>
				<%-- 表单元素结束 --%>
			</c:choose>
		</c:forEach>
		<%-- 生成所有字段表单元素结束 --%>		
		
		
		
	var  editPanel${identifier}=new Ext.FormPanel({
        id:'editPanel${identifier}',
        frame : true,
        bodyBorder:false,
        bodyStyle : 'padding:0px 0px 0px 0px',
        /*border:true,*/
        // The form will submit an AJAX request to this URL when submitted
        url: '<%=basePath%>${controllerBaseUrl}/update.do',
        defaultType: 'textfield',
        fieldDefaults: {
            labelWidth: 100,
            labelAlign: "right",
            width:400,
            flex: 0,//每项item的宽度权重。值为0或未设置此属性时，item的width值才起作用。
            margin: 8
        },
        items: [
			<c:forEach items="${updateFieldList}" var="fieldInfo" varStatus="fieldStatus">
						edit_${fieldInfo.name}_${identifier}
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
						submitEmptyText :false,
						waitMsg :'正在保存，请耐心等待......',
                        success: function(form, action) {
                            Ext.Msg.alert('提示信息', action.result.msg);
                            Ext.Msg.show({
                                title:"提示信息",
                                message:action.result.msg,
                                buttons:Ext.Msg.OK,
                                icon: Ext.Msg.INFO,
                                fn: function(btn) {
                                    //重置表单不起作用
                                    //var searForm = 	searPanel${identifier}.getForm( );
                                    //searForm.reset( false );

                                    //关闭编辑窗口
                                    editWindow_${identifier}.close();

                                    //重新加载列表页面数据
                                    //dataStore${identifier}.load({params:{page:1,start:0,limit:${pageSize}}});
                                    dataStore${identifier}.load();//刷新当前页面
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

    var editWindow_${identifier} = new Ext.Window({
        id:'editWindow_${identifier}',
        title: '编辑窗口',
        width:550,
		maxHeight:500,
		scrollable:true,
        //autoHeight:true,
        resizable:false,
        bodyStyle : 'padding:0px 0px 0px 0px',
        <%-- closeAction应该使用destroy --%>
		//closeAction : 'hide',   默认为destroy
        modal : true,
        plain:false,
        //listeners   : {'hide':{fn: closeWindowPlatform}},
		listeners   : {afterlayout:function(){
					resetComLeft(resetLeftComboArray);
				}
		},
        items: [
            editPanel${identifier}
        ]
		<c:if test='${controller.editWindowConfigs!=null}'>
				,${controller.editWindowConfigs}
		</c:if>
    });
	
	editWindow_${identifier}.show();
	
	resetComLeft(resetLeftComboArray);
}
	<%-- 编辑窗口方法结束 --%>

	var searPanel${identifier}=new Ext.FormPanel({
        id:'searPanel${identifier}',
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
								{id:"${fieldInfo.name}Sear${identifier}", xtype: "textfield", name: "${fieldInfo.name}", fieldLabel: "${fieldInfo.fieldLabel}" }<c:if test="${!searIsLastColspan}">,</c:if>
								<c:if test="${fieldStatus.last}">,

								{ id:'searReset${identifier}', xtype: 'button',text:'清空',width:60,height:25,
									margin:'0 10 0 10',
									listeners: {
										click: function() {
											searReset${identifier}();
										}
									}
								},
								{ id:'searSubmit${identifier}', xtype: 'button',text:'查询',width:60,height:25,
									margin:'0 10 0 10',
									listeners: {
										click: function() {
											searSubmit${identifier}();
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

	function searReset${identifier}() {
		var searForm = 	searPanel${identifier}.getForm( );
		searForm.reset( false );
	}

	function searSubmit${identifier}() {
		//相当于点击分页栏的首页按钮
		platformPageBar${identifier}.moveFirst( );
		//直接重新加载stroe无法刷新分页栏及Ext.grid.RowNumberer()的值
		//gridPanel${identifier}.getStore().load({params:{page:1,start:0,limit:${pageSize}}});
	}

	var sm${identifier} = new Ext.selection.CheckboxModel({checkOnly: false});

	Ext.define("Platform.model.Entity${identifier}", {
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

	var dataStore${identifier} = new Ext.data.Store({
    	model:"Platform.model.Entity${identifier}",
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

	dataStore${identifier}.on("beforeload",function(){
		//示例
		<%--Ext.apply(dataStore${identifier}.proxy.extraParams, {"id":"11","name":"name","namespace":"命名空间","note":"备注","code":"codevalue"});--%>

		var searForm = 	searPanel${identifier}.getForm( );//获取表单
		var formFieldsValues = searForm.getFieldValues();//获取表单中的字段及对应的值
		/*var formJsonStr = Ext.JSON.encode(formFieldsValues);
		 var formJsonObj = Ext.JSON.decode( formJsonStr );*/

		//extraParams参数可以传formFieldsValues，也可以传formJsonObj
		Ext.apply(dataStore${identifier}.proxy.extraParams, formFieldsValues );
	});

	var columnsHead${identifier} = [
	       			new Ext.grid.RowNumberer(),
					<c:forEach items="${showFieldList}" var="fieldInfo" varStatus="fieldStatus">
						{ header:'${fieldInfo.fieldLabel}',
						  dataIndex:'${fieldInfo.name}',
						  sortable:true,
						  hidden:
							<c:choose>
								<c:when test='${fieldInfo.isShowList}'>false</c:when>
								<c:otherwise>true</c:otherwise>
							</c:choose>
						}
						<c:choose>
							<c:when test="${fieldStatus.last}"></c:when>
							<c:otherwise>,</c:otherwise>
						</c:choose>
					</c:forEach>
	           ];
	
	
	var operateMenu${identifier} = [
		<c:forEach items="${functionBarList}" var="functionInfo" varStatus="functionStatus">
			{
				id:'${functionInfo.identify}${identifier}',
				text:'${functionInfo.name}',
				xtype: 'button',
				iconCls: '${functionInfo.iconCls}',
				listeners: {
					click: function(){
						<c:choose>
						<c:when test='${functionInfo.typeNum=="1"}'>
							//addWindow${identifier}.show();
							openAddWindow${identifier}();
						</c:when>
						<c:when test='${functionInfo.typeNum=="2"}'>
						var selection = gridPanel${identifier}.getSelection( );
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
											gridPanel${identifier}.getStore().load();
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
	
	var pagingBarMenu${identifier} = [{
    	text:'每页显示${pageSize}条',
    },{
    	text:'导出',
    }];
	
	var platformPageBar${identifier} = new Ext.PagingToolbar({
        store: dataStore${identifier},
        displayInfo: true,
        padding: "0 20 0 20",
        items: pagingBarMenu${identifier}
    });
	
	var gridPanel${identifier} = new Ext.grid.GridPanel({
		title:'查询结果',
		region: 'center',
		store: dataStore${identifier},
        columns: columnsHead${identifier},
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
        tbar: operateMenu${identifier} ,
        bbar: platformPageBar${identifier},
		listeners:{
			itemdblclick:function (dataview, record, item, index, e, eOpts) {
                var tempModel = record.getData();
                //查看model结构信息
                //var tempModelJSONStr = Ext.JSON.encode( tempModel );
                //Ext.Msg.alert('提示', '双击了一行！'+"    tempModel.name-->"+tempModel.name+"    tempModel.id-->"+tempModel.id );

                /**  编辑开始 */
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
                            var updateBean = responseJsonObj.bean;
                            openEditWindow_${identifier}( updateBean );
                        }else{
                            Ext.Msg.alert('提示', '操作失败！'+responseJsonObj.msg );
                        }
                    },
                    failure:function(response){
                        Ext.Msg.alert('提示', '抱歉，操作失败，出错了！' );
                    }
                });
                /**  编辑结束 */
			}
		}

    });
    
    var pagePanel${identifier} =new Ext.container.Container({
        id:'pagePanel${identifier}',  
        //renderTo:'pageContainer${identifier}',
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
        items:[searPanel${identifier},gridPanel${identifier}]
    });
	
    dataStore${identifier}.load({params:{page:1,start:0,limit:${pageSize}}});
    

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
	Ext.getCmp('right_tab_${identifier}').add( pagePanel${identifier} );

	/**onReady结束*/
}); 	

	</script>
  </head>
  <body>

  </body>
</html>
