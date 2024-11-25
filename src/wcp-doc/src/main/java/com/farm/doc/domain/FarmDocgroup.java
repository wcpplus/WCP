package com.farm.doc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * FarmDocgroup entity. @author MyEclipse Persistence Tools
 */

@Entity(name = "FarmDocgroup")
@Table(name = "farm_docgroup")
public class FarmDocgroup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;
    @Column(name = "JOINCHECK", length = 2, nullable = false)
    private String joincheck;
    @Column(name = "HOMEDOCID", length = 32)
    private String homedocid;
    @Column(name = "USERNUM", length = 10, nullable = false)
    private Integer usernum;
    @Column(name = "GROUPIMG", length = 32, nullable = false)
    private String groupimg;
    @Column(name = "GROUPTAG", length = 256, nullable = false)
    private String grouptag;
    @Column(name = "GROUPNOTE", length = 256, nullable = false)
    private String groupnote;
    @Column(name = "GROUPNAME", length = 128, nullable = false)
    private String groupname;
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
	public FarmDocgroup() {
	}

	/** minimal constructor */
	public FarmDocgroup(String ctime, String etime, String cusername,
			String cuser, String eusername, String euser, String pstate,
			String groupname, String groupnote, String grouptag,
			String groupimg, String joincheck) {
		this.ctime = ctime;
		this.etime = etime;
		this.cusername = cusername;
		this.cuser = cuser;
		this.eusername = eusername;
		this.euser = euser;
		this.pstate = pstate;
		this.groupname = groupname;
		this.groupnote = groupnote;
		this.grouptag = grouptag;
		this.groupimg = groupimg;
		this.joincheck = joincheck;
	}

	/** full constructor */
	public FarmDocgroup(String ctime, String etime, String cusername,
			String cuser, String eusername, String euser, String pstate,
			String pcontent, String groupname, String groupnote,
			String grouptag, String groupimg, String joincheck) {
		this.ctime = ctime;
		this.etime = etime;
		this.cusername = cusername;
		this.cuser = cuser;
		this.eusername = eusername;
		this.euser = euser;
		this.pstate = pstate;
		this.pcontent = pcontent;
		this.groupname = groupname;
		this.groupnote = groupnote;
		this.grouptag = grouptag;
		this.groupimg = groupimg;
		this.joincheck = joincheck;
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

	public String getGroupname() {
		return this.groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getGroupnote() {
		return this.groupnote;
	}

	public void setGroupnote(String groupnote) {
		this.groupnote = groupnote;
	}

	public String getGrouptag() {
		return this.grouptag;
	}

	public void setGrouptag(String grouptag) {
		this.grouptag = grouptag;
	}

	public String getGroupimg() {
		return this.groupimg;
	}

	public void setGroupimg(String groupimg) {
		this.groupimg = groupimg;
	}

	public String getJoincheck() {
		return this.joincheck;
	}

	public void setJoincheck(String joincheck) {
		this.joincheck = joincheck;
	}


	public Integer getUsernum() {
		return usernum;
	}

	public void setUsernum(Integer usernum) {
		this.usernum = usernum;
	}

	public String getHomedocid() {
		return homedocid;
	}

	public void setHomedocid(String homedocid) {
		this.homedocid = homedocid;
	}

}