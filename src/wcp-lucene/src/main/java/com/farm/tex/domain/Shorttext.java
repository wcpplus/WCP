package com.farm.tex.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/* *
 *功能：知识段落文本类
 *详细：
 *
 *版本：v2.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@Entity(name = "ShortText")
@Table(name = "farm_short_text")
public class Shorttext implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
	private String id;
	@Column(name = "PALL", length = 10, nullable = false)
	private Integer pall;
	@Column(name = "PNO", length = 10, nullable = false)
	private Integer pno;
	@Column(name = "LEN", length = 10, nullable = false)
	private Integer len;
	@Column(name = "DESCRIBES", length = 256)
	private String describes;
	@Column(name = "TEXT", length = 2048)
	private String text;
	@Column(name = "SID", length = 32, nullable = false)
	private String sid;
	@Column(name = "TYPEID", length = 32)
	private String typeid;
	@Column(name = "PCONTENT", length = 128)
	private String pcontent;
	@Column(name = "PSTATE", length = 2, nullable = false)
	private String pstate;
	@Column(name = "CUSER", length = 32)
	private String cuser;
	@Column(name = "CTIME", length = 16, nullable = false)
	private String ctime;
	@Column(name = "EMBEDDING")
	private byte[] embedding;
	@Column(name = "EMBLEN", length = 10)
	private Integer emblen;
	@Column(name = "EMBTIME", length = 16)
	private String embtime;
	@Column(name = "EMBTMODEL", length = 16)
	private String embtmodel;
	@Column(name = "TITLE", length = 256)
	private String title;
	@Column(name = "STYPE", length = 32, nullable = false)
	private String stype;

	/**
	 * 用于外部排序
	 */
	@Transient
	private int sort;

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getTitle() {
		return title;
	}

	public Integer getEmblen() {
		return emblen;
	}

	public void setEmblen(Integer emblen) {
		this.emblen = emblen;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public byte[] getEmbedding() {
		return embedding;
	}

	public void setEmbedding(byte[] embedding) {
		this.embedding = embedding;
	}

	public String getEmbtime() {
		return embtime;
	}

	public void setEmbtime(String embtime) {
		this.embtime = embtime;
	}

	public String getEmbtmodel() {
		return embtmodel;
	}

	public void setEmbtmodel(String embtmodel) {
		this.embtmodel = embtmodel;
	}

	public Integer getPall() {
		return this.pall;
	}

	public void setPall(Integer pall) {
		this.pall = pall;
	}

	public Integer getPno() {
		return this.pno;
	}

	public void setPno(Integer pno) {
		this.pno = pno;
	}

	public String getTypeid() {
		return typeid;
	}

	public Integer getLen() {
		return len;
	}

	public void setLen(Integer len) {
		this.len = len;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getDescribes() {
		return this.describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
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

	public String getCuser() {
		return this.cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public String getCtime() {
		return this.ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

}