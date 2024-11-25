<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<div class="pull-right" style="float: right;">
	<button type="button" id="addGroupButtonId" class="btn btn-info btn-sm">创建小组</button>
</div>
<script type="text/javascript">
	$(function() {
		$('#addGroupButtonId').bind('click', function() {
			window.location = basePath + 'webgroup/add.do';
		});
	});
</script>