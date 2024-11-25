<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="row doc_column_box">
	<div class="col-sm-12">
		<span class="glyphicon glyphicon-user   column_title">访问权限</span>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<hr class="hr_split" />
		<table class="table table-bordered table-hover"
			style="font-size: 12px;">
			<tr>
				<td class="active" style="width: 100px;"><b>创建人</b></td>
				<td><a href="webuser/PubHome.do?userid=${DOCE.user.id}">${DOCE.user.name}</a></td>
			</tr>
			<c:if test="${DOCE.group.groupname!=null}">
				<tr>
					<td class="active"><b>工作小组</b></td>
					<td><a href="webgroup/Pubshow.do?groupid=${DOCE.group.id}">${DOCE.group.groupname}</a></td>
				</tr>
			</c:if>
			<tr>
				<td class="active"><b>文档编辑权限</b></td>
				<td><c:if test="${DOCE.doc.writepop==3}">
			禁止
			</c:if> <c:if test="${DOCE.doc.writepop==1}">
			来自分类
			</c:if> <c:if test="${DOCE.doc.writepop==0}">
			创建者私有
			</c:if> <c:if test="${DOCE.doc.writepop==2}">
			工作小组
			</c:if></td>
			</tr>
			<tr>
				<td class="active"><b>文档阅读权限</b></td>
				<td><c:if test="${DOCE.doc.readpop==3}">
			禁止
			</c:if> <c:if test="${DOCE.doc.readpop==1}">
			来自分类
			</c:if> <c:if test="${DOCE.doc.readpop==0}">
			创建者私有
			</c:if> <c:if test="${DOCE.doc.readpop==2}">
			工作小组
			</c:if></td>
			</tr>
			<tr>
				<td class="active"><b>分类阅读权限</b></td>
				<td><c:if test="${fn:length(DOCE.typeReadPops)<=0}">所有人</c:if>
					<c:forEach items="${DOCE.typeReadPops}" var="node">
						${node.oname}<c:if test="${node.poptype=='1'}">(人员)</c:if>
						<c:if test="${node.poptype=='2'}">(机构)</c:if>&nbsp;&nbsp;&nbsp;&nbsp;
					</c:forEach></td>
			</tr>
			<tr>
				<td class="active"><b>分类编辑权限</b></td>
				<td><c:if test="${fn:length(DOCE.typeWritePops)<=0}">所有人</c:if>
					<c:forEach items="${DOCE.typeWritePops}" var="node">
						${node.oname}<c:if test="${node.poptype=='1'}">(人员)</c:if>
						<c:if test="${node.poptype=='2'}">(机构)</c:if>&nbsp;&nbsp;&nbsp;&nbsp;
					</c:forEach></td>
			</tr>
			<tr>
				<td class="active"><b>分类审核权限</b></td>
				<td><c:if test="${fn:length(DOCE.typeAuditPops)<=0}">无</c:if> <c:forEach
						items="${DOCE.typeAuditPops}" var="node">
						${node.oname}<c:if test="${node.poptype=='1'}">(人员)</c:if>
						<c:if test="${node.poptype=='2'}">(机构)</c:if>&nbsp;&nbsp;&nbsp;&nbsp;
					</c:forEach></td>
			</tr>
		</table>
	</div>
</div>