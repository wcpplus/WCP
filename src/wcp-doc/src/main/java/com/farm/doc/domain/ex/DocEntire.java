package com.farm.doc.domain.ex;

import java.util.ArrayList;
import java.util.List;

import com.farm.authority.domain.User;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.time.TimeTool;
import com.farm.doc.domain.Doc;
import com.farm.doc.domain.FarmDocfile;
import com.farm.doc.domain.FarmDocruninfo;
import com.farm.doc.domain.FarmDoctext;
import com.farm.doc.domain.FarmDoctype;
import com.farm.doc.server.plus.domain.DocAudit;
import com.farm.doc.server.plus.domain.Doctypepop;

public class DocEntire implements java.io.Serializable {

	public DocEntire(Doc doc) {
		this.doc = doc;
	}

	public DocEntire() {
		this.doc = new Doc();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3170304648147893712L;
	// 文档信息
	private Doc doc;
	// 文档内容
	private FarmDoctext texts;
	// 标签
	private List<String> tags;
	// 附件
	private List<FarmDocfile> files;
	// 类型
	/**
	 * 分类，1:1新增修改
	 */
	private FarmDoctype type;
	/**
	 * 工作小组，1:1新增修改
	 */
	private GroupEntire group;
	// --------------------------------------------------------------------------------------------------------
	/**
	 * 用量信息,输出
	 */
	private FarmDocruninfo runinfo;
	/**
	 * 当前知识分类，输出
	 */
	private List<FarmDoctype> currenttypes;
	/**
	 * 作者信息，输出
	 */
	private User user;
	/**
	 * 内容图，输出
	 */
	private String imgUrl;
	/**
	 * 照片,输出
	 */
	private String photoUrl;

	/**
	 * 分类读权限
	 */
	private List<Doctypepop> typeReadPops;
	/**
	 * 分类写权限
	 */
	private List<Doctypepop> typeWritePops;
	/**
	 * 分类审核权限
	 */
	private List<Doctypepop> typeAuditPops;

	/**
	 * 审核信息
	 */
	private DocAudit audit;

	// 待审版本
	private FarmDoctext auditTemp;

	
	public FarmDoctext getAuditTemp() {
		return auditTemp;
	}

	public void setAuditTemp(FarmDoctext auditTemp) {
		this.auditTemp = auditTemp;
	}

	public DocAudit getAudit() {
		return audit;
	}

	public void setAudit(DocAudit audit) {
		this.audit = audit;
	}

	public List<Doctypepop> getTypeReadPops() {
		return typeReadPops;
	}

	public void setTypeReadPops(List<Doctypepop> typeReadPops) {
		this.typeReadPops = typeReadPops;
	}

	public List<Doctypepop> getTypeWritePops() {
		return typeWritePops;
	}

	public void setTypeWritePops(List<Doctypepop> typeWritePops) {
		this.typeWritePops = typeWritePops;
	}

	public List<Doctypepop> getTypeAuditPops() {
		return typeAuditPops;
	}

	public void setTypeAuditPops(List<Doctypepop> typeAuditPops) {
		this.typeAuditPops = typeAuditPops;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Doc getDoc() {
		return doc;
	}

	public void setDoc(Doc doc) {
		this.doc = doc;
	}

	public FarmDoctext getTexts() {
		return texts;
	}

	public void setTexts(FarmDoctext texts) {
		this.texts = texts;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<FarmDocfile> getFiles() {
		if (files == null) {
			return new ArrayList<FarmDocfile>();
		}
		return files;
	}

	public void setFiles(List<FarmDocfile> files) {
		this.files = files;
	}

	/**
	 * 向文档添加附件的时候使用，该方法中的id会被封装到FarmDocfile对象中，单该对象其他属性都为null
	 * 
	 * @param fileid
	 */
	public void addFile(FarmDocfile file) {
		if (this.files == null) {
			this.files = new ArrayList<FarmDocfile>();
		}
		files.add(file);
	}

	public FarmDoctype getType() {
		return type;
	}

	public void setType(FarmDoctype type) {
		this.type = type;
	}

	public FarmDocruninfo getRuninfo() {
		return runinfo;
	}

	public void setRuninfo(FarmDocruninfo runinfo) {
		this.runinfo = runinfo;
	}

	public List<FarmDoctype> getCurrenttypes() {
		return currenttypes;
	}

	public void setCurrenttypes(List<FarmDoctype> currenttypes) {
		this.currenttypes = currenttypes;
	}

	public GroupEntire getGroup() {
		return group;
	}

	public void setGroup(GroupEntire group) {
		this.group = group;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setTexts(String text, LoginUser user) {
		FarmDoctext ntexts = new FarmDoctext();
		ntexts.setCtime(TimeTool.getTimeDate14());
		ntexts.setCuser(user.getId());
		ntexts.setCusername(user.getName());
		ntexts.setEtime(TimeTool.getTimeDate14());
		ntexts.setEuser(user.getId());
		ntexts.setEusername(user.getName());
		ntexts.setPstate("1");
		ntexts.setText1(text);
		this.texts = ntexts;
	}
}
