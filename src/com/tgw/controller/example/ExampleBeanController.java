package com.tgw.controller.example;

import com.tgw.bean.base.BaseEnConstant;
import com.tgw.bean.example.ExampleBean;
import com.tgw.controller.base.BaseController;
import com.tgw.service.example.ExampleBeanService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by zhaojg on 2017/03/25
 */
@Controller
@RequestMapping("/exampleBean")
public class ExampleBeanController extends BaseController<ExampleBean>{

    /**
     * 构造块，初始化控制器
     */
    {
        System.out.println("执行构造块，初始化Controller！");
        //构造字段
        this.addFieldId("id","ID",null);
        this.addFieldHidden( "formHidden","form隐藏域",true,true,true,null );
        String formTextConfig = "labelWidth:150,width:'400',emptyText:'文本提示信息'";
        this.addFieldText("formText","文本",true,true,true,true,true,formTextConfig);
        String formPasswordConfig = "labelWidth:120,width:'300',emptyText:'密码提示信息'";
        this.addFieldPassword("formPassword","密码",true,true,false,false,true,formPasswordConfig);
        this.addFieldTextArea("formTextArea","文本域",true,true,true,true,true,null);
        this.addFieldNumber("formNumber","数字",true,true,true,true,true,null);

        this.addFieldDate("formDate","日期",true,true,true,true,true,null);

        String radioJson = "[{name:'优秀',value:'90'},{name:'良好',value:'80'},{name:'中等',value:'70'},{name:'及格',value:'60'},{name:'差',value:'50'}]";
        this.addFieldRadioInitDataByJson("formRadio","单选",true,true,true,true,true,radioJson,null);

        String checkboxJson = "[{name:'读书',value:'readbook'},{name:'跑步',value:'running'},{name:'游泳',value:'swimming'},{name:'编程',value:'programme'},{name:'游戏',value:'game'}]";
        this.addFieldCheckboxInitDataByJson("formCheckbox","多选",true,true,true,true,true,checkboxJson,null);

        String comboBoxJson = "[{name:'优',value:'90'},{name:'良',value:'80'},{name:'中',value:'70'},{name:'及格',value:'60'},{name:'差',value:'50'}]";
        this.addFieldComboBoxByJSON("formComBox","下拉框(json)",true,true,false,false,true,comboBoxJson,null);
        this.addFieldComboBoxBySQL("formComBoxSql","下拉框(sql)",true,true,false,false,true,"loadComboboxData",null,null);
        this.addFieldComboBoxCascadeBySQL("二级级联",true,true,false,false,true,"formComboboxGroup1","10",new String[] {"cascade1a","cascade1b"},new String[] {"loadComboboxData","loadComboboxData"},null);
        this.addFieldComboBoxCascadeBySQL("三级级联",true,true,false,false,true,"formComboboxGroup2","1",new String[] {"cascade2a","cascade2b","cascade2c"},new String[] {"loadComboboxData","loadComboboxData","loadComboboxData"},null);

        this.addFieldDisplay("formDisplay","form面板",true,true,true,true,null);

        this.addFunction("menu1","功能1","baseConstant/pass.do",2,true,null,1);
        this.addFunction("menu2","功能2","baseConstant/notPass.do",2,false,"Applicationgo",2);

//        this.addField("id","ID","string",null,true,false,false,false,false,null,null);//ID的isValid参数要设置为true
        //this.addField("formHidden","form隐藏域","string","hiddenfield",true,true,true,true,true,null,null);
        //this.addField("formText","文本","string","textfield",true,true,true,true,true,null,null);
        //this.addField("formPassword","密码","string","textfield",true,true,true,true,true,null,null);
        //this.addField("formTextArea","文本域","string","textareafield",true,true,true,true,true,null,null);
//        this.addField("formNumber","formNumber","string",null,true,true,true,true,true,null,null);
//      this.addField("formDate","日期","string","datefield",true,true,true,true,true,null,null);
//      this.addField("formTime","时间","string","timefield",true,true,true,true,true,null,null);
//        this.addField("formDisplay","form面板","string","displayfield",true,true,true,true,true,null,null);

        /*        this.addField("baseByte","字段baseByte","string",null,true,true,true,true,true,null,null);
        this.addField("baseShort","字段baseShort","string",null,true,true,true,true,true,null,null);
        this.addField("baseInt","字段baseInt","string",null,true,true,true,true,true,null,null);
        this.addField("baseLong","字段baseLong","string",null,true,true,true,true,true,null,null);
        this.addField("baseFloat","字段baseFloat","string",null,true,true,true,true,true,null,null);
        this.addField("baseDouble","字段baseDouble","string",null,true,true,true,true,true,null,null);
        this.addField("baseBoolean","字段baseBoolean","string",null,true,true,true,true,true,null,null);
        this.addField("baseChar","字段baseChar","string",null,true,true,true,true,true,null,null);

        this.addField("baseByteObj","字段baseByteObj","string",null,true,true,true,true,true,null,null);
        this.addField("baseShortObj","字段baseShortObj","string",null,true,true,true,true,true,null,null);
        this.addField("baseIntegerObj","字段baseIntegerObj","string",null,true,true,true,true,true,null,null);
        this.addField("baseFloatObj","字段baseFloatObj","string",null,true,true,true,true,true,null,null);
        this.addField("baseDoubleObj","字段baseDoubleObj","string",null,true,true,true,true,true,null,null);
        this.addField("baseBooleanObj","字段baseBooleanObj","string",null,true,true,true,true,true,null,null);
        this.addField("baseStrObj","字段baseStrObj","string",null,true,true,true,true,true,null,null);
        this.addField("baseDateObj","字段baseDateObj","string",null,true,true,true,true,true,null,null);
        this.addField("baseTimeObj","字段baseTimeObj","string",null,true,true,true,true,true,null,null);
        this.addField("baseYearObj","字段baseYearObj","string",null,true,true,true,true,true,null,null);
        this.addField("baseTimestampObj","字段baseTimestampObj","string",null,true,true,true,true,true,null,null);
        this.addField("baseDateTimeObj","字段baseDateTimeObj","string",null,true,true,true,true,true,null,null);*/
    }

