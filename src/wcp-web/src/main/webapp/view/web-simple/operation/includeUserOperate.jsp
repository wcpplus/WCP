<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="btn-group" role="group" aria-label="...">
	<c:if test="${type=='know'}">
		<a href="webuser/PubHome.do?type=know&userid=${userid}"
			class="btn btn-info">发布知识</a>
	</c:if>
	<c:if test="${type!='know'}">
		<a href="webuser/PubHome.do?type=know&userid=${userid}"
			class="btn btn-default">发布知识</a>
	</c:if>
	<c:if test="${type=='file'}">
		<a href="webuser/PubHome.do?type=file&userid=${userid}"
			class="btn btn-info">发布资源</a>
	</c:if>
	<c:if test="${type!='file'}">
		<a href="webuser/PubHome.do?type=file&userid=${userid}"
			class="btn btn-default">发布资源</a>
	</c:if>
	<c:if test="${type=='joy'}">
		<c:if test="${self}">
			<a href="webuser/PubHome.do?type=joy&userid=${userid}"
				class="btn btn-info">我的关注</a>
		</c:if>
	</c:if>
	<c:if test="${type!='joy'}">
		<c:if test="${self}">
			<a href="webuser/PubHome.do?type=joy&userid=${userid}"
				class="btn btn-default">我的关注</a>
		</c:if>
	</c:if>
	<c:if test="${type=='group'}">
		<a href="webuser/PubHome.do?type=group&userid=${userid}"
			class="btn btn-info">小组</a>
	</c:if>
	<c:if test="${type!='group'}">
		<a href="webuser/PubHome.do?type=group&userid=${userid}"
			class="btn btn-default">小组</a>
	</c:if>
	<c:if test="${type=='usermessage'}">
		<c:if test="${self}">
			<a href="webuser/PubHome.do?type=usermessage&userid=${userid}"
				class="btn btn-info">用户消息</a>
		</c:if>
	</c:if>
	<c:if test="${type!='usermessage'}">
		<c:if test="${self}">
			<a href="webuser/PubHome.do?type=usermessage&userid=${userid}"
				class="btn btn-default">用户消息</a>
		</c:if>
	</c:if>
</div>
<div class="btn-group" role="group" aria-label="...">
	<c:if test="${type=='audit'}">
		<c:if test="${self}">
			<a href="webuser/PubHome.do?type=audit&userid=${userid}"
				class="btn btn-info">审核中</a>
		</c:if>
	</c:if>
	<c:if test="${type!='audit'}">
		<c:if test="${self}">
			<a href="webuser/PubHome.do?type=audit&userid=${userid}"
				class="btn btn-default">审核中</a>
		</c:if>
	</c:if>
	<c:if test="${type=='audith'}">
		<c:if test="${self}">
			<a href="webuser/PubHome.do?type=audith&userid=${userid}"
				class="btn btn-info">审核历史</a>
		</c:if>
	</c:if>
	<c:if test="${type!='audith'}">
		<c:if test="${self}">
			<a href="webuser/PubHome.do?type=audith&userid=${userid}"
				class="btn btn-default">审核历史</a>
		</c:if>
	</c:if>
	<c:if test="${type=='audito'}">
		<c:if test="${self}">
			<a href="webuser/PubHome.do?type=audito&userid=${userid}"
				class="btn btn-info">审核任务</a>
		</c:if>
	</c:if>
	<c:if test="${type!='audito'}">
		<c:if test="${self}">
			<a href="webuser/PubHome.do?type=audito&userid=${userid}"
				class="btn btn-default">审核任务</a>
		</c:if>
	</c:if>
</div>


<c:if test="${self}">
	<div class="btn-group pull-right" role="group" aria-label="...">
		<button type="button" class="btn btn-primary" onclick="toUserEdit()">修改信息</button>
		<button type="button" class="btn btn-primary"
			onclick="toUserPwdEdit()">修改密码</button>
		<PF:IfParameterEquals key="config.sys.opensource" val="false">
			<a href="resume/view.do?userid=${userid}" class="btn btn-primary">个人档案</a>
		</PF:IfParameterEquals>
	</div>
	<script type="text/javascript">
		function toUserEdit() {
			window.location.href = basePath + "webuser/edit.do";
		}
		function toUserPwdEdit() {
			window.location.href = basePath + "webuser/editCurrentUserPwd.do";
		}
	</script>
</c:if>
