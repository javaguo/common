package com.tgw.bean.system;

import com.tgw.bean.base.AbstractBaseBean;

/**
 * Created by zhaojg on 2016/10/20.
 */
public class SysEnControllerFunction extends AbstractBaseBean {
    private Integer id;
    private String  identify;//菜单英文标识
    private String  name;//菜单名称
    private String  url;//菜单请求地址
    private String  type;//菜单类型
    /**
     * 1：基本的添加方法
     * 2：基本的ajax请求方法
     */
    private Integer  typeNum;///菜单类型编码
    private boolean isSingle;//是否一次只能操作列表中的一条数据
    private String  iconCls;//菜单图标样式，参考\resource\js\extjs\extjs5\resources\css\icon.css
    private int  orderNum;//排序标识

    public SysEnControllerFunction(String identify,String name, String url, String type,boolean isSingle,String iconCls, int orderNum) {
        this.identify = identify;
        this.name = name;
        this.url = url;
        this.type = type;
        this.isSingle = isSingle;
        this.iconCls = iconCls;
        this.orderNum = orderNum;
    }

    public SysEnControllerFunction(String identify, String name, String url, Integer typeNum,boolean isSingle, String iconCls, int orderNum) {
        this.identify = identify;
        this.name = name;
        this.url = url;
        this.typeNum = typeNum;
        this.isSingle = isSingle;
        this.iconCls = iconCls;
        this.orderNum = orderNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTypeNum() {
        return typeNum;
    }

    public void setTypeNum(Integer typeNum) {
        this.typeNum = typeNum;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public boolean getIsSingle() {
        return isSingle;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
