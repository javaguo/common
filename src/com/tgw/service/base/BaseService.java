package com.tgw.service.base;

import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.dao.base.BaseModelMapper;
import com.tgw.exception.PlatformException;

import java.util.List;

public interface BaseService {
    /**
     * 为满足框架需要，具体业务层的service一定要调用此方法。
     * 将具体业务的mapper赋值给baseModelMapper
     */
    public abstract void initMapper();

    /*
     * 初始化查询列表数据方法，目前暂时用不着
     * @param pageNum
     * @param pageSize
     * @param object

    public abstract  void initSearchData(int pageNum,int pageSize,Object object);
*/
    /**
     * 根据条件分页查询列表数据接口
     * @param pageNum
     * @param pageSize
     * @param object
     * @return
     */
    public abstract Object searchData(int pageNum,int pageSize,Object object);

    /**
     * 保存对象
     * @param abstractBaseBean
     */
    public abstract void saveBean(AbstractBaseBean abstractBaseBean);

    /**
     * 更新对象
     * @param object
     * @throws PlatformException
     */
    public abstract void updateBean(Object object) throws PlatformException;

    /**
     * 根据ID查询唯一的对象
     * @param object   给Bean对象设置ID即可
     * @return
     * @throws PlatformException
     */
    public abstract Object selectUniqueBeanByPrimaryKey(Object object) throws PlatformException;

    /**
     * 批量删除bean对象
     * @param beanList
     * @throws PlatformException
     */
    public abstract void deleteBatchBean(List<Object>  beanList) throws PlatformException;

    /**
     * 查询数据
     * @param  loadDataMethodName Mapper查询数据的方法名
     * @param  value  查询条件值，下拉框父value
     * @return
     * @throws PlatformException
     */
    public abstract Object loadComboboxData(String loadDataMethodName,Object value) throws PlatformException;
}
