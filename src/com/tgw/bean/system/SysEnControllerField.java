package com.tgw.bean.system;

import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.bean.system.form.field.SysEnFieldBase;

/**
 * Created by zhaojg on 2016/10/20.
 */
public class SysEnControllerField extends AbstractBaseBean {
    private Integer id;
    private String name;//字段名称
    private String fieldLabel;//字段标签名称
    private String type;//数据类型
    private String xtype;//表单控件类型
    private boolean isValid;//字段是否有效
    private boolean isAllowAdd;//是否可添加
    private boolean isAllowUpdate;//是否可更新
    private boolean isShowList;//是否在列表中显示
    private boolean isAllowSearch;//是否可被搜索
    private boolean isSearByRange;//是否按区间搜索，如果为true，则搜索条件按照大于等于、小于等于查询搜索
    private boolean isAllowBlank;//是否可为空
    private SysEnFieldBase sysEnFieldAttr;//字段具体属性

    public SysEnControllerField(String name, String fieldLabel, String type, String xtype, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate,boolean isShowList, boolean isAllowSearch, boolean isAllowBlank) {
        this.name = name;
        this.fieldLabel = fieldLabel;
        this.type = type;
        this.xtype = xtype;
        this.isValid = isValid;
        this.isAllowAdd = isAllowAdd;
        this.isAllowUpdate = isAllowUpdate;
        this.isShowList = isShowList;
        this.isAllowSearch = isAllowSearch;
        this.isAllowBlank = isAllowBlank;
    }

    public SysEnControllerField(String name, String fieldLabel, String xtype, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate,boolean isShowList, boolean isAllowSearch, boolean isAllowBlank) {
        this.name = name;
        this.fieldLabel = fieldLabel;
        this.xtype = xtype;
        this.isValid = isValid;
        this.isAllowAdd = isAllowAdd;
        this.isAllowUpdate = isAllowUpdate;
        this.isShowList = isShowList;
        this.isAllowSearch = isAllowSearch;
        this.isAllowBlank = isAllowBlank;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getXtype() {
        return xtype;
    }

    public void setXtype(String xtype) {
        this.xtype = xtype;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isAllowAdd() {
        return isAllowAdd;
    }

    public void setAllowAdd(boolean allowAdd) {
        isAllowAdd = allowAdd;
    }

    public boolean isAllowUpdate() {
        return isAllowUpdate;
    }

    public void setAllowUpdate(boolean allowUpdate) {
        isAllowUpdate = allowUpdate;
    }

    public boolean getIsShowList(){
        return isShowList;
    }

    public boolean isShowList() {
        return isShowList;
    }

    public void setShowList(boolean showList) {
        isShowList = showList;
    }

    public boolean isAllowSearch() {
        return isAllowSearch;
    }

    public void setAllowSearch(boolean allowSearch) {
        isAllowSearch = allowSearch;
    }

    public boolean getIsSearByRange() {
        return isSearByRange;
    }

    public boolean isSearByRange() {
        return isSearByRange;
    }

    public void setSearByRange(boolean searByRange) {
        isSearByRange = searByRange;
    }

    public boolean getIsAllowBlank() {
        return isAllowBlank;
    }

    public boolean isAllowBlank() {
        return isAllowBlank;
    }

    public void setAllowBlank(boolean allowBlank) {
        isAllowBlank = allowBlank;
    }

    public SysEnFieldBase getSysEnFieldAttr() {
        return sysEnFieldAttr;
    }

    public void setSysEnFieldAttr(SysEnFieldBase sysEnFieldAttr) {
        this.sysEnFieldAttr = sysEnFieldAttr;
    }
}
