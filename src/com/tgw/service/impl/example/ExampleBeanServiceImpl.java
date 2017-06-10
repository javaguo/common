package com.tgw.service.impl.example;

import com.github.pagehelper.Page;
import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.dao.base.BaseModelMapper;
import com.tgw.dao.example.ExampleBeanMapper;
import com.tgw.exception.PlatformException;
import com.tgw.service.example.ExampleBeanService;
import com.tgw.service.impl.base.BaseServiceImpl;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by zhaojg on 2017/03/25.
 */
@Service("exampleBeanService")
public class ExampleBeanServiceImpl extends BaseServiceImpl implements ExampleBeanService {

    @Resource
    private ExampleBeanMapper exampleBeanMapper;

    @Override
    public void initMapper() {
        System.out.println("ExampleBeanServiceImpl-->initMapper()");
        /**
         * 具体业务层必须调用
         */
        if( null!=exampleBeanMapper ){
            super.setBaseModelMapper( this.getExampleBeanMapper() );
        }
    }

    public ExampleBeanMapper getExampleBeanMapper() {
        return exampleBeanMapper;
    }

    public void setExampleBeanMapper(ExampleBeanMapper exampleBeanMapper) {
        this.exampleBeanMapper = exampleBeanMapper;
    }
}
