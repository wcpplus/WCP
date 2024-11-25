<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!-- Single button -->
<c:if test="${USEROBJ!=null}">
	<c:if test="${usergroup==null}">
		<a class="btn btn-info btn-sm"
			href="webgroup/join.do?groupId=${docgroup.id}">加入小组</a>
	</c:if>
	<c:if test="${usergroup.leadis=='1'}">
		<a class="btn btn-info btn-sm"
			href="webgroup/homeedit.html?groupid=${docgroup.id}"><span
			class="glyphicon glyphicon-home"></span>&nbsp;编辑首页</a>
		<a class="btn btn-info btn-sm"
			href="webgroup/edit.html?groupid=${docgroup.id}"><span
			class="glyphicon glyphicon-cog"></span>&nbsp;修改信息</a>
		<a class="btn btn-info btn-sm"
			href="webgroup/userMag.html?groupid=${docgroup.id}"><span
			class="glyphicon glyphicon-user"></span>&nbsp;成员管理</a>
	</c:if>
	<c:if test="${usergroup!=null}">
		<a class="btn btn-info btn-sm"
			href="javascript:leaveGroupFunc('${docgroup.id}')"><span
			class="glyphicon glyphicon-remove"></span>&nbsp;退出小组</a>
	</c:if>
</c:if>
