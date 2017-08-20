package com.tgw.controller.base;

import com.github.pagehelper.Page;
import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.bean.system.SysEnController;
import com.tgw.bean.system.SysEnControllerField;
import com.tgw.bean.system.SysEnControllerFunction;
import com.tgw.bean.system.form.field.SysEnFieldComboBox;
import com.tgw.bean.system.form.field.SysEnFieldComboBoxGroup;
import com.tgw.bean.system.form.field.SysEnFieldDate;
import com.tgw.bean.system.form.field.SysEnFieldTag;
import com.tgw.bean.system.tree.SysEnTreeNode;
import com.tgw.exception.PlatformException;
import com.tgw.platform.propertyeditors.PlatformCustomDateEditor;
import com.tgw.service.base.BaseService;
import com.tgw.utils.PlatformInfo;
import com.tgw.utils.PlatformUtils;
import com.tgw.utils.config.PlatformSysConstant;
import com.tgw.utils.file.PlatformFileUtils;
import com.tgw.utils.string.PlatformStringUtils;
import com.tgw.utils.tree.PlatformTreeUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import sun.beans.editors.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

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
	 *
	 * 主要原因是BaseModelMapper在BaseServiceImpl中无法注入，导致baseService调用BaseModelMapper时报空指针异常。
	 * BaseController中的方法调用BaseService的方法时会报错（在BaseService的方法使用了BaseModelMapper的情况下）。
	 * @param baseService
     */
	public void initService(BaseService baseService){
		this.setBaseService(baseService);
		this.getBaseService().initMapper();
	}

	public void initSearch(HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView,SysEnController controller, T bean){
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
		ModelAndView modelAndView = new ModelAndView();

		try{

		this.beforeSearch(request,response,bean);
		System.out.println("----------------- BaseController  search -----------------");
		SysEnController controller = new SysEnController();

			/**
			 * 以下三个的初始化顺序不能变。
			 */
		this.initSearch(request,response,modelAndView,controller,bean);
		this.initField( controller );
		this.initFunction( controller );

		//初始化页面添加、编辑窗口配置
		//modelAndView.addObject("updateFieldList",updateFieldList );


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
			List<SysEnFieldComboBox> comboBoxAddList = new ArrayList<SysEnFieldComboBox>();
			List<SysEnFieldComboBox> comboBoxUpdateList = new ArrayList<SysEnFieldComboBox>();
			List<SysEnFieldComboBox> comboBoxSearchList = new ArrayList<SysEnFieldComboBox>();
			//页面上的所有tag控件，tag继承自comboBox
			List<SysEnFieldTag> tagList = new ArrayList<SysEnFieldTag>();
			List<SysEnFieldTag> tagAddList = new ArrayList<SysEnFieldTag>();
			List<SysEnFieldTag> tagUpdateList = new ArrayList<SysEnFieldTag>();
			List<SysEnFieldTag> tagSearchList = new ArrayList<SysEnFieldTag>();

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

				if( conField.isShowList() ){
                	showFieldList.add( conField );
				}

				if( PlatformSysConstant.FORM_XTYPE_COMBOBOX.equals( conField.getXtype() ) ){
					SysEnFieldComboBoxGroup comboBoxGroup = (SysEnFieldComboBoxGroup)conField.getSysEnFieldAttr();
					 comboBoxGroup.getComboBoxList();
					for( SysEnFieldComboBox comboBox:comboBoxGroup.getComboBoxList() ){
						comboBoxList.add( comboBox );
						if( conField.isAllowAdd() ){
							comboBoxAddList.add( comboBox );
						}
						if( conField.isAllowUpdate() ){
							comboBoxUpdateList.add( comboBox );
						}
						if( conField.isAllowSearch() ){
							comboBoxSearchList.add( comboBox );
						}

					}
				}

				if( PlatformSysConstant.FORM_XTYPE_TAG.equals( conField.getXtype() ) ){
					SysEnFieldTag sysEnFieldTag = (SysEnFieldTag)conField.getSysEnFieldAttr();
					tagList.add( sysEnFieldTag );
					if( conField.isAllowAdd() ){
						tagAddList.add(sysEnFieldTag);
					}
					if( conField.isAllowUpdate() ){
						tagUpdateList.add(sysEnFieldTag);
					}
					if( conField.isAllowSearch() ){
						tagSearchList.add( sysEnFieldTag );
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
			modelAndView.addObject("comboBoxAddList",comboBoxAddList);
			modelAndView.addObject("comboBoxUpdateList",comboBoxUpdateList);
			modelAndView.addObject("comboBoxSearchList",comboBoxSearchList);
			modelAndView.addObject("tagList",tagList);
			modelAndView.addObject("tagAddList",tagAddList);
			modelAndView.addObject("tagUpdateList",tagUpdateList);
			modelAndView.addObject("tagSearchList",tagSearchList);


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
		SysEnControllerFunction addFunction = new SysEnControllerFunction("baseAdd","添加","",PlatformSysConstant.MENU_TYPE_ADD,true,"Add",-100);
		SysEnControllerFunction delFunction = new SysEnControllerFunction("baseDelete","删除","/delete.do",PlatformSysConstant.MENU_TYPE_BASE_AJAX,false,"Delete",-99);
		sysEnControllerFunctionList.add( addFunction );
		sysEnControllerFunctionList.add( delFunction );
		sysEnControllerFunctionList.addAll( controller.getSysEnControllerFunctionList() );

		modelAndView.addObject("functionBarList", sysEnControllerFunctionList );
		//初始化页面上的菜单按钮信息结束

		modelAndView.addObject("pageSize", PlatformSysConstant.PAGE_SIZE );
		if( StringUtils.isNotBlank( controller.getIdentifier() ) ){
			modelAndView.addObject("identifier",controller.getIdentifier() );// 每一个列表页面的唯一身份id
		}else{
			throw new PlatformException("controller配置错误，没有配置identifier");
		}

		if( StringUtils.isNotBlank( controller.getLoadDataUrl() ) ){
			modelAndView.addObject("loadDataUrl",controller.getLoadDataUrl());
		}else{
			throw new PlatformException("controller配置错误，没有配置loadDataUrl");
		}

		if( StringUtils.isNotBlank( controller.getControllerBaseUrl() ) ){
			modelAndView.addObject("controllerBaseUrl",controller.getControllerBaseUrl() );
		}else{
			throw new PlatformException("controller配置错误，没有配置controllerBaseUrl");
		}
		modelAndView.addObject("controller",controller );


		modelAndView.setViewName("common/showDataSVP");
		this.afterSearch(request,response,bean);

		}catch( PlatformException e ){
			e.printStackTrace();
		}
		catch( Exception e ){
			e.printStackTrace();
		}

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

		/*try{
			String renameFileName = PlatformFileUtils.renameFileNameByTimeRandom( "简历.doc" );
			String path= PlatformInfo.getAbsolutePath(request,"/upload/pic");
			System.out.println("renameFileName-->"+renameFileName);
			System.out.println("path-->"+path);
		}catch( Exception e ){
			e.printStackTrace();
		}*/

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
			Page queryResPage = this.getBaseService().searchData(pageNum,pageSize,bean);
			List items = queryResPage.getResult();
			items = dealSearchData(request,response,bean,items);

			//组装查询结果
			JSONObject jo = JSONObject.fromObject("{}");
			jo.put("total",queryResPage.getTotal() );
			jo.put("items", items );

			modelAndView.addObject( PlatformSysConstant.JSONSTR, jo.toString() );

		} catch(Exception e) {
			e.printStackTrace();
		}

		modelAndView.setViewName( this.getJsonView() );
		return modelAndView;
	}

	/**
	 * 对查询结果进行处理。
	 * 如果需要对从数据库中查询出的结果进行处理，可以在具体业务中覆写此方法。
	 * @param request
	 * @param response
	 * @param bean
	 * @param dataList
     */
	public List dealSearchData(HttpServletRequest request, HttpServletResponse response, T bean,List dataList){
		return dataList;
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
			/**
			 * 查找上传的文件，将文件保存到指定路径中，将路径地址保存到数据库中。
			 */
		    //处理附件开始
			/*
			//自己实现的查找文件类型字段
			Class beanClass = bean.getClass();
			Field[] tempFields = beanClass.getDeclaredFields();
			Class multipartFileClass = MultipartFile.class;
			for( int i =0;i<tempFields.length;i++ ){
				Class fieldClass = tempFields[i].getType();
				if( fieldClass.equals( multipartFileClass ) ){//字段是文件类型
					String fieldName = tempFields[i].getName();
					Method method = beanClass.getDeclaredMethod("get"+PlatformUtils.firstLetterToUpperCase(fieldName));
					MultipartFile temp = (MultipartFile)method.invoke(bean);
				}
			}*/

			//使用spring mvc 自带的方法查找上传的附件
			//将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
			CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver( request.getSession().getServletContext() );
			//检查form中是否有enctype="multipart/form-data"
			if(multipartResolver.isMultipart(request)){
				//将request变成多部分request
				MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
				//获取multiRequest 中所有的文件名
				Iterator iter=multiRequest.getFileNames();

				while(iter.hasNext())
				{
					//一次遍历所有文件
					MultipartFile file=multiRequest.getFile(iter.next().toString());
					if(file!=null)
					{

						/**
						* 页面上有文件表单字段时，即使用户在浏览器中没有选择附件，MultipartFile也不为空。但其实文件内容是空的。
						 * 在此通过判断文件名是否为空来判断是否上传了文件 。
					 	*/
						if( StringUtils.isBlank( file.getOriginalFilename() ) ){
							//continue;
							System.out.println("OriginalFilename为空！");
						}

						if( file.isEmpty() ){
							continue;
						}

						//获取文件保存路径
						String savePath = request.getParameter( file.getName()+"SavePathHidden" );
						if( StringUtils.isBlank( savePath ) ){
							throw new PlatformException("没有获取到上传文件的保存路径！");
						}

						//路径修正
						savePath = savePath.replace("/",File.separator);
						savePath = savePath.replace("\\",File.separator);
						if( !savePath.startsWith( File.separator ) ){
							savePath = File.separator+savePath;
						}
						//所有上传的文件都放在项目根目录下的atta目录中
						savePath = File.separator+"atta"+savePath;

						//创建目录
						String realPath= PlatformInfo.getAbsolutePath(request,savePath);
						File fileDir = new File(realPath);
						if( !fileDir.exists() ){
							boolean mkdirRes = fileDir.mkdirs();
							if( !mkdirRes ){
								throw new PlatformException("创建上传的文件保存目录失败！");
							}
						}

						//重命名文件名
						String renameFileName = PlatformFileUtils.renameFileNameByTimeRandom( file.getOriginalFilename() );
						if( realPath.endsWith( File.separator ) ){
							realPath = realPath+renameFileName;
						}else{
							realPath = realPath+File.separator+renameFileName;
						}

						File objFile = new File(realPath);
						//保存文件
						file.transferTo( objFile );

						//将文件的存储路径及原文件名保存到数据库中
						if( savePath.endsWith( File.separator ) ){
							savePath = savePath+renameFileName;
						}else{
							savePath = savePath+File.separator+renameFileName;
						}
						try{
							Class beanClass = bean.getClass();
							Method fileUrlMethod = beanClass.getDeclaredMethod( "set"+ PlatformStringUtils.firstLetterToUpperCase(file.getName()+"Url"),String.class );
							Method fileNameMethod = beanClass.getDeclaredMethod( "set"+PlatformStringUtils.firstLetterToUpperCase(file.getName()+"OrigFileName"),String.class );
							fileUrlMethod.invoke(bean,savePath);
							fileNameMethod.invoke(bean,file.getOriginalFilename());
						}catch (NoSuchMethodException e){
							e.printStackTrace();
							throw new PlatformException("上传附件配置错误，缺少相关字段！");
						}

					}

				}

			}
			//处理附件结束

            this.getBaseService().saveBean(bean);
            jo.put("success",true);
            jo.put("msg","保存成功！");
        }catch( PlatformException e){
			jo.put("success",false);
			jo.put("msg","保存失败！"+e.getMsg() );
		}catch (Exception e){
            e.printStackTrace();
            jo.put("success",false);
            jo.put("msg","保存失败，发生异常！");
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

					/**
					 * 将对象的各属性值组成json串
					 */
					JSONObject objJSON = JSONObject.fromObject("{}");
					for( SysEnControllerField updateField : updateFieldList ){
						Method met=null;
						Object tempObj = null;

						if( PlatformSysConstant.FORM_XTYPE_COMBOBOX.equals( updateField.getXtype() ) ){//下拉框类型

							SysEnFieldComboBoxGroup sysEnFieldComboBoxGroup = (SysEnFieldComboBoxGroup)updateField.getSysEnFieldAttr();
							if( sysEnFieldComboBoxGroup.isCascade() ){//级联下拉框
								List<SysEnFieldComboBox>  comboBoxList = sysEnFieldComboBoxGroup.getComboBoxList();
								/**
                                 * 遍历级联下拉框组的每一个下拉框
								 */
								for( SysEnFieldComboBox sysEnFieldComboBox:comboBoxList ){
									met=obj.getClass().getDeclaredMethod( "get"+PlatformStringUtils.firstLetterToUpperCase( sysEnFieldComboBox.getComboBoxName() ) );
									tempObj = met.invoke(obj);

									objJSON.put( sysEnFieldComboBox.getComboBoxName(),tempObj );
								}

							}else{//单个下拉框
								met=obj.getClass().getDeclaredMethod( "get"+PlatformStringUtils.firstLetterToUpperCase(updateField.getName()) );
								tempObj = met.invoke(obj);

								objJSON.put( updateField.getName(),tempObj );
							}

						}else if( PlatformSysConstant.FORM_XTYPE_FILE.equals( updateField.getXtype() ) ){
							//编辑页面中显示文件的原始文件名
							met=obj.getClass().getDeclaredMethod( "get"+PlatformStringUtils.firstLetterToUpperCase(updateField.getName())+"OrigFileName" );
							tempObj = met.invoke(obj);

							objJSON.put( updateField.getName(),tempObj );
						}else if( updateField.getName().contains( "SavePathHidden" ) ){
							/**
							 * 包含SavePathHidden，则说明此字段为存储附件的路径字段。
                             * 保存文件的路径，此字段为框架自动加的，不需要给赋值，具体业务的controller在添加附件字段时已经给此字段配置了值。
							 */
						}else{//其他类型字段
							met=obj.getClass().getDeclaredMethod( "get"+PlatformStringUtils.firstLetterToUpperCase(updateField.getName()) );
							tempObj = met.invoke(obj);

							Class returnClass = met.getReturnType();
							if( PlatformSysConstant.FORM_XTYPE_DATE.equals( updateField.getXtype() ) ||
								PlatformSysConstant.FORM_XTYPE_DATE_TIME.equals(  updateField.getXtype()  )	){

								if( Date.class.equals( returnClass ) ){
									//java类中定义的时间属性为Date类型
									if( null!=tempObj ){
										SysEnFieldDate sysEnFieldDate = (SysEnFieldDate)updateField.getSysEnFieldAttr();
										SimpleDateFormat sdf = new SimpleDateFormat( sysEnFieldDate.getFormatJava() );
										Date tempDate = (Date)tempObj;
										objJSON.put( updateField.getName(),sdf.format(tempDate) );
									}
								}else{
									//java类中定义的时间属性为String类型
									objJSON.put( updateField.getName(),tempObj );
								}
							}else{
								objJSON.put( updateField.getName(),tempObj );
							}

						}


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

		/**
		 * 更新方法实现思路
		 *
		 * 1.根据表单提交的id，从数据库中获取到原先的oldBean对象。
		 * 2.将表单提交的bean对象的各字段的值赋值给oldBean对象。
		 * 3.更新保存oldBean对象。
		 */
		try{
			SysEnController controller = new SysEnController();
			this.initField( controller );

			Class beanClass = bean.getClass();

			//获取表单提交的id
			Method getIdMet=beanClass.getDeclaredMethod( "getId" );
			Object tempIdObj = getIdMet.invoke(bean);
			Integer tempId = Integer.parseInt( tempIdObj.toString() );

			//根据表单提交的id获取oldBean对象
			Object oldObj = beanClass.newInstance();
			Method setIdMethod = beanClass.getDeclaredMethod("setId",new Class[]{ Integer.class } );
			setIdMethod.invoke(oldObj,tempId );
			oldObj = this.getBaseService().selectUniqueBeanByPrimaryKey( oldObj );

			//将表单提交的bean对象的各字段的值赋值给oldBean对象
			if( null!=controller.getSysEnControllerFieldList() && controller.getSysEnControllerFieldList().size()>0 ) {

				//获取所有可被更新的字段
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
						Method getMet=null;
						Object tempObj =null;

						Method setMet=null;

						if( PlatformSysConstant.FORM_XTYPE_COMBOBOX.equals( updateField.getXtype() ) ){//下拉框类型

							SysEnFieldComboBoxGroup sysEnFieldComboBoxGroup = (SysEnFieldComboBoxGroup)updateField.getSysEnFieldAttr();
							if( sysEnFieldComboBoxGroup.isCascade() ){//级联下拉框
								List<SysEnFieldComboBox>  comboBoxList = sysEnFieldComboBoxGroup.getComboBoxList();
								/**
								 * 遍历级联下拉框组的每一个下拉框
								 */
								for( SysEnFieldComboBox sysEnFieldComboBox:comboBoxList ){
									getMet=beanClass.getDeclaredMethod( "get"+PlatformStringUtils.firstLetterToUpperCase(sysEnFieldComboBox.getComboBoxName()) );
									tempObj = getMet.invoke( bean );

									setMet=beanClass.getDeclaredMethod( "set"+PlatformStringUtils.firstLetterToUpperCase(sysEnFieldComboBox.getComboBoxName()),getMet.getReturnType() );
									setMet.invoke(oldObj,tempObj);
								}

							}else{//单个下拉框
								getMet=beanClass.getDeclaredMethod( "get"+PlatformStringUtils.firstLetterToUpperCase(updateField.getName()) );
								tempObj = getMet.invoke( bean );

								setMet=beanClass.getDeclaredMethod( "set"+PlatformStringUtils.firstLetterToUpperCase(updateField.getName()),getMet.getReturnType() );
								setMet.invoke(oldObj,tempObj);
							}

						}else if( PlatformSysConstant.FORM_XTYPE_FILE.equals( updateField.getXtype() ) ){//文件类型字段
							//文件类型字段不在此处更新，在后面对文件类型字段特殊处理
							continue;
						}else if( updateField.getName().contains( "SavePathHidden" ) ){
							/**
							 * 包含SavePathHidden，则说明此字段为存储附件的路径字段。
							 * 保存文件的路径，此字段为框架自动加的，不需要处理，在后面处理附件时使用。
							 */
						}else{//其它类型字段
							getMet=beanClass.getDeclaredMethod( "get"+PlatformStringUtils.firstLetterToUpperCase(updateField.getName()) );
							tempObj = getMet.invoke( bean );

							setMet=beanClass.getDeclaredMethod( "set"+PlatformStringUtils.firstLetterToUpperCase(updateField.getName()),getMet.getReturnType() );
							setMet.invoke(oldObj,tempObj);
						}

					}

					/**
					 * 查找上传的文件，将文件保存到指定路径中，将路径地址保存到数据库中。
					 */
					//处理附件开始

					//使用spring mvc 自带的方法查找上传的附件
					//将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
					CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver( request.getSession().getServletContext() );
					//检查form中是否有enctype="multipart/form-data"
					if(multipartResolver.isMultipart(request)){
						//将request变成多部分request
						MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
						//获取multiRequest 中所有的文件名
						Iterator iter=multiRequest.getFileNames();

						while(iter.hasNext())
						{
							//一次遍历所有文件
							MultipartFile file=multiRequest.getFile(iter.next().toString());
							if(file!=null)
							{

								/**
								 * 页面上有文件表单字段时，即使用户在浏览器中没有选择附件，MultipartFile也不为空。但其实文件内容是空的。
								 * 判断是否上传了文件 。
								 */
								if( file.isEmpty() ){
									continue;
								}
								//获取文件保存路径
								String savePath = request.getParameter( file.getName()+"SavePathHidden" );
								if( StringUtils.isBlank( savePath ) ){
									throw new PlatformException("没有获取到上传文件的保存路径！");
								}

								//路径修正
								savePath = savePath.replace("/",File.separator);
								savePath = savePath.replace("\\",File.separator);
								if( !savePath.startsWith( File.separator ) ){
									savePath = File.separator+savePath;
								}
								//所有上传的文件都放在项目根目录下的atta目录中
								savePath = File.separator+"atta"+savePath;

								//创建目录
								String realPath= PlatformInfo.getAbsolutePath(request,savePath);
								File fileDir = new File(realPath);
								if( !fileDir.exists() ){
									boolean mkdirRes = fileDir.mkdirs();
									if( !mkdirRes ){
										throw new PlatformException("创建上传的文件保存目录失败！");
									}
								}

								//重命名文件名
								String renameFileName = PlatformFileUtils.renameFileNameByTimeRandom( file.getOriginalFilename() );
								if( realPath.endsWith( File.separator ) ){
									realPath = realPath+renameFileName;
								}else{
									realPath = realPath+File.separator+renameFileName;
								}

								File objFile = new File(realPath);
								//保存文件
								file.transferTo( objFile );

								//将文件的存储路径及原文件名保存到数据库中
								if( savePath.endsWith( File.separator ) ){
									savePath = savePath+renameFileName;
								}else{
									savePath = savePath+File.separator+renameFileName;
								}
								try{
									Method fileUrlMethod = beanClass.getDeclaredMethod( "set"+PlatformStringUtils.firstLetterToUpperCase(file.getName()+"Url"),String.class );
									Method fileNameMethod = beanClass.getDeclaredMethod( "set"+PlatformStringUtils.firstLetterToUpperCase(file.getName()+"OrigFileName"),String.class );
									fileUrlMethod.invoke(oldObj,savePath);
									fileNameMethod.invoke(oldObj,file.getOriginalFilename());
								}catch (NoSuchMethodException e){
									e.printStackTrace();
									throw new PlatformException("上传附件配置错误，缺少相关字段！");
								}

							}

						}

					}
					//处理附件结束

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

	public void beforeMenuAjaxUpdate(  HttpServletRequest request, HttpServletResponse response, T bean  ){

	}

	/**
	 * 菜单异步更新部分字段值
	 * @param request
	 * @param response
	 * @param bean
     * @return
     */
	@RequestMapping("/menuAjaxUpdate.do")
	public ModelAndView menuAjaxUpdate( HttpServletRequest request, HttpServletResponse response, T bean ){
		this.beforeMenuAjaxUpdate( request,response,bean );

		ModelAndView modelAndView = new ModelAndView();
		JSONObject jo = JSONObject.fromObject("{}");

		/**
		 * extjs的form表单提交后根据返回值中的success值判断走success回调函数或failure函数
		 */

		/**
		 * 更新方法实现思路
		 *
		 * 1.根据表单提交的id，从数据库中获取到原先的oldBean对象。
		 * 2.将表单提交的bean对象的各字段的值赋值给oldBean对象。
		 * 3.更新保存oldBean对象。
		 */
		try{
			String ids = request.getParameter("ids");
			String ajaxUpdateFields = request.getParameter("ajaxUpdateFields");
			if( StringUtils.isBlank( ids ) || StringUtils.isBlank( ajaxUpdateFields ) ){
				throw new PlatformException("参数不能为空！");
			}

			List<Object> updateBeanList = new ArrayList<Object>();//所有要更新的对象
			String[] idArray = ids.split(",");
			String[] fieldArray = ajaxUpdateFields.split(",");
			if( idArray!=null && idArray.length>0 && fieldArray!=null && fieldArray.length>0 ){
				for( int i=0;i<idArray.length;i++ ){
					Class beanClass = bean.getClass();
					Integer tempId = Integer.parseInt( idArray[i] );

					//根据id获取oldBean对象
					Object oldObj = beanClass.newInstance();
					Method setIdMethod = beanClass.getDeclaredMethod("setId",new Class[]{ Integer.class } );
					setIdMethod.invoke(oldObj,tempId );
					oldObj = this.getBaseService().selectUniqueBeanByPrimaryKey( oldObj );

					//将表单提交的bean对象的各字段的值赋值给oldBean对象
					if( null!=fieldArray && fieldArray.length>0 ){
						for( int j=0;j<fieldArray.length;j++ ){
							Method getMet=null;
							Method setMet=null;
							Object tempObj =null;

							getMet=beanClass.getDeclaredMethod( "get"+PlatformStringUtils.firstLetterToUpperCase(fieldArray[j]) );
							tempObj = getMet.invoke( bean );

							setMet=beanClass.getDeclaredMethod( "set"+PlatformStringUtils.firstLetterToUpperCase(fieldArray[j]),getMet.getReturnType() );
							setMet.invoke(oldObj,tempObj);
						}

						updateBeanList.add( oldObj );

					}else{
						throw new PlatformException("没有可更新的字段！");
					}

				}
			}else{
				throw new PlatformException("参数不能为空！");
			}

			this.getBaseService().updateBeans( updateBeanList );
			jo.put("success",true);
			jo.put("msg","修改成功！");
		}catch (Exception e){
			e.printStackTrace();
			jo.put("success",false);
			jo.put("msg","修改失败！");
		}

		modelAndView.addObject( PlatformSysConstant.JSONSTR, jo.toString() );
		modelAndView.setViewName( this.getJsonView() );

		this.afterMenuAjaxUpdate(request,response,bean);
		return  modelAndView;
	}

	public void afterMenuAjaxUpdate(  HttpServletRequest request, HttpServletResponse response, T bean  ){

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
			//String loadDataMethodName = request.getParameter("loadDataMethodName");
			String value = request.getParameter("value");
			value = StringUtils.isBlank( value )?null:value.toString();
			/*if( StringUtils.isBlank( loadDataMethodName )  ){
				throw new PlatformException("加载下来框数据请求参数错误！");
			}*/

			String cascChild = request.getParameter("cascChild");
			if( StringUtils.isNotBlank( cascChild ) && "true".equals( cascChild.trim() ) && StringUtils.isBlank( value ) ){
				//级联框的非第一个下拉框请求数据时，value必须有值。
				//value为父级联框选中的值。
				throw new PlatformException("级联下拉框加载数据请求参数错误！");
			}

			//查询数据
			List<Map<String,Object>> resList = this.loadComboBoxDataMap(request,response,bean,value);

			JSONObject jo = JSONObject.fromObject("{}");
			jo.put("comboboxData", resList );
			json = jo.toString();
		} catch(PlatformException e) {
			json = "{\"comboboxData\":[]}";
		}catch(Exception e) {
			e.printStackTrace();
			json = "{\"comboboxData\":[]}";
		}

		modelAndView.addObject( PlatformSysConstant.JSONSTR, json );
		modelAndView.setViewName( this.getJsonView() );
		return modelAndView;
	}

	/**
	 * 查询下拉框数据方法
	 *
	 * 1.如果在具体业务的controller中覆写此方法，可以根据具体业务返回所需要的数据。
	 * 2.具体业务controller不覆写此方法，则调用BaseController类本身方法，走系统统一的接口查询数据。此情况之前的实现方式过于依赖具体业务的service,暂时放弃，后续再实现。
	 * @param request
	 * @param response
	 * @param bean
	 * @param parentId
     * @return
     */
	public List<Map<String,Object>> loadComboBoxDataMap(HttpServletRequest request, HttpServletResponse response, T bean,String parentId){
		List<Map<String,Object>> queryResList = new ArrayList<Map<String,Object>>();
		return queryResList;
	}

	/**
	 * 加载树节点json串
	 * @param request
	 * @param response
	 * @param bean
     * @return
     */
	@RequestMapping("/loadTreeData.do")
	public ModelAndView loadTreeData(HttpServletRequest request, HttpServletResponse response, T bean){
		System.out.println("----------------- BaseController --> loadTreeData()   -----------------");
		ModelAndView modelAndView = new ModelAndView();

		try {
			String fieldMap = request.getParameter("fieldMap");//查询结果字段与ext树节点属性对应关系
			String resType = request.getParameter("resType");//sql查询结果集
			String multiSelect = request.getParameter("multiSelect");//是否多选，多选时，树节点前有checkbox，单选没有

			if( StringUtils.isBlank( fieldMap )  ){
				throw new PlatformException("加载树节点请求参数错误！");
			}

			List<SysEnTreeNode> sysEnTreeNodeList = null;

			if( StringUtils.isBlank( resType ) || "map".equals( resType ) ){
				/**
				 * sql查询结果集映射为List<Map<String,Object>>，然后对map进行处理。
				 *
				 * loadTreeNodeDataMap方法返回树节点数据。
				 * 在具体业务controller中实现loadTreeNodeDataMap方法，
				 */
				List<Map<String,Object>> queryResList = this.loadTreeNodeDataMap(request,response,bean);
				sysEnTreeNodeList = PlatformTreeUtils.copyMapValueToTreeNode( queryResList,fieldMap );
			}else{
				/**
				 * sql查询结果集映射为List<Object>，后续再实现
				 */
				//sysEnTreeNodeList = PlatformTreeUtils.copyBeanProToTreeNode(null,fieldMap);
			}
			String treeNodeJson = PlatformTreeUtils.createExtTreeList( sysEnTreeNodeList,"true".equals( multiSelect )?true:false );

			modelAndView.addObject("jsonStr", treeNodeJson );
		} catch(Exception e) {
			e.printStackTrace();
			modelAndView.addObject("jsonStr", "[]" );
		}

		modelAndView.setViewName( this.getJsonView() );
		return modelAndView;
	}

	/**
	 * 查询树节点数据方法。
	 *
	 * 1.如果在具体业务的controller中覆写此方法，可以根据具体业务返回所需要的树节点数据。
	 * 2.具体业务controller不覆写此方法，则调用BaseController类本身方法，走系统统一的接口查询树结点数据。此情况实现了一半，后续再实现。
	 * @param request
	 * @param response
	 * @param bean
     * @return
     */
	public List<Map<String,Object>> loadTreeNodeDataMap(HttpServletRequest request, HttpServletResponse response, T bean){
		//List<Map<String,Object>> queryResList = this.getBaseService().loadTreeNodeDataMap("loadTreeNodeDataMap");
		List<Map<String,Object>> queryResList = new ArrayList<Map<String,Object>>();
		return queryResList;
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
