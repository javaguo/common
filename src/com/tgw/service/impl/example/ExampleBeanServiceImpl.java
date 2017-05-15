package com.tgw.service.impl.example;

import com.github.pagehelper.Page;
import com.tgw.bean.base.AbstractBaseBean;
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
    public void initSearchData(int pageNum, int pageSize, Object object) {
        System.out.println("----------------- ExampleBeanServiceImpl --> initSearchData-----------------");
        super.setBaseModelMapper( this.getExampleBeanMapper() );
    }

    /*@Override
    public Object loadComboboxData() throws PlatformException {
        List<Map<String,Object>> queryResList =this.getExampleBeanMapper().loadComboboxData();
        //Page queryResPage = (Page) queryResList.;
        //List items = queryResPage.getResult();

        //组装查询结果
        JSONObject jo = JSONObject.fromObject("{}");
        //jo.put("total",queryResList );
        jo.put("comboboxData", queryResList );
        String temp = jo.toString();
        return temp;
    }*/

    public ExampleBeanMapper getExampleBeanMapper() {
        return exampleBeanMapper;
    }

    public void setExampleBeanMapper(ExampleBeanMapper exampleBeanMapper) {
        this.exampleBeanMapper = exampleBeanMapper;
    }
}
