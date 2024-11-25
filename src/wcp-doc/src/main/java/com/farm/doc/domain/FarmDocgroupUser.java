package com.farm.doc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * FarmDocgroupUser entity. @author MyEclipse Persistence Tools
 */

@Entity(name = "FarmDocgroupUser")
@Table(name = "farm_docgroup_user")
public class FarmDocgroupUser implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
	private String id;
	@Column(name = "APPLYNOTE", length = 128)
	private String applynote;
	@Column(name = "SHOWHOME", length = 2, nullable = false)
	private String showhome;
	@Column(name = "SHOWSORT", length = 10, nullable = false)
	private Integer showsort;
	@Column(name = "EDITIS", length = 2, nullable = false)
	private String editis;
	@Column(name = "LEADIS", length = 2, nullable = false)
	private String leadis;
	@Column(name = "USERID", length = 32, nullable = false)
	private String userid;
	@Column(name = "GROUPID", length = 32, nullable = false)
	private String groupid;
	@Column(name = "PCONTENT", length = 128)
	private String pcontent;
	@Column(name = "PSTATE", length = 2, nullable = false)
	private String pstate;
	@Column(name = "EUSER", length = 32, nullable = false)
	private String euser;
	@Column(name = "EUSERNAME", length = 64, nullable = false)
	private String eusername;
	@Column(name = "CUSER", length = 32, nullable = false)
	private String cuser;
	@Column(name = "CUSERNAME", length = 64, nullable = false)
	private String cusername;
	@Column(name = "ETIME", length = 16, nullable = false)
	private String etime;
	@Column(name = "CTIME", length = 16, nullable = false)
	private String ctime;

	// Constructors

	/** default constructor */
	public FarmDocgroupUser() {
	}

	/** minimal constructor */
	public FarmDocgroupUser(String ctime, String etime, String cusername,
			String cuser, String eusername, String euser, String pstate,
			String groupid, String userid, String leadis, String editis,
			String showhome, Integer showsort) {
		this.ctime = ctime;
		this.etime = etime;
		this.cusername = cusername;
		this.cuser = cuser;
		this.eusername = eusername;
		this.euser = euser;
		this.pstate = pstate;
		this.groupid = groupid;
		this.userid = userid;
		this.leadis = leadis;
		this.editis = editis;
		this.showhome = showhome;
		this.showsort = showsort;
	}

	/** full constructor */
	public FarmDocgroupUser(String ctime, String etime, String cusername,
			String cuser, String eusername, String euser, String pstate,
			String pcontent, String groupid, String userid, String leadis,
			String editis, String showhome, Integer showsort) {
		this.ctime = ctime;
		this.etime = etime;
		this.cusername = cusername;
		this.cuser = cuser;
		this.eusername = eusername;
		this.euser = euser;
		this.pstate = pstate;
		this.pcontent = pcontent;
		this.groupid = groupid;
		this.userid = userid;
		this.leadis = leadis;
		this.editis = editis;
		this.showhome = showhome;
		this.showsort = showsort;
	}

	// Property accessors

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

	public String getApplynote() {
		return applynote;
	}

	public void setApplynote(String applynote) {
		this.applynote = applynote;
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

	public String getGroupid() {
		return this.groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getLeadis() {
		return this.leadis;
	}

	public void setLeadis(String leadis) {
		this.leadis = leadis;
	}

	public String getEditis() {
		return this.editis;
	}

	public void setEditis(String editis) {
		this.editis = editis;
	}

	public String getShowhome() {
		return this.showhome;
	}

	public void setShowhome(String showhome) {
		this.showhome = showhome;
	}

	public Integer getShowsort() {
		return this.showsort;
	}

	public void setShowsort(Integer showsort) {
		this.showsort = showsort;
	}

}