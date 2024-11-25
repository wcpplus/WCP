package com.farm.doc.domain.ex;

public class DocBrief implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7431017567548414571L;
	private String docid;
	private String title;
	private String text;
	private String domtype;
	private String etime;
	private int visitnum;
	private int praiseyes;
	private int hotnum;
	private int praiseno;
	private int evaluate;
	private int answeringnum;
	private String typename;
	private String typeid;
	private String pubtime;
	private String imgid;
	private String imgurl;
	private String author;
	private String authorid;
	private String photoid;
	private String photourl;
	private String userid;
	private String username;
	private String essence;
	private String docpopis;
	
	public String getDocpopis() {
		return docpopis;
	}

	public void setDocpopis(String docpopis) {
		this.docpopis = docpopis;
	}

	public String getEssence() {
		return essence;
	}

	public void setEssence(String essence) {
		this.essence = essence;
	}

	public String getDomtype() {
		return domtype;
	}
		
	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getPhotourl() {
		return photourl;
	}

	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}

	public String getPhotoid() {
		return photoid;
	}

	public void setPhotoid(String photoid) {
		this.photoid = photoid;
	}

	public int getHotnum() {
		return hotnum;
	}

	public void setHotnum(int hotnum) {
		this.hotnum = hotnum;
	}

	public void setDomtype(String domtype) {
		this.domtype = domtype;
	}

	public String getDocid() {
		return docid;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getVisitnum() {
		return visitnum;
	}

	public void setVisitnum(int visitnum) {
		this.visitnum = visitnum;
	}

	public int getPraiseyes() {
		return praiseyes;
	}

	public void setPraiseyes(int praiseyes) {
		this.praiseyes = praiseyes;
	}

	public int getPraiseno() {
		return praiseno;
	}

	public void setPraiseno(int praiseno) {
		this.praiseno = praiseno;
	}

	public int getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(int evaluate) {
		this.evaluate = evaluate;
	}

	public int getAnsweringnum() {
		return answeringnum;
	}

	public void setAnsweringnum(int answeringnum) {
		this.answeringnum = answeringnum;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getPubtime() {
		return pubtime;
	}

	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}

	public String getImgid() {
		return imgid;
	}

	public void setImgid(String imgid) {
		this.imgid = imgid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
