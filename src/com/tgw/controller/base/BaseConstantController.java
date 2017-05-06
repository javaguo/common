package com.tgw.controller.base;

import com.tgw.bean.base.BaseEnConstant;
import com.tgw.bean.example.ExampleBean;
import com.tgw.service.base.BaseConstantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhaojg on 2016/10/16.
 */
@Controller
@RequestMapping("/baseConstant")
public class BaseConstantController extends BaseController<BaseEnConstant>{

    /**
     * 构造块，初始化控制器
     */
    {
        //构造字段
        this.addField("id","ID","string",null,true,false,false,false,false,null,null);
        this.addField("name","名称","string",null,true,true,true,true,true,null,null);
        this.addField("code","编码","string",null,true,true,true,true,true,null,null);
        this.addField("namespace","命名空间","string",null,true,true,false,true,true,null,null);
        this.addField("note","备注","string",null,true,true,true,true,true,null,null);

        this.addFunction("menu1","通过","baseConstant/pass.do",2,true,null,1);
        this.addFunction("menu2","不通过","baseConstant/notPass.do",2,false,"Applicationgo",2);
        this.addFunction("menu3","当前活动批次","baseConstant/activity.do",2,true,"Applicationput",3);
    }

    @Resource
    private BaseConstantService baseConstantService;


    @Override
    public void initSearch(HttpServletRequest request, HttpServletResponse response, BaseEnConstant bean, ModelAndView modelAndView) {
        if( null!=this.getBaseConstantService() ){
            super.setBaseService( this.getBaseConstantService() );
        }else{

        }

        modelAndView.addObject("menuIdentify","BaseConstantList");// 每一个列表页面的唯一身份id
        modelAndView.addObject("loadDataUrl","baseConstant/searchData.do");//加载列表页面数据的方法
        modelAndView.addObject("controllerBaseUrl","baseConstant");//控制器的请求地址
    }

    public BaseConstantService getBaseConstantService() {
        return baseConstantService;
    }

    public void setBaseConstantService(BaseConstantService baseConstantService) {
        this.baseConstantService = baseConstantService;
    }
}
