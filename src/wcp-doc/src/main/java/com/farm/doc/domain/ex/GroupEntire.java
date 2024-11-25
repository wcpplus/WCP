package com.farm.doc.domain.ex;

import java.util.List;

import com.farm.authority.domain.User;

public class GroupEntire {
	private String id;
	private String joincheck;
	private String homedocid;
	private Integer usernum;
	private String groupimg;
	private String grouptag;
	private String groupnote;
	private String groupname;
	private String pcontent;
	private String pstate;
	private String euser;
	private String eusername;
	private String cuser;
	private String cusername;
	private String etime;
	private String ctime;
	private String imgurl;
	private List<String> tags;
	private List<User> users;
	private List<User> admins;
	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJoincheck() {
		return joincheck;
	}

	public void setJoincheck(String joincheck) {
		this.joincheck = joincheck;
	}

	public String getHomedocid() {
		return homedocid;
	}

	public void setHomedocid(String homedocid) {
		this.homedocid = homedocid;
	}

	public Integer getUsernum() {
		return usernum;
	}

	public void setUsernum(Integer usernum) {
		this.usernum = usernum;
	}

	public String getGroupimg() {
		return groupimg;
	}

	public void setGroupimg(String groupimg) {
		this.groupimg = groupimg;
	}

	public String getGrouptag() {
		return grouptag;
	}

	public void setGrouptag(String grouptag) {
		this.grouptag = grouptag;
	}

	public String getGroupnote() {
		return groupnote;
	}

	public void setGroupnote(String groupnote) {
		this.groupnote = groupnote;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getPcontent() {
		return pcontent;
	}

	public void setPcontent(String pcontent) {
		this.pcontent = pcontent;
	}

	public String getPstate() {
		return pstate;
	}

	public void setPstate(String pstate) {
		this.pstate = pstate;
	}

	public String getEuser() {
		return euser;
	}

	public void setEuser(String euser) {
		this.euser = euser;
	}

	public String getEusername() {
		return eusername;
	}

	public void setEusername(String eusername) {
		this.eusername = eusername;
	}

	public String getCuser() {
		return cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public String getCusername() {
		return cusername;
	}

	public void setCusername(String cusername) {
		this.cusername = cusername;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getAdmins() {
		return admins;
	}

	public void setAdmins(List<User> admins) {
		this.admins = admins;
	}
	
}
