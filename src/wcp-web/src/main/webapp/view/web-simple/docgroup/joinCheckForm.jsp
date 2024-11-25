<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>小组首页-<PF:ParameterValue key="config.sys.title" />
</title>
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="noindex,nofllow">
<jsp:include page="../atext/include-web.jsp"></jsp:include><script
	charset="utf-8" src="<PF:basePath/>text/lib/super-validate/validate.js"></script>
</head>
<body>
	<jsp:include page="../commons/head.jsp"></jsp:include>
	<jsp:include page="../commons/superContent.jsp"></jsp:include>
	<div class="containerbox">
		<div class="container ">
			<div class="row column_box">
				<div class="col-md-3 "></div>
				<div class="col-md-6">
					<div class="row">
						<div class="col-md-12">
							<ol class="breadcrumb">
								<li><a href="webgroup/PubHome.html">小组</a></li>
								<li><a href="webgroup/Pubshow.do?groupid=${group.id}">${group.groupname}</a></li>
								<li class="active">审请加入小组</li>
							</ol>
						</div>
					</div>
					<form role="form" action="webgroup/joincommit.do"
						id="knowSubmitFormId" method="post">
						<input type="hidden" name="groupId" id="group_id"
							value="${group.id}" />
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label for="exampleInputEmail1"> 申请说明 <span
										class="alertMsgClass">*</span>
									</label>
									<textarea name="message" id="usergroup_applynote"
										class="form-control" rows="3">${usergroup.applynote}</textarea>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-2">
								<button type="button" id="knowSubmitButtonId"
									class="btn btn-primary">提交申请</button>
							</div>
							<div class="col-md-10">
								<span class="alertMsgClass" id="errormessageShowboxId"></span>
							</div>
						</div>
					</form>
					<script>
						$(function() {
							$('#knowSubmitButtonId')
									.bind(
											'click',
											function() {
												if (!validate('knowSubmitFormId')) {
													$('#errormessageShowboxId')
															.text('信息录入有误，请检查！');
												} else {
													if (confirm("是否提交?")) {
														$('#knowSubmitFormId')
																.submit();
														$('#knowSubmitButtonId')
																.hide();
														$('#knowSubmitButtonId')
																.before(
																		"<h2><span class='label label-info glyphicon glyphicon-link'>提交中...</span></h2>");
													}
												}
											});
							// 小组名称
							validateInput('usergroup_applynote', function(id,
									val, obj) {
								if (valid_isNull(val)) {
									return {
										valid : false,
										msg : '不能为空'
									};
								}
								if (valid_maxLength(val, 64)) {
									return {
										valid : false,
										msg : '长度不能大于' + 64
									};
								}
								return {
									valid : true,
									msg : '正确'
								};
							});
						});
					</script>
				</div>
				<div class="col-md-3  "></div>
			</div>
			<div class="row column_box">
				<div class="col-lg-12"></div>
			</div>
		</div>
	</div>
	<!-- /.modal -->
	<jsp:include page="../commons/footServer.jsp"></jsp:include>
	<jsp:include page="../commons/foot.jsp"></jsp:include>
</body>
</html>