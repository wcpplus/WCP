<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="com.farm.web.constant.FarmConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
						style="margin: auto; width: 300px; margin-top: 100px; margin-bottom: 30px; background-color: #fcfcfc;">
						<div class="panel-body">
							<div class="text-center">
								<img class="img-thumbnail" src="text/img/logo.png" alt="wcp"
									style="margin: 20px;" />
							</div>
							<div class="alert alert-info" role="alert">${MESSAGE}</div>
							<c:forEach items="${LINKS}" var="entry">
								<a href="${entry.value}">${entry.key}</a>
								<br />
							</c:forEach>
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
