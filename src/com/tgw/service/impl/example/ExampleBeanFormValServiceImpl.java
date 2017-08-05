package com.tgw.service.impl.example;

import com.tgw.dao.example.ExampleBeanFormValMapper;
import com.tgw.exception.PlatformException;
import com.tgw.service.example.ExampleBeanFormValService;
import com.tgw.service.impl.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by zhaojg on 2017/03/25.
 */
@Service("exampleBeanFormValService")
public class ExampleBeanFormValServiceImpl extends BaseServiceImpl implements ExampleBeanFormValService {

    @Resource
    private ExampleBeanFormValMapper exampleBeanFormValMapper;

    @Override
    public void initMapper() {

        if( null!=exampleBeanFormValMapper ){
            super.setBaseModelMapper( this.getExampleBeanFormValMapper() );
        }
    }

    @Override
    public List<Map<String,Object>> queryDistrictTreeMap()  throws PlatformException{
        return this.getExampleBeanFormValMapper().queryDistrictTreeMap();
    }

    public ExampleBeanFormValMapper getExampleBeanFormValMapper() {
        return exampleBeanFormValMapper;
    }

    public void setExampleBeanFormValMapper(ExampleBeanFormValMapper exampleBeanFormValMapper) {
        this.exampleBeanFormValMapper = exampleBeanFormValMapper;
    }
}
