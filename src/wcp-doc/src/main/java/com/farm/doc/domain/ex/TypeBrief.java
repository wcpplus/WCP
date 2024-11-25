package com.farm.doc.domain.ex;

public class TypeBrief {
	// NAME,ID,PARENTID,NUM
	private String name;
	private String id;
	private String parentid;
	private int num;
	private String type;
	private String readpop;
	private String writepop;
	private String auditpop;

	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReadpop() {
		return readpop;
	}

	public void setReadpop(String readpop) {
		this.readpop = readpop;
	}

	public String getWritepop() {
		return writepop;
	}

	public void setWritepop(String writepop) {
		this.writepop = writepop;
	}

	public String getAuditpop() {
		return auditpop;
	}

	public void setAuditpop(String auditpop) {
		this.auditpop = auditpop;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
