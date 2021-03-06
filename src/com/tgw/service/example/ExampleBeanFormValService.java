package com.tgw.service.example;

import com.tgw.exception.PlatformException;
import com.tgw.service.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * Created by lee on 2017/03/25.
 */
public interface ExampleBeanFormValService extends BaseService{

   /**
     * 查询行政区划树节点数据
     * @return
     * @throws PlatformException
     */
    public abstract List<Map<String,Object>> queryDistrictTreeMap()  throws PlatformException;

}
