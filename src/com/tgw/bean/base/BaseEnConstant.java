package com.tgw.bean.base;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zhaojg on 2016/10/16.
 */
@Table(name="bas_en_constant")
public class BaseEnConstant extends AbstractBaseBean{

    @Id
    @GeneratedValue(generator = "JDBC")
    public Integer id;
    private String name;
    private String code;
    private String namespace;
    private String note;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
