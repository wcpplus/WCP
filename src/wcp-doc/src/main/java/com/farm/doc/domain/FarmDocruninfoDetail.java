package com.farm.doc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * FarmDocruninfoDetail entity. @author MyEclipse Persistence Tools
 */

@Entity(name = "FarmDocruninfoDetail")
@Table(name = "farm_docruninfo_detail")
public class FarmDocruninfoDetail implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;
    @Column(name = "USERIP", length = 128, nullable = false)
    private String userip;
    @Column(name = "RUNINFOID", length = 32, nullable = false)
    private String runinfoid;
    @Column(name = "DOCTEXTID", length = 32, nullable = false)
    private String doctextid;
    @Column(name = "PSTATE", length = 2, nullable = false)
    private String pstate;
    @Column(name = "PCONTENT", length = 128)
    private String pcontent;
    @Column(name = "VTYPE", length = 2, nullable = false)
    private String vtype;
    @Column(name = "CUSERNAME", length = 64)
    private String cusername;
    @Column(name = "CUSER", length = 32)
    private String cuser;
    @Column(name = "CTIME", length = 16, nullable = false)
    private String ctime;

	// Constructors

	/** default constructor */
	public FarmDocruninfoDetail() {
	}

	/** minimal constructor */
	public FarmDocruninfoDetail(String ctime, String pstate, String vtype,
			String doctextid, String runinfoid, String userip) {
		this.ctime = ctime;
		this.pstate = pstate;
		this.vtype = vtype;
		this.doctextid = doctextid;
		this.runinfoid = runinfoid;
		this.userip = userip;
	}

	/** full constructor */
	public FarmDocruninfoDetail(String ctime, String cusername, String cuser,
			String pstate, String pcontent, String vtype, String doctextid,
			String runinfoid, String userip) {
		this.ctime = ctime;
		this.cusername = cusername;
		this.cuser = cuser;
		this.pstate = pstate;
		this.pcontent = pcontent;
		this.vtype = vtype;
		this.doctextid = doctextid;
		this.runinfoid = runinfoid;
		this.userip = userip;
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

	public String getVtype() {
		return this.vtype;
	}

	public void setVtype(String vtype) {
		this.vtype = vtype;
	}

	public String getDoctextid() {
		return this.doctextid;
	}

	public void setDoctextid(String doctextid) {
		this.doctextid = doctextid;
	}

	public String getRuninfoid() {
		return this.runinfoid;
	}

	public void setRuninfoid(String runinfoid) {
		this.runinfoid = runinfoid;
	}

	public String getUserip() {
		return this.userip;
	}

	public void setUserip(String userip) {
		this.userip = userip;
	}

}