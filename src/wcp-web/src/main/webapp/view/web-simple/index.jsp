<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="com.farm.web.constant.FarmConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%><%@ taglib
	prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页- <PF:ParameterValue key="config.sys.title" /></title>
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="index,follow">
<jsp:include page="atext/include-web.jsp"></jsp:include>
</head>
<body style="background-color: #fafbfc;">
	<jsp:include page="commons/head.jsp"></jsp:include>
	<jsp:include page="commons/superContent.jsp"></jsp:include>
	<div class="wcp-space-h50"></div>
	<div class="wcp-zebra-1"> 
		<!-- 检索框 -->
		<div id="wcp-wide-search">
			<div class="container">
				<jsp:include page="/view/web-simple/help-style/includeMiniSearchForm.jsp"></jsp:include>
			</div>
		</div>
		<div class="container" style="padding: 0px;">
			<div class="row wcp-margin-top8">
				<div class="col-lg-12">
					<!-- 推荐阅读 -->
					<jsp:include
						page="/view/web-simple/help-style/includeTopKnows.jsp"></jsp:include>
				</div>
			</div> 
		</div>
	</div>
	<div class="wcp-zebra-2">
		<div class="container" style="padding: 0px;">
			<div class="row wcp-margin-top8">
				<c:if test="${!empty docbriefs}">
					<div class="col-lg-12" style="margin-bottom: 80px;">
						<div>
							<jsp:include page="/view/web-simple/help-style/includeNewDocs.jsp"></jsp:include>
						</div>
					</div>
				</c:if>
				<div class="col-lg-12" style="margin-bottom: 80px;">
					<div>
						<jsp:include
							page="/view/web-simple/help-style/includePubType.jsp"></jsp:include>
					</div>
				</div> 
			</div>
		</div>
	</div>
	<jsp:include page="commons/footServer.jsp"></jsp:include>
	<jsp:include page="commons/foot.jsp"></jsp:include>
</body>
</html>