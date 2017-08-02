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
        controller.addFieldText("alpha","alpha",true,true,true,false,false,extConfigsAlpha);

        String extConfigsAlphanum = "vtype:'alphanum'";
        controller.addFieldText("alphanum","alphanum",true,true,true,false,true,extConfigsAlphanum);

        String extConfigsEmail = "vtype:'email'";
        controller.addFieldText("email","email",true,true,true,false,true,extConfigsEmail);

        String extConfigsUrl = "vtype:'url'";
        controller.addFieldText("url","url",true,true,true,false,true,extConfigsUrl);

        String extConfigsLetter = "vtype:'letter'";
        controller.addFieldText("letter","字母",true,true,true,false,true,extConfigsLetter);

        String extConfigsUpperCase = "vtype:'upperCase'";
        controller.addFieldText("upperCase","大写字母",true,true,true,false,true,extConfigsUpperCase);

        String extConfigsLowerCase = "vtype:'lowerCase'";
        controller.addFieldText("lowerCase","小写字母",true,true,true,false,true,extConfigsLowerCase);

        String extConfigsLetterNum = "vtype:'letterNum'";
        controller.addFieldText("letterNum","字母数字",true,true,true,false,true,extConfigsLetterNum);

        String extConfigsLetterNumNnderline = "vtype:'letterNumUnderline'";
        controller.addFieldText("letterNumUnderline","字母数字下划线",true,true,true,false,true,extConfigsLetterNumNnderline);

        String extConfigsChineseletterNum = "vtype:'chineseLetterNum'";
        controller.addFieldText("chineseLetterNum","汉字字母数字",true,true,true,false,true,extConfigsChineseletterNum);

        String extConfigsChineseletterNumNnderline = "vtype:'chineseLetterNumUnderline'";
        controller.addFieldText("chineseLetterNumUnderline","汉字字母数字下划线",true,true,true,false,true,extConfigsChineseletterNumNnderline);

        String extConfigsChinese = "vtype:'chinese'";
        controller.addFieldText("chinese","汉字",true,true,true,false,true,extConfigsChinese);

        String extConfigsCharacter50 = "vtype:'character50'";
        controller.addFieldText("character50","50个字符",true,true,true,false,true,extConfigsCharacter50);

        String extConfigsEmailPlatform = "vtype:'emailPlatform'";
        controller.addFieldText("emailPlatform","自定义email",true,true,true,false,true,extConfigsEmailPlatform);

        String extConfigsMobileNo = "vtype:'mobileNo'";
        controller.addFieldText("mobileNo","手机号",true,true,true,false,true,extConfigsMobileNo);

        String extConfigsPhoneNo = "vtype:'phoneNo'";
        controller.addFieldText("phoneNo","电话号",true,true,true,false,true,extConfigsPhoneNo);

        String extConfigsPhoneNoMobileNo = "vtype:'phoneNoMobileNo'";
        controller.addFieldText("phoneNoMobileNo","电话或手机号",true,true,true,false,true,extConfigsPhoneNoMobileNo);

        String extConfigsIDNumber15 = "vtype:'IDNumber15'";
        controller.addFieldText("IDNumber15","15位身份证",true,true,true,false,true,extConfigsIDNumber15);

        String extConfigsIDNumber18 = "vtype:'IDNumber18'";
        controller.addFieldText("IDNumber18","18位身份证",true,true,true,false,true,extConfigsIDNumber18);

        String extConfigsIDNumber = "vtype:'IDNumber'";
        controller.addFieldText("IDNumber","身份证",true,true,true,false,true,extConfigsIDNumber);

        String extConfigsDateYMD = "vtype:'dateYMD'";
        controller.addFieldText("dateYMD","年月日",true,true,true,false,true,extConfigsDateYMD);

        String extConfigsQQ = "vtype:'QQ'";
        controller.addFieldText("QQ","QQ",true,true,true,false,true,extConfigsQQ);

        String extConfigsPostCode = "vtype:'postCode'";
        controller.addFieldText("postCode","邮政编码",true,true,true,false,true,extConfigsPostCode);

        String extConfigsIP = "vtype:'IP'";
        controller.addFieldText("IP","IP地址",true,true,true,false,true,extConfigsIP);

        String extConfigsAccountNumber = "vtype:'accountNumber'";
        controller.addFieldText("accountNumber","账号",true,true,true,false,true,extConfigsAccountNumber);

        String extConfigsDefaultPassword = "vtype:'generalPassword'";
        controller.addFieldPassword("generalPassword","普通密码",true,true,true,false,true,extConfigsDefaultPassword);

        String extConfigsStrongPassword = "vtype:'strongPassword'";
        controller.addFieldPassword("strongPassword","强密码",true,true,true,false,true,extConfigsStrongPassword);
        /***********************************************************************************************************
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String insertTimeConfigs = "value:'"+sdf.format( new Date() )+"'";
        String updateTimeConfigs = "value:'"+sdf.format( new Date() )+"'";
        controller.addFieldDatetime("insertTime","添加时间",true,true,false,false,false,insertTimeConfigs);
        controller.addFieldDatetime("updateTime","更新时间",true,true,true,false,false,updateTimeConfigs);
    }


    public ExampleBeanService getExampleBeanService() {
        return exampleBeanService;
    }

    public void setExampleBeanService(ExampleBeanService exampleBeanService) {
        this.exampleBeanService = exampleBeanService;
    }
}
