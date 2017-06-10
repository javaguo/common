package com.tgw.service.impl.system;

import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.dao.system.SysUserMapper;
import com.tgw.service.impl.base.BaseServiceImpl;
import com.tgw.service.system.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhaojg on 2016/10/11.
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    public SysUserMapper getSysUserMapper() {
        return sysUserMapper;
    }

    public void setSysUserMapper(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }


}
