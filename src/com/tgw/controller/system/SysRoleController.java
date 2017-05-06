package com.tgw.controller.system;

import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.controller.base.BaseController;
import com.tgw.service.system.SysEnMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/sysRole")
public class SysRoleController extends BaseController {

    /**
     * 构造块，初始化控制器
     */
    {
        //构造字段
        this.addField("id","ID","string");
        this.addField("roleName","名称","string");
        this.addField("roleStatus","编码","string");
    }

	@Resource
	public SysEnMenuService sysEnMenuService;


    @Override
    public void initSearch(HttpServletRequest request, HttpServletResponse response, AbstractBaseBean bean, ModelAndView modelAndView) {
        super.setBaseService( this.getSysEnMenuService() );

        modelAndView.addObject("menuIdentify","SysRoleList");
        modelAndView.addObject("loadDataUrl","sysRole/searchData.do");
    }

    public SysEnMenuService getSysEnMenuService() {
		return sysEnMenuService;
	}

	public void setSysEnMenuService(SysEnMenuService sysEnMenuService) {
		this.sysEnMenuService = sysEnMenuService;
	}
	
	
}
