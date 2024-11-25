package com.farm.doc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/* *
 *功能：推荐服务类
 *详细：
 *
 *版本：v2.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@Entity(name = "Weburl")
@Table(name = "farm_weburl")
public class Weburl implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
	private String id;
	@Column(name = "WEBNAME", length = 64, nullable = false)
	private String webname;
	@Column(name = "URL", length = 512, nullable = false)
	private String url;
	@Column(name = "CTIME", length = 16, nullable = false)
	private String ctime;
	@Column(name = "ETIME", length = 16, nullable = false)
	private String etime;
	@Column(name = "CUSERNAME", length = 64, nullable = false)
	private String cusername;
	@Column(name = "CUSER", length = 32, nullable = false)
	private String cuser;
	@Column(name = "EUSERNAME", length = 64, nullable = false)
	private String eusername;
	@Column(name = "EUSER", length = 32, nullable = false)
	private String euser;
	@Column(name = "PCONTENT", length = 128)
	private String pcontent;
	@Column(name = "PSTATE", length = 2, nullable = false)
	private String pstate;
	@Column(name = "SORT", nullable = false)
	private int sort;
	@Column(name = "FILEID")
	private String fileid;

	public String getWebname() {
		return this.webname;
	}

	public void setWebname(String webname) {
		this.webname = webname;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return this.id;
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
}