package com.farm.doc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * FarmRfDoctextfile entity. @author MyEclipse Persistence Tools
 */

@Entity(name = "FarmRfDoctextfile")
@Table(name = "farm_rf_doctextfile")
public class FarmRfDoctextfile implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
    private String id;
    @Column(name = "FILEID", length = 32, nullable = false)
    private String fileid;
    @Column(name = "DOCID", length = 32, nullable = false)
    private String docid;

	// Constructors

	/** default constructor */
	public FarmRfDoctextfile() {
	}

	/** full constructor */
	public FarmRfDoctextfile(String docid, String fileid) {
		this.docid = docid;
		this.fileid = fileid;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getFileid() {
		return this.fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

}