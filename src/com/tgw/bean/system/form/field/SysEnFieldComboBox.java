package com.tgw.bean.system.form.field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjg on 2017/5/13.
 */
public class SysEnFieldComboBox extends SysEnFieldPicker {

    private String comboBoxName;//下拉框名称
    /**
     * 加载下拉框数据的实现方式，两种实现方式：
     * 1.通过sql查询数据库（mapper层写一个查询数据的方法，将mapper的接口方法名赋值给loadDataMethodName）
     * 2.通过json字符串
     * 单个下拉框有两种实现方式获取数据
     * 级联下拉框只能通过sql查询数据库获取数据
     */
    private String loadDataImplModel;//值为sql或json
    private String loadDataMethodName;//查询下拉框数据的Mapper方法名
    private String firstComboBoxParamValue;//初始化下拉框的参数值，loadDataImplModel值为sql时使用
    private List<SysEnFieldComboBoxOption> comboBoxOptionList = new ArrayList<SysEnFieldComboBoxOption>();

    private boolean isCascade;//是否是级联下拉框
    private int comboBoxOrder;//级联下拉框顺序
    private String parentComboBox;//父级联下拉框名称
    private String childComboBox;//子级联下拉框名称
    private SysEnFieldComboBox parentSysEnFieldComboBox;
    private SysEnFieldComboBox childSysEnFieldComboBox;
    //下拉框的所有后续子级联框
    private List<SysEnFieldComboBox> cascadeList = new ArrayList<SysEnFieldComboBox>();
    private boolean isFirst;
    private boolean isLast;


    public String getComboBoxName() {
        return comboBoxName;
    }

    public void setComboBoxName(String comboBoxName) {
        this.comboBoxName = comboBoxName;
    }

    public String getLoadDataImplModel() {
        return loadDataImplModel;
    }

    public void setLoadDataImplModel(String loadDataImplModel) {
        this.loadDataImplModel = loadDataImplModel;
    }

    public String getLoadDataMethodName() {
        return loadDataMethodName;
    }

    public void setLoadDataMethodName(String loadDataMethodName) {
        this.loadDataMethodName = loadDataMethodName;
    }

    public String getFirstComboBoxParamValue() {
        return firstComboBoxParamValue;
    }

    public void setFirstComboBoxParamValue(String firstComboBoxParamValue) {
        this.firstComboBoxParamValue = firstComboBoxParamValue;
    }

    public List<SysEnFieldComboBoxOption> getComboBoxOptionList() {
        return comboBoxOptionList;
    }

    public void setComboBoxOptionList(List<SysEnFieldComboBoxOption> comboBoxOptionList) {
        this.comboBoxOptionList = comboBoxOptionList;
    }

    public boolean isCascade() {
        return isCascade;
    }


    public boolean getIsCascade() {
        return isCascade;
    }

    public void setCascade(boolean cascade) {
        isCascade = cascade;
    }

    public int getComboBoxOrder() {
        return comboBoxOrder;
    }

    public void setComboBoxOrder(int comboBoxOrder) {
        this.comboBoxOrder = comboBoxOrder;
    }

    public String getParentComboBox() {
        return parentComboBox;
    }

    public void setParentComboBox(String parentComboBox) {
        this.parentComboBox = parentComboBox;
    }

    public String getChildComboBox() {
        return childComboBox;
    }

    public SysEnFieldComboBox getParentSysEnFieldComboBox() {
        return parentSysEnFieldComboBox;
    }

    public void setParentSysEnFieldComboBox(SysEnFieldComboBox parentSysEnFieldComboBox) {
        this.parentSysEnFieldComboBox = parentSysEnFieldComboBox;
    }

    public SysEnFieldComboBox getChildSysEnFieldComboBox() {
        return childSysEnFieldComboBox;
    }

    public void setChildSysEnFieldComboBox(SysEnFieldComboBox childSysEnFieldComboBox) {
        this.childSysEnFieldComboBox = childSysEnFieldComboBox;
    }

    public List<SysEnFieldComboBox> getCascadeList() {
        return cascadeList;
    }

    public void setCascadeList(List<SysEnFieldComboBox> cascadeList) {
        this.cascadeList = cascadeList;
    }

    public void setChildComboBox(String childComboBox) {
        this.childComboBox = childComboBox;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean getIsFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public boolean getIsLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}