package com.farm.doc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * FarmDoctext entity. @author MyEclipse Persistence Tools
 */

@Entity(name = "FarmDoctext")
@Table(name = "farm_doctext")
public class FarmDoctext implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;
    @Column(name = "DOCID", length = 32)
    private String docid;
    @Column(name = "PSTATE", length = 2, nullable = false)
    private String pstate;
    @Column(name = "PCONTENT", length = 128)
    private String pcontent;
    @Column(name = "EUSER", length = 32, nullable = false)
    private String euser;
    @Column(name = "EUSERNAME", length = 64, nullable = false)
    private String eusername;
    @Column(name = "CUSER", length = 32, nullable = false)
    private String cuser;
    @Column(name = "CTIME", length = 16, nullable = false)
    private String ctime;
    @Column(name = "ETIME", length = 16, nullable = false)
    private String etime;
    @Column(name = "CUSERNAME", length = 64, nullable = false)
    private String cusername;
    @Column(name = "TEXT1", length = 2147483647, nullable = false)
    private String text1;
    @Column(name = "TEXT2", length = 2147483647)
    private String text2;
    @Column(name = "TEXT3", length = 2147483647)
    private String text3;

	// Constructors

	/** default constructor */
	public FarmDoctext() {
	}

	/** minimal constructor */
	public FarmDoctext(String text1, String ctime, String etime,
			String cusername, String cuser, String eusername, String euser,
			String pstate) {
		this.text1 = text1;
		this.ctime = ctime;
		this.etime = etime;
		this.cusername = cusername;
		this.cuser = cuser;
		this.eusername = eusername;
		this.euser = euser;
		this.pstate = pstate;
	}

	/** full constructor */
	public FarmDoctext(String text1, String text2, String text3, String ctime,
			String etime, String cusername, String cuser, String eusername,
			String euser, String pstate, String pcontent) {
		this.text1 = text1;
		this.text2 = text2;
		this.text3 = text3;
		this.ctime = ctime;
		this.etime = etime;
		this.cusername = cusername;
		this.cuser = cuser;
		this.eusername = eusername;
		this.euser = euser;
		this.pstate = pstate;
		this.pcontent = pcontent;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText1() {
		return this.text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return this.text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	public String getText3() {
		return this.text3;
	}

	public void setText3(String text3) {
		this.text3 = text3;
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

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}
	
}