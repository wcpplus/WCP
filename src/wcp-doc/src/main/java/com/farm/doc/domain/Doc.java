package com.farm.doc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.farm.core.time.TimeTool;
/* *
 *功能：文档类
 *详细：
 *
 *版本：v2.1
 *作者：FarmCode代码工程
 *日期：20150707114057
 *说明：
 */
@Entity(name = "Doc")
@Table(name = "farm_doc")
public class Doc implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        @Id
        @GenericGenerator(name = "systemUUID", strategy = "uuid")
        @GeneratedValue(generator = "systemUUID")
        @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
        private String id;
        @Column(name = "DOCGROUPID", length = 32)
        private String docgroupid;
        @Column(name = "RUNINFOID", length = 32, nullable = false)
        private String runinfoid;
        @Column(name = "READPOP", length = 2, nullable = false)
        private String readpop;
        @Column(name = "WRITEPOP", length = 2, nullable = false)
        private String writepop;
        @Column(name = "TEXTID", length = 32, nullable = false)
        private String textid;
        @Column(name = "PCONTENT", length = 128)
        private String pcontent;
        @Column(name = "CUSER", length = 32, nullable = false)
        private String cuser;
        @Column(name = "EUSERNAME", length = 64, nullable = false)
        private String eusername;
        @Column(name = "EUSER", length = 32, nullable = false)
        private String euser;
        @Column(name = "ETIME", length = 16, nullable = false)
        private String etime;
        @Column(name = "CUSERNAME", length = 64, nullable = false)
        private String cusername;
        @Column(name = "CTIME", length = 16, nullable = false)
        private String ctime;
        @Column(name = "STATE", length = 2, nullable = false)
        private String state;
        @Column(name = "IMGID", length = 32)
        private String imgid;
        @Column(name = "TOPLEVE", length = 10)
        private Integer topleve;
        @Column(name = "SOURCE", length = 256)
        private String source;
        @Column(name = "TAGKEY", length = 1024)
        private String tagkey;
        @Column(name = "SHORTTITLE", length = 64)
        private String shorttitle;
        @Column(name = "DOMTYPE", length = 2, nullable = false)
        private String domtype;
        @Column(name = "PUBTIME", length = 14, nullable = false)
        private String pubtime;
        @Column(name = "TITLE", length = 256, nullable = false)
        private String title;
        @Column(name = "AUTHOR", length = 64)
        private String author;
        @Column(name = "DOCDESCRIBE", length = 256)
        private String docdescribe;
        //--------------------------------------------

        public String  getDocgroupid() {
          return this.docgroupid;
        }
        public void setDocgroupid(String docgroupid) {
          this.docgroupid = docgroupid;
        }
        public String  getRuninfoid() {
          return this.runinfoid;
        }
        public void setRuninfoid(String runinfoid) {
          this.runinfoid = runinfoid;
        }
        public String  getReadpop() {
          return this.readpop;
        }
        public void setReadpop(String readpop) {
          this.readpop = readpop;
        }
        public String  getWritepop() {
          return this.writepop;
        }
        public void setWritepop(String writepop) {
          this.writepop = writepop;
        }
        public String  getTextid() {
          return this.textid;
        }
        public void setTextid(String textid) {
          this.textid = textid;
        }
        public String  getPcontent() {
          return this.pcontent;
        }
        public void setPcontent(String pcontent) {
          this.pcontent = pcontent;
        }
        public String  getCuser() {
          return this.cuser;
        }
        public void setCuser(String cuser) {
          this.cuser = cuser;
        }
        public String  getEusername() {
          return this.eusername;
        }
        public void setEusername(String eusername) {
          this.eusername = eusername;
        }
        public String  getEuser() {
          return this.euser;
        }
        public void setEuser(String euser) {
          this.euser = euser;
        }
        public String  getEtime() {
          return this.etime;
        }
        public void setEtime(String etime) {
          this.etime = etime;
        }
        public String  getCusername() {
          return this.cusername;
        }
        public void setCusername(String cusername) {
          this.cusername = cusername;
        }
        public String  getCtime() {
          return this.ctime;
        }
        public void setCtime(String ctime) {
          this.ctime = ctime;
        }
        public String  getId() {
          return this.id;
        }
        public void setId(String id) {
          this.id = id;
        }
        public String  getState() {
          return this.state;
        }
        public void setState(String state) {
          this.state = state;
        }
        public String  getImgid() {
          return this.imgid;
        }
        public void setImgid(String imgid) {
          this.imgid = imgid;
        }
        public Integer  getTopleve() {
          return this.topleve;
        }
        public void setTopleve(Integer topleve) {
          this.topleve = topleve;
        }
        public String  getSource() {
          return this.source;
        }
        public void setSource(String source) {
          this.source = source;
        }
        public String  getTagkey() {
          return this.tagkey;
        }
        public void setTagkey(String tagkey) {
          this.tagkey = tagkey;
        }
        public String  getShorttitle() {
          return this.shorttitle;
        }
        public void setShorttitle(String shorttitle) {
          this.shorttitle = shorttitle;
        }
        public String  getDomtype() {
          return this.domtype;
        }
        public void setDomtype(String domtype) {
          this.domtype = domtype;
        }
        public String  getPubtime() {
        	if(pubtime == null || pubtime.isEmpty()){
        		pubtime = TimeTool.getTimeDate14();
        	}
          return this.pubtime;
        }
        public void setPubtime(String pubtime) {
          this.pubtime = pubtime;
        }
        public String  getTitle() {
          return this.title;
        }
        public void setTitle(String title) {
          this.title = title;
        }
        public String  getAuthor() {
          return this.author;
        }
        public void setAuthor(String author) {
          this.author = author;
        }
        public String  getDocdescribe() {
          return this.docdescribe;
        }
        public void setDocdescribe(String docdescribe) {
          this.docdescribe = docdescribe;
        }
}