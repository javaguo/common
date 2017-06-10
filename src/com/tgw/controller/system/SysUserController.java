package com.tgw.controller.system;

import com.tgw.bean.system.SysEnUser;
import com.tgw.controller.base.BaseController;
import com.tgw.service.system.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/sysUser")
public class SysUserController extends BaseController<SysEnUser> {

	/**
	 * 构造块，初始化控制器
	 */
	{
		//构造字段
		/*this.addField("id","ID","string");
		this.addField("loginName","登录名","string");
		this.addField("password","密码","string");*/
	}

	@Resource
	private SysUserService sysUserService;

	@Override
	public void initSearch(HttpServletRequest request, HttpServletResponse response, SysEnUser bean, ModelAndView modelAndView) {
		super.setBaseService( this.getSysUserService() );

		modelAndView.addObject("menuIdentify","SysEnUserList");
		modelAndView.addObject("loadDataUrl","sysUser/searchData.do");
	}

	public SysUserService getSysUserService() {
		return sysUserService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}
}
