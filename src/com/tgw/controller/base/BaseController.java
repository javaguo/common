package com.tgw.controller.base;

import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.bean.system.*;
import com.tgw.bean.system.form.field.*;
import com.tgw.exception.PlatformException;
import com.tgw.platform.propertyeditors.PlatformCustomDateEditor;
import com.tgw.service.base.BaseService;
import com.tgw.utils.PlatformUtils;
import com.tgw.utils.config.PlatformSysConstant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import sun.beans.editors.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class BaseController<T extends AbstractBaseBean> implements Serializable {

	private T bean;
	private Class entityClass;
	private String jsonView = "common/json";//输出json字符串
	private String jsonStr;//controller为单例，最好不好定义变量，检查确认此变量无用之后，需要删除掉

	/*private List<SysEnControllerField> sysEnControllerFieldList = new ArrayList<SysEnControllerField>();
	private List<SysEnControllerFunction> sysEnControllerFunctionList = new ArrayList<SysEnControllerFunction>();*/

	@Resource
	private BaseService baseService;

	/**
	 * 为满足框架结构的需要，一定要在具体的业务Controller调用此方法。
	 * 作用：具体的业务Controller调用，将具体业务的service赋值给baseService
	 * @param baseService
     */
	public void initService(BaseService baseService){
		this.setBaseService(baseService);
		this.getBaseService().initMapper();
	}

	public void initSearch(HttpServletRequest request, HttpServletResponse response, T bean,ModelAndView modelAndView){
		System.out.println("----------------- BaseController  initSearch -----------------");
	}

	/**
	 * 初始化列表页面上的字段信息
	 * @param controller
     */
	public void initField( SysEnController controller ){

	}

	/**
	 * 初始化列表页面上的功能按钮
	 * @param controller
     */
	public void initFunction( SysEnController controller ){

	}

	/**
	 * 执行查询方法之前调用
	 */
	public void beforeSearch(HttpServletRequest request, HttpServletResponse response, T bean){
		System.out.println("----------------- BaseController  beforeSearch -----------------");
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

		SysEnController controller = new SysEnController();
		this.initField( controller );
		this.initFunction( controller );

		//初始化页面上的字段信息开始
        if( null!=controller.getSysEnControllerFieldList() && controller.getSysEnControllerFieldList().size()>0 ){

			/**
			 * 初始化页面上的字段信息
			 */
			//所有有效字段
			List<SysEnControllerField> validFieldList = new ArrayList<SysEnControllerField>();
			//可被添加的字段
            List<SysEnControllerField> addFieldList = new ArrayList<SysEnControllerField>();
			//可被更新的字段
			List<SysEnControllerField> updateFieldList = new ArrayList<SysEnControllerField>();
            //可被搜索的字段
			List<SysEnControllerField> searFieldList = new ArrayList<SysEnControllerField>();
            //可在列表显示的字段
			List<SysEnControllerField> showFieldList = new ArrayList<SysEnControllerField>();

			//页面上的所有下拉框
			List<SysEnFieldComboBox> comboBoxList = new ArrayList<SysEnFieldComboBox>();

            for( SysEnControllerField conField : controller.getSysEnControllerFieldList() ){
                /*if( "id".equals( conField.getName() ) ){//编辑页面一定需要id，不管id字段配置成什么状态，都要加上id
                    updateFieldList.add( conField );
                }*/

                if( !conField.isValid() ){
                    continue;
                }
				validFieldList.add( conField );

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

				if( PlatformSysConstant.FORM_XTYPE_COMBOBOX.equals( conField.getXtype() ) ){
					SysEnFieldComboBoxGroup comboBoxGroup = (SysEnFieldComboBoxGroup)conField.getSysEnFieldAttr();
					 comboBoxGroup.getComboBoxList();
					for( SysEnFieldComboBox comboBox:comboBoxGroup.getComboBoxList() ){
						comboBoxList.add( comboBox );
					}
				}
            }

			modelAndView.addObject("fieldList",controller.getSysEnControllerFieldList() );
			modelAndView.addObject("validFieldList",validFieldList);
			modelAndView.addObject("addFieldList",addFieldList );
			modelAndView.addObject("updateFieldList",updateFieldList );
			modelAndView.addObject("searFieldList",searFieldList );
			modelAndView.addObject("showFieldList",showFieldList );

			modelAndView.addObject("comboBoxList",comboBoxList);


            int searchConditionFieldNum = searFieldList.size();
            int searchConditionRowNum = 0;//列表页面上搜索条件区域布局的行数
            if( searchConditionFieldNum % PlatformSysConstant.LAYOUT_NUM_HORI_SEARCH_CONDITION == 0 ){
                searchConditionRowNum = searchConditionFieldNum / PlatformSysConstant.LAYOUT_NUM_HORI_SEARCH_CONDITION ;
            }else{
                searchConditionRowNum = searchConditionFieldNum / PlatformSysConstant.LAYOUT_NUM_HORI_SEARCH_CONDITION + 1;
            }

            modelAndView.addObject("searchConditionRowNum", searchConditionRowNum );
            modelAndView.addObject("layoutNumHoriSearchCondition", PlatformSysConstant.LAYOUT_NUM_HORI_SEARCH_CONDITION );


        }else{

        }
		//初始化页面上的字段信息结束

		//初始化页面上的菜单按钮信息开始
		List<SysEnControllerFunction> sysEnControllerFunctionList = new ArrayList<SysEnControllerFunction>();
		SysEnControllerFunction addFunction = new SysEnControllerFunction("baseAdd","添加","",1,true,"Add",-100);
		SysEnControllerFunction delFunction = new SysEnControllerFunction("baseDelete","删除","/delete.do",2,false,"Delete",-99);
		sysEnControllerFunctionList.add( addFunction );
		sysEnControllerFunctionList.add( delFunction );
		sysEnControllerFunctionList.addAll( controller.getSysEnControllerFunctionList() );

		modelAndView.addObject("functionBarList", sysEnControllerFunctionList );
		//初始化页面上的菜单按钮信息结束

		modelAndView.addObject("pageSize", PlatformSysConstant.PAGE_SIZE );

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
			SysEnController controller = new SysEnController();
			this.initField( controller );
			String beanId = request.getParameter("beanId");
			if( StringUtils.isBlank( beanId ) ){
				throw new PlatformException("参数不能为空！");
			}

			if( null!=controller.getSysEnControllerFieldList() && controller.getSysEnControllerFieldList().size()>0 ) {

				//可被更新的字段
				List<SysEnControllerField> updateFieldList = new ArrayList<SysEnControllerField>();
				for (SysEnControllerField conField : controller.getSysEnControllerFieldList()) {
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
			SysEnController controller = new SysEnController();
			this.initField( controller );

			Class beanClass = bean.getClass();

			Method getIdMet=beanClass.getDeclaredMethod( "getId" );
			Object tempIdObj = getIdMet.invoke(bean);
			Integer tempId = Integer.parseInt( tempIdObj.toString() );

			Object oldObj = beanClass.newInstance();
			Method setIdMethod = beanClass.getDeclaredMethod("setId",new Class[]{ Integer.class } );
			setIdMethod.invoke(oldObj,tempId );

			oldObj = this.getBaseService().selectUniqueBeanByPrimaryKey( oldObj );

			if( null!=controller.getSysEnControllerFieldList() && controller.getSysEnControllerFieldList().size()>0 ) {

				//可被更新的字段
				List<SysEnControllerField> updateFieldList = new ArrayList<SysEnControllerField>();
				for (SysEnControllerField conField : controller.getSysEnControllerFieldList()) {
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
	 * 加载下拉框数据
	 * @param request
	 * @param response
	 * @param bean
     * @return
     */
	@RequestMapping("/loadComboxData.do")
	public ModelAndView loadComboxData(HttpServletRequest request, HttpServletResponse response, T bean){
		System.out.println("----------------- BaseController --> loadComboxData()   -----------------");
		ModelAndView modelAndView = new ModelAndView();

		String json;
		try {
			String loadDataMethodName = request.getParameter("loadDataMethodName");
			String value = request.getParameter("value");
			value = StringUtils.isBlank( value )?null:value.toString();
			if( StringUtils.isBlank( loadDataMethodName )  ){
				throw new PlatformException("加载下来框数据请求参数错误！");
			}

			//查询数据
			json = (String)this.getBaseService().loadComboboxData(loadDataMethodName,value);
		} catch(Exception e) {
			e.printStackTrace();
			json = "{\"comboboxData\":[]}";
		}

		modelAndView.addObject( PlatformSysConstant.JSONSTR, json );
		modelAndView.setViewName( this.getJsonView() );
		return modelAndView;
	}

	/**
	 * 请求提交的参数给java基本类型及日期类型需要进行类型转换处理，否则请求报400
	 * 解决参数基本类型及日期类型传递问题
	 * @param binder
     */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		/**
		 * 提交的参数转换为Date类型
		 */
		//使用如下new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true)只能正确处理年月日格式
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
		//使用new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true)只能正确处理年月日时分秒格式
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		/**
		 * 使用PlatformCustomDateEditor可以同时处理yyyy-MM-dd和yyyy-MM-dd HH:mm:ss两种格式
		 */
		binder.registerCustomEditor(Date.class, new PlatformCustomDateEditor());

		/**
		 * 基本类型可以正常解析，暂不需要进行类型转换处理   201705
		 *
		 * 基本类型无法正常解析，加上转换器也不起作用   20170621
		 * java类型是基本类型的时候，页面参数为空或非数字时，无法进行类型转换，请求页面时报400错误。
		 *
		 */
		binder.registerCustomEditor(short.class,new ShortEditor());
		binder.registerCustomEditor(int.class, new IntEditor());
		binder.registerCustomEditor(long.class, new LongEditor());
		binder.registerCustomEditor(float.class, new FloatEditor());
		binder.registerCustomEditor(double.class, new DoubleEditor());
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

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
}
