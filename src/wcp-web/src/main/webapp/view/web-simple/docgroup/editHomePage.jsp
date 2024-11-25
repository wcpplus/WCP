<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/view/conf/farmtag.tld" prefix="PF"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<PF:basePath/>" />
<title>编辑小组首页-<PF:ParameterValue key="config.sys.title" />
</title>
<meta name="description"
	content='<PF:ParameterValue key="config.sys.mate.description"/>'>
<meta name="keywords"
	content='<PF:ParameterValue key="config.sys.mate.keywords"/>'>
<meta name="author"
	content='<PF:ParameterValue key="config.sys.mate.author"/>'>
<meta name="robots" content="noindex,nofllow">
<jsp:include page="../atext/include-web.jsp"></jsp:include>
<link rel="stylesheet"
	href="<PF:basePath/>text/lib/kindeditor/themes/default/default.css" />
<script charset="utf-8"
	src="<PF:basePath/>text/lib/kindeditor/kindeditor-all-min.js"></script>
<script charset="utf-8" src="<PF:basePath/>text/lib/kindeditor/zh_CN.js"></script>
<script charset="utf-8"
	src="<PF:basePath/>text/lib/super-validate/validate.js"></script>
</head>
<body>
	<jsp:include page="../commons/head.jsp"></jsp:include><jsp:include
		page="../commons/superContent.jsp"></jsp:include>
	<div class="containerbox">
		<div class="container ">
			<div class="row  column_box">
				<div class="col-md-2  visible-lg visible-md"></div>
				<div class="col-md-8">
					<div class="row">
						<div class="col-md-12">
							<ol class="breadcrumb">
								<li><a href="webgroup/PubHome.html">小组</a></li>
								<li><a href="webgroup/Pubshow.do?groupid=${group.id}">${group.groupname}</a></li>
								<li class="active">编辑小组首页</li>
							</ol>
						</div>
					</div>
					<form role="form" action="webgroup/homeeditCommit.do"
						id="knowSubmitFormId" method="post">
						<input type="hidden" name="groupId" value="${group.id}" /> <input
							type="hidden" name="docid" value="${doc.doc.id}" />
						<div class="row">
							<div class="col-md-12">
								<textarea name="text" id="contentId"
									style="height: 500px; width: 100%;">${doc.texts.text1}</textarea>
							</div>
						</div>
						<!-- 修改 -->
						<c:if test="${doc!=null&&doc.doc.id!=null}">
							<div class="row">
								<div class="col-md-12">
									<div class="form-group">
										<label for="exampleInputEmail1"> 修改原因 <span
											class="alertMsgClass">*</span>
										</label> <input type="text" class="form-control" id="knowEditNoteId"
											name="editNote" placeholder="修改原因将被记录在版本备注中" />
									</div>
								</div>
							</div>
						</c:if>
						<div class="row">
							<div class="col-md-12">
								<button type="button" id="knowSubmitButtonId"
									class="btn btn-primary">提交</button>
								<button type="button" id="knowCancelButtonId"
									class="btn btn-default">取消</button>
							</div>
						</div>
					</form>
				</div>
				<div class="col-md-2  visible-lg visible-md"></div>
			</div>
		</div>
	</div>
	<!-- /.modal -->
	<jsp:include page="../commons/foot.jsp"></jsp:include>
</body>
<script type="text/javascript">
	var editor = null;

	$(function() {
		editor = KindEditor.create('textarea[id="contentId"]', {
			resizeType : 1,
			cssPath : '<PF:basePath/>text/lib/kindeditor/editInner.css',
			uploadJson : basePath + 'admin/FPupload.do',
			allowPreviewEmoticons : false,
			allowImageUpload : true,
			items : [ 'source', 'fullscreen', '|', 'fontsize', 'forecolor',
					'bold', 'italic', 'underline', 'removeformat', '|',
					'justifyleft', 'justifycenter', 'justifyright',
					'insertorderedlist', 'insertunorderedlist', 'lineheight',
					'|', 'formatblock', 'quickformat', 'table', 'hr',
					'pagebreak', '|', 'link', 'image', 'code', 'insertfile',
					'wcpknow' ]
		});
		$('#openChooseTypeButtonId').bind('click', function() {
			$('#myModal').modal({
				keyboard : false
			})
		});
		$('select', '#knowSubmitFormId').each(function(i, obj) {
			var val = $(obj).attr('val');
			$(obj).val(val);
		});
		$('#knowCancelButtonId').bind('click', function() {
			window.location =basePath + "webgroup/Pubshow.do?groupid=?id=${group.id}";
		});
		$('#knowSubmitButtonId')
				.bind(
						'click',
						function() {
							editor.sync();
							if (!validate('knowSubmitFormId')) {
								$('#errormessageShowboxId').text('信息录入有误，请检查！');
							} else {
								if ($('#contentId').val() == null
										|| $('#contentId').val() == '') {
									$('#errormessageShowboxId')
											.text('请录入文档内容！');
									return false;
								}
								if ($('#contentId').val().length > 1000000) {
									$('#errormessageShowboxId')
											.text(
													'文档内容超长（'
															+ $('#contentId')
																	.val().length
															+ '>1000000)');
									return false;
								}
								if (confirm("是否提交数据?")) {
									$('#knowSubmitFormId').submit();
									$('#knowSubmitButtonId').hide();
									$('#knowSubmitButtonId')
											.before(
													"<h2><span class='label label-info glyphicon glyphicon-link'>提交中...</span></h2>");
								}
							}
						});
		//阅读权限
		validateInput('knowEditNoteId', function(id, val, obj) {
			if (valid_isNull(val)) {
				return {
					valid : false,
					msg : '不能为空'
				};
			}
			if (valid_maxLength(val, 128)) {
				return {
					valid : false,
					msg : '长度不能大于' + 128
				};
			}
			return {
				valid : true,
				msg : '正确'
			};
		});
	});
</script>
</html>