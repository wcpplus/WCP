<%@page import="java.net.URLEncoder"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="row">
	<div class="col-sm-12 ">
		<form action="websearch/PubDo.do" id="websearchpubdoId" method="post">
			<div class="input-group">
				<input type="text" class="form-control" name="word" value="${word}"
					placeholder="请输入关键字..."> <span class="input-group-btn">
					<button class="btn btn-info" type="submit">检索</button> <!-- 专业版功能 -->
					<PF:IfParameterEquals key="config.sys.opensource" val="false">
						<a id="searchPlusButtonId" style="margin-left: 16px;">高级检索</a>
					</PF:IfParameterEquals>
					<script type="text/javascript">
						$('#searchPlusButtonId').bind(
								'click',
								function() {
									$('#websearchpubdoId').attr('action',
											"websearchs/PubPlus.do");
									$('#websearchpubdoId').submit();
								});
					</script> <!-- 专业版功能 -->
				</span>
			</div>
		</form>
	</div>
</div>
<div class="row">
	<div class="col-sm-12 ">
		<c:forEach items="${hotCase}" var="node">
			<a href="websearch/PubDo.do?word=<PF:urlEncode val="${node}"/>">
				<span class="label label-danger" style="cursor: pointer;">${fn:length(node) <= 10 ? node : fn:substring(node,0,10) }</span>
			</a>
		</c:forEach>
	</div>
</div>
