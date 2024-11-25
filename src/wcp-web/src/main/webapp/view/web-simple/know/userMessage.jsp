<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>评论-${doc.doc.title}<PF:ParameterValue
		key="config.sys.title" /></title>
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
			<div class="row">
				<div class="col-md-9">
					<div class="row">
						<div class="col-md-12">
							<h2>消息主题：${usermessage.title }</h2>
							<hr />
							<div class="row">
								<div class="col-md-12">
									<div class="form-group">
										<div class="panel panel-default">
											<div class="panel-body">${usermessage.content }</div>
										</div>
										 <br /> <a class="btn btn-primary"
											href="webuser/PubHome.do?type=usermessage&num=${num }&userid=${USEROBJ.id}">返回消息列表</a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../commons/foot.jsp"></jsp:include>
</body>
<script type="text/javascript">
	
</script>
</html>