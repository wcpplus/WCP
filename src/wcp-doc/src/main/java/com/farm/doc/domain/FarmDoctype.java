package com.farm.doc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * FarmDoctype entity. @author MyEclipse Persistence Tools
 */

@Entity(name = "FarmDoctype")
@Table(name = "farm_doctype")
public class FarmDoctype implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
	private String id;
	@Column(name = "TAGS", length = 256)
	private String tags;
	@Column(name = "TREECODE", length = 256, nullable = false)
	private String treecode;
	@Column(name = "PARENTID", length = 32, nullable = false)
	private String parentid;
	@Column(name = "PSTATE", length = 2, nullable = false)
	private String pstate;
	@Column(name = "CUSER", length = 32, nullable = false)
	private String cuser;
	@Column(name = "EUSERNAME", length = 64, nullable = false)
	private String eusername;
	@Column(name = "EUSER", length = 32, nullable = false)
	private String euser;
	@Column(name = "PCONTENT", length = 128)
	private String pcontent;
	@Column(name = "CUSERNAME", length = 64, nullable = false)
	private String cusername;
	@Column(name = "ETIME", length = 16, nullable = false)
	private String etime;
	@Column(name = "CTIME", length = 16, nullable = false)
	private String ctime;
	@Column(name = "LINKURL", length = 256)
	private String linkurl;
	@Column(name = "METACONTENT", length = 256)
	private String metacontent;
	@Column(name = "CONTENTMOD", length = 128)
	private String contentmod;
	@Column(name = "SORT", length = 10, nullable = false)
	private Integer sort;
	@Column(name = "TYPE", length = 2, nullable = false)
	private String type;
	@Column(name = "METATITLE", length = 256)
	private String metatitle;
	@Column(name = "METAKEY", length = 256)
	private String metakey;
	@Column(name = "NAME", length = 128, nullable = false)
	private String name;
	@Column(name = "TYPEMOD", length = 128)
	private String typemod;
	@Column(name = "READPOP", length = 2, nullable = false)
	private String readpop;
	@Column(name = "WRITEPOP", length = 2, nullable = false)
	private String writepop;
	@Column(name = "AUDITPOP", length = 2, nullable = false)
	private String auditpop;

	// Constructors

	/** default constructor */
	public FarmDoctype() {
	}

	/** minimal constructor */
	public FarmDoctype(String name, Integer sort, String type, String ctime, String etime, String cusername,
			String cuser, String eusername, String euser, String pstate) {
		this.name = name;
		this.sort = sort;
		this.type = type;
		this.ctime = ctime;
		this.etime = etime;
		this.cusername = cusername;
		this.cuser = cuser;
		this.eusername = eusername;
		this.euser = euser;
		this.pstate = pstate;
	}

	/** full constructor */
	public FarmDoctype(String name, String typemod, String contentmod, Integer sort, String type, String metatitle,
			String metakey, String metacontent, String linkurl, String ctime, String etime, String cusername,
			String cuser, String eusername, String euser, String pcontent, String pstate) {
		this.name = name;
		this.typemod = typemod;
		this.contentmod = contentmod;
		this.sort = sort;
		this.type = type;
		this.metatitle = metatitle;
		this.metakey = metakey;
		this.metacontent = metacontent;
		this.linkurl = linkurl;
		this.ctime = ctime;
		this.etime = etime;
		this.cusername = cusername;
		this.cuser = cuser;
		this.eusername = eusername;
		this.euser = euser;
		this.pcontent = pcontent;
		this.pstate = pstate;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypemod() {
		return this.typemod;
	}

	public void setTypemod(String typemod) {
		this.typemod = typemod;
	}

	public String getContentmod() {
		return this.contentmod;
	}

	public void setContentmod(String contentmod) {
		this.contentmod = contentmod;
	}

	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMetatitle() {
		return this.metatitle;
	}

	public void setMetatitle(String metatitle) {
		this.metatitle = metatitle;
	}

	public String getMetakey() {
		return this.metakey;
	}

	public void setMetakey(String metakey) {
		this.metakey = metakey;
	}

	public String getMetacontent() {
		return this.metacontent;
	}

	public void setMetacontent(String metacontent) {
		this.metacontent = metacontent;
	}

	public String getLinkurl() {
		return this.linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public String getCtime() {
		return this.ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getEtime() {
		return this.etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getCusername() {
		return this.cusername;
	}

	public void setCusername(String cusername) {
		this.cusername = cusername;
	}

	public String getCuser() {
		return this.cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public String getEusername() {
		return this.eusername;
	}

	public void setEusername(String eusername) {
		this.eusername = eusername;
	}

	public String getEuser() {
		return this.euser;
	}

	public void setEuser(String euser) {
		this.euser = euser;
	}

	public String getPcontent() {
		return this.pcontent;
	}

	public void setPcontent(String pcontent) {
		this.pcontent = pcontent;
	}

	public String getPstate() {
		return this.pstate;
	}

	public void setPstate(String pstate) {
		this.pstate = pstate;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getTreecode() {
		return treecode;
	}

	public void setTreecode(String treecode) {
		this.treecode = treecode;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getReadpop() {
		return readpop;
	}

	public void setReadpop(String readpop) {
		this.readpop = readpop;
	}

	public String getWritepop() {
		return writepop;
	}

	public void setWritepop(String writepop) {
		this.writepop = writepop;
	}

	public String getAuditpop() {
		return auditpop;
	}

	public void setAuditpop(String auditpop) {
		this.auditpop = auditpop;
	}
	
}