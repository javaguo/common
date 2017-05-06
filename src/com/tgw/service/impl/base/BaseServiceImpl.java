package com.tgw.service.impl.base;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.dao.base.BaseModelMapper;
import com.tgw.exception.PlatformException;
import com.tgw.service.base.BaseService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service("baseService")
public class BaseServiceImpl  implements BaseService,Serializable {

//	public Log logger = LogFactory.getLog(this.getClss());

    /**
     * baseModelMapper目前没有实现注入，自动注入时报找不到bean
     */
    private BaseModelMapper baseModelMapper;

    @Override
    public void initSearchData(int pageNum, int pageSize, Object object) {

    }

    @Override
    public Object searchData(int pageNum, int pageSize,Object object) {
        System.out.println("----------------- 父类BaseServiceImpl --> searchData-----------------");

        this.initSearchData(pageNum,pageSize,object);

        //分页查询数据
        PageHelper.startPage(pageNum,pageSize);
        BaseModelMapper baseModelMappertemp = this.getBaseModelMapper();
        List<Map<String,Object>> queryResList =this.getBaseModelMapper().searchData(object);
        Page queryResPage = (Page) queryResList;
        List items = queryResPage.getResult();
        System.out.println("总数："+queryResPage.getTotal() +"   页数："+queryResPage.getPages()+"    第几页："+queryResPage.getPageNum() + "   每页大小："+queryResPage.getPageSize() );

        //组装查询结果
        JSONObject jo = JSONObject.fromObject("{}");
        jo.put("total",queryResPage.getTotal() );
        jo.put("items", items );

        return jo.toString();
    }

    @Override
    public void saveBean(AbstractBaseBean abstractBaseBean) {
        this.getBaseModelMapper().insert(abstractBaseBean);
    }

    @Override
    public void updateBean(Object object) throws PlatformException {
        this.getBaseModelMapper().updateByPrimaryKey(object);
    }

    @Override
    public Object selectUniqueBeanByPrimaryKey(Object object) throws PlatformException {
        Object obj = this.getBaseModelMapper().selectByPrimaryKey(object);
        return obj;
    }

    @Override
    public void deleteBatchBean(List<Object>  beanList) throws PlatformException {
        if( null==beanList || beanList.size()==0 ){
            throw new PlatformException("参数错误，没有要删除的对象！");
        }

        for( Object obj :beanList ){
            this.getBaseModelMapper().deleteByPrimaryKey(obj);
        }
    }

    public BaseModelMapper getBaseModelMapper() {
        return baseModelMapper;
    }

    public void setBaseModelMapper(BaseModelMapper baseModelMapper) {
        this.baseModelMapper = baseModelMapper;
    }
}
