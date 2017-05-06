package com.tgw.bean.system;

import com.tgw.bean.base.AbstractBaseBean;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Table(name="sys_en_menu")
public class SysEnMenu extends AbstractBaseBean {

	@Id
	@GeneratedValue(generator = "JDBC")
	private int id;
    private String menuIdentify;//菜单唯一字符串标识符
	private String text;//菜单名称
	private String link;//菜单链接地址
	private String qtip;//菜单提示
	private boolean isLeaf;//是否叶子节点
	private boolean isExpanded;//是否展开子节点
	private int parentId;
	private boolean isSelfUrl;//是否为平台自身内部url地址。1：是，0：否
	@Transient
	private List<SysEnMenu> children;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMenuIdentify() {
		return menuIdentify;
	}

	public void setMenuIdentify(String menuIdentify) {
		this.menuIdentify = menuIdentify;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getQtip() {
		return qtip;
	}

	public void setQtip(String qtip) {
		this.qtip = qtip;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean leaf) {
		isLeaf = leaf;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean expanded) {
		isExpanded = expanded;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public boolean isSelfUrl() {
		return isSelfUrl;
	}

	public void setSelfUrl(boolean selfUrl) {
		isSelfUrl = selfUrl;
	}

	public List<SysEnMenu> getChildren() {
		return children;
	}

	public void setChildren(List<SysEnMenu> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "SysEnMenu [id=" + id + ", text=" + text + ", link=" + link
				+ ", qtip=" + qtip + ", leaf=" + isLeaf + ", expanded="
				+ isExpanded + ", parentId=" + parentId + "]";
	}
	
	
}
