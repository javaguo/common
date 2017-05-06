package com.tgw.service.impl.system;

import com.tgw.bean.system.SysEnMenu;
import com.tgw.dao.system.SysEnMenuMapper;
import com.tgw.service.impl.base.BaseServiceImpl;
import com.tgw.service.system.SysEnMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("sysEnMenuService")
public class SysEnMenuServiceImpl extends BaseServiceImpl implements SysEnMenuService {

	@Resource
	private SysEnMenuMapper sysEnMenuMapper;
	
	@Override
	public List<SysEnMenu> loadMenuByRole(String roleId) {
		List result = this.getSysEnMenuMapper().loadMenuByRole( Long.parseLong(roleId) );
		return result;
	}

	public SysEnMenuMapper getSysEnMenuMapper() {
		return sysEnMenuMapper;
	}

	public void setSysEnMenuMapper(SysEnMenuMapper sysEnMenuMapper) {
		this.sysEnMenuMapper = sysEnMenuMapper;
	}

}
