<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>高级检索-<PF:ParameterValue key="config.sys.title" /></title>
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="index,follow">
<jsp:include page="../atext/include-web.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="../commons/head.jsp"></jsp:include>
	<jsp:include page="../commons/superContent.jsp"></jsp:include>
	<div class="containerbox">
		<div class="container ">
			<div style="margin-top: 20px;"></div>
			<div class="row column_box">
				<div class="col-lg-3"></div>
				<div class="col-lg-6">
					<div class="row">
						<div class="col-sm-12 column_box  hidden-xs">
							<img class=" center-block" src="text/img/logo.png" alt="wcp" />
						</div>
						<div class="col-sm-12 column_box">
							<div class="row">
								<div class="col-sm-12 ">
									<form action="websearchs/PubDo.do" id="websearchsPubDoFormId"
										method="post">
										<div class="input-group">
											<input type="hidden" name="indexTypes" id="indexTypesId"><input
												id="indexGroupsId" type="hidden" name="indexGroups">
											<input type="text" class="form-control" name="word"
												value="${word}" placeholder="请输入关键字..."> <span
												class="input-group-btn">
												<button class="btn btn-info" id="websearchsPubDoId"
													type="button">检索</button>
											</span>
										</div>
									</form>
									<script type="text/javascript">
										$(function() {
											$('#websearchsPubDoId,#websearchsPubDoFootId').bind('click',
												function() {
													var types,gourps;
													//拼接知识分类
													$("#groupidsbox :checked").each(function(i,obj){
														if(gourps){
															gourps=gourps+","+$(obj).attr('id');
														}else{
															gourps=$(obj).attr('id');
														}
													});
													//拼接知识小组
													$("#typeidsbox :checked").each(function(i,obj){
														if(types){
															types=types+","+$(obj).attr('id');
														}else{
															types=$(obj).attr('id');
														}
													});
													$('#indexTypesId').val(types);
													$('#indexGroupsId').val(gourps);
													$('#websearchsPubDoFormId').submit();
												});
										});
									</script>
								</div>
							</div>
						</div>
					</div>
					<div style="margin-top: 20px;"></div>
				</div>
				<div class="col-lg-3"></div>
			</div>
			<!-- /.row -->
			<div class="row">
				<div class="col-lg-3"></div>
				<div class="col-sm-6 " style="margin: auto;">
					<c:if test="${!empty groups}">
						<div class="row column_box">
							<div class="col-sm-12">
								<span class="column_title">选择小组，在小组下查询</span>
							</div>
						</div>
						<div class="panel panel-default  side_column">
							<div class="panel-body" id="groupidsbox">
								<c:forEach var="node" items="${groups}">
									<div class="checkbox">
										<label> <input type="checkbox" id="${node.ID}">${node.GROUPNAME}
										</label>
									</div>
								</c:forEach>
							</div>
						</div>
					</c:if>
					<div class="row column_box">
						<div class="col-sm-12">
							<span class="column_title">选择分类，在分类下查询</span>
						</div>
					</div>
					<div class="panel panel-default  side_column">
						<div class="panel-body" id="typeidsbox">
							<ul class="doctypeUl">
								<c:forEach var="node" items="${typesons}">
									<c:if test="${node.parentid=='NONE'}">
										<li>
											<h5 class="showLableType">
												<input type="checkbox" id="${node.id}"
													style="margin-right: 8px;">${node.name}
												<c:if test="${node.num>0}">
													<span style="color: #D9534F; font-weight: bold;">${node.num}</span>
												</c:if>
											</h5>
											<ul>
												<c:forEach var="node1" items="${typesons}">
													<c:if test="${node1.parentid==node.id}">
														<li>
															<h5 class="showLableType">
																<input type="checkbox" id="${node1.id}"
																	style="margin-right: 8px;">${node1.name}
																<c:if test="${node1.num>0}">
																	<span style="color: #D9534F; font-weight: bold;">${node1.num}</span>
																</c:if>
															</h5>
															<ul>
																<c:forEach var="node2" items="${typesons}">
																	<c:if test="${node2.parentid==node1.id}">
																		<li>
																			<h5 class="showLableType">
																				<input type="checkbox" id="${node2.id}"
																					style="margin-right: 8px;">${node2.name}
																				<c:if test="${node2.num>0}">
																					<span style="color: #D9534F; font-weight: bold;">${node2.num}</span>
																				</c:if>
																			</h5>
																		</li>
																	</c:if>
																</c:forEach>
															</ul>
														</li>
													</c:if>
												</c:forEach>
											</ul>
										</li>
									</c:if>
								</c:forEach>
							</ul>
						</div>
					</div>
					<div class="row column_box">
						<div class="col-sm-12">
							<span class="column_title"><button class="btn btn-info"
									id="websearchsPubDoFootId" type="button">检索</button></span>
						</div>
					</div>
					<hr />
				</div>
				<div class="col-lg-3"></div>
			</div>
		</div>
	</div>
	<jsp:include page="../commons/footServer.jsp"></jsp:include>
	<jsp:include page="../commons/foot.jsp"></jsp:include>
</body>
</html>