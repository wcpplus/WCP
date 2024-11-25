package com.farm.doc.server.plus.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
@Entity(name = "DocAudit")
@Table(name = "farm_doc_audit")
public class DocAudit implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        @Id
        @GenericGenerator(name = "systemUUID", strategy = "uuid")
        @GeneratedValue(generator = "systemUUID")
        @Column(name = "ID", length = 32, insertable = true, updatable = true, nullable = false)
        private String id;
        @Column(name = "DOCID", length = 32, nullable = false)
        private String docid;
        @Column(name = "TEXTID", length = 32, nullable = false)
        private String textid;
        @Column(name = "PCONTENT", length = 128)
        private String pcontent;
        @Column(name = "PSTATE", length = 2, nullable = false)
        private String pstate;
        @Column(name = "CUSER", length = 32, nullable = false)
        private String cuser;
        @Column(name = "CUSERNAME", length = 64, nullable = false)
        private String cusername;
        @Column(name = "ETIME", length = 16, nullable = false)
        private String etime;
        @Column(name = "CTIME", length = 16, nullable = false)
        private String ctime;

        public String  getDocid() {
          return this.docid;
        }
        public void setDocid(String docid) {
          this.docid = docid;
        }
        public String  getTextid() {
          return this.textid;
        }
        public void setTextid(String textid) {
          this.textid = textid;
        }
        public String  getId() {
          return this.id;
        }
        public void setId(String id) {
          this.id = id;
        }
        public String  getPcontent() {
          return this.pcontent;
        }
        public void setPcontent(String pcontent) {
          this.pcontent = pcontent;
        }
        public String  getPstate() {
          return this.pstate;
        }
        public void setPstate(String pstate) {
          this.pstate = pstate;
        }
        public String  getCuser() {
          return this.cuser;
        }
        public void setCuser(String cuser) {
          this.cuser = cuser;
        }
        public String  getCusername() {
          return this.cusername;
        }
        public void setCusername(String cusername) {
          this.cusername = cusername;
        }
        public String  getEtime() {
          return this.etime;
        }
        public void setEtime(String etime) {
          this.etime = etime;
        }
        public String  getCtime() {
          return this.ctime;
        }
        public void setCtime(String ctime) {
          this.ctime = ctime;
        }
}