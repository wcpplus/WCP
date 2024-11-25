package com.farm.doc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * FarmDocmessage entity. @author MyEclipse Persistence Tools
 */

@Entity(name = "FarmDocmessage")
@Table(name = "farm_docmessage")
public class FarmDocmessage implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;
    @Column(name = "READSTATE", length = 2, nullable = false)
    private String readstate;
    @Column(name = "APPID", length = 32)
    private String appid;
    @Column(name = "TITLE", length = 64, nullable = false)
    private String title;
    @Column(name = "CONTENT", length = 256, nullable = false)
    private String content;
    @Column(name = "READUSERID", length = 32, nullable = false)
    private String readuserid;
    @Column(name = "PCONTENT", length = 128)
    private String pcontent;
    @Column(name = "PSTATE", length = 2, nullable = false)
    private String pstate;
    @Column(name = "CUSERNAME", length = 64, nullable = false)
    private String cusername;
    @Column(name = "CUSER", length = 32, nullable = false)
    private String cuser;
    @Column(name = "CTIME", length = 16, nullable = false)
    private String ctime;
    @Column(name = "PRAISNUM")
    private int praisnum;
    @Column(name = "CRITCISMNUM")
    private int critcismnum;
    @Column(name = "PARENTID", length = 32)
    private String parentid;


	/** default constructor */
	public FarmDocmessage() {
	}

	/** minimal constructor */
	public FarmDocmessage(String ctime, String cusername, String pstate,
			String readuserid, String content, String title, String readstate) {
		this.ctime = ctime;
		this.cusername = cusername;
		this.pstate = pstate;
		this.readuserid = readuserid;
		this.content = content;
		this.title = title;
		this.readstate = readstate;
	}

	/** full constructor */
	public FarmDocmessage(String ctime, String cusername, String pstate,
			String pcontent, String readuserid, String content, String title,
			String appid, String readstate) {
		this.ctime = ctime;
		this.cusername = cusername;
		this.pstate = pstate;
		this.pcontent = pcontent;
		this.readuserid = readuserid;
		this.content = content;
		this.title = title;
		this.appid = appid;
		this.readstate = readstate;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCtime() {
		return this.ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getCusername() {
		return this.cusername;
	}

	public void setCusername(String cusername) {
		this.cusername = cusername;
	}

	public String getPstate() {
		return this.pstate;
	}

	public void setPstate(String pstate) {
		this.pstate = pstate;
	}

	public String getPcontent() {
		return this.pcontent;
	}

	public void setPcontent(String pcontent) {
		this.pcontent = pcontent;
	}

	public String getReaduserid() {
		return this.readuserid;
	}

	public void setReaduserid(String readuserid) {
		this.readuserid = readuserid;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAppid() {
		return this.appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getReadstate() {
		return this.readstate;
	}

	public void setReadstate(String readstate) {
		this.readstate = readstate;
	}

	public String getCuser() {
		return cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public int getPraisnum() {
		return praisnum;
	}

	public void setPraisnum(int praisnum) {
		this.praisnum = praisnum;
	}

	public int getCritcismnum() {
		return critcismnum;
	}

	public void setCritcismnum(int critcismnum) {
		this.critcismnum = critcismnum;
	}


}