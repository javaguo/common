package com.tgw.controller.example;

import com.tgw.bean.example.ExampleBean;
import com.tgw.bean.system.SysEnController;
import com.tgw.controller.base.BaseController;
import com.tgw.service.example.ExampleBeanService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaojg on 2017/08/01
 */
@Controller
@RequestMapping("/exampleBeanFormVal")
public class ExampleBeanFormValController extends BaseController<ExampleBean>{

    @Resource
    private ExampleBeanService exampleBeanService;

    @Override
    public void initSearch(HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView,SysEnController controller, ExampleBean bean ) {

        if( null!=this.getExampleBeanService() ){
            super.initService(  this.getExampleBeanService()  );
        }else{

        }

        controller.setIdentifier( "ExampleBeanFormVal" );// 每一个列表页面的唯一身份id
        controller.setLoadDataUrl( "exampleBeanFormVal/searchData.do" );//加载列表页面数据的方法
        controller.setControllerBaseUrl( "exampleBeanFormVal" );//控制器的请求地址

        //此处的配置会覆盖jsp页面中默认的配置
        String addWindowConfigs = "title: '添加窗口-表单验证示例'";
        String editWindowConfigs = "title: '编辑窗口-表单验证示例'";
        controller.addWindowConfig( addWindowConfigs,editWindowConfigs );
    }

    @Override
    public void initField( SysEnController controller ) {
        //构造字段
        controller.addFieldId("id","ID",null);

        /***********************************************************************************************************
         * 表单验证示例
         */

        String extConfigsAlpha = "vtype:'alpha'";
        controller.addFieldText("alpha","alpha",true,true,true,true,false,false,extConfigsAlpha);

        String extConfigsAlphanum = "vtype:'alphanum'";
        controller.addFieldText("alphanum","alphanum",true,true,true,true,false,false,extConfigsAlphanum);

        String extConfigsEmail = "vtype:'email'";
        controller.addFieldText("email","email",true,true,true,true,false,false,extConfigsEmail);

        String extConfigsUrl = "vtype:'url'";
        controller.addFieldText("url","url",true,true,true,true,false,false,extConfigsUrl);

        String extConfigsLetter = "vtype:'letter'";
        controller.addFieldText("letter","letter",true,true,true,true,false,false,extConfigsLetter);

        String extConfigsUpperCase = "vtype:'upperCase'";
        controller.addFieldText("upperCase","upperCase",true,true,true,true,false,false,extConfigsUpperCase);

        String extConfigsLowerCase = "vtype:'lowerCase'";
        controller.addFieldText("lowerCase","lowerCase",true,true,true,true,false,false,extConfigsLowerCase);

        String extConfigsLetterNum = "vtype:'letterNum'";
        controller.addFieldText("letterNum","letterNum",true,true,true,true,false,false,extConfigsLetterNum);

        String extConfigsLetterNumNnderline = "vtype:'letterNumNnderline'";
        controller.addFieldText("letterNumNnderline","letterNumNnderline",true,true,true,true,false,false,extConfigsLetterNumNnderline);

        String extConfigsChineseletterNum = "vtype:'chineseletterNum'";
        controller.addFieldText("chineseletterNum","chineseletterNum",true,true,true,true,false,false,extConfigsChineseletterNum);

        String extConfigsChineseletterNumNnderline = "vtype:'chineseletterNumNnderline'";
        controller.addFieldText("chineseletterNumNnderline","chineseletterNumNnderline",true,true,true,true,false,false,extConfigsChineseletterNumNnderline);

        String extConfigsChinese = "vtype:'chinese'";
        controller.addFieldText("chinese","chinese",true,true,true,true,false,false,extConfigsChinese);

        String extConfigsCharacter50 = "vtype:'character50'";
        controller.addFieldText("character50","character50",true,true,true,true,false,false,extConfigsCharacter50);

        String extConfigsEmailPlatform = "vtype:'emailPlatform'";
        controller.addFieldText("emailPlatform","emailPlatform",true,true,true,true,false,false,extConfigsEmailPlatform);

        String extConfigsMobileNo = "vtype:'mobileNo'";
        controller.addFieldText("mobileNo","mobileNo",true,true,true,true,false,false,extConfigsMobileNo);

        String extConfigsPhoneNo = "vtype:'phoneNo'";
        controller.addFieldText("phoneNo","phoneNo",true,true,true,true,false,false,extConfigsPhoneNo);

        String extConfigsPhoneNoMobileNo = "vtype:'phoneNoMobileNo'";
        controller.addFieldText("phoneNoMobileNo","phoneNoMobileNo",true,true,true,true,false,false,extConfigsPhoneNoMobileNo);

        String extConfigsIDNumber15 = "vtype:'IDNumber15'";
        controller.addFieldText("IDNumber15","IDNumber15",true,true,true,true,false,false,extConfigsIDNumber15);

        String extConfigsIDNumber18 = "vtype:'IDNumber18'";
        controller.addFieldText("IDNumber18","IDNumber18",true,true,true,true,false,false,extConfigsIDNumber18);

        String extConfigsIDNumber = "vtype:'IDNumber'";
        controller.addFieldText("IDNumber","IDNumber",true,true,true,true,false,false,extConfigsIDNumber);

        String extConfigsDateYMD = "vtype:'dateYMD'";
        controller.addFieldText("dateYMD","dateYMD",true,true,true,true,false,false,extConfigsDateYMD);

        String extConfigsQQ = "vtype:'QQ'";
        controller.addFieldText("QQ","QQ",true,true,true,true,false,false,extConfigsQQ);

        String extConfigsPostCode = "vtype:'postCode'";
        controller.addFieldText("postCode","postCode",true,true,true,true,false,false,extConfigsPostCode);

        String extConfigsIP = "vtype:'IP'";
        controller.addFieldText("IP","IP",true,true,true,true,false,false,extConfigsIP);

        String extConfigsAccountNumber = "vtype:'accountNumber'";
        controller.addFieldText("accountNumber","accountNumber",true,true,true,true,false,false,extConfigsAccountNumber);

        String extConfigsDefaultPassword = "vtype:'defaultPassword'";
        controller.addFieldPassword("defaultPassword","defaultPassword",true,true,true,false,false,extConfigsDefaultPassword);

        String extConfigsStrongPassword = "vtype:'strongPassword'";
        controller.addFieldPassword("strongPassword","strongPassword",true,true,true,false,false,extConfigsStrongPassword);
        /***********************************************************************************************************
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String insertTimeConfigs = "value:'"+sdf.format( new Date() )+"'";
        String updateTimeConfigs = "value:'"+sdf.format( new Date() )+"'";
        controller.addFieldDatetime("insertTime","添加时间",true,true,false,false,false,insertTimeConfigs);
        controller.addFieldDatetime("updateTime","更新时间",true,true,true,false,false,updateTimeConfigs);
    }

    @Override
    public void initFunction(SysEnController controller) {
        controller.addFunction("menu1","新功能","baseConstant/pass.do",2,true,null,1);
        controller.addFunction("menu2","功能2","baseConstant/notPass.do",2,false,"Applicationgo",2);
    }


    public ExampleBeanService getExampleBeanService() {
        return exampleBeanService;
    }

    public void setExampleBeanService(ExampleBeanService exampleBeanService) {
        this.exampleBeanService = exampleBeanService;
    }
}
