package com.farm.doc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * FarmDocenjoy entity. @author MyEclipse Persistence Tools
 */

@Entity(name = "FarmDocenjoy")
@Table(name = "farm_docenjoy")
public class FarmDocenjoy implements java.io.Serializable {
	 private static final long serialVersionUID = 1L;

     @Id
     @GenericGenerator(name = "systemUUID", strategy = "uuid")
     @GeneratedValue(generator = "systemUUID")
     @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
     private String id;
     @Column(name = "USERID", length = 32, nullable = false)
     private String userid;
     @Column(name = "DOCID", length = 32, nullable = false)
     private String docid;

	// Constructors

	/** default constructor */
	public FarmDocenjoy() {
	}

	/** full constructor */
	public FarmDocenjoy(String docid, String userid) {
		this.docid = docid;
		this.userid = userid;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDocid() {
		return this.docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}