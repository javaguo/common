package com.tgw.service.impl.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tgw.bean.base.AbstractBaseBean;
import com.tgw.bean.base.BaseEnConstant;
import com.tgw.bean.system.SysEnMenu;
import com.tgw.dao.base.BaseConstantMapper;
import com.tgw.dao.base.TestMapper;
import com.tgw.service.base.TestService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by zhaojg on 2016/10/16.
 */
@Service("testService")
public class TestServiceImpl extends BaseServiceImpl implements TestService {

    @Resource
    private TestMapper testMapper;

    @Resource
    private BaseConstantMapper baseEnConstantMapper;

    /**
     * 测试分页
     */
    @Override
    public void testPage() {
        /**
         * 框架自带的查询方法，返回实体的集合
         */
        System.out.println("<-------------------------------开始------------------------------->");
        System.out.println("--框架自带的查询方法分页查询，返回实体的集合--");
        PageHelper.startPage(1,2);
        List<BaseEnConstant> list = this.getTestMapper().selectAll();
        Page tempPage = (Page) list;
        System.out.println("总数："+tempPage.getTotal() +"   页数："+tempPage.getPages()+"    第几页："+tempPage.getPageNum() + "   每页大小："+tempPage.getPageSize() );
        List resA1 = tempPage.getResult();
        JSONObject joA1 = JSONObject.fromObject("{}");
        joA1.put("resA1",resA1);
        System.out.println( "resA1-->"+joA1.toString() );
        System.out.println("<-------------------------------结束------------------------------->");


        /**
         * 测试自定义的查询语句，返回实体的集合
         */
        System.out.println("<-------------------------------开始------------------------------->");
        System.out.println("--自定义sql语句分页查询，返回实体的集合--");
        PageHelper.startPage(2,2);
        List<SysEnMenu> list4 = this.getTestMapper().loadMenuByRole(1l);
        Page tempPage4 = (Page) list4;
        System.out.println("总数："+tempPage4.getTotal() +"   页数："+tempPage4.getPages()+"    第几页："+tempPage4.getPageNum() + "   每页大小："+tempPage4.getPageSize() );
        List resB1 = tempPage4.getResult();
        JSONObject joB1 = JSONObject.fromObject("{}");
        joB1.put("resB1",resB1);
        System.out.println( "resB1-->"+joB1.toString() );
        System.out.println("<-------------------------------结束------------------------------->");


        System.out.println("<-------------------------------开始------------------------------->");
        System.out.println("--自定义sql语句分页查询，返回List<Map<String,Object>>--");
        PageHelper.startPage(1,5);
        List<Map<String,Object>> listC1 = this.getTestMapper().testTypeAliasesMap(1L);
        Page tempPageC1 = (Page) listC1;
        System.out.println("总数："+tempPageC1.getTotal() +"   页数："+tempPageC1.getPages()+"    第几页："+tempPageC1.getPageNum() + "   每页大小："+tempPageC1.getPageSize() );
        List resC1 = tempPageC1.getResult();
        JSONObject joC1 = JSONObject.fromObject("{}");
        joC1.put("resC1",resC1);
        System.out.println( "resC1-->"+joC1.toString() );
        System.out.println("<-------------------------------结束------------------------------->");
    }

    @Override
    public List<Map<String,Object>> testTypeAliasesMap() {
        /**
         * 查询结果返回List<Map<String,Object>>
         */
        List<Map<String,Object>> tempList = this.getTestMapper().testTypeAliasesMap(1L);
        return tempList;
    }

    @Override
    public List testTypeAliasesList() {
        /**
         * 查询结果返回List<object[]>，目前此方法有问题，无法正确返回所有查询的列
         */
        List tempList = this.getTestMapper().testTypeAliasesList(1L);
        return tempList;
    }

    /**
     * 子类mapper实现父类定义的接口
     */
    @Override
    public void testParentMapper(){
        System.out.println("--testParentMapper--");
        try{
            BaseEnConstant con = new BaseEnConstant();

            PageHelper.startPage(2,2);
            List<Map<String,Object>> listC2 = this.getTestMapper().searchData(con);
            Page tempPageC2 = (Page) listC2;
            System.out.println("总数："+tempPageC2.getTotal() +"   页数："+tempPageC2.getPages()+"    第几页："+tempPageC2.getPageNum() + "   每页大小："+tempPageC2.getPageSize() );
            List resC2 = tempPageC2.getResult();
            JSONObject joC2 = JSONObject.fromObject("{}");
            joC2.put("tota2",tempPageC2.getTotal() );
            joC2.put("items", resC2 );
            System.out.println( "resC2-->"+joC2.toString() );
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            BaseEnConstant con = new BaseEnConstant();

            PageHelper.startPage(2,2);
            List<Map<String,Object>> listC2 = this.getBaseEnConstantMapper().searchData(con);
            Page tempPageC2 = (Page) listC2;
            System.out.println("总数："+tempPageC2.getTotal() +"   页数："+tempPageC2.getPages()+"    第几页："+tempPageC2.getPageNum() + "   每页大小："+tempPageC2.getPageSize() );
            List resC2 = tempPageC2.getResult();
            JSONObject joC2 = JSONObject.fromObject("{}");
            joC2.put("tota2",tempPageC2.getTotal() );
            joC2.put("items", resC2 );
            System.out.println( "resC2-->"+joC2.toString() );
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void initSearchData(int pageNum, int pageSize, Object object) {
        super.setBaseModelMapper( this.getTestMapper() );
    }

    public TestMapper getTestMapper() {
        return testMapper;
    }

    public void setTestMapper(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    public BaseConstantMapper getBaseEnConstantMapper() {
        return baseEnConstantMapper;
    }

    public void setBaseEnConstantMapper(BaseConstantMapper baseEnConstantMapper) {
        this.baseEnConstantMapper = baseEnConstantMapper;
    }
}
