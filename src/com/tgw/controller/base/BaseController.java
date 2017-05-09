package com.tgw.controller.base;

import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.bean.system.*;
import com.tgw.exception.PlatformException;
import com.tgw.service.base.BaseService;
import com.tgw.utils.PlatformUtils;
import com.tgw.utils.config.PlatformSysConstant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BaseController<T extends AbstractBaseBean> implements Serializable {

	private T bean;
	private Class entityClass;
	private String jsonView = "common/json";//输出json字符串
	private String jsonStr;//controller为单例，最好不好定义变量，检查确认此变量无用之后，需要删除掉

	private List<SysEnControllerField> sysEnControllerFieldList = new ArrayList<SysEnControllerField>();
	private List<SysEnControllerFunction> sysEnControllerFunctionList = new ArrayList<SysEnControllerFunction>();

	@Resource
	private BaseService baseService;


	/**
	 * 执行查询方法之前调用
	 */
	public void beforeSearch(HttpServletRequest request, HttpServletResponse response, T bean){
		System.out.println("----------------- BaseController  beforeSearch -----------------");
	}

	public void initSearch(HttpServletRequest request, HttpServletResponse response, T bean,ModelAndView modelAndView){
		System.out.println("----------------- BaseController  initSearch -----------------");
	}

	/**
	 * 进入查询列表页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/search.do")
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, T bean){
		this.beforeSearch(request,response,bean);
		System.out.println("----------------- BaseController  search -----------------");
		ModelAndView modelAndView = new ModelAndView();
		this.initSearch(request,response,bean,modelAndView);

        if( null!=this.getSysEnControllerFieldList() && this.getSysEnControllerFieldList().size()>0 ){

			/**
			 * 初始化页面上的字段信息
			 */
			//可被添加的字段
            List<SysEnControllerField> addFieldList = new ArrayList<SysEnControllerField>();
			//可被更新的字段
			List<SysEnControllerField> updateFieldList = new ArrayList<SysEnControllerField>();
            //可被搜索的字段
			List<SysEnControllerField> searFieldList = new ArrayList<SysEnControllerField>();
            //可在列表显示的字段
			List<SysEnControllerField> showFieldList = new ArrayList<SysEnControllerField>();

            for( SysEnControllerField conField : this.getSysEnControllerFieldList() ){
                /*if( "id".equals( conField.getName() ) ){//编辑页面一定需要id，不管id字段配置成什么状态，都要加上id
                    updateFieldList.add( conField );
                }*/

                if( !conField.isValid() ){
                    continue;
                }

                if( conField.isAllowAdd() ){
                    addFieldList.add( conField );
                }

                if( conField.isAllowUpdate()  || "id".equals( conField.getName() )  ){
                    updateFieldList.add( conField );
                }


                if( conField.isAllowSearch() ){
                    searFieldList.add( conField );
                }

                showFieldList.add( conField );

            }

			modelAndView.addObject("fieldList",this.getSysEnControllerFieldList() );
			modelAndView.addObject("addFieldList",addFieldList );
			modelAndView.addObject("updateFieldList",updateFieldList );
			modelAndView.addObject("searFieldList",searFieldList );
			modelAndView.addObject("showFieldList",showFieldList );


            int searchConditionFieldNum = searFieldList.size();
            int searchConditionRowNum = 0;//列表页面上搜索条件区域布局的行数
            if( searchConditionFieldNum % PlatformSysConstant.LAYOUT_NUM_HORI_SEARCH_CONDITION == 0 ){
                searchConditionRowNum = searchConditionFieldNum / PlatformSysConstant.LAYOUT_NUM_HORI_SEARCH_CONDITION ;
            }else{
                searchConditionRowNum = searchConditionFieldNum / PlatformSysConstant.LAYOUT_NUM_HORI_SEARCH_CONDITION + 1;
            }

            modelAndView.addObject("searchConditionRowNum", searchConditionRowNum );
            modelAndView.addObject("layoutNumHoriSearchCondition", PlatformSysConstant.LAYOUT_NUM_HORI_SEARCH_CONDITION );

			/**
			 * 初始化页面上的菜单按钮信息
			 */
			List<SysEnControllerFunction> sysEnControllerFunctionList = new ArrayList<SysEnControllerFunction>();
			SysEnControllerFunction addFunction = new SysEnControllerFunction("baseAdd","添加","",1,true,"Add",-100);
			SysEnControllerFunction delFunction = new SysEnControllerFunction("baseDelete","删除","/delete.do",2,false,"Delete",-99);
			sysEnControllerFunctionList.add( addFunction );
			sysEnControllerFunctionList.add( delFunction );
			sysEnControllerFunctionList.addAll( this.getSysEnControllerFunctionList() );

			modelAndView.addObject("functionBarList", sysEnControllerFunctionList );


            modelAndView.addObject("pageSize", PlatformSysConstant.PAGE_SIZE );

        }else{

        }

		modelAndView.setViewName("common/showDataSVP");
		this.afterSearch(request,response,bean);
		return  modelAndView;
	}


	/**
	 * 执行完查询方法调用
	 */
	public void afterSearch(HttpServletRequest request, HttpServletResponse response, T bean){
		System.out.println("----------------- BaseController  afterSearch -----------------");
	}

	/**
	 * 根据条件分页查询列表页面数据
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 */
	@RequestMapping("/searchData.do")
	public ModelAndView searchData(HttpServletRequest request, HttpServletResponse response, T bean){
		System.out.println("----------------- BaseController  searchData -----------------");
		ModelAndView modelAndView = new ModelAndView();

		try {
            /**
             * 处理分页参数
             * mysql和oracle分页使用pageNum和pageSize即可，mybatis底层会自动转换
             */
            String pageNumStr = request.getParameter("page");
			//String pageStartStr = request.getParameter("start");//数据开始的索引。屏蔽，暂时用不到
			String pageSizeStr = request.getParameter("limit");
			int pageNum = Integer.parseInt(pageNumStr);
			//int pageStart = Integer.parseInt(pageStartStr);
			int pageSize = Integer.parseInt(pageSizeStr);

			//查询数据
			String json = (String)this.getBaseService().searchData(pageNum,pageSize,bean);
			modelAndView.addObject( PlatformSysConstant.JSONSTR, json );

		} catch(Exception e) {
			e.printStackTrace();
		}

		modelAndView.setViewName( this.getJsonView() );
		return modelAndView;
	}

	public void beforeAdd(){

	}

	public ModelAndView add(){
		this.beforeAdd();

		ModelAndView modelAndView = new ModelAndView();

		this.afterAdd();
		return  modelAndView;
	}

	public void afterAdd(){

	}

	public void beforeSave(){

    }

    @RequestMapping("/save.do")
    public ModelAndView save( HttpServletRequest request, HttpServletResponse response, T bean ){
        this.beforeSave();

        ModelAndView modelAndView = new ModelAndView();
        JSONObject jo = JSONObject.fromObject("{}");

        /**
         * extjs的form表单提交后根据返回值中的success值判断走success回调函数或failure函数
         */
        try{
            this.getBaseService().saveBean(bean);
            jo.put("success",true);
            jo.put("msg","保存成功！");
        }catch (Exception e){
            e.printStackTrace();
            jo.put("success",false);
            jo.put("msg","保存失败！");
        }

        modelAndView.addObject( PlatformSysConstant.JSONSTR, jo.toString() );
        modelAndView.setViewName( this.getJsonView() );

        this.afterSave();
        return  modelAndView;
    }

    public void afterSave(){

    }

	public void beforeEdit(HttpServletRequest request, HttpServletResponse response, T bean){

	}

	public ModelAndView initEdit(HttpServletRequest request, HttpServletResponse response, T bean){
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("----------------- BaseController  initEdit -----------------");
		return  modelAndView;
	}

	@RequestMapping("/edit.do")
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response, T bean){
		this.beforeEdit(request,response,bean);

		ModelAndView modelAndView =initEdit(request,response,bean);
		JSONObject jo = JSONObject.fromObject("{}");

		try{
			String beanId = request.getParameter("beanId");
			if( StringUtils.isBlank( beanId ) ){
				throw new PlatformException("参数不能为空！");
			}

			if( null!=this.getSysEnControllerFieldList() && this.getSysEnControllerFieldList().size()>0 ) {

				//可被更新的字段
				List<SysEnControllerField> updateFieldList = new ArrayList<SysEnControllerField>();
				for (SysEnControllerField conField : this.getSysEnControllerFieldList()) {
					if (!conField.isValid()) {
						continue;
					}

					if ( conField.isAllowUpdate() || "id".equals( conField.getName() ) ) {
						updateFieldList.add(conField);
					}

				}

				if( null!=updateFieldList && updateFieldList.size()>0 ){
					Class beanClass = bean.getClass();
					Object obj = beanClass.newInstance();
					Integer tempId = Integer.parseInt( beanId );
					Method idMethod = beanClass.getDeclaredMethod("setId",new Class[]{ Integer.class } );
					idMethod.invoke(obj,tempId );

					obj = this.getBaseService().selectUniqueBeanByPrimaryKey( obj );
					if( null==obj ){
						throw new PlatformException("没有查找到要更新的信息！");
					}

					JSONObject objJSON = JSONObject.fromObject("{}");
					for( SysEnControllerField updateField : updateFieldList ){
						Method met=obj.getClass().getDeclaredMethod( "get"+PlatformUtils.firstLetterToUpperCase(updateField.getName()) );
						Object tempObj = met.invoke(obj);

						objJSON.put( updateField.getName(),tempObj );
					}
					jo.put( "bean",objJSON );
				}else{
					throw new PlatformException("没有可更新的字段！");
				}
			}else{
				throw new PlatformException("没有可操作的字段！");
			}

			jo.put("success",true);
		} catch( PlatformException e ){
			jo.put("success",false);
			jo.put("msg",e.getMsg());
		} catch (Exception e){
			e.printStackTrace();
			jo.put("success",false);
			jo.put("msg","发生异常！");
		}

		modelAndView.addObject( PlatformSysConstant.JSONSTR, jo.toString() );
		modelAndView.setViewName( this.getJsonView() );

		this.afterEdit(request,response,bean);
		return  modelAndView;
	}

	public void afterEdit(HttpServletRequest request, HttpServletResponse response, T bean){

	}

	public void beforeUpdate(  HttpServletRequest request, HttpServletResponse response, T bean  ){

	}

	@RequestMapping("/update.do")
	public ModelAndView update( HttpServletRequest request, HttpServletResponse response, T bean ){
		this.beforeUpdate( request,response,bean );

		ModelAndView modelAndView = new ModelAndView();
		JSONObject jo = JSONObject.fromObject("{}");

		/**
		 * extjs的form表单提交后根据返回值中的success值判断走success回调函数或failure函数
		 */
		try{
			Class beanClass = bean.getClass();

			Method getIdMet=beanClass.getDeclaredMethod( "getId" );
			Object tempIdObj = getIdMet.invoke(bean);
			Integer tempId = Integer.parseInt( tempIdObj.toString() );

			Object oldObj = beanClass.newInstance();
			Method setIdMethod = beanClass.getDeclaredMethod("setId",new Class[]{ Integer.class } );
			setIdMethod.invoke(oldObj,tempId );

			oldObj = this.getBaseService().selectUniqueBeanByPrimaryKey( oldObj );

			if( null!=this.getSysEnControllerFieldList() && this.getSysEnControllerFieldList().size()>0 ) {

				//可被更新的字段
				List<SysEnControllerField> updateFieldList = new ArrayList<SysEnControllerField>();
				for (SysEnControllerField conField : this.getSysEnControllerFieldList()) {
					if (!conField.isValid()) {
						continue;
					}

					if ( conField.isAllowUpdate() ) {
						updateFieldList.add(conField);
					}

				}

				if( null!=updateFieldList && updateFieldList.size()>0 ){
					for( SysEnControllerField updateField : updateFieldList ){
						Method getMet=beanClass.getDeclaredMethod( "get"+PlatformUtils.firstLetterToUpperCase(updateField.getName()) );
						Object tempObj = getMet.invoke( bean );

						Method setMet=beanClass.getDeclaredMethod( "set"+PlatformUtils.firstLetterToUpperCase(updateField.getName()),getMet.getReturnType() );
						setMet.invoke(oldObj,tempObj);
					}

					this.getBaseService().updateBean( oldObj );
				}else{
					throw new PlatformException("没有可更新的字段！");
				}
			}else{
				throw new PlatformException("没有可操作的字段！");
			}

			jo.put("success",true);
			jo.put("msg","修改成功！");
		}catch (Exception e){
			e.printStackTrace();
			jo.put("success",false);
			jo.put("msg","修改失败！");
		}

		modelAndView.addObject( PlatformSysConstant.JSONSTR, jo.toString() );
		modelAndView.setViewName( this.getJsonView() );

		this.afterUpdate(request,response,bean);
		return  modelAndView;
	}

	public void afterUpdate(  HttpServletRequest request, HttpServletResponse response, T bean  ){

	}

    public void beforeDelete(){

    }

    @RequestMapping("/delete.do")
    public ModelAndView delete( HttpServletRequest request, HttpServletResponse response, T bean){
        this.beforeDelete();

        ModelAndView modelAndView = new ModelAndView();
        JSONObject jo = JSONObject.fromObject("{}");

        try{
            String ids = request.getParameter("ids");
            if( StringUtils.isBlank( ids ) ){
                throw new PlatformException("参数不能为空！");
            }

            List<String> idList = PlatformUtils.idsToList( request );
            List<Object>  beanList = new ArrayList<Object>();

            Class beanClass = bean.getClass();
            Method idMethod = beanClass.getDeclaredMethod("setId",new Class[]{ Integer.class } );
            for( String idStr:idList ){
                Object obj = beanClass.newInstance();
                Integer tempId = Integer.parseInt( idStr );
                idMethod.invoke(obj,tempId );

                beanList.add(obj);
            }
            this.getBaseService().deleteBatchBean( beanList );

            jo.put("success",true);
            jo.put("msg","删除成功！");
        } catch( PlatformException e ){
            jo.put("success",false);
            jo.put("msg",e.getMsg());
        } catch (Exception e){
            e.printStackTrace();
            jo.put("success",false);
            jo.put("msg","发生异常！");
        }

        modelAndView.addObject( PlatformSysConstant.JSONSTR, jo.toString() );
        modelAndView.setViewName( this.getJsonView() );

        this.afterDelete();

        return  modelAndView;
    }

    public void afterDelete(){

    }

	/**
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
	public void addField(String name, String fieldLabel, String type, String xtype, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank, String emptyText, String vtype){
        SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,type,xtype,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,emptyText,vtype);

        /*if( null == this.getSysEnControllerFieldList() ){
            this.setSysEnControllerFieldList( new ArrayList<SysEnControllerField>());
        }*/

        this.getSysEnControllerFieldList().add( sysEnControllerField );
    }



	/**
	 * 添加列表页面每一个字段的相关属性
	 * @param name
	 * @param fieldLabel
	 * @param type
	 */
    public void addField( String name, String fieldLabel, String type ){
        this.addField(name,fieldLabel,type,null,true,true,true,true,true,null,null);
    }

	public void addFieldRadioInitDataByMethod(String name, String fieldLabel, String xtype, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String loadDataMethodName){
		String jsonData = null;
		this.addFieldRadio(name,fieldLabel,xtype,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,null,null,jsonData);
	}

	public void addFieldRadioInitDataByJson(String name, String fieldLabel, String xtype, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String jsonData){
		this.addFieldRadio(name,fieldLabel,xtype,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,null,null,jsonData);
	}

	public void addFieldRadio(String name, String fieldLabel, String xtype, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String emptyText, String vtype,String jsonData){
		SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,null,xtype,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,emptyText,vtype);

		SysEnFieldRadioGroup radioGroup = new SysEnFieldRadioGroup();

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
			radio.setName( tempJo.getString("name") );
			radio.setValue( tempJo.getString("value") );

			radioGroup.getRadioList().add(radio);
		}

		sysEnControllerField.setSysEnFormFieldAttr( radioGroup );
		this.getSysEnControllerFieldList().add( sysEnControllerField );

	}

	public void addFieldCheckboxInitDataByJson(String name, String fieldLabel, String xtype, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String jsonData){
		this.addFieldCheckbox(name,fieldLabel,xtype,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,null,null,jsonData);
	}

	public void addFieldCheckbox(String name, String fieldLabel, String xtype, boolean isValid, boolean isAllowAdd, boolean isAllowUpdate, boolean isAllowSearch, boolean isAllowBlank,String emptyText, String vtype,String jsonData){
		SysEnControllerField  sysEnControllerField = new SysEnControllerField(name,fieldLabel,null,xtype,isValid,isAllowAdd,isAllowUpdate,isAllowSearch,isAllowBlank,emptyText,vtype);

		SysEnFieldCheckboxGroup checkboxGroup = new SysEnFieldCheckboxGroup();

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
			checkbox.setName( tempJo.getString("name") );
			checkbox.setValue( tempJo.getString("value") );

			checkboxGroup.getCheckboxList().add(checkbox);
		}

		sysEnControllerField.setSysEnFormFieldAttr( checkboxGroup );
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

	public T getBean() {
		return bean;
	}

	public void setBean(T bean) {
		this.bean = bean;
	}

	public Class getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	public String getJsonView() {
		return jsonView;
	}

	public void setJsonView(String jsonView) {
		this.jsonView = jsonView;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
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

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
}
