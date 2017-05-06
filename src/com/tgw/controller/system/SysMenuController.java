package com.tgw.controller.system;

import com.tgw.bean.system.SysEnMenu;
import com.tgw.controller.base.BaseController;
import com.tgw.service.system.SysEnMenuService;
import com.tgw.utils.PlatformInfo;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sysMenu")
public class SysMenuController extends BaseController<SysEnMenu> {
	
	@Resource
	public SysEnMenuService sysEnMenuService;
	
	@RequestMapping("/queryMenuByUser.do")
	public ModelAndView queryMenuByUser(HttpServletRequest request,HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView();
		
		List<SysEnMenu> e2tList = this.getSysEnMenuService().loadMenuByRole( "1" );
		
		/**
         * 定义“数组-链表”，该数组链表的每一项相当于一深度为2的小树
         * Map的key相当于“数组”的某一项，Map的value相当于该key所拥有的“链表”
         * 这里，key为父节点ID，list为具有相同父节点ID的所有同级子节点实体list（属于该父节点的所有子节点）
         */
        Map<String, List<SysEnMenu>> arrayListMap = new HashMap<String, List<SysEnMenu>>();
 
        for (SysEnMenu e : e2tList) {
            // 变量定义务必在循环内，对象是引用，不能重复使用同一个对象变量
        	SysEnMenu e2t = new SysEnMenu();
        	e2t.setId( e.getId() );

            e2t.setMenuIdentify( e.getMenuIdentify() );
        	e2t.setText( e.getText() );
        	e2t.setQtip( e.getQtip() );
        	if( e2t.isSelfUrl() ){
        		e2t.setLink( PlatformInfo.baseUrl(request)+e.getLink() );
        	}else{
        		e2t.setLink( e.getLink() );
        	}
        	e2t.setLeaf( e.isLeaf() );
        	e2t.setExpanded( e.isExpanded() );
        	e2t.setParentId( e.getParentId() );
        	
            String fatherId = String.valueOf(  e.getParentId()  );
            // 获取当前遍历结点的父ID，并判断该父节点的数组链表项是否存在，如果该“数组项-链表项”不存在，则新建一个，并放入“数组-链表”
            if (arrayListMap.get(fatherId) == null) {
                List<SysEnMenu> list = new ArrayList<SysEnMenu>();
                list.add(e2t);
                arrayListMap.put(fatherId, list);
            } else {
                List<SysEnMenu> valueList = arrayListMap.get(fatherId);
                valueList.add(e2t);
                arrayListMap.put(fatherId, valueList);
            }
        }
        // 以上，至此，第一遍遍历完毕，非叶子节点都拥有一个“数组-链表项”，也即“最小的树”已创建完毕
 
        // 以下，对“数组链表”Map进行遍历，更改“最小的树”的从属关系（更改指针指向），也即把所有小树组装成大树
        for (Map.Entry<String, List<SysEnMenu>> entry : arrayListMap.entrySet()) {
            // 获取当前遍历“数组项-链表项”的链表项，并对链表项进行遍历，从“数组-链表”小树中找到它的子节点，并将该子节点加到该小树的children中
            List<SysEnMenu> smallTreeList = new ArrayList<SysEnMenu>();
            smallTreeList = entry.getValue();
            int nodeListSize = smallTreeList.size();
            for (int i = 0; i < nodeListSize; i++) {
                String findID = String.valueOf( smallTreeList.get(i).getId() );
                List<SysEnMenu> findList = arrayListMap.get(findID);
                // 以下操作不能取出对象存放在变量中，否则将破坏树的完整性
                smallTreeList.get(i).setChildren(findList);
            }
        }
        // 获取以0为父Id的链表项，该链表项是根节点实体，里面已封装好各子节点，可以由于多个根节点，即这些根结点的父Id都为0
        List<SysEnMenu> rootNodeList = arrayListMap.get("0");
 
        JSONArray jsonArray = JSONArray.fromObject(rootNodeList);
//        return jsonArray.toString();
        
        /**
         * 也可以直接向response输出内容
         */
        /*try {
			response.getWriter().print( jsonArray.toString() );
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
        
        modelAndView.setViewName( super.getJsonView() );
        //取第一个，不显示查询出的根结点，根结点由前端js代码设置
        modelAndView.addObject("jsonStr", jsonArray.get(0).toString() );
        return modelAndView;
	}

	public SysEnMenuService getSysEnMenuService() {
		return sysEnMenuService;
	}

	public void setSysEnMenuService(SysEnMenuService sysEnMenuService) {
		this.sysEnMenuService = sysEnMenuService;
	}
	
}
