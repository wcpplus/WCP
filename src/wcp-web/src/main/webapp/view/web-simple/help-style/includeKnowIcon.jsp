<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<c:if test="${param.domtype=='6'}">
	<span class="glyphicon glyphicon-globe"></span>
</c:if>
<c:if test="${param.domtype=='1'}">
	<span class="glyphicon glyphicon-book"></span>
</c:if>
<c:if test="${param.domtype=='5'}">
	<span class="glyphicon glyphicon-folder-close"></span>
</c:if>