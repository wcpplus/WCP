package com.farm.doc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * FarmDocruninfo entity. @author MyEclipse Persistence Tools
 */

@Entity(name = "FarmDocruninfo")
@Table(name = "farm_docruninfo")
public class FarmDocruninfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

    @Column(name = "ANSWERINGNUM", length = 10, nullable = false)
    private Integer answeringnum;
    @Column(name = "PRAISEYES", length = 10, nullable = false)
    private Integer praiseyes;
    @Column(name = "PRAISENO", length = 10, nullable = false)
    private Integer praiseno;
    @Column(name = "EVALUATE", length = 10, nullable = false)
    private Integer evaluate;
    @Column(name = "LASTVTIME", length = 14, nullable = false)
    private String lastvtime;
    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;
    @Column(name = "HOTNUM", length = 10, nullable = false)
    private Integer hotnum;
    @Column(name = "VISITNUM", length = 10, nullable = false)
    private Integer visitnum;

	// Constructors

	/** default constructor */
	public FarmDocruninfo() {
	}

	/** full constructor */
	public FarmDocruninfo(Integer visitnum, Integer hotnum, String lastvtime,
			Integer praiseyes, Integer praiseno, Integer evaluate) {
		this.visitnum = visitnum;
		this.hotnum = hotnum;
		this.lastvtime = lastvtime;
		this.praiseyes = praiseyes;
		this.praiseno = praiseno;
		this.evaluate = evaluate;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getVisitnum() {
		return this.visitnum;
	}

	public void setVisitnum(Integer visitnum) {
		this.visitnum = visitnum;
	}

	public Integer getHotnum() {
		return this.hotnum;
	}

	public void setHotnum(Integer hotnum) {
		this.hotnum = hotnum;
	}

	public String getLastvtime() {
		return this.lastvtime;
	}

	public void setLastvtime(String lastvtime) {
		this.lastvtime = lastvtime;
	}

	public Integer getPraiseyes() {
		return this.praiseyes;
	}

	public void setPraiseyes(Integer praiseyes) {
		this.praiseyes = praiseyes;
	}

	public Integer getPraiseno() {
		return this.praiseno;
	}

	public void setPraiseno(Integer praiseno) {
		this.praiseno = praiseno;
	}

	public Integer getEvaluate() {
		return this.evaluate;
	}

	public void setEvaluate(Integer evaluate) {
		this.evaluate = evaluate;
	}

	public Integer getAnsweringnum() {
		return answeringnum;
	}

	public void setAnsweringnum(Integer answeringnum) {
		this.answeringnum = answeringnum;
	}
	
}