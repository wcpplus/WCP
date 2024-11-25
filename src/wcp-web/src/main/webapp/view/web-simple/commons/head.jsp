<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="com.farm.web.constant.FarmConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation"
	style="margin: 0px;">
	<!-- Brand and toggle get grouped for better mobile display -->
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target="#bs-example-navbar-collapse-1">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand"
			style="color: #ffffff; font-weight: bold; padding: 5px;"
			href="<DOC:defaultIndexPage/>"> <img
			src="<PF:basePath/>/text/img/logo.png" height="40" alt="WCP"
			title="WCP" align="middle" />
		</a>
	</div>
	<!-- Collect the nav links, forms, and other content for toggling -->
	<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
		<ul class="nav navbar-nav">
			<PF:IfParameterEquals key="config.sys.opensource" val="false">
				<li class="active"><a href="home/Pubindex.html"><span
						class="glyphicon glyphicon-home"></span>&nbsp;资讯</a></li>
			</PF:IfParameterEquals>
			<li class="active"><a href="webtype/view/Pub1.html"><span
					class="glyphicon glyphicon-th"></span>&nbsp;分类</a></li>
			<li class="active"><a href="webgroup/PubHome.html"><span
					class="glyphicon glyphicon-tree-conifer"></span>&nbsp;小组</a></li>
			 <li class="active"><a href="webstat/PubHome.html"><span
					class="glyphicon glyphicon-stats"></span>&nbsp;荣誉</a></li>
			<li class="active"><a href="websearch/PubHome.html"><span
					class="glyphicon glyphicon-search"></span>&nbsp;检索</a></li>
			<PF:IfParameterEquals key="config.imbar.type" val="llm">		
				<li class="active"><a href="aiweb/PubAiChat.html"> <img alt="" src="text/img/logos/wcpai1.png"
								style="width: 14px; height: 14px; margin: 6px;  margin-top: 0px;">&nbsp;智能问答</a></li>
			</PF:IfParameterEquals>				
			<PF:IfParameterEquals key="config.imbar.type" val="search">		
				<li class="active"><a href="aiweb/PubAiChat.html"> <img alt="" src="text/img/logos/wcpai1.png"
								style="width: 14px; height: 14px; margin: 6px;  margin-top: 0px;">&nbsp;智能问答</a></li>
			</PF:IfParameterEquals>						
			<PF:IfParameterEquals key="config.about" val="true">
				<li class="active"><a href="home/PubAbout.html"><span
						class="glyphicon glyphicon-phone-alt"></span>&nbsp;联系方式</a></li>
			</PF:IfParameterEquals>
		</ul>
		<form action="websearch/PubDo.do" method="post"
			id="lucenesearchFormId"
			class="navbar-form navbar-left hidden-xs hidden-sm" role="search">
			<div class="form-group">
				<input type="text" name="word" id="wordId" value="${word}"
					class="form-control input-sm" placeholder="查询公开资源"> <input
					type="hidden" id="pageNumId" name="pagenum">
			</div>
			<button type="submit" class="btn btn-default btn-sm">检索</button>
			<jsp:include page="../operation/includeCreatOperate.jsp"></jsp:include>
		</form>
		<ul class="nav navbar-nav navbar-right" style="margin-right: 10px;">
			<c:if test="${USEROBJ==null}">
				<li class="active"><a href="login/webPage.html"><span
						class="glyphicon glyphicon glyphicon-user"></span>&nbsp;登录</a></li>
			</c:if>
			<c:if test="${USEROBJ!=null}">
				<li class="active"><a href="webuser/PubHome.do"><span
						class="glyphicon glyphicon-user"></span>&nbsp;${USEROBJ.name}</a></li>
				<li class="active"><a href="login/webout.html"><span
						class="glyphicon glyphicon-off"></span>&nbsp;注销</a></li>
			</c:if>
		</ul>
	</div>
	<!-- /.navbar-collapse -->
</div>
<script type="text/javascript">
	function luceneSearch(key) {
		$('#wordId').val(key);
		$('#lucenesearchFormId').submit();
	}
	function luceneSearchGo(page) {
		$('#pageNumId').val(page);
		$('#lucenesearchFormId').submit();
	}
//-->
</script>