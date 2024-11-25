<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div id="orgBoxId">
	<script type="text/javascript">
	$(function() {
		$("#orgBoxId").load("home/PubFPloadOrgs.do");
	});
</script>
</div>
