package com.tgw.dao.example;

import com.tgw.bean.base.BaseEnConstant;
import com.tgw.bean.example.ExampleBean;
import com.tgw.dao.base.BaseModelMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaojg on 2017/03/25.
 */
public interface ExampleBeanMapper extends BaseModelMapper<ExampleBean> {

    /**
     * 查询列表数据接口
     * @param  value  说明：value可以映数据库中的int和varchar类型。
     *                       sql中使用方式如下：[where menu.fk_parent_id = #{value}]或[where menu.qtip = #{value}]
     *                       fk_parent_id为int 类型，qtip为varchar类型。
     * @return
     */
    public abstract List<Map<String,Object>> loadComboboxData(Object value);

}
