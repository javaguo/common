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
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Service("baseService")
public  class BaseServiceImpl  implements BaseService,Serializable {

//	public Log logger = LogFactory.getLog(this.getClss());

    /**
     * baseModelMapper目前没有实现注入，自动注入时报找不到对应的bean。
     *
     * 注入时的报错信息
     * No qualifying bean of type [com.tgw.dao.base.BaseModelMapper] is defined: expected single matching bean but found 5: baseConstantMapper,testMapper,exampleBeanMapper,sysEnMenuMapper,sysUserMapper
     */
   // @Autowired
//    @Autowired(required=false)
//    @Resource(name="com.tgw.dao.base.BaseModelMapper")
    private BaseModelMapper baseModelMapper;

    @Override
    public void initMapper() {

    }

   /* @Override
    public void initSearchData(int pageNum, int pageSize, Object object) {

    }*/

    @Override
    public Page searchData(int pageNum, int pageSize,Object object) {
        System.out.println("----------------- 父类BaseServiceImpl --> searchData-----------------");

        //this.initSearchData(pageNum,pageSize,object);

        //分页查询数据
        PageHelper.startPage(pageNum,pageSize);
        BaseModelMapper baseModelMappertemp = this.getBaseModelMapper();
        List<Map<String,Object>> queryResList =this.getBaseModelMapper().searchData(object);
        Page queryResPage = (Page) queryResList;

        System.out.println("总数："+queryResPage.getTotal() +"   页数："+queryResPage.getPages()+"    第几页："+queryResPage.getPageNum() + "   每页大小："+queryResPage.getPageSize() );



        return queryResPage;
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
    public void updateBeans(List<Object> beanList) throws PlatformException {
        if( null!=beanList && beanList.size()>0 ){
            for(  Object object : beanList){
                this.getBaseModelMapper().updateByPrimaryKey(object);
            }
        }
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

    @Override
    public Object loadComboboxData(String loadDataMethodName,Object value) throws PlatformException {
        String resultStr;

        Class baseModelMapperClass = this.getBaseModelMapper().getClass();
        try{
            Method method = baseModelMapperClass.getDeclaredMethod(loadDataMethodName,Object.class );
            String paramValue = value==null?null:value.toString();
            List<Map<String,Object>> queryResList = (List<Map<String,Object>>)method.invoke(  this.getBaseModelMapper(),paramValue );

            //组装查询结果
            JSONObject jo = JSONObject.fromObject("{}");
            jo.put("comboboxData", queryResList );
            resultStr = jo.toString();
        }catch (NoSuchMethodException e){
            e.printStackTrace();
            throw new PlatformException("没有找到查询combobox数据的方法。");
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException("查询combobox数据出错。");
        }

        return resultStr;
    }

    @Override
    public List<Map<String,Object>> loadTreeNodeDataMap(String loadDataMethodName) throws PlatformException {
        List<Map<String,Object>> queryResList = null;

        Class baseModelMapperClass = this.getBaseModelMapper().getClass();
        try{
            Method method = baseModelMapperClass.getDeclaredMethod(loadDataMethodName );
           queryResList = (List<Map<String,Object>>)method.invoke(  this.getBaseModelMapper() );
        }catch (NoSuchMethodException e){
            e.printStackTrace();
            throw new PlatformException("没有找到查询Tree数据的方法。");
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException("查询Tree数据出错。");
        }

        return queryResList;
    }

    public BaseModelMapper getBaseModelMapper() {
        return baseModelMapper;
    }

    public void setBaseModelMapper(BaseModelMapper baseModelMapper) {
        this.baseModelMapper = baseModelMapper;
    }
}
