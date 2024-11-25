<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<c:if test="${TYPEDOCS != null }">
<!-- 分类下的知识列表，小的列表页面(缩写) -->
<div class="row doc_column_box">
	<div class="col-sm-12">
		<span class="glyphicon glyphicon-tree-deciduous column_title">同类知识</span>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<hr class="hr_split" />
		<div id="newTypeBoxId">
			<ol>
				<c:forEach items="${TYPEDOCS}" var="node">
					<li>
					    <a href="webdoc/view/Pub${node.docid}.html">${node.title}</a>
					</li>
				</c:forEach>
			</ol>
		</div>
	</div>
</div>
</c:if>