package com.tgw.bean.system;

import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.bean.system.form.field.*;
import com.tgw.exception.PlatformException;
import com.tgw.utils.PlatformUtils;
import com.tgw.utils.config.PlatformSysConstant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaojg on 2016/10/20.
 */
public class SysEnController extends AbstractBaseBean {
    private Integer id;
    private String name;
    private String identifier;

    private List<SysEnControllerField> sysEnControllerFieldList = new ArrayList<SysEnControllerField>();
    private List<SysEnControllerFunction> sysEnControllerFunctionList = new ArrayList<SysEnControllerFunction>();

    /**
     * 暂时先保留此方法，框架编写完成后删除
     * 添加列表页面每一个字段的相关属性
     * @param name
     * @param fieldLabel
     * @param type
     * @param xtype
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param emptyText
     * @param vtype
     */
    /*public void addField(String name, String fieldLabel, String type, String xtype, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank, String emptyText, String vtype){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,type,xtype,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank);
        this.getSysEnControllerFieldList().add( sysEnControllerField );
    }*/



    /**
     * 暂时先保留此方法，框架编写完成后删除
     * 添加列表页面每一个字段的相关属性
     * @param name
     * @param fieldLabel
     * @param type
     */
    /*public void addField( String name, String fieldLabel, String type ){
        this.addField(name,fieldLabel,type,null,true,true,true,true,true,null,null);
    }*/

    /**
     * 将JSONObject的键值赋值给SysEnFormField对应的字段
     * @param sysEnField
     * @param jo
     */
    public void dealFormFieldAttr(SysEnFieldBase sysEnField, JSONObject jo ){
        Class fieldClass = sysEnField.getClass();

        SysEnFieldBase formFieldBase = new SysEnFieldBase();
        Class baseClass = formFieldBase.getClass();
        String baseClassInfo = baseClass.toString();

        boolean endFlag = false;
        while( !endFlag ){
            /**
             *getDeclaredFields只能获取类本身声明的字段，父类声明的字段获取不到
             * 所以要通过循环设置所有的字段属性值
             */
            Field[] fields = fieldClass.getDeclaredFields();
            for( Field field : fields ){
                try{
                    String fieldName = field.getName();
                    if( jo.containsKey(fieldName) ){
                        Method method = fieldClass.getDeclaredMethod( "set"+ PlatformUtils.firstLetterToUpperCase( fieldName ),String.class );
                        method.invoke(sysEnField,jo.get(fieldName).toString() );
                    }
                }catch( Exception e ){
                    e.printStackTrace();
                }
            }

            String fieldClassInfo = fieldClass.toString();
            if( baseClassInfo.equals( fieldClassInfo ) ){
                endFlag = true;
            }else{
                fieldClass = fieldClass.getSuperclass();
            }
        }

    }

    /**
     * 添加配置ID字段
     * @param name
     * @param fieldLabel
     * @param configs
     */
    public void addFieldId( String name, String fieldLabel,String configs ){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel, PlatformSysConstant.FORM_XTYPE_HIDDEN,true,false,false,false,false);

        SysEnFieldBase formField = new SysEnFieldBase();
        formField.setConfigs( configs );
		/*if( StringUtils.isNotBlank( configs ) ){
			JSONObject jo = JSONObject.fromObject( "{"+configs+"}" );
			this.dealFormFieldAttr( formField,jo );
		}*/