    @Resource
    private ExampleBeanService exampleBeanService;


    @Override
    public void beforeSearch(HttpServletRequest request, HttpServletResponse response, ExampleBean bean){
        System.out.println("----------------- ExampleBeanController  beforeSearch -----------------");
    }

    @Override
    public void afterSearch(HttpServletRequest request, HttpServletResponse response, ExampleBean bean){
        System.out.println("----------------- ExampleBeanController  afterSearch -----------------");
    }


    @Override
    public void initSearch(HttpServletRequest request, HttpServletResponse response, ExampleBean bean,ModelAndView modelAndView) {
        System.out.println("----------------- ExampleBeanController  initSearch  -----------------");

        /**
         * 将具体的业务的service对象赋值给baseservice，必须的操作。
         * 要点：
         * 1.BaseController会调用统一的searchData()接口查询具体的业务数据。
         * 2.业务层的service需要覆写baseservice的searchData()接口
         * 3.覆写接口时需要将具体业务的mapper赋值给BaseModelMapper
         * 4.具体业务的mapper文件中实现searchData查询语句
         */
        if( null!=this.getExampleBeanService() ){
            super.setBaseService( this.getExampleBeanService() );
        }else{

        }

        modelAndView.addObject("menuIdentify","ExampleBeanList");// 每一个列表页面的唯一身份id
        modelAndView.addObject("loadDataUrl","exampleBean/searchData.do");//加载列表页面数据的方法
        modelAndView.addObject("controllerBaseUrl","exampleBean");//控制器的请求地址

    }

    public ExampleBeanService getExampleBeanService() {
        return exampleBeanService;
    }

    public void setExampleBeanService(ExampleBeanService exampleBeanService) {
        this.exampleBeanService = exampleBeanService;
    }
}
