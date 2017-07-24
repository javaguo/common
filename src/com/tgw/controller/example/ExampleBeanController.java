package com.tgw.controller.example;

import com.tgw.bean.base.BaseEnConstant;
import com.tgw.bean.example.ExampleBean;
import com.tgw.bean.system.SysEnController;
import com.tgw.bean.system.SysEnControllerField;
import com.tgw.controller.base.BaseController;
import com.tgw.exception.PlatformException;
import com.tgw.service.example.ExampleBeanService;
import com.tgw.utils.PlatformInfo;
import com.tgw.utils.PlatformUtils;
import com.tgw.utils.config.PlatformSysConstant;
import com.tgw.utils.file.PlatformFileUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhaojg on 2017/03/25
 */
@Controller
@RequestMapping("/exampleBean")
public class ExampleBeanController extends BaseController<ExampleBean>{

    public ExampleBeanController(){
        if( this.getExampleBeanService()!=null ){
            System.out.println("exampleBeanService存在-->"+exampleBeanService.toString());
        }else{
            System.out.println("exampleBeanService不存在！");
        }
    }

    @Resource
    private ExampleBeanService exampleBeanService;

    @Override
    public void beforeSearch(HttpServletRequest request, HttpServletResponse response, ExampleBean bean){
        //覆写了beforeSearch方法，则只调用覆写的beforeSearch方法
        System.out.println("----------------- ExampleBeanController  beforeSearch -----------------");
        //可手动调用父类的方法
        super.beforeSearch(request,response,bean);
    }

    @Override
    public void afterSearch(HttpServletRequest request, HttpServletResponse response, ExampleBean bean){
        //覆写了afterSearch方法，则只调用覆写的afterSearch方法
        System.out.println("----------------- ExampleBeanController  afterSearch -----------------");
        //可手动调用父类的方法
        super.afterSearch(request,response,bean);
    }


    @Override
    public void initSearch(HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView,SysEnController controller, ExampleBean bean ) {
        //覆写了initSearch方法，则只调用覆写的initSearch方法
        System.out.println("----------------- ExampleBeanController  initSearch  -----------------");

        /**
         * 将具体的业务的service对象赋值给baseservice，必须的操作。
         * service层需要将具体业务的mapper赋值给BaseModelMapper
         *
         * 此操作主要解决的问题是BaseModelMapper无法注入到BaseServiceImpl中的问题。手动赋值。
         *
         * 要点：
         * 1.BaseController会调用统一的searchData()接口查询具体的业务数据。
         * 2.具体业务的mapper文件中实现searchData查询语句
         *
         *
         */
        if( null!=this.getExampleBeanService() ){
            super.initService(  this.getExampleBeanService()  );
        }else{

        }

        controller.setIdentifier( "ExampleBeanList" );// 每一个列表页面的唯一身份id
        controller.setLoadDataUrl( "exampleBean/searchData.do" );//加载列表页面数据的方法
        controller.setControllerBaseUrl( "exampleBean" );//控制器的请求地址

        //此处的配置会覆盖jsp页面中默认的配置
        String addWindowConfigs = "title: '添加窗口-示例',width:800";
        String editWindowConfigs = "title: '编辑窗口-示例',width:800";
        controller.setAddWindowConfigs( addWindowConfigs );
        controller.setEditWindowConfigs( editWindowConfigs );
    }

