package com.tgw.dao.example;

import com.tgw.bean.example.ExampleBean;
import com.tgw.bean.example.ExampleBeanFormVal;
import com.tgw.dao.base.BaseModelMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaojg on 2017/08/05.
 */
public interface ExampleBeanFormValMapper extends BaseModelMapper<ExampleBeanFormVal> {

    /**
     * 查询行政区划树节点数据接口
     * @return
     */
    public abstract List<Map<String,Object>> queryDistrictTreeMap();


}
