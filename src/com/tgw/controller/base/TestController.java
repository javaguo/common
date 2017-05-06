package com.tgw.controller.base;

import com.tgw.bean.base.BaseEnConstant;
import com.tgw.bean.system.SysEnUser;
import com.tgw.service.base.BaseConstantService;
import com.tgw.service.base.TestService;
import com.tgw.service.system.SysEnMenuService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaojg on 2016/10/16.
 */
@Controller
@RequestMapping("/test")
public class TestController extends BaseController<BaseEnConstant>{

    @Resource
    private BaseConstantService baseConstantService;

    @Resource
    private SysEnMenuService sysEnMenuService;

    @Resource
    private TestService testService;


    @RequestMapping("/list.do")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response){
        try {
            /**
             * 测试分页
             */
            this.getTestService().testPage();
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            /**
             * 子类mapper实现父类定义的接口
             */
            this.getTestService().testParentMapper();
        }catch (Exception e){
            e.printStackTrace();
        }

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("menuIdentify","TestList");
        modelAndView.addObject("loadDataUrl","sysMenu/loadData.do");
        modelAndView.addObject("jsonStr", "abd" );

        modelAndView.setViewName( super.getJsonView() );
        return modelAndView;
    }

    @RequestMapping("/testTypeAliasesMap.do")
    public ModelAndView testTypeAliasesMap(HttpServletRequest request, HttpServletResponse response){
        JSONObject jo = JSONObject.fromObject("{}");

        List<Map<String,Object>> tempListMap = new ArrayList<Map<String,Object>>();
        List<Object[]> listObj = new ArrayList<Object[]>();
        try {
            /**
             * 测试   查询结果为map
             * 将查询结果转换为List<Object[]> 形式
             */
            tempListMap = this.getTestService().testTypeAliasesMap();
            jo.put("tempListMap",tempListMap);

            //将查询结果转换为List<Object[]> 形式
            for( Map<String,Object> map : tempListMap ){
                Collection values = map.values();
                List tempList = new ArrayList( values );
                listObj.add ( tempList.toArray() );
            }
            jo.put("listObj",listObj);
        }catch (Exception e){
            e.printStackTrace();
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("jsonStr", jo.toString() );
        modelAndView.setViewName( super.getJsonView() );
        return modelAndView;
    }

    @RequestMapping("/testTypeAliasesList.do")
    public ModelAndView testTypeAliasesList(HttpServletRequest request, HttpServletResponse response){
        JSONObject jo = JSONObject.fromObject("{}");

        /**
         * 查询结果返回List<object[]>，此方法目前不能正确返回结果
         */
        List resList = new ArrayList();
        try {
            resList = this.getTestService().testTypeAliasesList();
        }catch (Exception e){
            e.printStackTrace();
        }

        ModelAndView modelAndView = new ModelAndView();

        jo.put("resList",resList);
        modelAndView.addObject("jsonStr", jo.toString() );
        modelAndView.setViewName( super.getJsonView() );
        return modelAndView;
    }

    /**
     * 访问SearchData前，重新给baseservice赋值
     * @param request
     * @param response
     * @param baseEnConstant
     * @return
     */
    @RequestMapping("/beforeSearchData.do")
    public ModelAndView beforeSearchData(HttpServletRequest request, HttpServletResponse response,BaseEnConstant baseEnConstant){

        ModelAndView modelAndView = new ModelAndView();
        try {
            super.setBaseService( this.getTestService() );
        } catch(Exception e) {
            e.printStackTrace();
        }

        modelAndView.setViewName( super.getJsonView() );
        return modelAndView;
    }


    public BaseConstantService getBaseConstantService() {
        return baseConstantService;
    }

    public void setBaseConstantService(BaseConstantService baseConstantService) {
        this.baseConstantService = baseConstantService;
    }

    public SysEnMenuService getSysEnMenuService() {
        return sysEnMenuService;
    }

    public void setSysEnMenuService(SysEnMenuService sysEnMenuService) {
        this.sysEnMenuService = sysEnMenuService;
    }

    public TestService getTestService() {
        return testService;
    }

    public void setTestService(TestService testService) {
        this.testService = testService;
    }
}
