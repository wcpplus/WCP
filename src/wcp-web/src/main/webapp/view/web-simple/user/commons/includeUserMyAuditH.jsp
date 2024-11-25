<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%><%@taglib
	uri="http://java.sun.com/jsp/jstl/functions" prefix="fun"%>
<table class="table">
	<thead>
		<tr>
			<th>标题</th>
			<th>审核时间</th>
			<th>分类</th>
			<th>状态</th>
			<th>版本备注</th>
			<th>审核备注</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="node" items="${docs.resultList}">
			<tr>
				<th scope="row"><a href="audit/tempdoc.do?auditid=${node.AUDITID}">${node.TITLE}</a></th>
				<td><PF:FormatTime date="${node.ETIME}" yyyyMMddHHmmss="yyyy/MM/dd HH:mm" /> </td>
				<td>${node.TYPENAME}</td>
				<td>${node.STATE}</td>
				<td>${node.CONTENT}</td>
				<td>${node.AUDITCONTENT}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<c:if test="${fun:length(docs.resultList) > 0}">
	<div class="row">
		<div class="col-xs-12">
			<ul class="pagination pagination-sm">
				<c:forEach var="page" begin="${docs.startPage}"
					end="${docs.endPage}" step="1">
					<c:if test="${page==docs.currentPage}">
						<li class="active"><a>${page} </a></li>
					</c:if>
					<c:if test="${page!=docs.currentPage}">
						<li><a href="webuser/PubHome.do?type=${type}&num=${page}&userid=${userid}">${page}</a></li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:if>