        sysEnControllerField.setSysEnFieldAttr( formField );
        this.getSysEnControllerFieldList().add( sysEnControllerField );
    }

    /**
     * 添加一个隐藏域
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param configs
     */
    public void addFieldHidden( String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate,String configs){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,PlatformSysConstant.FORM_XTYPE_HIDDEN,isValid,isAllowAdd,isAllowUpdate,false,true);

        SysEnFieldHidden formField = new SysEnFieldHidden();
        formField.setConfigs( configs );
		/*if( StringUtils.isNotBlank(configs) ){
			JSONObject jo = JSONObject.fromObject( "{"+ configs +"}" );
			this.dealFormFieldAttr( formField,jo );
		}*/

        sysEnControllerField.setSysEnFieldAttr( formField );
        this.getSysEnControllerFieldList().add( sysEnControllerField );
    }

    /**
     * 添加一个text字段
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     */
    public void addFieldText( String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank ){
        String tempConfigs = "inputType:'"+PlatformSysConstant.FORM_INPUTTYPE_TEXT+"'";
        this.addFieldText(name,fieldLabel,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,tempConfigs);
    }

    /**
     * 添加一个text字段
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param emptyText
     * @param vtype
     */
    public void addFieldText( String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank, String emptyText, String vtype ){
        String tempConfigs = "inputType:'"+PlatformSysConstant.FORM_INPUTTYPE_TEXT+"',emptyText:'"+emptyText+"',"+"vtype:'"+vtype+"'";
        this.addFieldText(name,fieldLabel,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,tempConfigs);
    }

    /**
     * 添加一个text字段
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param emptyText
     * @param vtype
     * @param configs
     */
    public void addFieldText( String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank, String emptyText, String vtype,String configs ){
        String tempConfigs = "inputType:'"+PlatformSysConstant.FORM_INPUTTYPE_TEXT+"',emptyText:'"+emptyText+"',"+"vtype:'"+vtype+"',"+configs;
        this.addFieldText(name,fieldLabel,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,tempConfigs);
    }

    /**
     * 添加一个Password字段
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     */
    public void addFieldPassword( String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank ){
        String tempConfigs = "inputType:'"+PlatformSysConstant.FORM_INPUTTYPE_PASSWORD+"'";
        this.addFieldText(name,fieldLabel,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,tempConfigs);
    }

    /**
     * 添加一个Password字段
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param emptyText
     * @param vtype
     */
    public void addFieldPassword( String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank, String emptyText, String vtype ){
        String tempConfigs = "inputType:'"+PlatformSysConstant.FORM_INPUTTYPE_PASSWORD+"',emptyText:'"+emptyText+"',"+"vtype:'"+vtype+"'";
        this.addFieldText(name,fieldLabel,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,tempConfigs);
    }

    /**
     * 添加一个Password字段
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param emptyText
     * @param vtype
     * @param configs
     */
    public void addFieldPassword( String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank, String emptyText, String vtype,String configs ){
        String tempConfigs = "inputType:'"+PlatformSysConstant.FORM_INPUTTYPE_PASSWORD+"',emptyText:'"+emptyText+"',"+"vtype:'"+vtype+"',"+configs;
        this.addFieldText(name,fieldLabel,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,tempConfigs);
    }

    /**
     * 添加一个Password字段
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param configs
     */
    public void addFieldPassword( String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String configs ){
        String tempConfigs = "inputType:'"+PlatformSysConstant.FORM_INPUTTYPE_PASSWORD+"',"+configs;
        this.addFieldText(name,fieldLabel,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,tempConfigs);
    }

    /**
     * 添加一个Text字段
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param configs
     */
    public void addFieldText( String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String configs ){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,PlatformSysConstant.FORM_XTYPE_TEXT,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank);

        SysEnFieldText formField = new SysEnFieldText();
        if( StringUtils.isNotBlank( configs ) ){
            configs = "allowBlank:"+isAllowBlank+","+configs;
        }else{
            configs = "allowBlank:"+isAllowBlank;
        }

        formField.setConfigs( configs );
		/*if( StringUtils.isNotBlank( configs ) ){
			JSONObject jo = JSONObject.fromObject( "{"+configs+"}" );
			this.dealFormFieldAttr( formField,jo );
		}*/

        sysEnControllerField.setSysEnFieldAttr( formField );
        this.getSysEnControllerFieldList().add( sysEnControllerField );
    }

    /**
     * 添加一个TextArea字段
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param configs
     */
    public void addFieldTextArea( String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String configs ){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,PlatformSysConstant.FORM_XTYPE_TEXTAREA,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank);

        SysEnFieldTextArea formField = new SysEnFieldTextArea();
        if( StringUtils.isNotBlank( configs ) ){
            configs = "allowBlank:"+isAllowBlank+","+configs;
        }else{
            configs = "allowBlank:"+isAllowBlank;
        }

        formField.setConfigs( configs );
		/*if( StringUtils.isNotBlank( configs ) ){
			JSONObject jo = JSONObject.fromObject( "{"+configs+"}" );
			this.dealFormFieldAttr( formField,jo );
		}*/

        sysEnControllerField.setSysEnFieldAttr( formField );
        this.getSysEnControllerFieldList().add( sysEnControllerField );
    }

    /**
     * 添加一个Number字段
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param configs
     */
    public void addFieldNumber(String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String configs){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,PlatformSysConstant.FORM_XTYPE_NUMBER,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank);

        SysEnFieldNumber formField = new SysEnFieldNumber();
        if( StringUtils.isNotBlank( configs ) ){
            configs = "allowBlank:"+isAllowBlank+","+configs;
        }else{
            configs = "allowBlank:"+isAllowBlank;
        }

        formField.setConfigs( configs );
		/*if( StringUtils.isNotBlank( configs ) ){
			JSONObject jo = JSONObject.fromObject( "{"+configs+"}" );
			this.dealFormFieldAttr( formField,jo );
		}*/

        sysEnControllerField.setSysEnFieldAttr( formField );
        this.getSysEnControllerFieldList().add( sysEnControllerField );
    }

    /**
     * 添加一个Date字段
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param configs
     */
    public void addFieldDate(String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String configs){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,PlatformSysConstant.FORM_XTYPE_DATE,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank);

        SysEnFieldDate fieldDate = new SysEnFieldDate();
        if( StringUtils.isNotBlank( configs ) ){
            configs = "allowBlank:"+isAllowBlank+","+configs;
        }else{
            configs = "allowBlank:"+isAllowBlank;
        }

        if( StringUtils.isNotBlank( configs ) ){
            configs = "format:'"+PlatformSysConstant.DATE_FORMAT_EXT_YMD+"',"+configs;
        }else{
            configs = "format:'"+PlatformSysConstant.DATE_FORMAT_EXT_YMD;
        }

        fieldDate.setConfigs( configs );
		/*if( StringUtils.isNotBlank( configs ) ){
			JSONObject jo = JSONObject.fromObject( "{"+configs+"}" );
			this.dealFormFieldAttr( fieldDate,jo );
		}
		fieldDate.setFormat( PlatformSysConstant.DATE_FORMAT_EXT_YMD );
		*/

        sysEnControllerField.setSysEnFieldAttr( fieldDate );
        this.getSysEnControllerFieldList().add( sysEnControllerField );
    }

    /**
     * 添加一个DateTime字段
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param configs
     */
    public void addFieldDatetime(String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String configs){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,PlatformSysConstant.FORM_XTYPE_DATE_TIME,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank);

        SysEnFieldDate fieldDate = new SysEnFieldDate();
        if( StringUtils.isNotBlank( configs ) ){
            configs = "allowBlank:"+isAllowBlank+","+configs;
        }else{
            configs = "allowBlank:"+isAllowBlank;
        }

        if( StringUtils.isNotBlank( configs ) ){
            configs = "format:'"+PlatformSysConstant.DATE_FORMAT_EXT_YMDHMS+"',"+configs;
        }else{
            configs = "format:'"+PlatformSysConstant.DATE_FORMAT_EXT_YMDHMS;
        }

        fieldDate.setConfigs( configs );
		/*if( StringUtils.isNotBlank( configs ) ){
			JSONObject jo = JSONObject.fromObject( "{"+configs+"}" );
			this.dealFormFieldAttr( fieldDate,jo );
		}
		fieldDate.setFormat( PlatformSysConstant.DATE_FORMAT_EXT_YMD );
		*/

        sysEnControllerField.setSysEnFieldAttr( fieldDate );
        this.getSysEnControllerFieldList().add( sysEnControllerField );
    }

    /**
     * 添加一组复选框。
     * 通过json初始化数据
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param jsonData
     * @param checkboxGroupConfigs
     * @param checkboxConfigs
     */
    public void addFieldCheckboxInitDataByJson(String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String jsonData,String checkboxGroupConfigs,String checkboxConfigs){
        this.addFieldCheckbox(name,fieldLabel,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,jsonData,checkboxGroupConfigs,checkboxConfigs);
    }

    /**
     * 添加一组复选框。
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param jsonData
     * @param checkboxGroupConfigs
     * @param checkboxConfigs
     */
    public void addFieldCheckbox(String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String jsonData,String checkboxGroupConfigs,String checkboxConfigs){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,PlatformSysConstant.FORM_XTYPE_CHECKBOXGROUP,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank);

        SysEnFieldCheckboxGroup checkboxGroup = new SysEnFieldCheckboxGroup();
        checkboxGroup.setConfigs( checkboxGroupConfigs );

        JSONArray ja = JSONArray.fromObject(jsonData);
        for( int i=0 ;i<ja.size();i++ ){
            JSONObject tempJo = ja.getJSONObject(i);
            if( !tempJo.containsKey("name") ){
                throw new PlatformException("缺少name属性。");
            }
            if( !tempJo.containsKey("value") ){
                throw new PlatformException("缺少value属性。");
            }

            SysEnFieldCheckbox checkbox = new SysEnFieldCheckbox();
            checkbox.setBoxLabel( tempJo.getString("name") );
            checkbox.setInputValue( tempJo.getString("value") );
            checkbox.setConfigs( checkboxConfigs );

            checkboxGroup.getCheckboxList().add(checkbox);
        }

        sysEnControllerField.setSysEnFieldAttr( checkboxGroup );
        this.getSysEnControllerFieldList().add( sysEnControllerField );

    }

    /**
     *暂时没有实现
     */
    public void addFieldRadioInitDataByMethod(String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String loadDataMethodName,String radioGroupConfigs,String radioConfigs){
        String jsonData = null;
        this.addFieldRadio(name,fieldLabel,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,jsonData,radioGroupConfigs,radioConfigs);
    }


    /**
     * 添加一组单选按钮。
     * 通过json初始化数据
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param jsonData
     * @param radioGroupConfigs
     * @param radioConfigs
     */
    public void addFieldRadioInitDataByJson(String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String jsonData,String radioGroupConfigs,String radioConfigs){
        this.addFieldRadio(name,fieldLabel,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,jsonData,radioGroupConfigs,radioConfigs);
    }

    /**
     * 添加一组单选按钮
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param jsonData
     * @param radioGroupConfigs
     * @param radioConfigs
     */
    public void addFieldRadio(String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String jsonData,String radioGroupConfigs,String radioConfigs){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,PlatformSysConstant.FORM_XTYPE_RADIOGROUP,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank);

        SysEnFieldRadioGroup radioGroup = new SysEnFieldRadioGroup();
        radioGroup.setConfigs( radioGroupConfigs );

        JSONArray ja = JSONArray.fromObject(jsonData);
        for( int i=0 ;i<ja.size();i++ ){
            JSONObject tempJo = ja.getJSONObject(i);
            if( !tempJo.containsKey("name") ){
                throw new PlatformException("缺少name属性。");
            }
            if( !tempJo.containsKey("value") ){
                throw new PlatformException("缺少value属性。");
            }

            SysEnFieldRadio radio = new SysEnFieldRadio();
            radio.setBoxLabel( tempJo.getString("name") );
            radio.setInputValue( tempJo.getString("value") );
            radio.setConfigs( radioConfigs );

            radioGroup.getRadioList().add(radio);
        }

        sysEnControllerField.setSysEnFieldAttr( radioGroup );
        this.getSysEnControllerFieldList().add( sysEnControllerField );
    }

    /**
     * 添加一个下拉框。
     * 通过请求后台查询数据库初始化下拉框数据。
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param loadDataMethodName
     * @param firstComboBoxParamValue
     * @param comboBoxConfigs
     */
    public void addFieldComboBoxBySQL( String name, String fieldLabel,  boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String loadDataMethodName,String firstComboBoxParamValue,String comboBoxConfigs){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,PlatformSysConstant.FORM_XTYPE_COMBOBOX,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank);

        SysEnFieldComboBox comboBox = new SysEnFieldComboBox();
        comboBox.setComboBoxName( name );
        comboBox.setLoadDataImplModel("sql");
        comboBox.setLoadDataMethodName( loadDataMethodName );
        comboBox.setFirstComboBoxParamValue( firstComboBoxParamValue );
        comboBox.setCascade(false);

        if( StringUtils.isNotBlank( comboBoxConfigs ) ){
            comboBoxConfigs = "allowBlank:"+isAllowBlank+","+comboBoxConfigs;
        }else{
            comboBoxConfigs = "allowBlank:"+isAllowBlank;
        }

        comboBox.setConfigs( comboBoxConfigs );


        SysEnFieldComboBoxGroup comboBoxGroup = new SysEnFieldComboBoxGroup();
        comboBoxGroup.setCascade( false );
        comboBoxGroup.getComboBoxList().add( comboBox );
       // comboBoxGroup.setConfigs( comboBoxGroupConfigs );

        sysEnControllerField.setSysEnFieldAttr( comboBoxGroup );
        this.getSysEnControllerFieldList().add(sysEnControllerField);
    }

    /**
     * 添加一个下拉框。
     * 通过给定的json初始化下拉数据。
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param jsonData
     * @param comboBoxConfigs
     */
    public void addFieldComboBoxByJSON( String name, String fieldLabel,  boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String jsonData,String comboBoxConfigs ){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,PlatformSysConstant.FORM_XTYPE_COMBOBOX,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank);

        SysEnFieldComboBox comboBox = new SysEnFieldComboBox();
        comboBox.setComboBoxName( name );
        comboBox.setLoadDataImplModel("json");
        comboBox.setCascade(false);

        if( StringUtils.isNotBlank( comboBoxConfigs ) ){
            comboBoxConfigs = "allowBlank:"+isAllowBlank+","+comboBoxConfigs;
        }else{
            comboBoxConfigs = "allowBlank:"+isAllowBlank;
        }

        comboBox.setConfigs( comboBoxConfigs );


        JSONArray ja = JSONArray.fromObject(jsonData);
        for( int i=0 ;i<ja.size();i++ ){
            JSONObject tempJo = ja.getJSONObject(i);
            if( !tempJo.containsKey("name") ){
                throw new PlatformException("缺少name属性。");
            }
            if( !tempJo.containsKey("value") ){
                throw new PlatformException("缺少value属性。");
            }

            SysEnFieldComboBoxOption comboBoxOption = new SysEnFieldComboBoxOption();
            comboBoxOption.setName( tempJo.getString("name") );
            comboBoxOption.setValue( tempJo.getString("value") );

            comboBox.getComboBoxOptionList().add( comboBoxOption );
        }

        SysEnFieldComboBoxGroup comboBoxGroup = new SysEnFieldComboBoxGroup();
        comboBoxGroup.setCascade( false );
        comboBoxGroup.getComboBoxList().add( comboBox );
