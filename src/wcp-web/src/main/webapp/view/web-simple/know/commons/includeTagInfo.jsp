<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<c:if test="${DOCE.tags != null }">
<div class="row doc_column_box" >
	<div class="col-sm-12">
		<span class="glyphicon glyphicon-tags   column_title">标签</span>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<hr class="hr_split" />
		<c:forEach items="${DOCE.tags}" var="node">
			<span class="label label-info tagsearch" style="cursor: pointer;"
				title="${node}">${node}</span>
		</c:forEach>
	</div>
</div>
</c:if>
