package com.tgw.service.base;

import com.tgw.bean.base.AbstractBaseBean;

import java.util.List;
import java.util.Map;

/**
 * Created by lee on 2016/10/16.
 */
public interface TestService extends BaseService{
    public abstract  void testPage();

    public abstract List<Map<String,Object>> testTypeAliasesMap();

    public abstract List testTypeAliasesList();

    public void testParentMapper();

}
