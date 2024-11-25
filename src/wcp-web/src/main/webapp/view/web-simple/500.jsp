<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="com.farm.web.constant.FarmConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>首页-<PF:ParameterValue key="config.sys.title" /></title>
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="noindex,nofllow">
<jsp:include page="atext/include-web.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="commons/head.jsp"></jsp:include><div
		class="super_content">
		<br />
	</div>
	<div class="containerbox" style="background-color: #fff;">
		<div class="container ">
			<div class="row">
				<div class="col-sm-12">
					<div class="panel panel-default userbox"
						style="margin: auto; width: 300px; margin-top: 100px; margin-bottom: 100px; background-color: #fcfcfc;">
						<div class="panel-body">
							<div class="text-center">
								<img class="img-thumbnail" src="text/img/logo.png" alt="wcp"
									style="margin: 20px;" />
							</div>
							<div class="alert alert-danger" role="alert">
								错误码：
								<%=request.getAttribute("javax.servlet.error.status_code")%>
								<br> 信息：
								<%=request.getAttribute("javax.servlet.error.message")%>
								<br> 异常：
								<%=request.getAttribute("javax.servlet.error.exception_type")%>
								<br> <a
									href="<DOC:defaultIndexPage/>">返回首页</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="commons/footServer.jsp"></jsp:include>
	<jsp:include page="commons/foot.jsp"></jsp:include>
</body>
</html>
