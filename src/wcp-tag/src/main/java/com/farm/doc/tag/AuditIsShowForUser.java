package com.farm.doc.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.farm.core.auth.domain.LoginUser;
import com.farm.doc.server.FarmDocManagerInter;
import com.farm.doc.server.FarmDocOperateRightInter;
import com.farm.util.spring.BeanFactory;
import com.farm.web.constant.FarmConstant;

/**
 * 用户是否有该文档的审核权限
 * 
 * @author wangdong
 *
 */
public class AuditIsShowForUser extends TagSupport {
	private String docId;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static FarmDocOperateRightInter aloneIMP = (FarmDocOperateRightInter) BeanFactory
			.getBean("farmDocOperateRightImpl");
	private final static FarmDocManagerInter docIMP = (FarmDocManagerInter) BeanFactory.getBean("farmDocManagerImpl");

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) super.pageContext.getRequest();
		LoginUser user = (LoginUser) request.getSession().getAttribute(FarmConstant.SESSION_USEROBJ);
		// EVAL_BODY_INCLUDE
		// 则执行自定义标签的标签体；
		// 返回SKIP_BODY则忽略自定义标签的标签体，直接解释执行自定义标签的结果标记。
		if (aloneIMP.isAudit(user, docIMP.getDoc(docId).getDoc())) {
			return EVAL_BODY_INCLUDE;
		}
		return SKIP_BODY;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

}