//        comboBoxGroup.setConfigs( comboBoxGroupConfigs );


        sysEnControllerField.setSysEnFieldAttr( comboBoxGroup );
        this.getSysEnControllerFieldList().add(sysEnControllerField);
    }

    /**
     * 添加一组级联下拉框（级联层数不限）
     * 通过请求后台查询数据库初始化下拉框数据。
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param comboBoxGroupName
     * @param firstComboBoxParamValue
     * @param comboBoxNames
     * @param loadDataMethodNames
     * @param comboBoxGroupConfigs
     * @param comboBoxConfigs
     */
    public void addFieldComboBoxCascadeBySQL(  String fieldLabel,  boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,
                                               String comboBoxGroupName,String firstComboBoxParamValue,String[] comboBoxNames,String[] loadDataMethodNames,String comboBoxGroupConfigs,String comboBoxConfigs ){
        if( comboBoxNames==null || loadDataMethodNames ==null || ( comboBoxNames.length!=loadDataMethodNames.length ) ){
            throw new PlatformException("构造下拉框出错！参数错误！");
        }

        if( comboBoxNames.length<1 ){
            throw new PlatformException("构造下拉框出错！至少需要配置一个下来框！");
        }

        SysEnControllerField  sysEnControllerField = new SysEnControllerField(comboBoxGroupName,fieldLabel,PlatformSysConstant.FORM_XTYPE_COMBOBOX,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank);

        SysEnFieldComboBoxGroup comboBoxGroup = new SysEnFieldComboBoxGroup();
        comboBoxGroup.setCascade( true );
        comboBoxGroup.setConfigs( comboBoxGroupConfigs );

        int comboBoxNum = comboBoxNames.length;
        for( int i=0;i<comboBoxNum;i++ ){
            if( StringUtils.isBlank( comboBoxNames[i] )  || StringUtils.isBlank( loadDataMethodNames[i] ) ){
                throw new PlatformException("构造下拉框出错！参数为空！");
            }

            SysEnFieldComboBox tempComboBox = new SysEnFieldComboBox();
            tempComboBox.setComboBoxName( comboBoxNames[i] );
            tempComboBox.setLoadDataImplModel("sql");
            if( i==0 ){
                tempComboBox.setFirstComboBoxParamValue( firstComboBoxParamValue );
            }
            tempComboBox.setLoadDataMethodName( loadDataMethodNames[i] );
            tempComboBox.setCascade(true);
            tempComboBox.setComboBoxOrder( i+1 );
            if( i>0 ){
                tempComboBox.setParentComboBox( comboBoxNames[i-1] );
            }
            if( i+1<comboBoxNum ){
                tempComboBox.setChildComboBox( comboBoxNames[i+1] );
            }

            if( i==0 && i+1==comboBoxNum ){
                tempComboBox.setFirst( true );
                tempComboBox.setLast( true );
            }else if(i==0){
                tempComboBox.setFirst( true );
                tempComboBox.setLast( false );
            }else if( i+1==comboBoxNum ){
                tempComboBox.setFirst( false );
                tempComboBox.setLast( true );
            }else{
                tempComboBox.setFirst( false );
                tempComboBox.setLast( false );
            }

            if( StringUtils.isNotBlank( comboBoxConfigs ) ){
                comboBoxConfigs = "allowBlank:"+isAllowBlank+","+comboBoxConfigs;
            }else{
                comboBoxConfigs = "allowBlank:"+isAllowBlank;
            }

            tempComboBox.setConfigs( comboBoxConfigs );

            comboBoxGroup.getComboBoxList().add( tempComboBox );
        }

        /**
         * 设置每个下拉框的父子关系及所有后续级联下拉框
         */
        List<SysEnFieldComboBox> comboboxList = comboBoxGroup.getComboBoxList();
        for( int i=0;i<comboBoxGroup.getComboBoxList().size();i++ ){
            if( i>0 ){
                comboboxList.get(i-1).setChildSysEnFieldComboBox( comboboxList.get(i) );
                comboboxList.get(i).setParentSysEnFieldComboBox( comboboxList.get(i-1) );
            }

            SysEnFieldComboBox tempComboBox = comboboxList.get(i);
            if( !tempComboBox.isLast() ){
                List<SysEnFieldComboBox> cascadeList = new ArrayList<SysEnFieldComboBox>();
                for( int j=i+1;j<comboboxList.size();j++ ){
                    cascadeList.add( comboboxList.get(j) );
                }
                tempComboBox.setCascadeList( cascadeList );
            }
        }

        sysEnControllerField.setSysEnFieldAttr( comboBoxGroup );
        this.getSysEnControllerFieldList().add(sysEnControllerField);
    }

    /**
     * 添加一个下拉树。
     * 通过请求后台查询数据库初始化下拉树节点数据。
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param isAllowBlank
     * @param configs
     * @param loadDataUrl
     */
    public void addFieldComboBoxTree(String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String configs,String loadDataUrl){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,PlatformSysConstant.FORM_XTYPE_COMBOBOXTREE,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank);

        SysEnFieldComboBoxTree fieldComboBoxTree = new SysEnFieldComboBoxTree();
        fieldComboBoxTree.setUrl( loadDataUrl );

        if( StringUtils.isNotBlank( configs ) ){
            configs = "allowBlank:"+isAllowBlank+","+configs;
        }else{
            configs = "allowBlank:"+isAllowBlank;
        }

        fieldComboBoxTree.setConfigs( configs );

        sysEnControllerField.setSysEnFieldAttr( fieldComboBoxTree );
        this.getSysEnControllerFieldList().add( sysEnControllerField );
    }

    /**
     * 添加一个Display表单控件
     * @param name
     * @param fieldLabel
     * @param isValid
     * @param isAllowAdd
     * @param isAllowUpdate
     * @param isAllowSearch
     * @param configs
     */
    public void addFieldDisplay(String name, String fieldLabel, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch,String configs){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,PlatformSysConstant.FORM_XTYPE_DISPLAY,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,true);

        SysEnFieldDisplay formField = new SysEnFieldDisplay();
        formField.setConfigs( configs );
		/*if( StringUtils.isNotBlank(configs) ){
			JSONObject jo = JSONObject.fromObject( "{"+ configs +"}" );
			this.dealFormFieldAttr( formField,jo );
		}*/

        sysEnControllerField.setSysEnFieldAttr( formField );
        this.getSysEnControllerFieldList().add( sysEnControllerField );
    }

    /**
     * 添加菜单按钮
     * @param identify
     * @param name
     * @param url
     * @param typeNum
     * @param isSingle
     * @param iconCls
     * @param orderNum
     */
    public void addFunction( String identify,String name,String url, Integer typeNum,boolean isSingle,String iconCls,int orderNum ){
        SysEnControllerFunction sysEnControllerFunction = new SysEnControllerFunction(identify,name,url,typeNum,isSingle,iconCls,orderNum);
        this.getSysEnControllerFunctionList().add( sysEnControllerFunction );
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<SysEnControllerField> getSysEnControllerFieldList() {
        return sysEnControllerFieldList;
    }

    public void setSysEnControllerFieldList(List<SysEnControllerField> sysEnControllerFieldList) {
        this.sysEnControllerFieldList = sysEnControllerFieldList;
    }

    public List<SysEnControllerFunction> getSysEnControllerFunctionList() {
        return sysEnControllerFunctionList;
    }

    public void setSysEnControllerFunctionList(List<SysEnControllerFunction> sysEnControllerFunctionList) {
        this.sysEnControllerFunctionList = sysEnControllerFunctionList;
    }
}
