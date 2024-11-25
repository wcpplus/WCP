<%@page import="com.farm.doc.domain.ex.TypeBrief"%>
<%@page import="com.farm.util.spring.BeanFactory"%>
<%@page import="com.farm.doc.server.FarmDocTypeInter"%>
<%@page import="com.farm.doc.domain.FarmDoctype"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib uri="/view/conf/farmdoc.tld" prefix="DOC"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title><PF:ParameterValue key="config.sys.title" /></title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="index,follow">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
	<a href="<DOC:defaultIndexPage/>">进入主站</a>
	<%
		FarmDocTypeInter aloneIMP = (FarmDocTypeInter) BeanFactory.getBean("farmDocTypeManagerImpl");
		List<TypeBrief> types = aloneIMP.getPubTypes();
		for (TypeBrief type : types) {
	%><br />
	<a href="webtype/view<%=type.getId()%>/Pub1.html"><%=type.getName()%></a>
	<%
		}
	%>
	Loading...
	<br>
</body>
<script type="text/javascript">
	window.location = '<DOC:defaultIndexPage/>';
</script>
</html>