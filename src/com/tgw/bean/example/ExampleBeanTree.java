package com.tgw.bean.example;

import com.tgw.bean.base.AbstractBaseBean;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by zhaojg on 2017/07/20.
 */
@Table(name="example_bean_tree")
public class ExampleBeanTree extends AbstractBaseBean{
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private String name;
    private Integer parentId;

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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
