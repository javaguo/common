package com.tgw.dao.base;

import com.tgw.bean.base.BaseEnConstant;
import com.tgw.bean.system.SysEnMenu;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaojg on 2016/10/19.
 */
public interface TestMapper extends BaseModelMapper<BaseEnConstant> {
     public abstract  List<SysEnMenu> loadMenuByRole(Long roleId);

     public abstract List<Map<String,Object>> testTypeAliasesMap(Long roleId);

     public abstract List<String> testTypeAliasesList(Long roleId);
}
