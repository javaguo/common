package com.tgw.dao.system;

import com.tgw.bean.system.SysEnMenu;
import com.tgw.dao.base.BaseModelMapper;

import java.util.List;


public interface SysEnMenuMapper extends BaseModelMapper<SysEnMenu> {

	/**
	 * 根据角色查询对应的功能菜单
	 * @param roleId
	 * @return
	 */
	List<SysEnMenu> loadMenuByRole(Long roleId);

}


