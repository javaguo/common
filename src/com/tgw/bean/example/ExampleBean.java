package com.tgw.bean.example;

import com.tgw.bean.base.AbstractBaseBean;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zhaojg on 2017/03/24.
 */
@Table(name="example_bean")
public class ExampleBean extends AbstractBaseBean{

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private byte baseByte;
    private short baseShort;
    private int baseInt;
    private long baseLong;
    private float baseFloat;
    private double baseDouble;
    private boolean baseBoolean;
    //org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.builder.BuilderException: Error resolving class. Cause: org.apache.ibatis.type.TypeException: Could not resolve type alias 'char'.  Cause: java.lang.ClassNotFoundException: Cannot find class: char
    //private char baseChar;

    private Byte baseByteObj;
    private Short baseShortObj;
    private Integer baseIntegerObj;
    private Long baseLongObj;
    private Float baseFloatObj;
    private Double baseDoubleObj;
    private Boolean baseBooleanObj;
    private String baseStrObj;
    private Date baseDateObj;
    private Date baseTimeObj;
    private Date baseYearObj;
    private Date baseTimestampObj;
    private Date baseDateTimeObj;

    private String formText;
    private String formTextArea;
    private String formCheckbox;
    private String formRadio;
    private String formComboBox;
    private String formHidden;
    private String formDate;
    private String formTime;
    private String formDisplay;
    private int  formNumber;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte getBaseByte() {
        return baseByte;
    }

    public void setBaseByte(byte baseByte) {
        this.baseByte = baseByte;
    }

    public short getBaseShort() {
        return baseShort;
    }

    public void setBaseShort(short baseShort) {
        this.baseShort = baseShort;
    }

    public int getBaseInt() {
        return baseInt;
    }

    public void setBaseInt(int baseInt) {
        this.baseInt = baseInt;
    }

    public long getBaseLong() {
        return baseLong;
    }

    public void setBaseLong(long baseLong) {
        this.baseLong = baseLong;
    }

    public float getBaseFloat() {
        return baseFloat;
    }

    public void setBaseFloat(float baseFloat) {
        this.baseFloat = baseFloat;
    }

    public double getBaseDouble() {
        return baseDouble;
    }

    public void setBaseDouble(double baseDouble) {
        this.baseDouble = baseDouble;
    }

    public boolean isBaseBoolean() {
        return baseBoolean;
    }

    public void setBaseBoolean(boolean baseBoolean) {
        this.baseBoolean = baseBoolean;
    }

    public Byte getBaseByteObj() {
        return baseByteObj;
    }

    public void setBaseByteObj(Byte baseByteObj) {
        this.baseByteObj = baseByteObj;
    }

    public Short getBaseShortObj() {
        return baseShortObj;
    }

    public void setBaseShortObj(Short baseShortObj) {
        this.baseShortObj = baseShortObj;
    }

    public Integer getBaseIntegerObj() {
        return baseIntegerObj;
    }

    public void setBaseIntegerObj(Integer baseIntegerObj) {
        this.baseIntegerObj = baseIntegerObj;
    }

    public Long getBaseLongObj() {
        return baseLongObj;
    }

    public void setBaseLongObj(Long baseLongObj) {
        this.baseLongObj = baseLongObj;
    }

    public Float getBaseFloatObj() {
        return baseFloatObj;
    }

    public void setBaseFloatObj(Float baseFloatObj) {
        this.baseFloatObj = baseFloatObj;
    }

    public Double getBaseDoubleObj() {
        return baseDoubleObj;
    }

    public void setBaseDoubleObj(Double baseDoubleObj) {
        this.baseDoubleObj = baseDoubleObj;
    }

    public Boolean getBaseBooleanObj() {
        return baseBooleanObj;
    }

    public void setBaseBooleanObj(Boolean baseBooleanObj) {
        this.baseBooleanObj = baseBooleanObj;
    }

    public String getBaseStrObj() {
        return baseStrObj;
    }

    public void setBaseStrObj(String baseStrObj) {
        this.baseStrObj = baseStrObj;
    }

    public Date getBaseDateObj() {
        return baseDateObj;
    }

    public void setBaseDateObj(Date baseDateObj) {
        this.baseDateObj = baseDateObj;
    }

    public Date getBaseTimeObj() {
        return baseTimeObj;
    }

    public void setBaseTimeObj(Date baseTimeObj) {
        this.baseTimeObj = baseTimeObj;
    }

    public Date getBaseYearObj() {
        return baseYearObj;
    }

    public void setBaseYearObj(Date baseYearObj) {
        this.baseYearObj = baseYearObj;
    }

    public Date getBaseTimestampObj() {
        return baseTimestampObj;
    }

    public void setBaseTimestampObj(Date baseTimestampObj) {
        this.baseTimestampObj = baseTimestampObj;
    }

    public Date getBaseDateTimeObj() {
        return baseDateTimeObj;
    }

    public void setBaseDateTimeObj(Date baseDateTimeObj) {
        this.baseDateTimeObj = baseDateTimeObj;
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }

    public String getFormTextArea() {
        return formTextArea;
    }

    public void setFormTextArea(String formTextArea) {
        this.formTextArea = formTextArea;
    }

    public String getFormCheckbox() {
        return formCheckbox;
    }

    public void setFormCheckbox(String formCheckbox) {
        this.formCheckbox = formCheckbox;
    }

    public String getFormRadio() {
        return formRadio;
    }

    public void setFormRadio(String formRadio) {
        this.formRadio = formRadio;
    }

    public String getFormComboBox() {
        return formComboBox;
    }

    public void setFormComboBox(String formComboBox) {
        this.formComboBox = formComboBox;
    }

    public String getFormHidden() {
        return formHidden;
    }

    public void setFormHidden(String formHidden) {
        this.formHidden = formHidden;
    }

    public String getFormDate() {
        return formDate;
    }

    public void setFormDate(String formDate) {
        this.formDate = formDate;
    }

    public String getFormTime() {
        return formTime;
    }

    public void setFormTime(String formTime) {
        this.formTime = formTime;
    }

    public String getFormDisplay() {
        return formDisplay;
    }

    public void setFormDisplay(String formDisplay) {
        this.formDisplay = formDisplay;
    }

    public int getFormNumber() {
        return formNumber;
    }

    public void setFormNumber(int formNumber) {
        this.formNumber = formNumber;
    }
}
