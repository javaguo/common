package com.tgw.bean.system;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjg on 2017/5/7.
 */
public class SysEnFieldRadioGroup extends SysEnFormField{
    private List<SysEnFieldRadio> radioList = new ArrayList<SysEnFieldRadio>();

    public List<SysEnFieldRadio> getRadioList() {
        return radioList;
    }

    public void setRadioList(List<SysEnFieldRadio> radioList) {
        this.radioList = radioList;
    }
}
