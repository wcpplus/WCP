package com.farm.doc.server.plus;

import java.util.List;

import com.farm.core.auth.domain.LoginUser;
import com.farm.doc.server.plus.domain.DocAudit;
import com.farm.doc.server.plus.domain.Doctypepop;

public interface FarmTypePopServerInter {
	public DocAudit auditHandleForDocCreate(String docid, String textid, LoginUser user);

	public String getMessageForNeedAudit(String title);

	public String getMessageTitleForNeedAudit(String title);

	public DocAudit auditHandleForDocEdit(String docid, String textid, LoginUser user);

	public void delAuditInfo(String docid);

	public void getTypePopsHandle(List<Doctypepop> poplist, String typereadpop, String typewritepop, String auditpop,
			String typeid);

	public void delTypePop(String typeId);
}
