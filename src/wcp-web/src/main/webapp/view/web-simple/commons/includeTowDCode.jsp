<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="java.net.InetAddress"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div class="row">
	<div class="col-sm-12  visible-lg visible-md"
		style="text-align: center;">
		<a href="<PF:basePath/>"><img title="扫描二维码访问本站" alt="扫描二维码访问本站"
			style="text-align: center; max-width: 110px;"
			src="<PF:basePath/>home/PubQRCode.do" /></a>
	</div>
</div>