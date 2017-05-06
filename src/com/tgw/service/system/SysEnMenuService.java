package com.tgw.service.system;

import java.util.List;

import com.tgw.bean.system.SysEnMenu;
import com.tgw.service.base.BaseService;

public interface SysEnMenuService  extends BaseService {
	/**
	 * 根据角色查询对应的功能菜单
	 * @param roleId
	 * @return
	 */
	List<SysEnMenu> loadMenuByRole(String roleId);
}
