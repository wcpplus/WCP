<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>${group.groupname}小组成员管理-<PF:ParameterValue
		key="config.sys.title" />
</title>
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="noindex,nofllow">
<jsp:include page="../atext/include-web.jsp"></jsp:include>
<link rel="stylesheet"
	href="<PF:basePath/>text/lib/kindeditor/themes/default/default.css" />
<script charset="utf-8"
	src="<PF:basePath/>text/lib/kindeditor/kindeditor-all-min.js"></script>
<script charset="utf-8" src="<PF:basePath/>text/lib/kindeditor/zh_CN.js"></script>
<script charset="utf-8"
	src="<PF:basePath/>text/lib/super-validate/validate.js"></script>
</head>
<body>
	<jsp:include page="../commons/head.jsp"></jsp:include>
	<jsp:include page="../commons/superContent.jsp"></jsp:include>
	<div class="containerbox">
		<div class="container ">
			<div class="row column_box">
				<div class="col-md-2 "></div>
				<div class="col-md-8">
					<div class="row">
						<div class="col-md-12">
							<ol class="breadcrumb">
								<li><a href="webgroup/PubHome.html">小组</a></li>
								<li><a href="webgroup/Pubshow.do?groupid=${group.id}">${group.groupname}</a></li>
								<li class="active">加入申请</li>
							</ol>
						</div>
					</div>
					<jsp:include page="commons/includeApplyMng.jsp"></jsp:include>
					<div class="row">
						<div class="col-md-12">
							<ol class="breadcrumb">
								<li class="active">成员</li>
							</ol>
						</div>
					</div>
					<jsp:include page="commons/includeUserMng.jsp"></jsp:include>
				</div>
				<div class="col-md-2"></div>
			</div>
		</div>
	</div>
	<!-- /.modal -->
	<jsp:include page="../commons/foot.jsp"></jsp:include>
</body>
</html>