    @Override
    public void initField( SysEnController controller ) {
        //构造字段
        controller.addFieldId("id","ID",null);

        /***********************************************************************************************************
         * 隐藏域、文本框、密码框、文本域
         */
        String formHiddenConfigs = "value:'hidden隐藏域值'";
        //,afterLabelTextTpl:['afterLabelTextTpl'],afterLabelTpl:['afterLabelTpl'],afterBodyEl:['afterBodyEl'],beforeBodyEl :['beforeBodyEl'],beforeLabelTextTpl :['beforeLabelTextTpl'],beforeLabelTpl:['beforeLabelTpl']
        String formTextConfigs = "labelWidth:100,width:400,emptyText:'文本提示信息'";
        String formPasswordConfigs = "labelWidth:100,width:400,emptyText:'密码提示信息'";
        String formTextAreaConfigs = "labelWidth:100,width:400,height:80,emptyText:'文本域内容......',maxLength:50,maxLengthText:'最长为50个字',minLength:5,minLengthText:'最小为5个字'";

        controller.addFieldHidden( "formHidden","form隐藏域",true,true,true,formHiddenConfigs );
        controller.addFieldText("formText","文本文本",true,true,true,true,true,false,formTextConfigs);
        controller.addFieldPassword("formPassword","密码",true,true,true,false,false,formPasswordConfigs);
        controller.addFieldTextArea("formTextArea","文本域",true,true,true,true,false,formTextAreaConfigs);

        /***********************************************************************************************************
         * 数字控件
         *
         * 对应java各数据类型
         *
         * 基本类型的数据目前提交表单时还存在问题。当值为空的时候，表单提交报400错误。
         * 所以项目中定义数据变量时使用包装类型
         */
        String formNumberIntConfigs = "labelWidth:100,width:300,height:25,emptyText:'整数',allowDecimals:false,maxValue:20,maxText:'最大为20',minValue:6,minText:'最小为6'";
        String formNumberDoubleConfigs = "labelWidth:100,width:300,height:25,emptyText:'小数',maxValue:999.9,maxText:'最大为999.9',minValue:0.1,minText:'最小为0.1',step:100";
        String formDecimalConfigs = "labelWidth:100,width:300,height:25,emptyText:'得分',maxValue:99999.9,maxText:'最大为99999.9',minValue:0.1,minText:'最小为0.1',step:100";
        String formRadioGroupConfigsBoolean = "labelWidth:100,width:400";
        String formRadioConfigsBoolean = "width:80";

        /*controller.addFieldNumber("formNumberShortBase","short",true,true,true,false,true,formNumberIntConfigs);
        controller.addFieldNumber("formNumberIntBase","int",true,true,true,false,false,formNumberIntConfigs);
        controller.addFieldNumber("formNumberLongBase","long",true,true,true,false,true,formNumberIntConfigs);
        controller.addFieldNumber("formNumberFloatBase","float",true,true,true,false,true,formNumberDoubleConfigs);
        controller.addFieldNumber("formNumberDoubleBase","double",true,true,true,false,true,formNumberDoubleConfigs);
        controller.addFieldRadioInitDataByJson("formBooleanBase","boolean",true,true,false,false,false,booleanJson,formRadioGroupConfigs,formRadioConfigs);*/

        controller.addFieldNumber("formNumberShort","Short",true,true,true,true,true,formNumberIntConfigs);
        controller.addFieldNumber("formNumberInteger","Integer",true,true,true,true,true,formNumberIntConfigs);
        controller.addFieldNumber("formNumberLong","Long",true,true,true,true,true,formNumberIntConfigs);
        controller.addFieldNumber("formNumberFloat","Float",true,true,true,true,true,formNumberDoubleConfigs);
        controller.addFieldNumber("formNumberDouble","Double",true,true,true,true,true,formNumberDoubleConfigs);
        String booleanJson = "[{name:'是',value:'true',eleId:'formBooleanShi'},{name:'否',value:'false',eleId:'formBooleanFou'}]";
        controller.addFieldRadioInitDataByJson("formBoolean","Boolean",true,true,true,false,true,booleanJson,formRadioGroupConfigsBoolean,formRadioConfigsBoolean);
        /***********************************************************************************************************
         * 日期、时间控件
         */
        String formDateConfigs = "labelWidth:100,width:300,height:25,emptyText:'录入日期',editable:false,maxValue:'2017-12-31',maxText:'不能超过2017-12-31',minValue:'2015-03-21',minText:'最小为2015-03-21'";
        String formDateTimeConfigs = "labelWidth:100,width:300,height:25,emptyText:'精确时间'";

        controller.addFieldDate("formDateString","日期(String)",true,true,true,true,false,formDateConfigs);
        controller.addFieldDate("formDateDate","日期(Date)",true,true,true,false,true,formDateConfigs);
        controller.addFieldDatetime("formDatetimeString","时间(String)",true,true,true,false,false,formDateTimeConfigs);
        controller.addFieldDatetime("formDatetimeDate","时间(Date)",true,true,true,false,true,formDateTimeConfigs);
        /***********************************************************************************************************
        * 单选及多选控件
        */
        String formRadioGroupConfigs = "labelWidth:100,width:500";
        String formRadioConfigs = "width:80";//,style:{margin-right:'0px'}
        String formCheckboxGroupConfigs = "labelWidth:100,width:500";
        String formCheckboxConfigs = "width:80";//,style:{margin-right:'0px'}

        String radioJson = "[{name:'优秀',value:'90',eleId:'formRadioYX'},{name:'良好',value:'80',eleId:'formRadioLH'},{name:'中等',value:'70',eleId:'formRadioZD'}]";
        controller.addFieldRadioInitDataByJson("formRadio","单选",true,true,true,true,true,radioJson,formRadioGroupConfigs,formRadioConfigs);

        String checkboxJson = "[{name:'读书',value:'readbook',eleId:'DS'},{name:'跑步',value:'running',eleId:'PB'},{name:'游泳',value:'swimming',eleId:'YY'}]";
        controller.addFieldCheckboxInitDataByJson("formCheckbox","多选",true,true,true,true,false,checkboxJson,formCheckboxGroupConfigs,formCheckboxConfigs);
        /***********************************************************************************************************
         * 下拉框控件
         */
        String formComBoxConfigs = "labelWidth:100,emptyText:'选择类型',width:200";
        String formComBoxSqlConfigs = "labelWidth:100,emptyText:'选择',width:200";
        String formComboboxGroup1Configs = "labelWidth:100";
        String formComboboxGroup1ComConfigs = "emptyText:'请选择',width:100";
        String formComboboxGroup2Configs = "labelWidth:100,width:500";
        String formComboboxGroup2ComConfigs = "emptyText:'请选择...',width:100";

        String comboBoxJson = "[{name:'优',value:'90'},{name:'良',value:'80'},{name:'中',value:'70'},{name:'及格',value:'60'},{name:'差',value:'50'}]";

        controller.addFieldComboBoxByJSON("formComboBoxJson","下拉框(json)",true,true,true,false,false,comboBoxJson,formComBoxConfigs);
        controller.addFieldComboBoxBySQL("formComboBoxSql","下拉框(sql)",true,true,true,false,true,"loadMenu",null,formComBoxSqlConfigs);
        controller.addFieldComboBoxCascadeBySQL("二级级联",true,true,false,false,true,"formComboboxGroup1","1",new String[] {"cascade1a","cascade1b"},new String[] {"loadMenu","loadMenu"},formComboboxGroup1Configs,formComboboxGroup1ComConfigs);
        controller.addFieldComboBoxCascadeBySQL("三级级联",true,true,false,false,false,"formComboboxGroup2",null,new String[] {"formComboBoxCascade1","formComboBoxCascade2","formComboBoxCascade3"},new String[] {"loadDistrict","loadDistrict","loadDistrict"},formComboboxGroup2Configs,formComboboxGroup2ComConfigs);
        /***********************************************************************************************************
         * 树控件
         */
        String formComboBoxTreeConfigs1 = "labelWidth:100,width:400,multiSelect:true,multiCascade:true,selectedIds:'A,A1,A2,A13'";
        String formComboBoxTreeConfigs2 = "labelWidth:100,width:400,multiSelect:true,multiCascade:false,selectedIds:'A,A2,A132,B1,B4a,C'";
        String formComboBoxTreeConfigs3 = "labelWidth:100,width:400,multiSelect:false";
        String formComboBoxTreeConfigs4 = "labelWidth:100,width:400,multiSelect:true,multiCascade:true";
        String formComboBoxTreeConfigs5 = "labelWidth:100,width:400,multiSelect:true,multiCascade:false";
        String formComboBoxTreeConfigs6 = "labelWidth:100,width:400,multiSelect:false";

        String treeUrl1=  "resource/js/extjs/plugin/tree2.json";
        String treeUrl2 = "resource/js/extjs/plugin/tree2.json";
        String treeUrl3 = "sysMenu/queryMenuByUser.do";
        String treeUrl4 = "exampleBean/loadTreeData.do?fieldMap=id:id,text:text,parentId:fk_parent_id&treeFlag=menu&resType=map&multiSelect=true";
        String treeUrl5 = "exampleBean/loadTreeData.do?fieldMap=id:id,text:text,parentId:fk_parent_id&treeFlag=menu&resType=map&multiSelect=true";
        String treeUrl6 = "exampleBean/loadTreeData.do?fieldMap=id:id,text:name,parentId:parent_id&treeFlag=district&resType=map&multiSelect=false";

        controller.addFieldComboBoxTree( "formComboBoxTree1","树(多选级联)",true,true,true,false,false,formComboBoxTreeConfigs1,treeUrl1 );
        controller.addFieldComboBoxTree( "formComboBoxTree2","树(多选不级联)",true,true,true,false,true,formComboBoxTreeConfigs2,treeUrl2 );
        controller.addFieldComboBoxTree( "formComboBoxTree3","树(单选)",true,true,true,false,true,formComboBoxTreeConfigs3,treeUrl3 );
        controller.addFieldComboBoxTree( "formComboBoxTree4","菜单树(多选级联)",true,true,true,false,true,formComboBoxTreeConfigs4,treeUrl4 );
        controller.addFieldComboBoxTree( "formComboBoxTree5","菜单树(多选不级联)",true,true,true,false,true,formComboBoxTreeConfigs5,treeUrl5 );
        controller.addFieldComboBoxTree( "formComboBoxTree6","行政区划树(单选)",true,true,true,false,true,formComboBoxTreeConfigs6,treeUrl6 );
        /***********************************************************************************************************
         * 富文本编辑器控件
         *
         * htmlEditor没有继承Ext.form.field.Base
         */
        String formHtmlEditorConfigs = "labelWidth:100,width:400,height:200";

        controller.addFieldHtmlEditor("formHtmlEditor","编辑器",true,true,true,false,formHtmlEditorConfigs);
        /***********************************************************************************************************
         * 附件控件
         *
         * extjs会自动判断提交的表单是否包含有附件（判断表单中是否有inputType="file"类型的表单元素，与是否选择了文件无关）
         * ，以此决定是否使用enctype="multipart/form-data"提交表单
         */
        String formFileConfigs = "labelWidth:100,width:400,emptyText:'请选择附件...'";
        /**
         * 上传附件的serviceConfigs为必须项，savePath和allowFileType必须配置。
         * 以下格式的路径都可以，框架会自动处理修正。例：/upload/pic/,/upload/pic,upload/pic/。
         */
        String formFileServiceConfigs1 = "savePath:'/upload/doc/',allowFileType:'doc,docx'";
        String formFileServiceConfigs2 = "savePath:'/upload/txt',allowFileType:'txt'";
        String formFileServiceConfigs3 = "savePath:'upload/pdf',allowFileType:'pdf'";

        controller.addFieldFile("formFile1","附件1",true,true,false,true,formFileConfigs,formFileServiceConfigs1);
        controller.addFieldFile("formFile2","附件2",true,true,false,true,formFileConfigs,formFileServiceConfigs2);
        controller.addFieldFile("formFile3","附件3",true,true,false,true,formFileConfigs,formFileServiceConfigs3);
        /***********************************************************************************************************
         * 面板控件
         */
        String formDisplayConfigs = "labelWidth:100,width:400,value:'管理员',submitValue:true";

        controller.addFieldDisplay("formDisplay","form面板",true,true,true,true,formDisplayConfigs);

        /***********************************************************************************************************
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String insertTimeConfigs = "value:'"+sdf.format( new Date() )+"'";
        String updateTimeConfigs = "value:'"+sdf.format( new Date() )+"'";
        controller.addFieldDatetime("insertTime","添加时间",true,true,true,false,false,insertTimeConfigs);
        controller.addFieldDatetime("updateTime","更新时间",true,true,true,false,false,updateTimeConfigs);
    }

    @Override
    public void initFunction(SysEnController controller) {
        controller.addFunction("menu1","新功能","baseConstant/pass.do",2,true,null,1);
        controller.addFunction("menu2","功能2","baseConstant/notPass.do",2,false,"Applicationgo",2);
    }

    /**
     *具体的业务controller可以覆写BaseController的方法，
     * 以此来自己实现save或update等操作。
     */
   /* @RequestMapping("/save.do")
    public ModelAndView save( HttpServletRequest request, HttpServletResponse response, ExampleBean bean ){
        this.beforeSave();

        ModelAndView modelAndView = new ModelAndView();
        JSONObject jo = JSONObject.fromObject("{}");

        try{

            jo.put("success",true);
            jo.put("msg","保存成功（子类覆写的save方法）！");
        }catch( PlatformException e){
            jo.put("success",false);
            jo.put("msg","保存失败（子类覆写的save方法）！"+e.getMsg() );
        }catch (Exception e){
            e.printStackTrace();
            jo.put("success",false);
            jo.put("msg","保存失败（子类覆写的save方法），发生异常！");
        }

        modelAndView.addObject( PlatformSysConstant.JSONSTR, jo.toString() );
        modelAndView.setViewName( this.getJsonView() );

        this.afterSave();
        return  modelAndView;
    }*/
    @Override
    public List<Map<String,Object>> loadComboBoxDataMap(HttpServletRequest request, HttpServletResponse response, ExampleBean bean,String parentId){
        /**
         * 查询下拉框数据
         *
         * Controller中所有获取下拉框数据的方法都在此方法中实现
         */
        String comboBoxFlag = request.getParameter("comboBoxFlag");
        List<Map<String,Object>> res = null;

        if( "loadMenu".equals( comboBoxFlag ) ){
            res = this.getExampleBeanService().queryMenuComboBoxMap( parentId );
        }else if( "loadDistrict".equals( comboBoxFlag ) ){
            res = this.getExampleBeanService().queryDistrictComboBoxMap( parentId );
        }

        return res;
    }

    @Override
    public List<Map<String,Object>> loadTreeNodeDataMap(HttpServletRequest request, HttpServletResponse response, ExampleBean bean){
        /**
         * 加载树结点需要的数据
         *
         * Controller中所有获取树节点数据的方法都在此方法中实现。
         * 除非自定义了url
         */
        String treeFlag = request.getParameter("treeFlag");
        List<Map<String,Object>> res = null;

        if( "district".equals( treeFlag ) ){//行政区划树
            res = getExampleBeanService().queryDistrictTreeMap();
        }else if( "menu".equals( treeFlag ) ){//菜单树
            res = getExampleBeanService().queryMenuTreeMap();
        }

        return res;
    }


    public ExampleBeanService getExampleBeanService() {
        return exampleBeanService;
    }

    public void setExampleBeanService(ExampleBeanService exampleBeanService) {
        this.exampleBeanService = exampleBeanService;
    }
}
