package com.tgw.service.impl.base;

import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.dao.base.BaseConstantMapper;
import com.tgw.service.base.BaseConstantService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * Created by zhaojg on 2016/10/16.
 */
@Service("baseConstantService")
public class BaseConstantServiceImpl extends BaseServiceImpl implements BaseConstantService {

    @Resource
    private BaseConstantMapper baseEnConstantMapper;

    @Override
    public void initMapper() {
        System.out.println("ExampleBeanServiceImpl-->initMapper()");
        /**
         * 具体业务层必须调用
         */
        if( null!=baseEnConstantMapper ){
            super.setBaseModelMapper( this.getBaseEnConstantMapper() );
        }
    }

    public BaseConstantMapper getBaseEnConstantMapper() {
        return baseEnConstantMapper;
    }

    public void setBaseEnConstantMapper(BaseConstantMapper baseEnConstantMapper) {
        this.baseEnConstantMapper = baseEnConstantMapper;
    }
}
