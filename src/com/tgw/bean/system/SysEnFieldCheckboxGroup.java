package com.tgw.bean.system;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjg on 2017/5/7.
 */
public class SysEnFieldCheckboxGroup extends SysEnFormField{
    private List<SysEnFieldCheckbox> checkboxList = new ArrayList<SysEnFieldCheckbox>();

    public List<SysEnFieldCheckbox> getCheckboxList() {
        return checkboxList;
    }

    public void setCheckboxList(List<SysEnFieldCheckbox> checkboxList) {
        this.checkboxList = checkboxList;
    }
}
