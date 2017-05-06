package com.tgw.service.impl.example;

import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.dao.example.ExampleBeanMapper;
import com.tgw.service.example.ExampleBeanService;
import com.tgw.service.impl.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * Created by zhaojg on 2017/03/25.
 */
@Service("exampleBeanService")
public class ExampleBeanServiceImpl extends BaseServiceImpl implements ExampleBeanService {

    @Resource
    private ExampleBeanMapper exampleBeanMapper;

    @Override
    public void initSearchData(int pageNum, int pageSize, Object object) {
        System.out.println("----------------- ExampleBeanServiceImpl --> initSearchData-----------------");
        super.setBaseModelMapper( this.getExampleBeanMapper() );
    }

    public ExampleBeanMapper getExampleBeanMapper() {
        return exampleBeanMapper;
    }

    public void setExampleBeanMapper(ExampleBeanMapper exampleBeanMapper) {
        this.exampleBeanMapper = exampleBeanMapper;
    }
}
