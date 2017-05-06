package com.tgw.bean.system;

import com.tgw.bean.base.AbstractBaseBean;

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
    private boolean isAllowSearch;//是否可被搜索
    private boolean isAllowBlank;//是否可为空
    private String emptyText;//字段提示信息
    private String vtype;//字段正则验证类型

    public SysEnControllerField(String name, String fieldLabel, String type, String xtype, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank, String emptyText, String vtype) {
        this.name = name;
        this.fieldLabel = fieldLabel;
        this.type = type;
        this.xtype = xtype;
        this.isValid = isValid;
        this.isAllowAdd = isAllowAdd;
        this.isAllowUpdate = isAllowUpdate;
        this.isAllowSearch = isAllowSearch;
        this.isAllowBlank = isAllowBlank;
        this.emptyText = emptyText;
        this.vtype = vtype;
    }

    public SysEnControllerField(String name, String fieldLabel, String xtype, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank, String emptyText, String vtype) {
        this.name = name;
        this.fieldLabel = fieldLabel;
        this.xtype = xtype;
        this.isValid = isValid;
        this.isAllowAdd = isAllowAdd;
        this.isAllowUpdate = isAllowUpdate;
        this.isAllowSearch = isAllowSearch;
        this.isAllowBlank = isAllowBlank;
        this.emptyText = emptyText;
        this.vtype = vtype;
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

    public boolean isAllowSearch() {
        return isAllowSearch;
    }

    public void setAllowSearch(boolean allowSearch) {
        isAllowSearch = allowSearch;
    }

    public boolean isAllowBlank() {
        return isAllowBlank;
    }

    public void setAllowBlank(boolean allowBlank) {
        isAllowBlank = allowBlank;
    }

    public String getEmptyText() {
        return emptyText;
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    public String getVtype() {
        return vtype;
    }

    public void setVtype(String vtype) {
        this.vtype = vtype;
    }
}
