<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%><%@taglib
	uri="http://java.sun.com/jsp/jstl/functions" prefix="fun"%>
<table class="table">
	<!-- 等待审核的文章，审核操作页面 -->
	<thead>
		<tr>
			<th>标题</th>
			<th>创建日期</th>
			<th>分类</th>
			<th>版本说明</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="node" items="${docs.resultList}">
			<tr>
				<th scope="row"><a href="audit/auditdoc.do?auditid=${node.AUDITID}">${node.TITLE}</a></th>
				<td>${node.PUBTIME}</td>
				<td>${node.TYPENAME}</td>
				<td>${node.CONTENT}</td>
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
						<li><a
							href="webuser/PubHome.do?type=${type}&num=${page}&userid=${userid}">${page}</a></li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:if>