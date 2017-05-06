package com.tgw.dao.base;

import com.tgw.bean.base.AbstractBaseBean;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaojg on 2016/10/10.
 */
public interface BaseModelMapper<T> extends Mapper<T>,MySqlMapper<T> {
    /**
     * 查询列表数据接口
     * @return
     */
    public abstract List<Map<String,Object>> searchData(Object object);
}