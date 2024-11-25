<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
<style>
.wcp-newdoc-title a {
	font-size: 14px;
	color: #373d41;
}

.wcp-newdoc-title a span {
	color: #777777;
}

.wcp-newdoc-title a:hover {
	color: #ba2636;
	text-decoration: none;
}

.wcp-newdoc-title .wcp-tag-zhuan {
	color: #d9534f;
	font-weight: 500;
}

.wcp-newdoc-title .wcp-tag-mi {
	color: #f0ad4e;
	font-weight: 500;
}
</style>
<c:if test="${!empty docbriefs}">
	<div>
		<div
			style="font-weight: 600; font-size: 20px; letter-spacing: -0.35px; color: #D9534F;">
			<i class=" glyphicon glyphicon-unchecked"></i> 最新知识
		</div>
	</div>
	<div class="row"
		style="background-color: #ffffff; border: 1px solid #eee; border-radius: 5px; padding: 20px; padding-top: 4px; margin: 0px; margin-top: 20px;">
		<c:forEach items="${docbriefs}" var="node">
			<div class="col-sm-3 wcp-newdoc-title" style="margin-top: 20px;">
				<div style="font-size: 14px;white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
					<a target="${config_sys_link_newwindow_target}" title="<PF:FormatTime date="${node.etime}"
								yyyyMMddHHmmss="yyyy-MM-dd HH:mm" />${node.title}"
						href="webdoc/view/Pub${node.docid}.html"> <jsp:include
							page="includeKnowIcon.jsp">
							<jsp:param value="${node.domtype}" name="domtype" />
						</jsp:include> <c:if test="${node.essence=='1' }">
							<span class="wcp-tag-zhuan" title="精华">精</span>
						</c:if> <c:if test="${node.docpopis=='1'||node.docpopis=='3'}">
							<span class="wcp-tag-mi" title="知识自定义权限">密</span>
						</c:if> ${node.title}
					</a>
				</div>
			</div>
		</c:forEach>
	</div>
</c:if>